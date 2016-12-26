package com.lsxy.yunhuni.api.config.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.config.model.GlobalConfig;

/**
 * 配置类接口
 * Created by zhangxb on 2016/8/23.
 */
public interface GlobalConfigService extends BaseService<GlobalConfig> {
    /**
     * 获取配置对象
     * @param type 类型
     * @param name 名字
     * @return
     */
    GlobalConfig findByTypeAndName(String type, String name);
    GlobalConfig findByTypeAndKeyName(String type,String keyName);
}
