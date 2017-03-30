package com.lsxy.call.center.api.service;

import com.lsxy.call.center.api.model.CallCenterAgent;
import com.lsxy.call.center.api.operations.AgentSkillOperationDTO;
import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.core.utils.Page;

import java.util.List;

/**
 * Created by zhangxb on 2016/10/22.
 */
public interface CallCenterAgentService extends BaseService<CallCenterAgent> {

    /**
     * 座席登录
     * @param agent
     * @return
     * @throws YunhuniApiException
     */
    String login(CallCenterAgent agent) throws YunhuniApiException;

    /**
     * 座席注销
     * @param tenantId
     * @param appId
     * @param agentName
     * @param force
     * @throws YunhuniApiException
     */
    void logout(String tenantId, String appId,String subaccountId, String agentName, boolean force) throws YunhuniApiException;

    /**
     * 座席定时注册
     * @param appId
     * @param agentName
     * @throws YunhuniApiException
     */
    void keepAlive(String appId,String subaccountId, String agentName) throws YunhuniApiException;

    /**
     * 获取通道下所有的座席ID
     * @param tenantId
     * @param appId
     * @param subaccountId 子账号id 为null 代表主账号
     * @return
     */
    List<String> getAgentIdsBySubaccountId(String tenantId, String appId, String subaccountId);

    /**
     * 获取单个座席
     * @param appId
     * @param agentName
     * @return
     * @throws YunhuniApiException
     */
    CallCenterAgent get(String appId, String agentName) throws YunhuniApiException;

    CallCenterAgent get(String appId,String accountId, String agentName) throws YunhuniApiException;

    CallCenterAgent getSimple(String appId,String accountId, String agentName) throws YunhuniApiException;

    String getId(String appId, String agentName) throws YunhuniApiException;

    String getId(String appId,String subaccountId, String agentName) throws YunhuniApiException;
    /**
     * 获取应用下所有的座席
     * @param appId
     * @param pageNo
     * @param pageSize
     * @return
     * @throws YunhuniApiException
     */
    Page getPageForPotal(String appId, Integer pageNo, Integer pageSize,String agentNum,String subaccountId) throws YunhuniApiException;

    Page getPageForApiGW(String appId,String subaccountId, Integer pageNo, Integer pageSize) throws YunhuniApiException;

    /**
     * 修改座席分机
     * @param appId
     * @param agentName
     * @param extensionId
     * @throws YunhuniApiException
     */
    void extension(String appId, String agentName,String extensionId,String subaccountId) throws YunhuniApiException;

    /**
     * 修改座席状态
     * @param appId
     * @param agentName
     * @param state
     * @throws YunhuniApiException
     */
    String state(String appId,String subaccountId, String agentName, String state) throws YunhuniApiException;

    String state(String tenantId,String appId,String agentId, String state,boolean force) throws YunhuniApiException;

    String getState(String agentId) throws YunhuniApiException;


    /**
     * 修改座席技能
     * @param tenantId
     * @param appId
     * @param agentName
     * @param skillOpts 座席技能修改的操作类
     * @throws YunhuniApiException
     */
    void skills(String tenantId, String appId,String subaccountId, String agentName, List<AgentSkillOperationDTO> skillOpts) throws YunhuniApiException;

}
