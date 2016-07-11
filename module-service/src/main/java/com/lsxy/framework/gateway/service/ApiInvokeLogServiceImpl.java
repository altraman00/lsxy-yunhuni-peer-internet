package com.lsxy.framework.gateway.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.gateway.service.ApiInvokeLogService;
import com.lsxy.framework.gateway.dao.ApiInvokeLogDao;
import com.lsxy.framework.api.gateway.model.ApiInvokeLog;
import com.lsxy.framework.api.sms.model.SMSSendLog;
import com.lsxy.framework.api.sms.service.SMSSendLogService;
import com.lsxy.framework.base.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by Tandy on 2016/6/24.
 */
@Service
public class ApiInvokeLogServiceImpl extends AbstractService<ApiInvokeLog> implements ApiInvokeLogService {

    @Autowired
    private ApiInvokeLogDao apiInvokeLogDao;

    @Override
    public BaseDaoInterface<ApiInvokeLog, Serializable> getDao() {
        return apiInvokeLogDao;
    }



}
