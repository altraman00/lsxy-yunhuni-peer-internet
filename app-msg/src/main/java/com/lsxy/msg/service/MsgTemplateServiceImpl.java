package com.lsxy.msg.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.msg.api.model.MsgTemplate;
import com.lsxy.msg.api.service.MsgTemplateService;
import com.lsxy.msg.dao.MsgTemplateDao;
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
}
