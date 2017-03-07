package com.lsxy.msg.api.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.msg.api.model.MsgTemplate;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by liups on 2017/3/1.
 */
public interface MsgTemplateService extends BaseService<MsgTemplate> {
    MsgTemplate createTemplate(MsgTemplate msgTemplate);

    Page<MsgTemplate> getPageForGW(String appId, String subaccountId, Integer pageNo, Integer pageSize);

    MsgTemplate findByTempId(String appId, String subaccountId, String tempId, boolean isGW);

    void deleteMsgTemplate(String appId, String subaccountId, String tempId, boolean isGW) throws InvocationTargetException, IllegalAccessException;

    void updateMsgTemplate(MsgTemplate msgTemplate, boolean isGW) throws YunhuniApiException;
}
