package com.lsxy.framework.message.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.message.model.Message;
import com.lsxy.framework.api.message.service.MessageService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.message.dao.MessageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
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
    @Override
    public BaseDaoInterface<Message, Serializable> getDao() {
        return messageDao;
    }

    @Override
    public Page<Message> pageList(Integer type,Integer status, String startTime, String endTime, Integer pageNo, Integer pageSize) {

        Date date1 = null;
        Date date2 = null;
        try {
            date1 = DateUtils.parseDate(startTime, "yyyy-MM-dd");
        }catch (Exception e){}
        try {
            date2 = DateUtils.parseDate(endTime + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        }catch (Exception e){}
        String hql = "from Message obj ";
        boolean flag = true;
        int i=1;
        if(date1!=null){
            if(flag){
                hql +=" where ";
                flag=false;
            }else{
                hql +=" and ";
            }
            hql +=" obj.lineTime>=?"+i;
            i++;
        }
        if(date2!=null){
            if(flag){
                hql +=" where ";
                flag=false;
            }else{
                hql +=" and ";
            }
            hql +=" obj.lineTime<=?"+i;
            i++;
        }
        if(type!=null){
            if(flag){
                hql +=" where ";
                flag=false;
            }else{
                hql +=" and ";
            }
            hql+=" obj.type='"+type+"' ";
        }
        if(status!=null){
            if(flag){
                hql +=" where ";
                flag=false;
            }else{
                hql +=" and ";
            }
            hql+=" obj.status='"+status+"' ";
        }
        Page<Message> page = null;
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
    public List<Message> bacthUpdateStatus(Date startTime, Date endTime) {
        String hql =" from Message  WHERE type=? AND status=?  AND line_time BETWEEN ? and ? ";
        List list = this.list(hql,Message.MESSAGE_ACTIVITY,Message.NOT,startTime,endTime);
        String sql = "UPDATE db_lsxy_base.tb_base_message SET status=? WHERE deleted=0 AND type=? AND status=?  AND line_time BETWEEN ? and ?  ";
        jdbcTemplate.update(sql,Message.ONLINE,Message.MESSAGE_ACTIVITY,Message.NOT,startTime,endTime);
        return list;
    }
}
