package com.lsxy.framework.customer.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.customer.model.Feedback;
import com.lsxy.framework.api.customer.service.FeedbackService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.customer.dao.FeedbackDao;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public Page<Feedback> pageList( Integer pageNo, Integer pageSize, String startTime, String endTime,Integer status) {
        Date date1 = DateUtils.parseDate(startTime,"yyyy-MM-dd");
        Date date2 = DateUtils.parseDate(endTime+" 23:59:59","yyyy-MM-dd HH:mm:ss");
        String hql = "from Feedback obj where obj.createTime>=?1 and obj.createTime<=?2 ";
        if(status!=null){
            hql+=" and obj.status='"+status+"' ";
        }
        Page<Feedback> page = this.pageList(hql,pageNo,pageSize,date1,date2);
        return page;
    }
}
