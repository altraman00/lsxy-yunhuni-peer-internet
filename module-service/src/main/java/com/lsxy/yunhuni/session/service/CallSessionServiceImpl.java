package com.lsxy.yunhuni.session.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.session.model.CallSession;
import com.lsxy.yunhuni.api.session.service.CallSessionService;
import com.lsxy.yunhuni.session.dao.CallSessionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2016/6/29.
 */
@Service
public class CallSessionServiceImpl extends AbstractService<CallSession> implements CallSessionService {

    @Autowired
    private CallSessionDao callSessionDao;

    @Override
    public BaseDaoInterface<CallSession, Serializable> getDao() {
        return this.callSessionDao;
    }

    @Override
    public Long currentCallSessionCount(String appId) {
        return callSessionDao.countByStatusAndAppId(CallSession.STATUS_CALLING,appId);
    }

    @Override
    public void updateStatusByRelevanceId(String relevanceId, Integer status) {
        if(relevanceId == null){
            return ;
        }
        if(status == null){
            return;
        }
        callSessionDao.updateStatusByRelevanceId(relevanceId,status);
    }
}
