package com.lsxy.yunhuni.session.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yuhuni.api.app.model.App;
import com.lsxy.yuhuni.api.session.model.CallSession;

import java.io.Serializable;

/**
 * Created by liups on 2016/6/29.
 */
public interface CallSessionDao extends BaseDaoInterface<CallSession, Serializable> {
    Long countByStatusAndApp(int status,App app);
}
