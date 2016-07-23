package com.lsxy.framework.consume.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.consume.model.ConsumeMonth;

import java.io.Serializable;
import java.util.Date;

/**
 * 消费月统计DAO
 * Created by zhangxb on 2016/7/6.
 */
public interface ConsumeMonthDao extends BaseDaoInterface<ConsumeMonth,Serializable> {
    /**
     * 获取用户开始消费的第一个月统计数据
     * @param tenantId
     * @return
     */
    ConsumeMonth findFirst1ByTenantIdOrderByDtAsc(String tenantId);

}
