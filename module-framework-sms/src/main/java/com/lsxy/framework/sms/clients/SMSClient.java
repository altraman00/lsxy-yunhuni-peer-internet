package com.lsxy.framework.sms.clients;

/**
 * Created by Tandy on 2016/7/7.
 */
public interface SMSClient {
    /**
     * 发送短信给制定手机号
     * @param to  目标手机号
     * @param msg 消息类型
     * @return
     *      成功  true
     *      失败  false
     */
    public String sendsms(String to, String msg);

    /**
     * 剩余额度查询
     * @return
     */
    public int balance();


    /**
     * 提供商名称
     * @return
     */
    public String getClientName();
}
