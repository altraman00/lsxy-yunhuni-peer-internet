package com.lsxy.msg.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.msg.api.model.MsgSupplierTemplate;
import com.lsxy.msg.api.service.MsgSupplierTemplateService;
import com.lsxy.msg.dao.MsgSupplierTemplateDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2017/3/1.
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class MsgSupplierTemplateServiceImpl extends AbstractService<MsgSupplierTemplate> implements MsgSupplierTemplateService {
    @Autowired
    MsgSupplierTemplateDao msgSupplierTemplateDao;

    @Override
    public BaseDaoInterface<MsgSupplierTemplate, Serializable> getDao() {
        return this.msgSupplierTemplateDao;
    }
}
