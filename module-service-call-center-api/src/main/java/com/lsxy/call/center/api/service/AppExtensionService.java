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

    String register(AppExtension appExtension) throws YunhuniApiException;

    boolean login(String tenantId,String appId,String user,String pass);

    /**
     * 根据应用id获取对于的分机
     * @param appId
     * @return
     */
    List<AppExtension> findByAppId(String appId);

    void delete(String extensionId,String appId) throws YunhuniApiException;


    Page<AppExtension> getPage(String appId, Integer pageNo, Integer pageSize) throws YunhuniApiException;
}
