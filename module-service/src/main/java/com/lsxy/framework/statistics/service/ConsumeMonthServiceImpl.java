package com.lsxy.framework.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.statistics.model.ConsumeMonth;
import com.lsxy.framework.api.statistics.service.ConsumeMonthService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.statistics.dao.ConsumeMonthDao;
import com.lsxy.framework.core.utils.Page;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 消费月统计serviceimpl
 * Created by zhangxb on 2016/7/6.
 */
@Service
public class ConsumeMonthServiceImpl extends AbstractService<ConsumeMonth> implements ConsumeMonthService{
    @Autowired
    ConsumeMonthDao consumeMonthDao;
    @Autowired
    TenantService tenantService;
    @Override
    public BaseDaoInterface<ConsumeMonth, Serializable> getDao() {
        return consumeMonthDao;
    }
    @Override
    public Page<ConsumeMonth> pageList(String userName, String appId, String startTime, String endTime, Integer pageNo, Integer pageSize) {
        Tenant tenant = tenantService.findTenantByUserName(userName);
        Page<ConsumeMonth> page = null;
        if("0".equals(appId)){//表示查询全部
            String hql = "from ConsumeMonth obj where obj.tenantId=?1 and ( DATE_FORMAT(obj.dt,'%Y')=?2 or DATE_FORMAT(obj.dt,'%Y')=?3 ) ORDER BY obj.dt,obj.month";
            page = this.pageList(hql,pageNo,pageSize,tenant.getId(),endTime,startTime);
        }else{
            String hql = "from ConsumeMonth obj where obj.tenantId=?1 and obj.appId=?2 and ( DATE_FORMAT(obj.dt,'%Y')=?3  or DATE_FORMAT(obj.dt,'%Y')=?4 )ORDER BY obj.dt,obj.month";
            page = this.pageList(hql,pageNo,pageSize,tenant.getId(),appId,endTime,startTime);
        }
        return page;
    }

    @Override
    public List<ConsumeMonth> list(String userName, String appId, String startTime) {
        Tenant tenant = tenantService.findTenantByUserName(userName);
        List<ConsumeMonth> list = null;
        if("0".equals(appId)){//表示查询全部
            String hql = "from ConsumeMonth obj where obj.tenantId=?1 and DATE_FORMAT(obj.dt,'%Y')=?2 ORDER BY obj.month";
            list = this.findByCustomWithParams(hql, tenant.getId(),startTime);
        }else{
            String hql = "from ConsumeMonth obj where obj.tenantId=?1 and obj.appId=?2 and DATE_FORMAT(obj.dt,'%Y')=?3 ORDER BY obj.month";
            list = this.findByCustomWithParams(hql, tenant.getId(),appId,startTime);
        }
        return list;
    }

    @Override
    public String getStartMonthByTenantId(String tenantId) {
        String start = null;
        ConsumeMonth consumeMonth = consumeMonthDao.findFirst1ByTenantIdOrderByDtAsc(tenantId);
        if(consumeMonth != null){
            start = new SimpleDateFormat("yyyy-MM").format(consumeMonth.getDt());
        }
        return start;
    }

    @Override
    public BigDecimal sumAmountByTime(String tenantId, String start, String end){
        BigDecimal amount;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
            Date startTime;
            Date endTime = null;
            String hql;
            if(StringUtils.isNotBlank(start)){
                startTime = format.parse(start);
            }else{
                throw new IllegalArgumentException("参数异常");
            }
            if(StringUtils.isNotBlank(end)){
                endTime = format.parse(end);
            }
            if(endTime != null){
                hql = "select sum(obj.amongAmount) from ConsumeMonth obj where obj.tenantId = ?1 and obj.dt between ?2 and ?3";
            }else{
                hql = "select sum(obj.amongAmount) from ConsumeMonth obj where obj.tenantId = ?1 and obj.dt >= ?2";
            }
            Query query = this.getEm().createQuery(hql);
            query.setParameter(1, tenantId);
            query.setParameter(2, startTime);
            if(endTime != null){
                query.setParameter(3, endTime);
            }
            Object obj = query.getSingleResult();
            amount = (BigDecimal) obj;
        }catch (Exception e){
            throw new IllegalArgumentException("参数异常");
        }
        if(amount == null){
            amount = new BigDecimal(0);
        }
        return amount;
    }

    @Override
    public void monthStatistics(Date date1, int day1, Date date2, int day2, String[] select) throws SQLException {
        
    }
}
