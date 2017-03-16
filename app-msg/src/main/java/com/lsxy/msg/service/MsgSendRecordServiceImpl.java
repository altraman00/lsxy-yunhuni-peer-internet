package com.lsxy.msg.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.msg.api.model.MsgConstant;
import com.lsxy.msg.api.model.MsgSendRecord;
import com.lsxy.msg.api.model.MsgUserRequest;
import com.lsxy.msg.api.service.MsgSendRecordService;
import com.lsxy.msg.dao.MsgSendRecordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
    public void updateNoMassStateByTaskId(String taskId, int state) {
        MsgSendRecord record = msgSendRecordDao.findFirstByTaskId(taskId);
        record.setState(state);
        if(state == MsgSendRecord.STATE_SUCCESS){
            record.setSuccNum(1L);
            record.setFailNum(0L);
            record.setPendingNum(0L);
        }else{
            record.setSuccNum(0L);
            record.setFailNum(1L);
            record.setPendingNum(0L);
        }
        this.save(record);
    }



    @Override
    public void updateStateAndTaskIdById(String id, int state, String taskId) {
        msgSendRecordDao.updateStateAndTaskIdById(id,state,taskId);
    }

    @Override
    public List<MsgSendRecord> findUssdSendOneFailAndSendNotOver() {
        Date date = new Date(System.currentTimeMillis() - 1000 * 120);
        String hql = "from MsgSendRecord obj where obj.isMass = false and obj.state = ?1 and obj.sendType = ?2 and obj.sendFailNum <= ?3 and obj.lastTime >= ?4 ";
        return this.list(hql,MsgSendRecord.STATE_FAIL, MsgConstant.MSG_USSD,3,date);
    }

    @Override
    public List<MsgSendRecord> findWaitedSendMassBySupplier(String supplierCode){
        return msgSendRecordDao.findBySupplierCodeAndStateAndIsMass(supplierCode,MsgSendRecord.STATE_WAIT,true);
    }

    @Override
    public List<MsgSendRecord> findByMsgKey(String msgKey) {
        return msgSendRecordDao.findByMsgKey(msgKey);
    }
}
