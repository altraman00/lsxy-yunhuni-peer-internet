package com.lsxy.msg.api.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.msg.api.model.MsgUserRequest;

import java.util.Date;

/**
 * Created by liups on 2017/3/1.
 */
public interface MsgUserRequestService extends BaseService<MsgUserRequest> {
    Page<MsgUserRequest> getPageByCondition(Integer pageNo, Integer pageSize, String sendType,String appId,Date start, Date end, int isMass, String taskName, String mobile );

}
