package com.lsxy.msg.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.core.exceptions.api.RequestIllegalArgumentException;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.msg.api.model.MsgTemplate;
import com.lsxy.msg.api.service.MsgSupplierTemplateService;
import com.lsxy.msg.api.service.MsgTemplateService;
import com.lsxy.msg.dao.MsgTemplateDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.regex.Pattern;

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
    @Autowired
    MsgSupplierTemplateService msgSupplierTemplateService;

    @Override
    public BaseDaoInterface<MsgTemplate, Serializable> getDao() {
        return this.msgTemplateDao;
    }

    @Override
    public MsgTemplate createTemplate(MsgTemplate msgTemplate){
        msgTemplate.setTempId(getMsgTempNum());
        msgTemplate.setStatus(MsgTemplate.STATUS_WAIT);
        msgTemplate = this.save(msgTemplate);
        return msgTemplate;
    }

    public String getMsgTempNum() {
        long num = 10000 + redisCacheService.incr(MSG_TEMP_NUM);
        return num + "";
    }

    @Override
    public Page<MsgTemplate> getPageByCondition(Integer pageNo, Integer pageSize, String appId, String subaccountId, String name) {
        String hql = " from MsgTemplate obj where obj.appId=?1 ";
        if(StringUtil.isNotEmpty(subaccountId)){
            hql += " and obj.subaccountId in ("+subaccountId+") ";
        }
        if(StringUtil.isNotEmpty(name)){
            hql += " and obj.name like '%"+name+"%' ";
        }
        return pageList(hql,pageNo,pageSize,appId);
    }

    @Override
    public Page<MsgTemplate> getPageByCondition(Integer pageNo, Integer pageSize, int state,Date date1, Date date2, String[] tenantId) {
        String hql = " from MsgTemplate obj where obj.status = ?1 ";
        if(tenantId!=null&& tenantId.length>0){
            String tenantIds = "";
            for(int i=0;i<tenantId.length;i++){
                tenantIds += " '"+tenantId[i]+"' ";
                if(i!=(tenantId.length-1)){
                    tenantIds+=",";
                }
            }
            hql +=" AND obj.tenantId in("+tenantIds+") ";
        }
        if(date1!=null&&date2!=null){
            hql += " and obj.lateTime between ?2 and ?3 ";
            return pageList(hql,pageNo,pageSize,state,date1,date2);
        }else{
            return pageList(hql,pageNo,pageSize,state);
        }
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
    public void deleteMsgTemplate(String appId, String subaccountId, String tempId, boolean isGW) throws InvocationTargetException, IllegalAccessException {
        if(StringUtils.isNotBlank(subaccountId) || isGW){
            MsgTemplate msgTemplate = msgTemplateDao.findByAppIdAndSubaccountIdAndTempId(appId, subaccountId, tempId);
            this.delete(msgTemplate);
        }else {
            MsgTemplate msgTemplate = msgTemplateDao.findByAppIdAndTempId(appId,tempId);
            this.delete(msgTemplate);
        }
        msgSupplierTemplateService.deleteMsgTemplate(appId, subaccountId, tempId, isGW);
    }

    @Override
    public MsgTemplate updateMsgTemplate(MsgTemplate msgTemplate,boolean isGW) throws YunhuniApiException {
        String tempId = msgTemplate.getTempId();
        if(StringUtils.isBlank(tempId) || StringUtils.isBlank(msgTemplate.getAppId())){
            throw new RequestIllegalArgumentException();
        }
        MsgTemplate oldTemplate;
        if(StringUtils.isNotBlank(msgTemplate.getSubaccountId()) || isGW){
            oldTemplate = msgTemplateDao.findByAppIdAndSubaccountIdAndTempId(msgTemplate.getAppId(), msgTemplate.getSubaccountId(), tempId);
        }else {
            oldTemplate = msgTemplateDao.findByAppIdAndTempId(msgTemplate.getAppId(),tempId);
        }
        if(oldTemplate == null){
            throw new RequestIllegalArgumentException();
        }
        boolean flag = false;
        if(StringUtils.isNotBlank(msgTemplate.getName())){
            flag = true;
            oldTemplate.setName(msgTemplate.getName());
        }
        if(StringUtils.isNotBlank(msgTemplate.getType())){
            flag = true;
            oldTemplate.setName(msgTemplate.getType());
        }
        if(StringUtils.isNotBlank(msgTemplate.getContent())){
            flag = true;
            oldTemplate.setName(msgTemplate.getContent());
        }
        if(StringUtils.isNotBlank(msgTemplate.getRemark())){
            flag = true;
            oldTemplate.setName(msgTemplate.getReason());
        }
        if(flag){
            oldTemplate.setStatus(MsgTemplate.STATUS_WAIT);
            this.save(oldTemplate);
        }
        return oldTemplate;
    }

    @Override
    public long findByWait() {
        String hql2 = "  from MsgTemplate obj where obj.status=?1  ";
        return this.countByCustom(hql2, MsgTemplate.STATUS_WAIT);
    }

}
