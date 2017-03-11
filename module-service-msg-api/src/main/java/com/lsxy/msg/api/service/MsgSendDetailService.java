package com.lsxy.msg.api.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.msg.api.model.MsgSendDetail;
import com.lsxy.msg.api.model.MsgSendRecord;

import java.util.List;

/**
 * Created by liups on 2017/3/1.
 */
public interface MsgSendDetailService extends BaseService<MsgSendDetail> {
    void batchInsertDetail(MsgSendRecord msgSendRecord, List<String> pendingPhones,int state);

    void updateStateByMsgKey(String msgKey, int state);

    MsgSendDetail findByTaskIdAndMobile(String taskId, String mobile);
}
