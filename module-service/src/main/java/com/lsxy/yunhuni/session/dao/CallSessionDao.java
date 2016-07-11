package com.lsxy.yunhuni.session.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.session.model.CallSession;

import java.io.Serializable;

/**
 * 会话查询类
 * Created by liups on 2016/6/29.
 */
public interface CallSessionDao extends BaseDaoInterface<CallSession, Serializable> {
    /**
     * 根据状态获取应用的会话条数
     * @param status 会话状态
     * @param app 应用
     * @return 条数
     */
    Long countByStatusAndApp(int status,App app);
}
