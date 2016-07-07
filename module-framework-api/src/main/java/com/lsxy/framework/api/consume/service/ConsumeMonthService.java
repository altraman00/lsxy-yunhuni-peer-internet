package com.lsxy.framework.api.consume.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.consume.model.ConsumeMonth;
import com.lsxy.framework.core.utils.Page;

import java.util.List;

/**
 * 消费月统计Service
 * Created by zhangxb on 2016/7/6.
 */
public interface ConsumeMonthService extends BaseService<ConsumeMonth> {
    /**
     * 获取用户区间内的分页数据
     * @param userName 用户名
     * @param appId 应用id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    public Page<ConsumeMonth> pageList(String userName, String appId, String startTime, String endTime, Integer pageNo, Integer pageSize);

    /**
     * 获取用户某时间的列表数据
     * @param userName 用户名
     * @param appId 应用id
     * @param startTime 时间
     * @return
     */
    public List<ConsumeMonth> list(String userName, String appId, String startTime);
}
