package com.lsxy.msg.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.msg.api.model.MsgTemplate;
import com.lsxy.msg.api.service.MsgTemplateService;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2017/3/1.
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class MsgTemplateServiceImpl extends AbstractService<MsgTemplate> implements MsgTemplateService {

    @Override
    public BaseDaoInterface<MsgTemplate, Serializable> getDao() {
        return null;
    }
}
