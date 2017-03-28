package com.lsxy.msg.api.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.msg.api.model.MsgSupplierTemplate;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by liups on 2017/3/1.
 */
public interface MsgSupplierTemplateService extends BaseService<MsgSupplierTemplate> {
    void deleteMsgTemplate(String appId, String subaccountId, String tempId, boolean isGW) throws InvocationTargetException, IllegalAccessException;

    String findSupplierTempIdByTempIdAndSupplierCode(String tempId, String supplierCode);
    List<MsgSupplierTemplate> findByTempId(String tempId);
    List<MsgSupplierTemplate> findByTempIdAndStatus(String tempId,Integer status);
    List<MsgSupplierTemplate> findBySupplierTempId(String supplierTempId);
}
