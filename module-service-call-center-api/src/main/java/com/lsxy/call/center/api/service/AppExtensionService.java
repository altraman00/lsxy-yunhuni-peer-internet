package com.lsxy.call.center.api.service;

import com.lsxy.call.center.api.model.AppExtension;
import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.core.utils.Page;

import java.util.List;
import java.util.Map;

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
    AppExtension create(String appId,String subaccountId,AppExtension appExtension) throws YunhuniApiException;


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
    void delete(String extensionId,String appId,String subaccountId) throws YunhuniApiException;

    /**
     * 获取应用下的所有分机
     * @param appId
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<AppExtension> getPage(String appId, Integer pageNo, Integer pageSize);

    Page<AppExtension> getPage(String appId,String subaccountId, Integer pageNo, Integer pageSize);

    /**
     * 获取一个分机
     * @param appId
     * @param extensionId
     * @return
     */
    AppExtension findOne(String appId, String extensionId,String subaccountId) throws YunhuniApiException;

    /**
     * 分机登录
     * @param user
     */
    void login(String user);

    /**
     * 分机注销
     * @param user
     */
    void logout(String user);

    List<Map<String,Object>> exs(String appId);
}
