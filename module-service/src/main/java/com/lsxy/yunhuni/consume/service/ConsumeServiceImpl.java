package com.lsxy.yunhuni.consume.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.billing.service.CalBillingService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.yunhuni.api.apicertificate.model.CertAccountQuotaType;
import com.lsxy.yunhuni.api.consume.model.Consume;
import com.lsxy.yunhuni.api.consume.service.ConsumeService;
import com.lsxy.yunhuni.consume.dao.ConsumeDao;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 消费记录ServiceImpl
 * Created by zhangxb on 2016/7/8.
 */
@Service
public class ConsumeServiceImpl extends AbstractService<Consume> implements ConsumeService {
    private static final Logger logger = LoggerFactory.getLogger(ConsumeServiceImpl.class);
    @Autowired
    ConsumeDao consumeDao;
    @Autowired
    TenantService tenantService;
    @Autowired
    EntityManager em;
    @Autowired
    CalBillingService calBillingService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public BaseDaoInterface<Consume, Serializable> getDao() {
        return consumeDao;
    }

    @Override
    public List<Consume> listConsume(String userName, String startTime, String endTime, String appId) {
        Date startDate = null;
        Date endDate = null;
        if(StringUtils.isNotBlank(startTime)){
            startDate = DateUtils.parseDate(startTime+"-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
        }
        if(StringUtils.isNotBlank(endTime)){
            endDate = DateUtils.getLastTimeOfMonth(DateUtils.parseDate(endTime,"yyyy-MM"));
        }
        String tmepAppId = "";
        if(StringUtils.isNotEmpty(appId)){
            tmepAppId += " and  obj.appId ='"+appId+"'";
        }
        Tenant tenant = tenantService.findTenantByUserName(userName);
        String hql = "from Consume obj where obj.tenantId=?1 and obj.dt BETWEEN  ?2 and ?3 "+tmepAppId+" ORDER BY obj.dt desc";
        List<Consume> list = this.list(hql,tenant.getId(),startDate,endDate);
        return list;
    }

    @Override
    public Page<Consume> pageList(String userName,Integer pageNo, Integer pageSize,String startTime,String endTime,String appId) {
        Date startDate = null;
        Date endDate = null;
        if(StringUtils.isNotBlank(startTime)){
            startDate = DateUtils.parseDate(startTime+"-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
        }
        if(StringUtils.isNotBlank(endTime)){
            endDate = DateUtils.getLastTimeOfMonth(DateUtils.parseDate(endTime,"yyyy-MM"));
        }
        String tmepAppId = "";
        if(StringUtils.isNotEmpty(appId)){
            tmepAppId += " and  obj.appId ='"+appId+"'";
        }
        Tenant tenant = tenantService.findTenantByUserName(userName);
        String hql = "from Consume obj where obj.tenantId=?1 and obj.dt BETWEEN  ?2 and ?3 "+tmepAppId+" ORDER BY obj.dt desc";
        Page<Consume> page = this.pageList(hql,pageNo,pageSize,tenant.getId(),startDate,endDate);
        return page;
    }

    @Override
    public Page<Consume> pageList(String tenantId, Integer pageNo, Integer pageSize, Date startTime, Date endTime) {
        String hql = "from Consume obj where obj.tenantId=?1 and obj.dt<=?2 and obj.dt>=?3   ORDER BY obj.dt";
        Page<Consume> page = this.pageList(hql,pageNo,pageSize,tenantId,endTime,startTime);
        return page;
    }

    @Override
    public Page<Consume> pageListByTenantAndDate(String tenantId, Integer year, Integer month, Integer pageNo, Integer pageSize) {

        String hql = "from Consume obj where obj.tenantId=?1 AND  obj.dt BETWEEN ?2 AND ?3  ORDER BY obj.dt desc";
        Date date1 = DateUtils.parseDate(year+"-"+month+"-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
        Date date2 = DateUtils.getLastTimeOfMonth(DateUtils.parseDate(year+"-"+month,"yyyy-MM"));
        Page<Consume>  page = this.pageList(hql,pageNo,pageSize,tenantId,date1,date2);
//        int start = (pageNo-1)*pageSize;
//        String db_name = "db_lsxy_base";//year +"-" + month;
//        String countsql = "select count(1) from "+db_name+".tb_bi_consume where tenant_id=:tenant AND BETWEEN dt ? AND ? ";
//        Query countQuery = em.createNativeQuery(countsql);
//        countQuery.setParameter("tenant",tenantId);
//        long total = ((BigInteger)countQuery.getSingleResult()).longValue();
//        if(total == 0){
//            return new Page<>(start,total,pageSize,null);
//        }
//        String pagesql = "select * from "+db_name+".tb_bi_consume where tenant_id=:tenant AND BETWEEN dt ? AND ? ";
//        Query pageQuery = em.createNativeQuery(pagesql,Consume.class);
//        pageQuery.setParameter("tenant",tenantId);
//        pageQuery.setMaxResults(pageSize);
//        pageQuery.setFirstResult(start);
//        return new Page<>(start,total,pageSize,pageQuery.getResultList());
        return page;
    }

    @Override
    public void consume(Consume consume) {
        if(logger.isDebugEnabled()){
            logger.info("插入消费记录：{}", JSONUtil.objectToJson(consume));
        }
        if(consume.getAmount().compareTo(BigDecimal.ZERO) == -1){
            throw new IllegalArgumentException("金额不能小于0");
        }
        this.save(consume);
        //Redis中消费增加
        calBillingService.incConsume(consume.getTenantId(),consume.getDt(),consume.getAmount());
    }

    @Override
    public BigDecimal getConsumeByTenantIdAndDate(String tenantId, Date startDate, Date endDate) {
        String sql = "SELECT IFNULL(SUM(c.amount),0) as consume FROM db_lsxy_bi_yunhuni.tb_bi_consume c WHERE c.tenant_id=? AND c.dt >= ? AND c.dt < ? and c.deleted=0";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql, tenantId,startDate,endDate);
        return (BigDecimal) map.get("consume");
    }

    @Override
    public BigDecimal getConsumeByAppIdAndDate(String appId, Date startDate, Date endDate) {
        String sql = "SELECT IFNULL(SUM(c.amount),0) as consume FROM db_lsxy_bi_yunhuni.tb_bi_consume c WHERE c.app_id=? AND c.dt >= ? AND c.dt < ? and c.deleted=0";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql, appId,startDate,endDate);
        return (BigDecimal) map.get("consume");
    }

    @Override
    public void batchConsume(Date dt, String type, BigDecimal cost, String remark, String appId, String tenantId, String subaccountId, List<String> detailIds) {
        String values = "id,dt,type,amount,remark,app_id,tenant_id,relevance_id,subaccount_id,create_time,last_time,deleted,sortno,version";
        String valuesMark = "";
        int length = values.split(",").length;
        for(int i = 0;i<length;i++){
            if(i == length -1){
                valuesMark += "?";
            }else{
                valuesMark += "?,";
            }
        }
        String insertSql =  " insert into db_lsxy_bi_yunhuni.tb_bi_consume("+ values + ") values ("+valuesMark+")";
        Date date = new Date();
        BigDecimal allCost = null;
        List resultList = new ArrayList();
        if(detailIds != null && detailIds.size() > 0){
            for(String detailId:detailIds){
                String uuid = UUIDGenerator.uuid();
                Object[] obj = new Object[]{uuid,dt,type,cost,remark,appId,tenantId,detailId,subaccountId,date,date,0,0,0};
                resultList.add(obj);
            }
            allCost = cost.multiply(new BigDecimal(detailIds.size()));
        }

        if(allCost != null && allCost.compareTo(BigDecimal.ZERO) != 0){
            if(resultList != null && resultList.size() > 0){
                jdbcTemplate.batchUpdate(insertSql,resultList);
            }
            //Redis中消费增加
            calBillingService.incConsume(tenantId,dt,allCost);
        }
    }

}
