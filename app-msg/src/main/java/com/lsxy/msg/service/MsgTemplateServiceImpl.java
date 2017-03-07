package com.lsxy.msg.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.exceptions.api.RequestIllegalArgumentException;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.msg.api.model.MsgTemplate;
import com.lsxy.msg.api.service.MsgTemplateService;
import com.lsxy.msg.dao.MsgTemplateDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2017/3/1.
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class MsgTemplateServiceImpl extends AbstractService<MsgTemplate> implements MsgTemplateService {
    public final static String MSG_TEMP_NUM = "MSG_TEMP_NUM";

    @Autowired
    MsgTemplateDao msgTemplateDao;
    @Autowired
    private RedisCacheService redisCacheService;

    @Override
    public BaseDaoInterface<MsgTemplate, Serializable> getDao() {
        return this.msgTemplateDao;
    }

    @Override
    public MsgTemplate createTemplate(MsgTemplate msgTemplate){
        msgTemplate.setTempId(getMsgTempNum());
        msgTemplate.setStatus(1);
        msgTemplate.setSync(false);
        this.save(msgTemplate);
        return msgTemplate;
    }

    public String getMsgTempNum() {
        long num = 10000 + redisCacheService.incr(MSG_TEMP_NUM);
        return num + "";
    }

    @Override
    public Page<MsgTemplate> getPageForGW(String appId, String subaccountId, Integer pageNo, Integer pageSize){
        Page<MsgTemplate> page;
        if(StringUtils.isBlank(subaccountId)){
            String hql = "from MsgTemplate obj where obj.appId=?1 and obj.subaccountId is null ";
            page = this.pageList(hql,pageNo,pageSize,appId);
        }else{
            String hql = "from MsgTemplate obj where obj.appId=?1 and obj.subaccountId = ?2 ";
            page = this.pageList(hql,pageNo,pageSize,appId,subaccountId);
        }
        return page;
    }

    @Override
    public MsgTemplate findByTempId(String appId, String subaccountId, String tempId, boolean isGW){
        if(StringUtils.isNotBlank(subaccountId) || isGW){
            return msgTemplateDao.findByAppIdAndSubaccountIdAndTempId(appId,subaccountId,tempId);
        }else{
            return msgTemplateDao.findByAppIdAndTempId(appId,tempId);
        }
    }

    @Override
    public void deleteMsgTemplate(String appId, String subaccountId, String tempId, boolean isGW){
        if(StringUtils.isNotBlank(subaccountId) || isGW){
            msgTemplateDao.deleteByAppIdAndSubaccountIdAndTempId(appId,subaccountId,tempId);
        }else {
            msgTemplateDao.deleteByAppIdAndTempId(appId,tempId);
        }
    }

    public void updateMsgTemplate(MsgTemplate msgTemplate,boolean isGW) throws YunhuniApiException {
        String tempId = msgTemplate.getTempId();
        if(StringUtils.isBlank(tempId) || StringUtils.isBlank(msgTemplate.getAppId())){
            throw new RequestIllegalArgumentException();
        }
        if (StringUtils.isNotBlank(msgTemplate.getSubaccountId()) || isGW) {
//            msgTemplateDao.updateMsgTemplate()
        }

    }

}
