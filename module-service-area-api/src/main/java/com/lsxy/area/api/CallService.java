package com.lsxy.area.api;

import com.lsxy.framework.core.exceptions.api.YunhuniApiException;

import java.util.List;

/**
 * Created by tandy on 16/8/17.
 * 呼叫相关服务器
 */
public interface CallService {
    /**
     * 发起呼叫
     * @param from
     * @param to
     * @return
     */
    String call(String from, String to, int maxAnswerSec, int maxRingSec) throws YunhuniApiException;

    /**
     * 双向回拔
     * @param ip ip
     * @param appId 应用id
     * @return
     */
    String duoCallback(String ip,String appId,String from1,String to1,String from2,String to2,String ring_tone,Integer ring_tone_mode,
                       Integer max_dial_duration,Integer max_call_duration ,Boolean recording,Integer record_mode,String user_data) throws YunhuniApiException;
    /**
     * 取消双向回拔
     * @param ip
     * @param appId
     * @param callId
     * @return
     */
    void duoCallbackCancel(String ip, String appId, String callId) throws YunhuniApiException;
    /**
     * 外呼通知
     * @param ip
     * @param appId
     * @return
     */
    String notifyCall(String ip, String appId, String from,String to,String play_file,List<List<Object>> play_content,
                      Integer repeat,Integer max_dial_duration,String user_data) throws YunhuniApiException;

    /**
     * 语音验证码/拨通电话 告诉用户验证码
     * @param ip
     * @param appId
     * @param from
     * @param to
     * @param maxDialDuration
     * @param verifyCode
     * @param playFile
     * @param userData
     * @return
     */
    String verifyCall(String ip, String appId, String from, String to, Integer maxDialDuration,
                      String verifyCode, String playFile,Integer repeat, String userData) throws YunhuniApiException;
}
