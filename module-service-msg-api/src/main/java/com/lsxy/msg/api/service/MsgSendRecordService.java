package com.lsxy.msg.api.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.msg.api.model.MsgSendRecord;
import com.lsxy.msg.api.model.MsgUserRequest;

import java.util.List;

/**
 * Created by liups on 2017/3/1.
 */
public interface MsgSendRecordService extends BaseService<MsgSendRecord> {
    MsgSendRecord findByTaskId(String taskId);

    void updateNoMassStateByTaskId(String msgKey, int state);

    void updateStateAndTaskIdById(String id, int state, String taskId);

    List<MsgSendRecord> findUssdSendOneFailAndSendNotOver();

    List<MsgSendRecord> findWaitedSendMassBySupplier(String supplierCode);

    List<MsgSendRecord> findByMsgKey(String msgKey);
}
