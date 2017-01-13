package com.lsxy.yunhuni.recharge.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.api.billing.service.BillingService;
import com.lsxy.framework.api.billing.service.CalBillingService;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.portal.RechargeSuccessEvent;
import com.lsxy.yunhuni.api.recharge.enums.RechargeSource;
import com.lsxy.yunhuni.api.recharge.enums.RechargeStatus;
import com.lsxy.yunhuni.api.recharge.enums.RechargeType;
import com.lsxy.yunhuni.api.recharge.model.Recharge;
import com.lsxy.yunhuni.api.recharge.service.RechargeService;
import com.lsxy.yunhuni.recharge.dao.RechargeDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by liups on 2016/7/1.
 */
@Service
public class RechargeServiceImpl extends AbstractService<Recharge> implements RechargeService {

    @Autowired
    RechargeDao rechargeDao;
    @Autowired
    private TenantService tenantService;
    @Autowired
    BillingService billingService;
    @Autowired
    EntityManager em;
    @Autowired
    CalBillingService calBillingService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MQService mqService;

    @Override
    public BaseDaoInterface<Recharge, Serializable> getDao() {
        return rechargeDao;
    }

    @Override
    public Recharge createRecharge(String username, String type, BigDecimal amount){
        Recharge recharge = null;
        //充值类型一定要是规定好的类型,当没有该类型时，枚举类会抛出IllegalArgumentException异常
        RechargeType rechargeType = RechargeType.valueOf(type);
        if(rechargeType != null && amount.compareTo(new BigDecimal(0))==1 && username != null){
            Tenant tenant = tenantService.findTenantByUserName(username);
            if(tenant != null){
                String orderId = UUIDGenerator.uuid();
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DAY_OF_MONTH, 1);
                Date deadline = c.getTime();
                recharge = new Recharge(tenant.getId(),amount, RechargeSource.USER,rechargeType, RechargeStatus.NOTPAID,orderId,null,deadline);
                rechargeDao.save(recharge);
            }
        }
        return recharge;
    }

    @Override
    public Recharge getRechargeByOrderId(String orderId) {
        return rechargeDao.findByOrderId(orderId);
    }

    @Override
    public Recharge paySuccess(String orderId, BigDecimal totalFee) throws MatchMutiEntitiesException {
        Recharge recharge = rechargeDao.findByOrderId(orderId);
        BigDecimal amount = recharge.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP);

        if(recharge != null && amount.compareTo(totalFee)==0){
            String status = recharge.getStatus();
            //如果充值记录是未支付状态，则将支付状态改成已支付，并将钱加到账务表里
            if(RechargeStatus.NOTPAID.name().equals(status)){
                Date curTime = new Date();
                //状态变成已支付
                recharge.setStatus(RechargeStatus.PAID.name());
                recharge.setPayTime(curTime);
                rechargeDao.save(recharge);
                mqService.publish(new RechargeSuccessEvent(recharge.getTenantId()));
                //redis插入今日充值
                calBillingService.incRecharge(recharge.getTenantId(),curTime,recharge.getAmount());
            }
        }
        return  recharge;
    }

    @Override
    public Page<Recharge> pageListByUserNameAndTime(String userName, Integer pageNo, Integer pageSize, Date startTime, Date endTime) throws MatchMutiEntitiesException {
        Page<Recharge> page = null;
        Tenant tenant = tenantService.findTenantByUserName(userName);
        Date date = new Date();
        if(tenant != null){
            if(startTime != null && endTime != null){
                String hql = "from Recharge obj where (obj.status='PAID' or (obj.status='NOTPAID' and obj.deadline>=?1 )) and obj.tenantId=?2 and obj.createTime between ?3 and ?4 order by obj.createTime desc";
                page =  this.pageList(hql,pageNo,pageSize,date,tenant.getId(),startTime,endTime);
            }else if(startTime != null && endTime == null){
                String hql = "from Recharge obj where (obj.status='PAID' or (obj.status='NOTPAID' and obj.deadline>=?1 )) and obj.tenantId=?2 and obj.createTime >= ?3 order by obj.createTime desc";
                page =  this.pageList(hql,pageNo,pageSize,date,tenant.getId(),startTime);
            }else if(startTime == null && endTime != null){
                String hql = "from Recharge obj where (obj.status='PAID' or (obj.status='NOTPAID' and obj.deadline>=?1 )) and obj.tenantId=?2 and obj.createTime <= ?3 order by obj.createTime desc";
                page =  this.pageList(hql,pageNo,pageSize,date,tenant.getId(),endTime);
            }else{
                String hql = "from Recharge obj where (obj.status='PAID' or (obj.status='NOTPAID' and obj.deadline>=?1 )) and obj.tenantId=?2 order by obj.createTime desc";
                page =  this.pageList(hql,pageNo,pageSize,date,tenant.getId());
            }
        }
        return page;
    }

    @Override
    public boolean doRecharge(String tenantId, BigDecimal amount,String source) {
        if(StringUtil.isEmpty(tenantId)){
            throw new IllegalArgumentException();
        }
        if(amount == null || amount.doubleValue() < 0){
            throw new IllegalArgumentException();
        }
        Tenant tenant = tenantService.findById(tenantId);
        if(tenant == null){
            throw new IllegalArgumentException();
        }
        String orderId = UUIDGenerator.uuid();
        Date curTime = new Date();
        Recharge recharge = new Recharge(tenantId,amount,RechargeSource.valueOf(source),RechargeType.RENGONG, RechargeStatus.PAID,orderId,curTime);
        rechargeDao.save(recharge);
        mqService.publish(new RechargeSuccessEvent(recharge.getTenantId()));
        // redis插入今日充值
        calBillingService.incRecharge(tenantId,curTime,amount);
        return true;
    }

    @Override
    public List<Recharge> listByTenant(String tenant) {
        String hql = "from Recharge obj where obj.tenantId=?1 order by obj.createTime desc";
        return this.list(hql,tenant);
    }

    @Override
    public Page<Recharge> pageListByTenant(String tenant,String type,String source, Integer pageNo, Integer pageSize) {
        Page<Recharge> page = null;
        if(StringUtils.isNotBlank(type) && StringUtils.isNotBlank(source)){
            String hql = "from Recharge obj where obj.tenantId=?1 and obj.status ='PAID' and type=?2 and source=?3 order by obj.createTime desc";
            page =  this.pageList(hql,pageNo,pageSize,tenant,type,source);
        }else if(StringUtils.isNotBlank(type)){
            String hql = "from Recharge obj where obj.tenantId=?1 and obj.status ='PAID' and type=?2 order by obj.createTime desc";
            page =  this.pageList(hql,pageNo,pageSize,tenant,type);
        }else if(StringUtils.isNotBlank(source)){
            String hql = "from Recharge obj where obj.tenantId=?1 and obj.status ='PAID' and source=?2 order by obj.createTime desc";
            page =  this.pageList(hql,pageNo,pageSize,tenant,source);
        }else{
            String hql = "from Recharge obj where obj.tenantId=?1 and obj.status ='PAID' order by obj.createTime desc";
            page =  this.pageList(hql,pageNo,pageSize,tenant);
        }
        return page;
    }

    @Override
    public BigDecimal getRechargeByTenantIdAndDate(String tenantId, Date startDate, Date endDate) {
        String sql = "SELECT IFNULL(SUM(r.amount),0) as recharge FROM db_lsxy_base.tb_base_recharge r WHERE r.tenant_id=? AND r.pay_time >= ? AND r.pay_time < ? and r.deleted=0";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql, tenantId,startDate,endDate);
        return (BigDecimal) map.get("recharge");
    }

}
