package com.oneyun.sapi;


import com.oneyun.sapi.config.SapiConstants;
import com.oneyun.sapi.config.SapiSdkConfig;
import com.oneyun.sapi.utils.HttpClientUtils;
import com.oneyun.sapi.utils.LoggerUtil;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxb on 2016/8/26.
 */
public class SapiSdk extends SapiSdkConfig {
    public SapiSdk(String url, String certId, String secretKey) {
        super(url, certId, secretKey);
    }

    /**
     * 双向回拨--挂断
     * @param appId 应用id 必填
     * @param callId 会话id 必填
     * @return
     */
    public String duoCallbackStop(String appId,String callId) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("callId",callId);
        LoggerUtil.info("双向回拨--挂断，参数："+params);
        return HttpClientUtils.doPost(getUrl(SapiConstants.CALL_DUO_CALLBACK_STOP),getSecretKey(),getCertId(),appId,params);
    }
    /**
     * 双向回拨
     * @param appId 应用id 必填
     * @param from1 第一方主叫号码 选填 如有绑定IVR号码，可填绑定号码,否则为空
     * @param to1 第一方被叫号码 必填
     * @param from2 第二方主叫号码 选填 如有绑定IVR号码，可填绑定号码,否则为空
     * @param to2 第二方被叫号码 必填
     * @param ringTone 自定义回铃音 选填
     * @param ringToneMode 自定义回铃音播放模式 0：收到对端回铃后开始播放 1：拨号时即开始播放，收到对端回铃后停止播放 2：拨号时即开始播放，对端接听或者挂机后停止播放 选填
     * @param maxDialDuration 最大拨号等待时间（秒） 必填
     * @param maxCallDuration 最大接通时间（秒） 必填
     * @param recording 是否录音 选填
     * @param recordMode 录音模式： 0: 双向接通后录音 1：开始呼叫第一方时启动录音 2: 开始呼叫第二方时启动录音 选填
     * @param userData 用户数据 选填
     * @return
     */
    public String duoCallback(String appId,String from1,String to1,String from2,String to2,String ringTone,Integer ringToneMode,
                                          Integer maxDialDuration,Integer maxCallDuration,Boolean recording,Integer recordMode,String userData) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("from1",from1);
        params.put("to1",to1);
        params.put("from2",from2);
        params.put("to2",to2);
        params.put("ring_tone",ringTone);
        params.put("ring_tone_mode",ringToneMode);
        params.put("max_dial_duration",maxDialDuration);
        params.put("max_call_duration",maxCallDuration);
        params.put("recording",recording);
        params.put("record_mode",recordMode);
        params.put("user_data",userData);
        LoggerUtil.info("双向回拨，参数："+params);
        return HttpClientUtils.doPost(getUrl(SapiConstants.CALL_DUO_CALLBACK),getSecretKey(),getCertId(),appId,params);
    }


    /**
     * 语音验证码
     * @param appId 应用id 必填
     * @param from 主叫号码，只能填租用的号码，不填平台会自动选择一个可用的 选填
     * @param to 被叫号码 必填
     * @param maxDialDuration 最大拨号等待时间（秒），默认50秒 选填
     * @param repeat 重复播放次数，默认1，表示播放两次。注意，0表示播放一次，不重复 选填
     * @param files 验证码提示音文件 文件名为开发者用户中心的放音文件名 选填
     * @param verifyCode 要播放的验证码内容，只能是十进制数字，1到12个字符 选填
     * 参数 files 与 verifyCode 不得同时为空。 两者都被赋值的情况下，首先播放 files 指定的文件，然后才播放 verifyCode 中的内容。
     * @param userData 用户数据,最大128个字符 选填
     * @return
     */
    public String verifyCall(
            String appId,String from,String to,Integer maxDialDuration,Integer repeat
            ,String[] files,String verifyCode,String userData
    ) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        String play_file = "";
        play_file = getArrayString(files, play_file);
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("from",from);
        params.put("to",to);
        params.put("max_dial_duration",maxDialDuration);
        params.put("repeat",repeat);
        params.put("play_file",play_file);
        params.put("verify_code",verifyCode);
        params.put("user_data",userData);
        LoggerUtil.info("语音验证码，参数："+params);
        return HttpClientUtils.doPost(getUrl(SapiConstants.CALL_VERIFY_CALL),getSecretKey(),getCertId(),appId,params);
    }


    /**
     * 外呼通知
     * @param appId 应用id 必填
     * @param from 主叫号码，只能填租用的号码，不填平台会自动选择一个可用的 选填
     * @param to 被叫号码 必填
     * @param maxDialDuration 最大拨号等待时间（秒），默认50秒 选填
     * @param repeat 重复播放次数，默认1，表示播放两次。注意，0表示播放一次，不重复 选填
     * @param files 提示音文件，文件名或者文件名列表，文件名为开发者用户中心的放音文件名 选填
     * @param playContent 动态播放内容，可以是数字、日期、金额 选填
     * 参数 files 与 playContent 不得同时为空。 两者都被赋值的情况下，首先播放 files 指定的文件，然后才播放 playContent 中的内容
     * @param userData 用户数据,最大128个字符 选填
     * @return
     */
    public String notifyCall(
            String appId,String from,String to,Integer maxDialDuration,Integer repeat,String[] files,
            String[][] playContent, String userData
    ) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        String play_file = "";
        play_file = getArrayString(files, play_file);
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("from",from);
        params.put("to",to);
        params.put("max_dial_duration",maxDialDuration);
        params.put("repeat",repeat);
        params.put("play_file",play_file);
        params.put("play_content",playContent);
        params.put("user_data",userData);
        LoggerUtil.info("外呼通知，参数："+params);
        return HttpClientUtils.doPost(getUrl(SapiConstants.CALL_NOTIFY_CALL),getSecretKey(),getCertId(),appId,params);
    }

    private String getArrayString(String[] files, String play_file) {
        if(files!=null) {
            for (int i = 0; i < files.length; i++) {
                if(StringUtils.isNotEmpty(files[i])) {
                    play_file += files[i];
                    if (i !=(files.length - 1)) {
                        play_file += "|";
                    }
                }
            }
        }
        return play_file;
    }



    /**
     * 创建会议
     * @param appId 应用id
     * @param maxDuration 呼叫最大时间（秒）
     * @param maxParts 最大与会方数
     * @param recording 是否自动启动录音
     * @param autoHangup 会议结束自动挂断与会方
     * @param bgmFile 背景音文件
     * @param userData 用户数据 会在会议事件通知中返回给开发者
     * @return
     */
    public String confCreate(String appId,Integer maxDuration,Integer maxParts,Boolean recording,Boolean autoHangup,String bgmFile,String userData) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("max_duration",maxDuration);
        params.put("max_parts",maxParts);
        params.put("recording",recording);
        params.put("auto_hangup",autoHangup);
        params.put("bgm_file",bgmFile);
        params.put("user_data",userData);
        return HttpClientUtils.doPost(getUrl(SapiConstants.CONF_CREATE),getSecretKey(),getCertId(),appId,params);
    }

    /**
     * 解散会议
     * @param appId 应用id
     * @param confId 会议id
     * @return
     */
    public String confDismiss(String appId,String confId) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        return HttpClientUtils.doPost(getUrl(SapiConstants.CONF_DISMISS,confId),getSecretKey(),getCertId(),appId,params);
    }


    /**
     * 退出会议
     * @param appId 应用id
     * @param confId 会议id
     * @param callId 呼叫id
     * @return
     */
    public String confQuit(String appId,String confId,String callId) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("call_id",callId);
        return HttpClientUtils.doPost(getUrl(SapiConstants.CONF_QUIT,confId),getSecretKey(),getCertId(),appId,params);
    }

    /**
     * 会议邀请呼叫
     * @param appId 应用id
     * @param confId 会议id
     * @param from 主叫号码
     * @param to 被叫号码
     * @param maxDuration 最大会议时间（秒）
     * @param maxDialDuration 最大拨号等待时间（秒）
     * @param dialVoiceStopCond 自定义拨号音停止播放条件。0：振铃停止；1：接听或者挂断停止。
     * @param playFile 加入后在会议播放这个文件
     * @param voiceMode 加入后的声音模式。1: 能够听；能够说2: 不能听；能够说3: 能够听；不能说4: 不能听；不能说
     * @return
     */
    public String confInviteCall(
            String appId,String confId,String from ,String to,Integer maxDuration ,
            Integer maxDialDuration ,Integer dialVoiceStopCond ,String playFile ,Integer voiceMode ) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("from",from);
        params.put("to",to);
        params.put("max_duration",maxDuration);
        params.put("max_dial_duration",maxDialDuration);
        params.put("dial_voice_stop_cond",dialVoiceStopCond);
        params.put("play_file",playFile);
        params.put("voice_mode",voiceMode);
        return HttpClientUtils.doPost(getUrl(SapiConstants.CONF_INVITE_CALL,confId),getSecretKey(),getCertId(),appId,params);
    }

    /**
     * 将通话加入到会议
     * @param appId 应用id
     * @param confId 会议id
     * @param callId 要加入的呼叫的ID
     * @param maxDuration 最大会议时间（秒）
     * @param playFile 加入后在会议播放这个文件
     * @param voiceMode 加入后的声音模式。 1: 能够听；能够说 2: 不能听；能够说 3: 能够听；不能说 4: 不能听；不能说
     * @return
     */
    public String confJoin(
            String appId,String confId,String callId  ,Integer maxDuration
            ,String playFile ,Integer voiceMode ) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("call_id",callId);
        params.put("max_duration",maxDuration);
        params.put("play_file",playFile);
        params.put("voice_mode",voiceMode);
        return HttpClientUtils.doPost(getUrl(SapiConstants.CONF_JOIN,confId),getSecretKey(),getCertId(),appId,params);
    }



    /**
     * 开始放音
     * @param appId 应用id
     * @param confId 会议id
     * @param files 播放文件（列表）
     * @return
     */
    public String confStartPlay(String appId,String confId,String[] files) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("files",files);
        return HttpClientUtils.doPost(getUrl(SapiConstants.CONF_START_PLAY,confId),getSecretKey(),getCertId(),appId,params);
    }

    /**
     * 停止放音
     * @param appId 应用id
     * @param confId 会议id
     * @return
     */
    public String confStopPlay(String appId,String confId) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        return HttpClientUtils.doPost(getUrl(SapiConstants.CONF_STOP_PLAY,confId),getSecretKey(),getCertId(),appId,params);
    }

    /**
     * 开始录音
     * @param appId 应用id
     * @param confId 会议id
     * @param maxDuration 录音时长（秒）
     * @return
     */
    public String confStartRecord(String appId,String confId,Integer maxDuration) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("max_duration",maxDuration);
        return HttpClientUtils.doPost(getUrl(SapiConstants.CONF_START_RECORD,confId),getSecretKey(),getCertId(),appId,params);
    }

    /**
     * 停止录音
     * @param appId 应用id
     * @param confId 会议id
     * @return
     */
    public String confStopRecord(String appId,String confId) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        return HttpClientUtils.doPost(getUrl(SapiConstants.CONF_STOP_RECORD,confId),getSecretKey(),getCertId(),appId,params);
    }

    /**
     * 会议成员录放音模式
     * @param appId 应用id
     * @param confId 会议id
     * @param callId 会议成员的 call_id
     * @param voiceMode 声音模式。 1: 能够听；能够说 2: 不能听；能够说  3: 能够听；不能说 4: 不能听；不能说
     * @return
     */
    public String  confSetVoiceMode(String appId,String confId,String callId ,Integer voiceMode ) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("call_id",callId);
        params.put("voice_mode",voiceMode);
        return HttpClientUtils.doPost(getUrl(SapiConstants.CONF_SET_VOICE_MODE,confId),getSecretKey(),getCertId(),appId,params);
    }
    /**
     * 会议成员录放音模式
     * @param appId 应用id
     *
     * @return
     */
    /**
     *新建分机
     * @param appId 应用id
     * @param type 分机类型 1 SIP 终端。如IP电话、软电话。2	SIP 网关。3 电话。普通电话，如手机、固话。
     * @param user 	SIP 注册用户名，仅用于 type==1的情况
     * @param password SIP 注册用密码，仅用于 type==1的情况
     * @param ipaddr SIP 网关IP地址与端口，默认5060，仅用于 type==2的情况
     * @param telnum 仅用于 type==3 的情况
     * @return
     */
    public String  CallcenterExtensionNew(String appId,Integer type,String user,String password,String ipaddr,String telnum ) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("type",type);
        params.put("user",user);
        params.put("password",password);
        params.put("ipaddr",ipaddr);
        params.put("telnum",telnum);
        return HttpClientUtils.doPost(getUrl(SapiConstants.CALLCENTER_EXTENSION),getSecretKey(),getCertId(),appId,params);
    }

    /**
     * 删除指定分机
     * @param appId 应用id
     * @param extensionId 分机id
     * @return
     */
    public String  CallcenterExtensionDelete(String appId,String  extensionId) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        return HttpClientUtils.doDelete(getUrl(SapiConstants.CALLCENTER_EXTENSION+"/"+extensionId),getSecretKey(),getCertId(),appId,params);
    }

    /**
     *  根据分机id获取分机
     * @param appId 应用id
     * @param extensionId 分机id
     * @return
     */
    public String  CallcenterExtensionByExtensionId(String appId,String  extensionId) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        return HttpClientUtils.doGet(getUrl(SapiConstants.CALLCENTER_EXTENSION+"/"+extensionId),getSecretKey(),getCertId(),appId,params);
    }

    /**
     *  获取分页分机
     * @param appId 应用id
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @return
     */
    public String  CallcenterExtensionGetPage(String appId,Integer pageNo,Integer pageSize) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("pageNo",pageNo);
        params.put("pageSize",pageSize);
        return HttpClientUtils.doGet(getUrl(SapiConstants.CALLCENTER_EXTENSION),getSecretKey(),getCertId(),appId,params);
    }

    /**
     * 坐席登录
     * @param appId 应用id
     * @param name 该坐席的唯一标识，应用内必须唯一
     * @param channel 坐席所在 工作通道 的 id
     * @param num 	该坐席的工号，如果不填，就无法播报工号
     * @param state 登录后的初始状态
     * @param skills 登录后的初始技能
     * @param extension 登录后初始绑定分机
     * @return
     */
    public String  CallcenterAgentLogin(String appId, String name, String channel, String num, String state, List<AgentSkill> skills,String extension) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("name",name);
        params.put("channel",channel);
        params.put("num",num);
        params.put("state",state);
        params.put("skills",skills.toArray());
        params.put("extension",extension);
        return HttpClientUtils.doPost(getUrl(SapiConstants.CALLCENTER_AGENT),getSecretKey(),getCertId(),appId,params);
    }

    /**
     *  注销坐席
     * @param appId 应用id
     * @param agentName 坐席名
     * @param force true 强行注销
     * @return
     */
    public String  CallcenterAgentLogout(String appId, String agentName, Boolean force) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("force",force);
        return HttpClientUtils.doDelete(getUrl(SapiConstants.CALLCENTER_AGENT+"/"+agentName),getSecretKey(),getCertId(),appId,params);
    }

    /**
     *  坐席报道
     * @param appId 应用id
     * @param agentName 坐席名
     * @return
     */
    public String  CallcenterAgentKeepalive(String appId, String agentName) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        return HttpClientUtils.doGet(getUrl(SapiConstants.CALLCENTER_AGENT_KEEPALIVE,agentName),getSecretKey(),getCertId(),appId,params);
    }
    /**
     *  根据坐席名获取坐席信息
     * @param appId 应用id
     * @param agentName 坐席名
     * @return
     */
    public String  CallcenterAgentByAgentName(String appId, String agentName) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        return HttpClientUtils.doGet(getUrl(SapiConstants.CALLCENTER_AGENT+"/"+agentName),getSecretKey(),getCertId(),appId,params);
    }
    /**
     *  获取坐席分页信息
     * @param appId 应用id
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @return
     */
    public String  CallcenterAgentGetPage(String appId, Integer pageNo,Integer pageSize) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("pageNo",pageNo);
        params.put("pageSize",pageSize);
        return HttpClientUtils.doGet(getUrl(SapiConstants.CALLCENTER_AGENT),getSecretKey(),getCertId(),appId,params);
    }
    /**
     *  设置坐席分机
     * @param appId 应用id
     * @param agentName 坐席名
     * @param id 分机id
     * @return
     */
    public String  CallcenterAgentExtension(String appId, String agentName,String id) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("id",id);
        return HttpClientUtils.doPost(getUrl(SapiConstants.CALLCENTER_AGENT_EXTENSION,agentName),getSecretKey(),getCertId(),appId,params);
    }
    /**
     * 设置坐席状态
     * @param appId 应用id
     * @param agentName 坐席名
     * @param state 新的状态
     * @return
     */
    public String  CallcenterAgentState(String appId, String agentName,String state) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("state",state);
        return HttpClientUtils.doPost(getUrl(SapiConstants.CALLCENTER_AGENT_STATE,agentName),getSecretKey(),getCertId(),appId,params);
    }
    /**
     * 设置坐席技能
     * @param appId 应用id
     * @param agentName 坐席名
     * @param opts 技能操作列表
     * @return
     */
    public String  CallcenterAgentSkills(String appId, String agentName,List<AgentSkillOperation> opts) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("opts",opts.toArray());
        return HttpClientUtils.doPost(getUrl(SapiConstants.CALLCENTER_AGENT_SKILLS,agentName),getSecretKey(),getCertId(),appId,params);
    }

    /**
     * 新建通道
     * @param appId 应用id
     * @param maxAgent 	该工作通道所容纳的最大坐席数量
     * @param maxSkill 	该工作通道所容纳的最大技能数量
     * @param maxCondition 该工作通道所容纳的最大排队条件设置数量
     * @param maxQueue 	该工作通道所容纳的最大排队任务数量
     * @param remark 备注信息
     * @return
     */
    public String  CallcenterChannelNew(String appId, Integer maxAgent,Integer maxSkill,Integer maxCondition,Integer maxQueue,String remark) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("max_agent",maxAgent);
        params.put("max_skill",maxSkill);
        params.put("max_condition",maxCondition);
        params.put("max_queue",maxQueue);
        params.put("remark",remark);
        return HttpClientUtils.doPost(getUrl(SapiConstants.CALLCENTER_CHANNEL),getSecretKey(),getCertId(),appId,params);
    }

    /**
     *  删除通道
     * @param appId 应用id
     * @param channelId 通道id
     * @return
     */
    public String  CallcenterChannelDelete(String appId,String channelId) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        return HttpClientUtils.doDelete(getUrl(SapiConstants.CALLCENTER_CHANNEL+"/"+channelId),getSecretKey(),getCertId(),appId,params);
    }
    /**
     * 修改通道
     * @param appId 应用id
     * @param channelId  通道id
     * @param maxAgent 	该工作通道所容纳的最大坐席数量
     * @param maxSkill 	该工作通道所容纳的最大技能数量
     * @param maxCondition 该工作通道所容纳的最大排队条件设置数量
     * @param maxQueue 	该工作通道所容纳的最大排队任务数量
     * @param remark 备注信息
     * @return
     */
    public String  CallcenterChannelEdit(String appId,String channelId ,Integer maxAgent,Integer maxSkill,Integer maxCondition,Integer maxQueue,String remark) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("max_agent",maxAgent);
        params.put("max_skill",maxSkill);
        params.put("max_condition",maxCondition);
        params.put("max_queue",maxQueue);
        params.put("remark",remark);
        return HttpClientUtils.doPost(getUrl(SapiConstants.CALLCENTER_CHANNEL+"/"+channelId),getSecretKey(),getCertId(),appId,params);
    }
    /**
     *  根据通道id查询对应通道
     * @param appId 应用id
     * @param channelId 通道id
     * @return
     */
    public String  CallcenterChannelByChannelId(String appId,String channelId) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        return HttpClientUtils.doGet(getUrl(SapiConstants.CALLCENTER_CHANNEL+"/"+channelId),getSecretKey(),getCertId(),appId,params);
    }
    /**
     *  根据全部通道
     * @param appId 应用id
     * @return
     */
    public String  CallcenterChannelGetList(String appId) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        return HttpClientUtils.doGet(getUrl(SapiConstants.CALLCENTER_CHANNEL),getSecretKey(),getCertId(),appId,params);
    }
    /**
     * 新建排队条件
     * @param appId 应用id
     * @param channel 条件所属的工作通道ID
     * @param where 条件选择表达式
     * @param sort 默认不排序，所有的坐席权值相同。
     * @param priority 数值大的优先级高。默认值是 0。
     * @param queueTimeout 	该条件的排队等待超时时间(秒)。默认null表示一直等待直到呼叫结束。0表示找不到坐席就立即超时。
     * @param fetchTimeout 	该条件的坐席分机接听超时时间(秒)。默认null表示一直等待直到呼叫结束。
     * @param remark 备注
     * @return
     */
    public String  CallcenterConditionNew(String appId,String channel,String where,String sort,Integer priority,Integer queueTimeout,Integer fetchTimeout,String remark) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("channel",channel);
        params.put("where",where);
        params.put("sort",sort);
        params.put("priority",priority);
        params.put("queue_timeout",queueTimeout);
        params.put("fetch_timeout",fetchTimeout);
        params.put("remark",remark);
        return HttpClientUtils.doPost(getUrl(SapiConstants.CALLCENTER_CONDITION),getSecretKey(),getCertId(),appId,params);
    }

    /**
     * 删除排队条件
     * @param appId 应用id
     * @param conditionId 排队条件id
     * @return
     */
    public String  CallcenterConditionDelete(String appId,String conditionId) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        return HttpClientUtils.doDelete(getUrl(SapiConstants.CALLCENTER_CONDITION+"/"+conditionId),getSecretKey(),getCertId(),appId,params);
    }
    /**
     * 新建排队条件
     * @param appId 应用id
     * @param conditionId 排队条件id
     * @param channel 条件所属的工作通道ID
     * @param where 条件选择表达式
     * @param sort 默认不排序，所有的坐席权值相同。
     * @param priority 数值大的优先级高。默认值是 0。
     * @param queueTimeout 	该条件的排队等待超时时间(秒)。默认null表示一直等待直到呼叫结束。0表示找不到坐席就立即超时。
     * @param fetchTimeout 	该条件的坐席分机接听超时时间(秒)。默认null表示一直等待直到呼叫结束。
     * @param remark 备注
     * @return
     */
    public String  CallcenterConditionEdit(String appId,String conditionId,String channel,String where,String sort,Integer priority,Integer queueTimeout,Integer fetchTimeout,String remark) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("channel",channel);
        params.put("where",where);
        params.put("sort",sort);
        params.put("priority",priority);
        params.put("queue_timeout",queueTimeout);
        params.put("fetch_timeout",fetchTimeout);
        params.put("remark",remark);
        return HttpClientUtils.doPost(getUrl(SapiConstants.CALLCENTER_CONDITION+"/"+conditionId),getSecretKey(),getCertId(),appId,params);
    }
    /**
     * 根据排队条件id获取排队条件
     * @param appId 应用id
     * @param conditionId 排队条件id
     * @return
     */
    public String  CallcenterConditionByConditionId(String appId,String conditionId) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        return HttpClientUtils.doGet(getUrl(SapiConstants.CALLCENTER_CONDITION+"/"+conditionId),getSecretKey(),getCertId(),appId,params);
    }
    /**
     * 获取通道上的全部排队条件
     * @param appId 应用id
     * @param channelId 通道id
     * @return
     */
    public String  CallcenterConditionByConditionGetList(String appId,String channelId) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        return HttpClientUtils.doGet(getUrl(SapiConstants.CALLCENTER_CHANNEL_CONDITION,channelId),getSecretKey(),getCertId(),appId,params);
    }

    /**
     * IVR呼叫
     * @param appId 应用id
     * @param from 主叫号码，只能填租用的号码，不填平台会自动选择一个可用的
     * @param to 	被叫号码
     * @param maxDialDuration 最大拨号等待时间（秒），默认50秒
     * @param maxCallDuration 最大接通时间（秒）
     * @param userDate 用户数据,最大128个字符
     * @return
     */
    public String IvrCall(String appId,String from,String to,Integer maxDialDuration,Integer maxCallDuration,String userDate) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("from",from);
        params.put("to",to);
        params.put("max_dial_duration",maxDialDuration);
        params.put("max_call_duration",maxCallDuration);
        params.put("user_data",userDate);
        return HttpClientUtils.doPost(getUrl(SapiConstants.IVR_CALL),getSecretKey(),getCertId(),appId,params);
    }

    public String extensionListTest(String appId,Integer pageNo,Integer pageSize) throws IOException, NoSuchAlgorithmException, InvalidKeyException, KeyManagementException {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("pageNo",pageNo);
        params.put("pageSize",pageSize);
        return HttpClientUtils.doGet(getUrl(SapiConstants.EXTENSION_STATE),getSecretKey(),getCertId(),appId,params);
    }
}
