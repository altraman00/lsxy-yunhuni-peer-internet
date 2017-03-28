package com.lsxy.msg.api.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.msg.api.model.MsgSupplier;

import java.util.List;

/**
 * Created by liups on 2017/3/1.
 */
public interface MsgSupplierService extends BaseService<MsgSupplier> {
    MsgSupplier findByCode(String code);
    List<MsgSupplier> findByCodeAndOperatorAndSendTypeAndIsMassAndOrder(List<String> code, String operator, String sendType, int isMass, String order);
}
