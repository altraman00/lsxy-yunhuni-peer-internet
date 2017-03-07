package com.lsxy.msg.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.msg.api.model.MsgSupplier;
import com.lsxy.msg.api.service.MsgSupplierService;
import com.lsxy.msg.dao.MsgSupplierDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2017/3/1.
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class MsgSupplierServiceImpl extends AbstractService<MsgSupplier> implements MsgSupplierService {
    @Autowired
    MsgSupplierDao msgSupplierDao;
    @Override
    public BaseDaoInterface<MsgSupplier, Serializable> getDao() {
        return this.msgSupplierDao;
    }
}
