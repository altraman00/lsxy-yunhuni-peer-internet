package com.lsxy.msg.api.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.msg.api.model.MsgSendDetail;

import java.util.List;

/**
 * Created by liups on 2017/3/1.
 */
public interface MsgSendDetailService extends BaseService<MsgSendDetail> {
    Page<MsgSendDetail> getPageByContiton(Integer pageNo, Integer pageSize, String msgKey, String mobile, String state);
    List<MsgSendDetail> findByMsgKey(String msgKey);
}
