package com.lsxy.yunhuni.session.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.session.model.CaptchaCall;
import com.lsxy.yunhuni.api.session.service.CaptchaCallService;
import com.lsxy.yunhuni.session.dao.CaptchaCallDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by zhangxb on 2016/7/19.
 */
@Service
public class CaptchaCallServiceImpl extends AbstractService<CaptchaCall> implements CaptchaCallService {
    @Autowired
    private CaptchaCallDao captchaCallDao;
    @Override
    public BaseDaoInterface<CaptchaCall, Serializable> getDao() {
        return captchaCallDao;
    }

}
