package com.lsxy.msg.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.msg.api.model.MsgUserRequest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by liups on 2017/3/1.
 */
public interface MsgUserRequestDao extends BaseDaoInterface<MsgUserRequest, Serializable> {
    @Modifying
    @Query("update MsgUserRequest re set re.state = :state where re.msgKey = :msgKey")
    void updateStateByMsgKey(@Param("msgKey") String msgKey,@Param("state") int state);

    List<MsgUserRequest> findByStateAndSendTimeBetween(int stateWait, Date start, Date current);
}
