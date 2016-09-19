package com.lsxy.framework.api.consume.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.consume.model.Consume;
import com.lsxy.framework.core.utils.Page;

import java.util.Date;

/**
 * 消费记录Service
 * Created by zhangxb on 2016/7/8.
 */
public interface ConsumeService extends BaseService<Consume> {
    /**
     * 获取用户区间内的分页数据
     * @param userName 用户名
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @param startTime 开始时间(yyyy-MM)
     * @param endTime 结束时间(yyyy-MM)
     * @return
     */
     Page<Consume> pageList(String userName,Integer pageNo, Integer pageSize,String startTime,String endTime);
    /**
     * 获取用户区间内的分页数据
     * @param tenantId 用户名
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
     Page<Consume> pageList(String tenantId, Integer pageNo, Integer pageSize, Date startTime, Date endTime);

    /**
     * 查询某个租户的消费记录
     */
    Page<Consume> pageListByTenantAndDate(String tenantId, Integer year, Integer month, Integer pageNo, Integer pageSize);
}
