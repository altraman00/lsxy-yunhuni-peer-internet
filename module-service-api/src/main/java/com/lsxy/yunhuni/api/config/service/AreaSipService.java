package com.lsxy.yunhuni.api.config.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.config.model.AreaSip;

/**
 * Created by liups on 2016/11/1.
 */
public interface AreaSipService extends BaseService<AreaSip> {
    /**
     * 根据区域Id获取一个sip接入信息
     * @param areaId
     * @return
     */
    AreaSip getOneAreaSipByAreaId(String areaId);
}
