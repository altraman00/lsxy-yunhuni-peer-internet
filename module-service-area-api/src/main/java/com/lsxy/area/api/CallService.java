package com.lsxy.area.api;

import com.lsxy.area.api.exceptions.InvokeCallException;

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
    String call(String from, String to, int maxAnswerSec, int maxRingSec) throws InvokeCallException;

    /**
     * 双向回拔
     * @param ip ip
     * @param appId 应用id
     * @param duoCallbackVO 双向回拔数据
     * @param account_id 鉴权账号
     * @return
     */
    String duoCallback(String ip,String appId, DuoCallbackVO duoCallbackVO, String account_id);
}
