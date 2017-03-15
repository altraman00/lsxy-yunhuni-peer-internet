package com.lsxy.msg.api.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.msg.api.model.MsgSendDetail;
import com.lsxy.msg.api.model.MsgSendRecord;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by liups on 2017/3/1.
 */
public interface MsgSendDetailService extends BaseService<MsgSendDetail> {
    List<String> batchInsertDetail(MsgSendRecord msgSendRecord, List<String> pendingPhones,int state);

    void updateStateByMsgKey(String msgKey, int state);

    MsgSendDetail findByTaskIdAndMobile(String taskId, String mobile);

    List<String> updateStateAndTaskIdByRecordIdAndPhones(String recordId, List<String> phones, int state, String taskId);

    List<String> updateStateAndSetEndTimeByRecordIdAndPhones(String recordId, List<String> phones, int state,Date endTime);

    void updateStateAndSetEndTimeFromWaitedToSuccessByRecordId(String recordId,Date endTime);

    Map getStateCountByRecordId(String recordId);
    Page<MsgSendDetail> getPageByContiton(Integer pageNo, Integer pageSize, String msgKey, String mobile, String state);
    List<MsgSendDetail> findByMsgKey(String msgKey);

}
