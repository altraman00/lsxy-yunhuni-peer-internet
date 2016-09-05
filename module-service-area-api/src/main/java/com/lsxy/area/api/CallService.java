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
     * 外呼通知
     * @param ip
     * @param appId
     * @param notifyCallDTO
     * @return
     */
    String notifyCall(String ip, String appId, NotifyCallDTO notifyCallDTO) throws YunhuniApiException;

    /**
     * 语音验证码
     * @param ip
     * @param appId
     * @param dto
     * @return
     */
    String captchaCall(String ip, String appId, CaptchaCallDTO dto) throws YunhuniApiException;
}
