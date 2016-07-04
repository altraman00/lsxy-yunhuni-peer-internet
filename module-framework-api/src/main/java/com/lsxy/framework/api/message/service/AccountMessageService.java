package com.lsxy.framework.api.message.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.message.model.AccountMessage;

import java.util.List;

/**
 * 用户消息service
 * Created by zhangxb on 2016/7/4.
 */
public interface AccountMessageService extends BaseService<AccountMessage>{

    public List<AccountMessage> list(String userName);
}
