package com.lsxy.msg.api.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.msg.api.model.MsgUserRequest;

import java.util.Date;
import java.util.List;

/**
 * Created by liups on 2017/3/1.
 */
public interface MsgUserRequestService extends BaseService<MsgUserRequest> {
    Page<MsgUserRequest> getPageByCondition(Integer pageNo, Integer pageSize, String sendType,String appId,Date start, Date end, int isMass, String taskName, String mobile );

    void updateNoMassStateByMsgKey(String msgKey, int state);

    List<MsgUserRequest> findAwaitedRequets();

    List<MsgUserRequest> findAwaitedButOverdueRequets();

    MsgUserRequest findByMsgKeyAndSendType(String appId, String subaccountId, String msgKey, String sendType);

    Page<MsgUserRequest> findPageBySendTypeForGW(String appId, String subaccountId, String sendType, Integer pageNo, Integer pageSize);

    MsgUserRequest findByMsgKey(String msgKey);
}
