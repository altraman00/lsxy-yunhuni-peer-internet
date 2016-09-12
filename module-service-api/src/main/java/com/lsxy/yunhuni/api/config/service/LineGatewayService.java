package com.lsxy.yunhuni.api.config.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.config.model.LineGateway;

/**
 * Created by liups on 2016/8/24.
 */
public interface LineGatewayService extends BaseService<LineGateway> {
    LineGateway getBestLineGatewayByNumber(String number);
}
