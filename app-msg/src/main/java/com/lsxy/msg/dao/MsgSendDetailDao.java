package com.lsxy.msg.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.msg.api.model.MsgSendDetail;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liups on 2017/3/1.
 */
public interface MsgSendDetailDao extends BaseDaoInterface<MsgSendDetail, Serializable> {
    List<MsgSendDetail> findByMsgKey(String msgKey);
}
