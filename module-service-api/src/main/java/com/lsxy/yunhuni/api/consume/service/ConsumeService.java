package com.lsxy.yunhuni.api.consume.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.consume.model.Consume;
import com.lsxy.framework.core.utils.Page;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 消费记录Service
 * Created by zhangxb on 2016/7/8.
 */
public interface ConsumeService extends BaseService<Consume> {
    /**
     * 获取用户区间内的数据
     * @param userName
     * @param startTime
     * @param endTime
     * @param appId
     * @return
     */
    List<Consume> listConsume(String userName,String startTime,String endTime,String appId);
    /**
     * 获取用户区间内的分页数据
     * @param userName 用户名
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @param startTime 开始时间(yyyy-MM)
     * @param endTime 结束时间(yyyy-MM)
     * @return
     */
    Page<Consume> pageList(String userName,Integer pageNo, Integer pageSize,String startTime,String endTime,String appId);
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

    /**
     * 进行消费
     */
    void consume(Consume consume);

    /**
     * 获取租户一个时间段的消费金额
     * @param tenantId 租户ID
     * @param startDate 开始时间（大于等于，非空）
     * @param endDate 结束时间 （小于，非空）
     * @return
     */
    BigDecimal getConsumeByTenantIdAndDate(String tenantId, Date startDate, Date endDate);

    /**
     * 获取应用一个时间段的消费金额
     * @param appId
     * @param startDate 开始时间（大于等于，非空）
     * @param endDate 结束时间 （小于，非空）
     * @return
     */
    BigDecimal getConsumeByAppIdAndDate(String appId,Date startDate, Date endDate);

    /**
     * 批量消费
     * @param dt
     * @param type
     * @param cost
     * @param remark
     * @param appId
     * @param tenantId
     * @param subaccountId
     * @param detailIds
     */
    void batchConsume(Date dt, String type, BigDecimal cost, String remark, String appId, String tenantId, String subaccountId, List<String> detailIds);
}
