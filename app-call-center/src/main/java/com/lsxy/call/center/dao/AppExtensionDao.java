package com.lsxy.call.center.dao;

import com.lsxy.call.center.api.model.AppExtension;
import com.lsxy.framework.api.base.BaseDaoInterface;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangxb on 2016/10/21.
 *
 */
public interface AppExtensionDao extends BaseDaoInterface<AppExtension, Serializable> {
    /**
     * 列出所有的分机
     * @param appId
     * @return
     */
    List<AppExtension> findByAppId(String appId);

    /**
     * 根据用户名查看分机是否存在
     * @param user
     * @return
     */
    long countByUser(String user);

    /**
     * 根据用户名查看分机
     * @param user
     * @return
     */
    AppExtension findByUser(String user);
}
