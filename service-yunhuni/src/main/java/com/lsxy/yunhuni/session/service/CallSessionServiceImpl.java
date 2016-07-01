package com.lsxy.yunhuni.session.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yuhuni.api.app.model.App;
import com.lsxy.yuhuni.api.session.model.CallSession;
import com.lsxy.yuhuni.api.session.service.CallSessionService;
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
        App app = new App();
        app.setId(appId);
        return callSessionDao.countByStatusAndApp(CallSession.STATUS_CALLING,app);
    }
}
