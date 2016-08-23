package com.lsxy.yunhuni.config.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.config.model.ConfigGlobal;

import java.io.Serializable;

/**
 * 配置类DAO
 * Created by zhangxb on 2016/8/23.
 */
public interface ConfigGlobalDao extends BaseDaoInterface<ConfigGlobal, Serializable> {
    ConfigGlobal findByTypeAndName(String type, String name);
}
