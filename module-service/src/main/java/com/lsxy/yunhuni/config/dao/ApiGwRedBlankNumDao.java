package com.lsxy.yunhuni.config.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.config.model.ApiGwRedBlankNum;

import java.io.Serializable;

/**
 * Created by liups on 2016/8/23.
 */
public interface ApiGwRedBlankNumDao extends BaseDaoInterface<ApiGwRedBlankNum, Serializable> {
    ApiGwRedBlankNum findByNumberAndStatus(String number, int status);
}
