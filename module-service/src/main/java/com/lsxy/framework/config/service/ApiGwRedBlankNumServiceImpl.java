package com.lsxy.framework.config.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.config.model.ApiGwRedBlankNum;
import com.lsxy.framework.api.config.service.ApiGwRedBlankNumService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.config.dao.ApiGwRedBlankNumDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * Created by liups on 2016/8/23.
 */
public class ApiGwRedBlankNumServiceImpl extends AbstractService<ApiGwRedBlankNum> implements ApiGwRedBlankNumService {
    @Autowired
    ApiGwRedBlankNumDao apiGwRedBlankNumDao;

    @Override
    public BaseDaoInterface<ApiGwRedBlankNum, Serializable> getDao() {
        return this.apiGwRedBlankNumDao;
    }

    @Override
    public boolean isRedOrBlankNum(String number) {
        ApiGwRedBlankNum result = apiGwRedBlankNumDao.findByNumberAndStatus(number, ApiGwRedBlankNum.STATUS_ENABLED);
        return result != null;
    }
}
