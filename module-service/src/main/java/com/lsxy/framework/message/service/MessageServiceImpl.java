package com.lsxy.framework.message.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.message.model.Message;
import com.lsxy.framework.api.message.service.MessageService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.message.dao.MessageDao;
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
 * 消息实现类
 * Created by zhangxb on 2016/7/4.
 */
@Service
public class MessageServiceImpl extends AbstractService<Message> implements MessageService{
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    MessageDao messageDao;
    @PersistenceContext
    private EntityManager em;
    @Override
    public BaseDaoInterface<Message, Serializable> getDao() {
        return messageDao;
    }

    @Override
    public Page<Message> pageList(Integer type,Integer status, String startTime, String endTime, Integer pageNo, Integer pageSize) {
        String sql = " FROM db_lsxy_base.tb_base_message obj WHERE obj.deleted=0 ";
        Date date1 = null;
        Date date2 = null;
        try {
            if(StringUtil.isNotEmpty(startTime)) {
                date1 = DateUtils.parseDate(startTime, "yyyy-MM-dd");
            }
        }catch (Exception e){}
        try {
            if(StringUtil.isNotEmpty(endTime)) {
                date2 = DateUtils.parseDate(endTime + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
            }
        }catch (Exception e){}
        if(date1!=null){
            sql +=" AND obj.line_time>= :date1 ";
        }
        if(date2!=null){
            sql +=" AND obj.line_time<= :date2 ";
        }
        if(type!=null){
            sql +=" AND obj.type='"+type+"' ";
        }
        if(status!=null){
            sql +=" AND obj.status='"+status+"' ";
        }
        String countSql = " SELECT COUNT(1) "+sql;
        String pageSql = " SELECT * "+sql;
        Query countQuery = em.createNativeQuery(countSql);
        pageSql +=" group by obj.line_time desc";
        Query pageQuery = em.createNativeQuery(pageSql,Message.class);
        if(date1!=null){
            countQuery.setParameter("date1",date1);
            pageQuery.setParameter("date1",date1);
        }
        if(date2!=null){
            countQuery.setParameter("date2",date2);
            pageQuery.setParameter("date2",date2);
        }
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
    public List<Message> bacthUpdateStatus(Date startTime, Date endTime) {
        String hql =" from Message obj WHERE obj.type=? AND obj.status=?  AND obj.lineTime BETWEEN ? and ? ";
        List list = this.list(hql,Message.MESSAGE_ACTIVITY,Message.NOT,startTime,endTime);
        String sql = "UPDATE db_lsxy_base.tb_base_message SET status=? WHERE deleted=0 AND type=? AND status=?  AND line_time BETWEEN ? and ?  ";
        jdbcTemplate.update(sql,Message.ONLINE,Message.MESSAGE_ACTIVITY,Message.NOT,startTime,endTime);
        return list;
    }
}
