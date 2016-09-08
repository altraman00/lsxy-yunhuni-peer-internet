package com.lsxy.yunhuni.config.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.config.model.ApiGwRedBlankNum;
import com.lsxy.yunhuni.api.config.service.ApiGwRedBlankNumService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.config.dao.ApiGwRedBlankNumDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2016/8/23.
 */
@Service
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

    @Override
    public boolean isRedNum(String number) {
        ApiGwRedBlankNum result = apiGwRedBlankNumDao.findByNumberAndTypeAndStatus(number,ApiGwRedBlankNum.TYPE_RED, ApiGwRedBlankNum.STATUS_ENABLED);
        return result != null;
    }

    @Override
    public boolean isBlankNum(String number) {
        ApiGwRedBlankNum result = apiGwRedBlankNumDao.findByNumberAndTypeAndStatus(number,ApiGwRedBlankNum.TYPE_BLACK, ApiGwRedBlankNum.STATUS_ENABLED);
        return result != null;
    }
}
