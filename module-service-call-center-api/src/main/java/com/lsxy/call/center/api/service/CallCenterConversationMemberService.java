package com.lsxy.call.center.api.service;

import com.lsxy.call.center.api.model.CallCenterConversationMember;
import com.lsxy.framework.api.base.BaseService;
import java.util.List;
/**
 * 交谈成员表
 * Created by liuws on 2016/11/18.
 */
public interface CallCenterConversationMemberService extends BaseService<CallCenterConversationMember> {
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
