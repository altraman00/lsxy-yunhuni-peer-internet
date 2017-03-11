package com.lsxy.msg.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.msg.api.model.MsgSendRecord;
import com.lsxy.msg.api.service.MsgSendRecordService;
import com.lsxy.msg.dao.MsgSendRecordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2017/3/1.
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class MsgSendRecordServiceImpl extends AbstractService<MsgSendRecord> implements MsgSendRecordService {
    @Autowired
    MsgSendRecordDao msgSendRecordDao;

    @Override
    public BaseDaoInterface<MsgSendRecord, Serializable> getDao() {
        return this.msgSendRecordDao;
    }

    @Override
    public MsgSendRecord findByTaskId(String taskId) {
        return msgSendRecordDao.findFirstByTaskId(taskId);
    }

    @Override
    public void updateStateByMsgKey(String msgKey, int state) {
        msgSendRecordDao.updateStateByMsgKey(msgKey,state);
    }
}
