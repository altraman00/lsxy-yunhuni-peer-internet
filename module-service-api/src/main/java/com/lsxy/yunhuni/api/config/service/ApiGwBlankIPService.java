package com.lsxy.yunhuni.api.config.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.config.model.ApiGwBlankIP;

/**
 * Created by Tandy on 2016/7/7.
 * API网关IP黑名单服务
 */
public interface ApiGwBlankIPService  extends BaseService<ApiGwBlankIP> {
    /**
     * 判断指定的ip是否为被列入IP黑名单
     * @param ip
     * @return
     *
     */
    boolean isBlankIP(String ip);
}
