package com.lsxy.framework.api.message.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.message.model.AccountMessage;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.utils.Page;

/**
 * 用户消息service
 * Created by zhangxb on 2016/7/4.
 */
public interface AccountMessageService extends BaseService<AccountMessage>{
    /**
     * 获取用户消息的分页列表信息
     * @param userName 用户名
     * @param pageNo 第几页
     * @param pageSize 多少页
     * @return
     * @throws MatchMutiEntitiesException
     */
    public Page<AccountMessage> pageListByAccountId(String userName,Integer pageNo,Integer pageSize)throws MatchMutiEntitiesException ;
}
