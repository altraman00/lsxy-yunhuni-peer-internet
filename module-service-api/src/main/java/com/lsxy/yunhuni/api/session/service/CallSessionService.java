package com.lsxy.yunhuni.api.session.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.session.model.CallSession;

/**
 * 会话相关接口
 * Created by liups on 2016/6/29.
 */
public interface CallSessionService extends BaseService<CallSession> {
    /**
     * 根据应用ID查找当前通话数量
     * @param appId 应用ID
     * @return
     */
    Long currentCallSessionCount(String appId);

}
