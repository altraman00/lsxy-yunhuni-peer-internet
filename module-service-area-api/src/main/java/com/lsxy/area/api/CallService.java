package com.lsxy.area.api;

import com.lsxy.area.api.exceptions.YunhuniApiException;

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
     * @param duoCallbackDTO 双向回拔数据
     * @return
     */
    String duoCallback(String ip,String appId, DuoCallbackDTO duoCallbackDTO) throws YunhuniApiException;
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
     * @param notifyCallDTO
     * @return
     */
    String notifyCall(String ip, String appId, NotifyCallDTO notifyCallDTO) throws YunhuniApiException;

    /**
     * 语音验证码/高级版 有收码功能
     * @param ip
     * @param appId
     * @param dto
     * @return
     */
    String captchaCall(String ip, String appId, CaptchaCallDTO dto) throws YunhuniApiException;

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
