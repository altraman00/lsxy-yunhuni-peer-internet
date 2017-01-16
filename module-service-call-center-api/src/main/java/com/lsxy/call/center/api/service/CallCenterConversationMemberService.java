package com.lsxy.call.center.api.service;

import com.lsxy.call.center.api.model.CallCenterConversationMember;
import com.lsxy.framework.api.base.BaseService;

import java.util.List;

/**
 * 呼叫中心交互成员
 * Created by zhangxb on 2016/11/11.
 */
public interface CallCenterConversationMemberService  extends BaseService<CallCenterConversationMember> {
    /**
     * 根据所属session查找全部交互成员
     * @param sessionId
     * @return
     */
    List<String> getListBySessionId(String sessionId);

    /**
     * 根据交谈id和callid 获取交谈成员
     * @param relevanceId
     * @param callId
     * @return
     */
    public CallCenterConversationMember findOne(String relevanceId,String callId);

    public List<CallCenterConversationMember> list(String conversationId);

    public void updateMode(String conversationId,String callId, int mode);
}
