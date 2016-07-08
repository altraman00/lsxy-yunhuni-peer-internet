package com.lsxy.framework.api.consume.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.consume.model.Consume;
import com.lsxy.framework.core.utils.Page;

/**
 * 消费记录Service
 * Created by zhangxb on 2016/7/8.
 */
public interface ConsumeService extends BaseService<Consume> {
    /**
     * 获取用户区间内的分页数据
     * @param userName 用户名
     * @return
     */
    public Page<Consume> pageList(String userName,Integer pageNo, Integer pageSize);

}
