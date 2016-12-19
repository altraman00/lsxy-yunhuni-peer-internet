package com.lsxy.yunhuni.api.config.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.config.model.ApiGwRedBlankNum;

/**
 * Created by liups on 2016/8/23.
 */
public interface ApiGwRedBlankNumService  extends BaseService<ApiGwRedBlankNum> {
    boolean isRedNum(String number);
    boolean isBlackNum(String number);
    Page<ApiGwRedBlankNum> getPage(Integer pageNo,Integer pageSize,Integer type);
    long findByNumber(String number);
}
