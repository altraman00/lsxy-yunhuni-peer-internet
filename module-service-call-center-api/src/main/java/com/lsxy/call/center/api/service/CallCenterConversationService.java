package com.lsxy.call.center.api.service;

import com.lsxy.call.center.api.model.CallCenterConversation;
import com.lsxy.call.center.api.model.CallCenterConversationDetail;
import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.core.utils.Page;

/**
 * Created by liuws on 2016/11/18.
 */
public interface CallCenterConversationService extends BaseService<CallCenterConversation> {

    public CallCenterConversationDetail detail(String subaccountId, String ip, String appId, String conversationId) throws YunhuniApiException;

    public Page<CallCenterConversationDetail> conversationPageList(String subaccountId,String ip, String appId, int page, int size) throws YunhuniApiException;
}
