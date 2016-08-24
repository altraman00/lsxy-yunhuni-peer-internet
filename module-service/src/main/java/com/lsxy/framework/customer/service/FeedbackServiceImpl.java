package com.lsxy.framework.customer.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.customer.model.Feedback;
import com.lsxy.framework.api.customer.service.FeedbackService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.customer.dao.FeedbackDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;

/**
 * 反馈信息实现
 * Created by zhangxb on 2016/7/4.
 */
@Service
public class FeedbackServiceImpl extends AbstractService<Feedback> implements FeedbackService {
    @Autowired
    FeedbackDao feedbackDao;
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
        String hql = "from Feedback obj  ";
        boolean flag = true;
        int i=1;
        if(date1!=null){
            if(flag){
                hql +=" where ";
                flag=false;
            }else{
                hql +=" and ";
            }
            hql +=" obj.createTime>=?"+i;
            i++;
        }
        if(date2!=null){
            if(flag){
                hql +=" where ";
                flag=false;
            }else{
                hql +=" and ";
            }
            hql +=" obj.createTime<=?"+i;
            i++;
        }
        if(status!=null){
            if(flag){
                hql +=" where ";
                flag=false;
            }else{
                hql +=" and ";
            }
            hql+="  obj.status='"+status+"' ";
        }
        Page<Feedback> page = null;
        if(date1!=null&&date2!=null){
            page = this.pageList(hql,pageNo,pageSize,date1,date2);
        }else if(date1!=null&&date2==null){
            page = this.pageList(hql,pageNo,pageSize,date1);
        }else if(date1==null&&date2!=null){
            page = this.pageList(hql,pageNo,pageSize,date2);
        }else{
            page = this.pageList(hql,pageNo,pageSize);
        }
        return page;
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
