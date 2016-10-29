package com.lsxy.yunhuni.api.app.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.app.model.AppExtension;

import java.util.List;

/**
 * Created by zhangxb on 2016/10/21.
 */
public interface AppExtensionService extends BaseService<AppExtension> {
    /**
     * 根据应用id获取对于的分机
     * @param appId
     * @return
     */
    List<AppExtension> findByAppId(String appId);
}
