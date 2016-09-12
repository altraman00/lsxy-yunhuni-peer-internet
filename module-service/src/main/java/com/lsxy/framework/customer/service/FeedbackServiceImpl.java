package com.lsxy.framework.customer.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.customer.model.Feedback;
import com.lsxy.framework.api.customer.service.FeedbackService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.customer.dao.FeedbackDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * 反馈信息实现
 * Created by zhangxb on 2016/7/4.
 */
@Service
public class FeedbackServiceImpl extends AbstractService<Feedback> implements FeedbackService {
    @Autowired
    FeedbackDao feedbackDao;
    @PersistenceContext
    private EntityManager em;
    @Override
    public BaseDaoInterface<Feedback, Serializable> getDao() {
        return feedbackDao;
    }
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public Page<Feedback> pageList( Integer pageNo, Integer pageSize, String startTime, String endTime,Integer status) {
        Date date1 = null;
        Date date2 = null;
        try {
             date1 = DateUtils.parseDate(startTime, "yyyy-MM-dd");
        }catch (Exception e){}
        try {
            date2 = DateUtils.parseDate(endTime + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        }catch (Exception e){}

        String sql = " FROM db_lsxy_base.tb_base_customer_feedback obj WHERE obj.deleted=0 ";
        if(date1!=null){
            sql +=" AND obj.create_time >= :date1 ";
        }
        if(date2!=null){
            sql +=" AND obj.create_time <= :date2 ";
        }
        if(status!=null){
            sql +=" AND obj.status = :status ";
        }
        String countSql = " SELECT COUNT(1) "+sql;
        String pageSql = " SELECT * "+sql;
        Query countQuery = em.createNativeQuery(countSql);
        pageSql +=" GROUP BY obj.create_time DESC";
        Query pageQuery = em.createNativeQuery(pageSql,Feedback.class);
        if(date1!=null){
            countQuery.setParameter("date1",date1);
            pageQuery.setParameter("date1",date1);
        }
        if(date2!=null){
            countQuery.setParameter("date2",date2);
            pageQuery.setParameter("date2",date2);
        }
        if(status!=null){
            countQuery.setParameter("status",status);
            pageQuery.setParameter("status",status);
        }
        int total = ((BigInteger)countQuery.getSingleResult()).intValue();
        int start = (pageNo-1)*pageSize;
        if(total == 0){
           return  new Page<>(start,total,pageSize,null);
        }
        pageQuery.setMaxResults(pageSize);
        pageQuery.setFirstResult(start);
        List list = pageQuery.getResultList();
        return new Page<>(start,total,pageSize,list);
    }

    @Override
    public void batchModifyStatus(String[] ids) {
        String sql = "update db_lsxy_base.tb_base_customer_feedback set status='"+Feedback.READ+"' where id in ( ";
        for(int i=0;i<ids.length;i++){
            if(i==ids.length-1){
                sql += " '" + ids[i]+ "' ";
            }else {
                sql += " '" + ids[i] + "', ";
            }
        }
        sql+=" )";
        jdbcTemplate.update(sql);
    }
}
