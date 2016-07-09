package com.lsxy.framework.config.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.config.model.ApiGwBlankIP;
import com.lsxy.framework.api.config.service.ApiGwBlankIPService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.config.dao.ApiGwBlankIPDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by Tandy on 2016/7/7.
 */
@Service
public class ApiGwBlankIPServiceImpl extends AbstractService<ApiGwBlankIP> implements ApiGwBlankIPService {
    @Autowired
    ApiGwBlankIPDao apiGwBlankIPDao;
    @Override
    public BaseDaoInterface<ApiGwBlankIP, Serializable> getDao() {
        return apiGwBlankIPDao;
    }

    @Override
    public boolean isBlankIP(String ip) {
        ApiGwBlankIP result = apiGwBlankIPDao.findByIpAndStatus(ip,ApiGwBlankIP.ST_ENABLED);
        //如果不为null 表示指定的ip被指定为黑名单
        return result != null;
    }
}
