package com.lsxy.framework.api.message.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.message.model.AccountMessage;
import com.lsxy.framework.api.message.model.Message;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.core.utils.Page;

import java.util.Date;
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
     * 根据用户id和时间，修改状态
     * @param accountId 用户id
     * @param status 时间
     */
    void modifyMessageStatus(String accountId,Integer status,Date endTime);
    /**
     * 根据消息id修改消息状态
     * @param messageId 消息id
     * @param status 状态
     */
    void modifyMessageStatus(String messageId,Integer status);

    /**
     * 获取指定时间内的记录数
     *
     * @return
     */
     Long countAll(String accountId,Date startTime,Date endTime);

     List listAll(String accountId,Date startTime,Date endTime);

     Page pageAll(String accountId,Date startTime,Date endTime,Integer pageNo,Integer pageSize);


}
