package com.lsxy.yunhuni.statistics.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.statistics.model.ConsumeDay;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 消费日统计DAO
 * Created by zhangxb on 2016/7/6.
 */
public interface ConsumeDayDao extends BaseDaoInterface<ConsumeDay,Serializable> {
    /**
     * 获取用户的消费日账单
     * @param tenantId 租户Id
     * @param dt 时间
     * @param appId 应用Id
     * @return
     */
    List<ConsumeDay> findByTenantIdAndDtAndAppIdAndTypeNotNull(String tenantId, Date dt, String appId);

    /**
     * 获取用户的消费日账单 （所有应用）
     * @param tenantId
     * @param dt
     * @return
     */
    List<ConsumeDay> findByTenantIdAndDtAndAppIdIsNullAndTypeNotNull(String tenantId, Date dt);


}
