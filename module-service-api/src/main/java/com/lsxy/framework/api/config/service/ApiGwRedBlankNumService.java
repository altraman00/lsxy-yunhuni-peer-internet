package com.lsxy.framework.api.config.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.config.model.ApiGwRedBlankNum;

/**
 * Created by liups on 2016/8/23.
 */
public interface ApiGwRedBlankNumService  extends BaseService<ApiGwRedBlankNum> {
    boolean isRedOrBlankNum(String number);
}
