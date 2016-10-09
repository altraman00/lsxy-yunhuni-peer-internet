package com.lsxy.yunhuni.consume.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.consume.model.Consume;
import com.lsxy.yunhuni.api.consume.service.ConsumeService;
import com.lsxy.yunhuni.consume.dao.ConsumeDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 消费记录ServiceImpl
 * Created by zhangxb on 2016/7/8.
 */
@Service
public class ConsumeServiceImpl extends AbstractService<Consume> implements ConsumeService {
    @Autowired
    ConsumeDao consumeDao;
    @Autowired
    TenantService tenantService;
    @Autowired
    EntityManager em;

    @Override
    public BaseDaoInterface<Consume, Serializable> getDao() {
        return consumeDao;
    }

    @Override
    public List<Consume> list(String userName, String startTime, String endTime, String appId) {
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
        String hql = "from Consume obj where obj.tenant.id=?1 and obj.dt BETWEEN  ?2 and ?3 "+tmepAppId+" ORDER BY obj.dt desc";
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
        String hql = "from Consume obj where obj.tenant.id=?1 and obj.dt BETWEEN  ?2 and ?3 "+tmepAppId+" ORDER BY obj.dt desc";
        Page<Consume> page = this.pageList(hql,pageNo,pageSize,tenant.getId(),startDate,endDate);
        return page;
    }

    @Override
    public Page<Consume> pageList(String tenantId, Integer pageNo, Integer pageSize, Date startTime, Date endTime) {
        String hql = "from Consume obj where obj.tenant.id=?1 and obj.dt<=?2 and obj.dt>=?3   ORDER BY obj.dt";
        Page<Consume> page = this.pageList(hql,pageNo,pageSize,tenantId,endTime,startTime);
        return page;
    }

    @Override
    public Page<Consume> pageListByTenantAndDate(String tenantId, Integer year, Integer month, Integer pageNo, Integer pageSize) {

        String hql = "from Consume obj where obj.tenant.id=?1 AND  obj.dt BETWEEN ?2 AND ?3  ORDER BY obj.dt desc";
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

}
