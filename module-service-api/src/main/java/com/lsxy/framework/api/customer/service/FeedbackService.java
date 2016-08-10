package com.lsxy.framework.api.customer.service;

import com.lsxy.framework.api.customer.model.Feedback;
import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.utils.Page;

/**
 * 客服中心-反馈信息
 * Created by zhangxb on 2016/7/4.
 */
public interface FeedbackService extends BaseService<Feedback> {
    /**
     * 获取分页信息
     * @param pageNo 第几页
     * @param pageSize 每页面记录数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    Page<Feedback> pageList( Integer pageNo, Integer pageSize, String startTime, String endTime,Integer status);
}
