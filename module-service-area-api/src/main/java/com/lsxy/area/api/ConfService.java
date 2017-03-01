package com.lsxy.area.api;

import com.lsxy.framework.core.exceptions.api.YunhuniApiException;

import java.util.List;
import java.util.Set;

/**
 * Created by liuws on 2016/8/25.
 * 会议相关
 */
public interface ConfService {

    /**
     * 创建会议
     * ip 调用方IP
     * appId 调用方AppID
     * max_duration 呼叫最大接通时间（秒）
     * max_parts 最大与会方数
     * recording 是否自动启动录音
     * auto_hangup 会议结束自动挂断与会方
     * bgm_file 背景音文件
     * callback_url 事件通知地址
     * user_data 用户数据
     * @return
     */
    public String create(String subaccountId,String ip,String appId,Integer maxDuration,Integer maxParts,
                         Boolean recording,Boolean autoHangup,String bgmFile,String userData) throws YunhuniApiException;

    /**
     * 解散会议
     */
    public boolean dismiss(String subaccountId,String ip,String appId,String confId) throws YunhuniApiException;

    /**
     * 会议邀请呼叫
     * @return
     */
    public String invite(String subaccountId,String ip,String appId,String confId,
                         String from,String to,Integer maxDuration,Integer maxDialDuration,
                         Integer dialVoiceStopCond,String playFile,Integer voiceMode) throws YunhuniApiException;

    /**
     * 将通话加入到会议
     * @return
     */
    public boolean join(String subaccountId,String ip,String appId,String confId,String callId,Integer maxDuration,
                        String playFile,Integer voiceMode) throws YunhuniApiException;


    /**
     * 退出会议
     * @return
     */
    public boolean quit(String subaccountId,String ip,String appId,String confId,String callId) throws YunhuniApiException;

    /**
     * 会议放音
     * @return
     */
    public boolean startPlay(String subaccountId,String ip,String appId,String confId,List<String> playFiles) throws YunhuniApiException;

    /**
     * 停止会议放音
     * @return
     */
    public boolean stopPlay(String subaccountId,String ip,String appId,String confId) throws YunhuniApiException;

    /**
     * 会议录音
     * @return
     */
    public boolean startRecord(String subaccountId,String ip,String appId,String confId,Integer maxDuration) throws YunhuniApiException;

    /**
     * 停止会议录音
     * @return
     */
    public boolean stopRecord(String subaccountId,String ip,String appId,String confId) throws YunhuniApiException;

    /**
     * 设置成员声音模式
     * 1: 能够听；能够说
     * 2: 不能听；能够说
     * 3: 能够听；不能说
     * 4: 不能听；不能说
     * @return
     */
    public boolean setVoiceMode(String subaccountId,String ip,String appId,String confId,String callId,Integer voiceMode) throws YunhuniApiException;

    /**
     * 将呼叫加入到会议
     * @param call_id 呼叫业务id
     * @param conf_id   会议业务id
     * @return
     */
    public boolean confEnter(String call_id,String conf_id, Integer maxDuration, String playFile, Integer voiceMode) throws YunhuniApiException;


    public boolean outOfParts(String confId,Integer maxParts);

    /**
     * 增加会议成员
     * @param confId
     */
    public void incrPart(String confId,String callId);

    /**
     * 减少会议成员
     * @param confId
     */
    public void decrPart(String confId,String callId);

    /**
     * 获取会议成员的call_id
     * @param confId
     * @return
     */
    public Set<String> getParts(String confId);

    /**
     * 弹出会议成员，并清空
     */
    public Set<String> popParts(String confId);
}
