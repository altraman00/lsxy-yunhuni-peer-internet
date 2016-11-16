package com.lsxy.yunhuni.config.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.config.model.AreaSip;

import java.io.Serializable;

/**
 * Created by liups on 2016/11/1.
 */
public interface AreaSipDao  extends BaseDaoInterface<AreaSip, Serializable> {
    /**
     * 获取这个区域第一个sip接入点信息
     * @param areaId
     * @return
     */
    AreaSip findFirstByAreaId(String areaId);
}
