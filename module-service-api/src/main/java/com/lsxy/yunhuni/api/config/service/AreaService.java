package com.lsxy.yunhuni.api.config.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.config.model.Area;

/**
 * Created by liups on 2016/8/24.
 */
public interface AreaService  extends BaseService<Area> {
    /**
     * 获取一个有效的区域，用于应用上线绑定区域
     * @return
     */
    Area getOneAvailableArea();
}
