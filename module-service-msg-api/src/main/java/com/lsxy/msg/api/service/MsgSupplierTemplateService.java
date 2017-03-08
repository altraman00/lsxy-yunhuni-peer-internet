package com.lsxy.msg.api.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.msg.api.model.MsgSupplierTemplate;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by liups on 2017/3/1.
 */
public interface MsgSupplierTemplateService extends BaseService<MsgSupplierTemplate> {
    void deleteMsgTemplate(String appId, String subaccountId, String tempId, boolean isGW) throws InvocationTargetException, IllegalAccessException;
}
