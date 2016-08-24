package com.lsxy.yunhuni.api.config.service;

import com.lsxy.yunhuni.api.config.model.GlobalConfig;

/**
 * 配置类接口
 * Created by zhangxb on 2016/8/23.
 */
public interface GlobalConfigService {
    /**
     * 获取配置对象
     * @param type 类型
     * @param name 名字
     * @return
     */
    public GlobalConfig findByTypeAndName(String type, String name);
}
