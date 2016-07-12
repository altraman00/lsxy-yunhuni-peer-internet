package com.lsxy.framework.consume.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.consume.model.BillMonth;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 月结账单查询Dao
 * Created by liups on 2016/7/11.
 */
public interface BillMonthDao extends BaseDaoInterface<BillMonth,Serializable> {
    List<BillMonth> findByTenantIdAndAppIdAndDt(String tenantId, String appId, Date dt);

}
