package com.lsxy.call.center.service;

import com.lsxy.call.center.api.model.CallCenterConversationMember;
import com.lsxy.call.center.api.service.CallCenterConversationMemberService;
import com.lsxy.call.center.dao.CallCenterConversationMemberDao;
import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liuws on 2016/11/18.
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class CallCenterConversationMemberServiceImpl extends AbstractService<CallCenterConversationMember> implements CallCenterConversationMemberService {

    @Autowired
    private CallCenterConversationMemberDao callCenterConversationMemberDao;
    @Override
    public BaseDaoInterface<CallCenterConversationMember, Serializable> getDao() {
        return callCenterConversationMemberDao;
    }
}
