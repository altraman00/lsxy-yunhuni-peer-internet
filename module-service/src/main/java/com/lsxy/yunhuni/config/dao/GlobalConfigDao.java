package com.lsxy.yunhuni.config.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.config.model.GlobalConfig;

import java.io.Serializable;

/**
 * 配置类DAO
 * Created by zhangxb on 2016/8/23.
 */
public interface GlobalConfigDao extends BaseDaoInterface<GlobalConfig, Serializable> {
    GlobalConfig findByTypeAndName(String type, String name);
    GlobalConfig findByTypeAndKeyName(String type,String keyName);
}
