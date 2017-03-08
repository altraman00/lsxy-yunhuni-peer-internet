package com.lsxy.msg.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.msg.api.model.MsgSendDetail;
import com.lsxy.msg.api.service.MsgSendDetailService;
import com.lsxy.msg.dao.MsgSendDetailDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2017/3/1.
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class MsgSendDetailServiceImpl extends AbstractService<MsgSendDetail> implements MsgSendDetailService {
    @Autowired
    MsgSendDetailDao msgSendDetailDao;

    @Override
    public BaseDaoInterface<MsgSendDetail, Serializable> getDao() {
        return this.msgSendDetailDao;
    }
}
