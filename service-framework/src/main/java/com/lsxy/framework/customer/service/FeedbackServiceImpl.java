package com.lsxy.framework.customer.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.customer.model.Feedback;
import com.lsxy.framework.api.customer.service.FeedbackService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.customer.dao.FeedbackDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

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
}
