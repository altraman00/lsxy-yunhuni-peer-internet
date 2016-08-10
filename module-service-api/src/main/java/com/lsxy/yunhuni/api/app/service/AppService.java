package com.lsxy.yunhuni.api.app.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.app.model.App;

import java.util.List;

/**
 * 应用相关接口
 * Created by liups on 2016/6/29.
 */
public interface AppService extends BaseService<App> {
    /**
     * 获取记录的集合
     * @param tenantId 租户id
     * @return
     */
    List<App> findAppByUserName(String tenantId)  ;

    /**
     * 获取分页信息
     * @param tenantId 租户id
     * @param pageNo 第几页
     * @param pageSize 每页面记录数
     * @return
     */
    Page<App> pageList(String tenantId,Integer pageNo,Integer pageSize);

    /**
     * 应用是否属于用户
     * @param userName
     * @param appId
     * @return
     */
    boolean isAppBelongToUser(String userName, String appId);
}
