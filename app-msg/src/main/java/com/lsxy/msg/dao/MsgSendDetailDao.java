package com.lsxy.msg.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.msg.api.model.MsgSendDetail;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liups on 2017/3/1.
 */
public interface MsgSendDetailDao extends BaseDaoInterface<MsgSendDetail, Serializable> {
    @Modifying
    @Query("update MsgSendDetail d set d.state = :state where d.msgKey = :msgKey")
    void updateStateByMsgKey(@Param("msgKey") String msgKey, @Param("state") int state);

    MsgSendDetail findFirstByTaskIdAndMobile(String taskId, String mobile);

    @Modifying
    @Query("update MsgSendDetail d set d.state = :state,d.taskId = :taskId where d.recordId = :recordId AND d.mobile IN (:phones)")
    void updateDetailStateAndTaskIdByRecordId(@Param("recordId") String recordId,@Param("phones") List<String> phones, @Param("state") int state,@Param("taskId") String taskId);


    List<String> findIdByRecordIdAndMobileIn(String recordId, List<String> phones);
}
