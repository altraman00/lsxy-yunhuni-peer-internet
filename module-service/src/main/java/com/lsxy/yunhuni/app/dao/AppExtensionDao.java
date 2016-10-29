package com.lsxy.yunhuni.app.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.app.model.AppExtension;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangxb on 2016/10/21.
 */
public interface AppExtensionDao extends BaseDaoInterface<AppExtension, Serializable> {
    List<AppExtension> findByAppId(String appId);
}
