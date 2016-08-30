package com.lsxy.framework.api.message.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.message.model.AccountMessage;
import com.lsxy.framework.api.message.model.Message;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.core.utils.Page;

import java.util.List;

/**
 * 用户消息service
 * Created by zhangxb on 2016/7/4.
 */
public interface AccountMessageService extends BaseService<AccountMessage> {
    /**
     * 获取用户消息的分页列表信息
     * @param userName 用户名
     * @param pageNo 第几页
     * @param pageSize 多少页
     * @return
     */
    public Page<AccountMessage> pageListByAccountId(String userName, Integer pageNo, Integer pageSize);

    /**
     * 获取指定状态的记录数
     * @param userName 用户名
     * @param status 状态
     * @return
     */
    public Long count(String userName,Integer status);

    /**
     * 给指定的用户发送消息
     * @param list 用户集合
     * @param message 消息体
     */
    public void insertMultiple(List<Account> list ,Message message);

    /**
     * 给指定用户发送模板信息
     * @param tenantId 指定租户
     * @param type 模板类型
     */
    public AccountMessage sendTenantTempletMessage(String originator,String tenantId,String type);
    /**
     * 给指定用户发送模板信息
     * @param accountId 指定用户
     * @param type 模板类型
     */
    public AccountMessage sendTempletMessage(String originator,String accountId,String type);
    /**
     * 给指定用户发送消息
     * @param accountId 指定用户
     * @param title 消息标题
     * @param content 消息内容
     */
    public AccountMessage sendMessage(String originator,String accountId,String title,String content);

    /**
     * 修改状态为已读
     * @param accountId
     * @param status
     */
    void modifyMessageStatus(String accountId,Integer status);

}
