package com.lsxy.yunhuni.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.statistics.model.SubaccountMonth;
import com.lsxy.yunhuni.api.statistics.model.SubaccountStatisticalVO;
import com.lsxy.yunhuni.api.statistics.service.SubaccountMonthService;
import com.lsxy.yunhuni.statistics.dao.SubaccountMonthDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by liups on 2017/2/21.
 */
@Service
public class SubaccountMonthServiceImpl extends AbstractService<SubaccountMonth> implements SubaccountMonthService {
    @Autowired
    SubaccountMonthDao subaccountMonthDao;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @PersistenceContext
    private EntityManager em;
    @Override
    public BaseDaoInterface<SubaccountMonth, Serializable> getDao() {
        return this.subaccountMonthDao;
    }

    @Override
    public Page<SubaccountStatisticalVO> getPageByConditions(Integer pageNo, Integer pageSize, Date startTime, Date endTime, String tenantId,  String appId,String subaccountId) {
        String sql = " FROM db_lsxy_bi_yunhuni.tb_bi_cert_subaccount_month obj LEFT ON db_lsxy_bi_yunhuni.tb_bi_app a ON obj.appId=a.id WHERE deleted=0 ";
        if(StringUtils.isNotEmpty(appId)){
            sql += " AND app_id = :appId";
        }
        if(StringUtils.isNotEmpty(subaccountId)){
            sql += " AND obj.subaccount_id = :subaccountId ";
        }
        sql += " AND obj.tenant_id =:tenantId AND obj.dt BETWEEN :startTime AND :endTime ";
        String countSql = " SELECT COUNT(1) "+sql;
        String pageSql = " SELECT * "+sql;
        Query countQuery = em.createNativeQuery(countSql);
        pageSql +=" group by obj.dt desc";
        Query pageQuery = em.createNativeQuery(pageSql,SubaccountStatisticalVO.class);
        if(StringUtils.isNotEmpty(appId)){
            countQuery.setParameter("appId",appId);
            pageQuery.setParameter("appId",appId);
        }
        if(StringUtils.isNotEmpty(subaccountId)){
            countQuery.setParameter("subaccountId",subaccountId);
            pageQuery.setParameter("subaccountId",subaccountId);
        }
        countQuery.setParameter("tenantId",tenantId);
        pageQuery.setParameter("tenantId",tenantId);
        countQuery.setParameter("startTime",startTime);
        pageQuery.setParameter("startTime",startTime);
        countQuery.setParameter("endTime",endTime);
        pageQuery.setParameter("endTime",endTime);
        int total = ((BigInteger)countQuery.getSingleResult()).intValue();
        int start = (pageNo-1)*pageSize;
        if(total == 0){
            return new Page<>(start,total,pageSize,null);
        }
        pageQuery.setMaxResults(pageSize);
        pageQuery.setFirstResult(start);
        List list = pageQuery.getResultList();
        return new Page<>(start,total,pageSize,list);
    }

    @Override
    public List<SubaccountStatisticalVO> getListByConditions(Date startTime, Date endTime, String tenantId, String appId, String subaccountId) {
        int pageNo = 1;
        int pageSize = 100;
        List list = new ArrayList();
        Page<SubaccountStatisticalVO> page = getPageByConditions(pageNo,pageSize,startTime,  endTime,  tenantId,  appId,  subaccountId);
        list.addAll(page.getResult());
        if(page.getCurrentPageNo() < page.getTotalPageCount()){
            pageNo = Long.valueOf(page.getCurrentPageNo()).intValue()+1;
            page = getPageByConditions(pageNo,pageSize,startTime,  endTime,  tenantId,  appId,  subaccountId);
            list.addAll(page.getResult());
        }
        return list;
    }

    @Override
    public Map sum(Date start, Date end, String tenantId, String appId, String subaccountId) {
        //amongAmount
        //amongDuration
        String sql = " select sum(among_amount) as amongAmount,sum(among_duration) as amongDuration from db_lsxy_bi_yunhuni.tb_bi_cert_subaccount_month where deleted=0 ";
        if(StringUtils.isNotEmpty(appId)){
            sql += " AND app_id = '"+appId+"'";
        }
        if(StringUtils.isNotEmpty(subaccountId)){
            sql += " AND subaccount_id = '"+subaccountId+"'";
        }
        sql += " AND tenant_id =? AND dt BETWEEN ? AND ? ";
        return jdbcTemplate.queryForMap(sql,tenantId,start,end);
    }
}
