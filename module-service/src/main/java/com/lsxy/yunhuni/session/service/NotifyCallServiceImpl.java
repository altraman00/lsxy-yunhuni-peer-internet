package com.lsxy.yunhuni.session.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.session.model.NotifyCall;
import com.lsxy.yunhuni.api.session.service.NotifyCallService;
import com.lsxy.yunhuni.session.dao.NotifyCallDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2016/9/6.
 */
@Service
public class NotifyCallServiceImpl extends AbstractService<NotifyCall> implements NotifyCallService {
    @Autowired
    NotifyCallDao notifyCallDao;
    @Override
    public BaseDaoInterface<NotifyCall, Serializable> getDao() {
        return this.notifyCallDao;
    }
}
