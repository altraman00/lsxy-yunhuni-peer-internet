package com.lsxy.msg.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.msg.api.model.MsgSupplierTemplate;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liups on 2017/3/1.
 */
public interface MsgSupplierTemplateDao extends BaseDaoInterface<MsgSupplierTemplate, Serializable> {
    List<MsgSupplierTemplate> findByAppIdAndSubaccountIdAndTempId(String appId, String subaccountId, String tempId);

    List<MsgSupplierTemplate> findByAppIdAndTempId(String appId, String tempId);

    MsgSupplierTemplate findFirstByTempIdAndSupplierCode(String tempId, String supplierCode);
    List<MsgSupplierTemplate> findByTempId(String tempId);
    List<MsgSupplierTemplate> findBySupplierTempId(String supplierTempId);
}
