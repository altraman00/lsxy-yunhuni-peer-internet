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
    void logout(String tenantId, String appId, String agentName, boolean force) throws YunhuniApiException;

    /**
     * 座席定时注册
     * @param appId
     * @param agentName
     * @throws YunhuniApiException
     */
    void keepAlive(String appId, String agentName) throws YunhuniApiException;

    /**
     * 获取通道下所有的座席ID
     * @param tenantId
     * @param appId
     * @param channelId
     * @return
     */
    List<String> getAgentIdsByChannel(String tenantId, String appId, String channelId);

    /**
     * 获取单个座席
     * @param appId
     * @param agentName
     * @return
     * @throws YunhuniApiException
     */
    CallCenterAgent get(String appId, String agentName) throws YunhuniApiException;

    /**
     * 获取应用下所有的座席
     * @param appId
     * @param pageNo
     * @param pageSize
     * @return
     * @throws YunhuniApiException
     */
    Page getPage(String appId, Integer pageNo, Integer pageSize) throws YunhuniApiException;

    /**
     * 修改座席分机
     * @param appId
     * @param agentName
     * @param extensionId
     * @throws YunhuniApiException
     */
    void extension(String appId, String agentName,String extensionId) throws YunhuniApiException;

    /**
     * 修改座席状态
     * @param appId
     * @param agentName
     * @param state
     * @throws YunhuniApiException
     */
    void state(String appId, String agentName, String state) throws YunhuniApiException;

    void state(String tenantId,String appId,String agentId, String state,boolean force) throws YunhuniApiException;

    String getState(String agentId) throws YunhuniApiException;

    /**
     * 修改座席技能
     * @param tenantId
     * @param appId
     * @param agentName
     * @param skillOpts 座席技能修改的操作类
     * @throws YunhuniApiException
     */
    void skills(String tenantId, String appId, String agentName, List<AgentSkillOperationDTO> skillOpts) throws YunhuniApiException;

}
