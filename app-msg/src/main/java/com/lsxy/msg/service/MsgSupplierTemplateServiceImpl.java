package com.lsxy.msg.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.msg.api.model.MsgSupplierTemplate;
import com.lsxy.msg.api.model.MsgTemplate;
import com.lsxy.msg.api.service.MsgSupplierTemplateService;
import com.lsxy.msg.dao.MsgSupplierTemplateDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

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

    @Override
    public void deleteMsgTemplate(String appId, String subaccountId, String tempId, boolean isGW) throws InvocationTargetException, IllegalAccessException{
        if(StringUtils.isNotBlank(subaccountId) || isGW){
            List<MsgSupplierTemplate> supTemplates = msgSupplierTemplateDao.findByAppIdAndSubaccountIdAndTempId(appId, subaccountId, tempId);
            for(MsgSupplierTemplate supTemplate : supTemplates){
                this.delete(supTemplate);
            }
        }else {
            List<MsgSupplierTemplate> supTemplates = msgSupplierTemplateDao.findByAppIdAndTempId(appId,tempId);
            for(MsgSupplierTemplate supTemplate : supTemplates){
                this.delete(supTemplate);
            }
        }
    }

    @Override
    public String findSupplierTempIdByTempIdAndSupplierCode(String tempId, String supplierCode) {
        MsgSupplierTemplate template = msgSupplierTemplateDao.findFirstByTempIdAndSupplierCode(tempId,supplierCode);
        if(template != null){
            return template.getSupplierTempId();
        }else{
            return null;
        }
    }

}
