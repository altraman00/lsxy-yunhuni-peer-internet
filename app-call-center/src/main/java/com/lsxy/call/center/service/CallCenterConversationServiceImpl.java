package com.lsxy.call.center.service;

import com.lsxy.call.center.api.model.CallCenterConversation;
import com.lsxy.call.center.api.model.CallCenterConversationDetail;
import com.lsxy.call.center.api.model.CallCenterConversationMember;
import com.lsxy.call.center.api.service.CallCenterConversationMemberService;
import com.lsxy.call.center.api.service.CallCenterConversationService;
import com.lsxy.call.center.dao.CallCenterConversationDao;
import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.exceptions.api.*;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.app.service.ServiceType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuws on 2016/11/18.
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class CallCenterConversationServiceImpl extends AbstractService<CallCenterConversation> implements CallCenterConversationService {

    @Autowired
    private CallCenterConversationDao callCenterConversationDao;

    @Autowired
    private AppService appService;

    @Autowired
    private CallCenterConversationMemberService callCenterConversationMemberService;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public BaseDaoInterface<CallCenterConversation, Serializable> getDao() {
        return callCenterConversationDao;
    }

    @Override
    public CallCenterConversationDetail detail(String ip, String appId, String conversationId) throws YunhuniApiException {
        if(StringUtils.isBlank(conversationId)){
            throw new RequestIllegalArgumentException();
        }
        App app = appService.findById(appId);
        if(app == null){
            throw new AppNotFoundException();
        }
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException();
        }
        CallCenterConversation conversation = applicationContext.getBean(CallCenterConversationService.class).findById(conversationId);
        if(conversation == null){
            return null;
        }
        CallCenterConversationDetail detail = new CallCenterConversationDetail();

        detail.setId(conversation.getId());
        detail.setType(conversation.getType());
        detail.setChannelId(conversation.getChannelId());
        detail.setQueueId(conversation.getQueueId());
        detail.setStartTime(conversation.getStartTime());
        detail.setEndTime(conversation.getEndTime());
        detail.setEndReason(conversation.getEndReason());

        List<CallCenterConversationMember> cms = callCenterConversationMemberService.list(conversationId);
        if(cms != null && cms.size() > 0){
            List<CallCenterConversationDetail.MemberDetail> members = new ArrayList<>();
            for (CallCenterConversationMember cm : cms) {
                CallCenterConversationDetail.MemberDetail member = new CallCenterConversationDetail.MemberDetail();
                member.setAgentName(cm.getAgentName());
                member.setExtensionId(cm.getExtensionId());
                member.setTelnumber(cm.getJoinNum());
                member.setCallId(cm.getCallId());
                member.setStartTime(cm.getStartTime());
                member.setEndTime(cm.getEndTime());
                member.setMode(cm.getMode());
                members.add(member);
            }
            detail.setMembers(members);
        }
        return detail;
    }

    @Override
    public Page<CallCenterConversationDetail> pageList(String ip, String appId, int page, int size) throws YunhuniApiException {
        App app = appService.findById(appId);
        if(app == null){
            throw new AppNotFoundException();
        }
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException();
        }
        Page<CallCenterConversation> queryResult = pageList("from CallCenterConversation obj where obj.state=?1",
                page,size,CallCenterConversation.STATE_READY);

        Page<CallCenterConversationDetail> result = new Page<>(queryResult.getStartIndex(),queryResult.getTotalCount(),queryResult.getPageSize(),null);

        List<CallCenterConversation> list = queryResult.getResult();
        if(list != null && list.size() > 0){
            List<CallCenterConversationDetail> ds = new ArrayList<>(list.size());
            for (CallCenterConversation conversation : list) {
                CallCenterConversationDetail detail = new CallCenterConversationDetail();
                detail.setId(conversation.getId());
                detail.setType(conversation.getType());
                detail.setChannelId(conversation.getChannelId());
                detail.setQueueId(conversation.getQueueId());
                detail.setStartTime(conversation.getStartTime());//发起时间
                detail.setEndTime(conversation.getEndTime());//结束时间
                detail.setEndReason(conversation.getEndReason());
                ds.add(detail);
            }
            result.setResult(ds);
        }
        return result;
    }
}
