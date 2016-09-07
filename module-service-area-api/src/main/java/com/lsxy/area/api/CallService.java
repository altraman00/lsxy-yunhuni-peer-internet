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
     * @param duoCallbackVO 双向回拔数据
     * @return
     */
    String duoCallback(String ip,String appId, DuoCallbackVO duoCallbackVO) throws YunhuniApiException;

    /**
     * 外呼通知
     * @param ip
     * @param appId
     * @param notifyCallVO
     * @return
     */
    String notifyCall(String ip, String appId, NotifyCallVO notifyCallVO) throws YunhuniApiException;
}
