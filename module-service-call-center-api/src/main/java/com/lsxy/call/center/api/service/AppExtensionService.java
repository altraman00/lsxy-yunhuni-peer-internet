package com.lsxy.call.center.api.service;

import com.lsxy.call.center.api.model.AppExtension;
import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.exceptions.api.ExtensionUserExistException;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.core.utils.Page;

import java.util.List;

/**
 * Created by zhangxb on 2016/10/21.
 */
public interface AppExtensionService extends BaseService<AppExtension> {

    /**
     * 分机注册
     * @param appExtension
     * @return
     * @throws YunhuniApiException
     */
    AppExtension register(AppExtension appExtension) throws YunhuniApiException;

    /**
     * 分机登录
     * @param tenantId
     * @param appId
     * @param user
     * @param pass
     * @return
     */
    boolean login(String tenantId,String appId,String user,String pass);

    /**
     * 根据应用id获取对于的分机
     * @param appId
     * @return
     */
    List<AppExtension> findByAppId(String appId);

    /**
     * 删除分机
     * @param extensionId
     * @param appId
     * @throws YunhuniApiException
     */
    void delete(String extensionId,String appId) throws YunhuniApiException;

    /**
     * 获取应用下的所有分机
     * @param appId
     * @param pageNo
     * @param pageSize
     * @return
     * @throws YunhuniApiException
     */
    Page<AppExtension> getPage(String appId, Integer pageNo, Integer pageSize) throws YunhuniApiException;

    /**
     * 获取一个分机
     * @param appId
     * @param extensionId
     * @return
     */
    AppExtension findOne(String appId, String extensionId);
}
