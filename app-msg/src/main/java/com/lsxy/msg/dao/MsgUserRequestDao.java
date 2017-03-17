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

    List<MsgUserRequest> findByStateAndSendTimeBetween(int stateWait, Date start, Date current);

    MsgUserRequest findByMsgKey(String msgKey);

    MsgUserRequest findFirstByAppIdAndSubaccountIdAndMsgKey(String appId, String subaccountId, String msgKey);
}
