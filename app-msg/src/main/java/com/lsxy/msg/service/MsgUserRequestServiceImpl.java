package com.lsxy.msg.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.msg.api.model.MsgUserRequest;
import com.lsxy.msg.api.service.MsgUserRequestService;
import com.lsxy.msg.dao.MsgUserRequestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2017/3/1.
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class MsgUserRequestServiceImpl extends AbstractService<MsgUserRequest> implements MsgUserRequestService {
    @Autowired
    MsgUserRequestDao msgUserRequestDao;

    @Override
    public BaseDaoInterface<MsgUserRequest, Serializable> getDao() {
        return this.msgUserRequestDao;
    }
}
