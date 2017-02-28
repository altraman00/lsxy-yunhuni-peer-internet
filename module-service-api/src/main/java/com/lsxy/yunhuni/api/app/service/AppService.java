package com.lsxy.yunhuni.api.app.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.app.model.App;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

/**
 * 应用相关接口
 * Created by liups on 2016/6/29.
 */
public interface AppService extends BaseService<App> {
    long countByTenantIdAndName(String tenantId,String name);
    /**
     * 获取记录的集合
     * @param tenantId 租户id
     * @return
     */
    List<App> findAppByUserName(String tenantId)  ;
    /**
     * 获取记录的集合
     * @param tenantId 租户id
     * @return
     */
    List<App> findAppByTenantIdAndServiceType(String tenantId, String serviceType)  ;
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

    /**
     * 已上线应用数
     * @return
     */
    long countOnline();

    /**
     * 应用总数
     * @return
     */
    long countValid();

    /**
     * 获取时间范围内创建的应用总数
     * @param d1
     * @param d2
     * @return
     */
    int countValidDateBetween(Date d1, Date d2);

    /**
     * 获取租户的app列表
     * @param tenantId
     * @return
     */
    List<App> getAppsByTenantId(String tenantId);

    /**
     * 创建应用
     * @param app
     */
    App create(App app);

    Long getExtensionPrefixNum();

    boolean enabledService(String tenantId, String appId, ServiceType service);

    void deleteApp(String appId) throws InvocationTargetException, IllegalAccessException;
}
