package com.lsxy.framework.consume.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.consume.model.BillDay;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by liups on 2016/7/22.
 */
public interface BillDayDao extends BaseDaoInterface<BillDay,Serializable> {
    List<BillDay> findByTenantIdAndAppIdAndDt(String tenantId, String appId, Date dt);
}
