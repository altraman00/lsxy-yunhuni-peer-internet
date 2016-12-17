package com.lsxy.call.center.api.service;

import com.lsxy.call.center.api.model.CallCenter;
import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.utils.Page;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangxb on 2016/10/22.
 */
public interface CallCenterService  extends BaseService<CallCenter> {
    /**
     *  根据以下条件获取分页信息
     * @param tenantId 所属租户
     * @param appId 所属应用
     * @param startTime 开始时间
     * @param endTime 结束事件
     * @param type 呼叫类型
     * @param agent 坐席
     * @return
     */
    Page<CallCenter> pList(Integer pageNo,Integer pageSize,String tenantId,String appId,String startTime,String endTime,String type,String callnum,String agent);
    /**
     *  根据以下条件获取分页信息
     * @param tenantId 所属租户
     * @param appId 所属应用
     * @param startTime 开始时间
     * @param endTime 结束事件
     * @param type 呼叫类型
     * @param agent 坐席
     * @return
     */
    List<CallCenter> getAllList(String tenantId, String appId, String startTime, String endTime, String type, String callnum, String agent);
    /**
     *  根据以下条件获取分页信息
     * @param tenantId 所属租户
     * @param appId 所属应用
     * @param startTime 开始时间
     * @param endTime 结束事件
     * @param type 呼叫类型
     * @param agent 坐席
     * @return
     */
    Map sum(String tenantId, String appId, String startTime, String endTime, String type,String callnum, String agent);

    void incrCost(String callCenterId, double cost);
}
