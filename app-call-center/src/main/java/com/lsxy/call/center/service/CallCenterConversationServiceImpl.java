package com.lsxy.call.center.service;

import com.lsxy.call.center.api.model.CallCenterConversation;
import com.lsxy.call.center.api.service.CallCenterConversationService;
import com.lsxy.call.center.dao.CallCenterConversationDao;
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
public class CallCenterConversationServiceImpl extends AbstractService<CallCenterConversation> implements CallCenterConversationService {

    @Autowired
    private CallCenterConversationDao callCenterConversationDao;
    @Override
    public BaseDaoInterface<CallCenterConversation, Serializable> getDao() {
        return callCenterConversationDao;
    }
}
