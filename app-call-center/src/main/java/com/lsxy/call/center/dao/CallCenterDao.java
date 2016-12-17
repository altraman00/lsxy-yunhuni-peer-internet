package com.lsxy.call.center.dao;

import com.lsxy.call.center.api.model.CallCenter;
import com.lsxy.framework.api.base.BaseDaoInterface;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 呼叫中心系统
 * Created by zhangxb on 2016/10/22.
 */
public interface CallCenterDao extends BaseDaoInterface<CallCenter, Serializable> {

    @Modifying
    @Query("update CallCenter a set a.cost=a.cost+:cost where a.id=:id")
    public void incrCost(@Param("id") String id,@Param("cost") BigDecimal cost);
}
