package com.lsxy.yunhuni.session.service;

import com.lsxy.framework.core.persistence.BaseDaoInterface;
import com.lsxy.framework.core.service.AbstractService;
import com.lsxy.yuhuni.app.model.App;
import com.lsxy.yuhuni.session.model.Session;
import com.lsxy.yuhuni.session.service.SessionService;
import com.lsxy.yunhuni.session.dao.SessionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2016/6/29.
 */
@Service
public class SessionServiceImpl extends AbstractService<Session> implements SessionService {

    @Autowired
    private SessionDao sessionDao;

    @Override
    public BaseDaoInterface<Session, Serializable> getDao() {
        return this.sessionDao;
    }

    @Override
    public Long currentSessionCount(String appId) {
        App app = new App();
        app.setId(appId);
        return sessionDao.countByStatusAndApp(Session.STATUS_CALLING,app);
    }
}
