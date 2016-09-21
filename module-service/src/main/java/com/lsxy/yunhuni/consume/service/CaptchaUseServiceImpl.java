package com.lsxy.yunhuni.consume.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.consume.model.CaptchaUse;
import com.lsxy.yunhuni.api.consume.service.CaptchaUseService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.consume.dao.CaptchaUseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2016/8/30.
 */
@Service
public class CaptchaUseServiceImpl extends AbstractService<CaptchaUse> implements CaptchaUseService {
    @Autowired
    CaptchaUseDao captchaUseDao;
    @Override
    public BaseDaoInterface<CaptchaUse, Serializable> getDao() {
        return this.captchaUseDao;
    }
}
