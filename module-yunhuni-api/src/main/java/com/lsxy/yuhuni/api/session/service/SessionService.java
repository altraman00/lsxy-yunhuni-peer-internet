package com.lsxy.yuhuni.api.session.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yuhuni.api.session.model.Session;

/**
 * 会话相关接口
 * Created by liups on 2016/6/29.
 */
public interface SessionService extends BaseService<Session> {
    /**
     * 根据应用ID查找当前通话数量
     * @param appId 应用ID
     * @return
     */
    Long currentSessionCount(String appId);

}
