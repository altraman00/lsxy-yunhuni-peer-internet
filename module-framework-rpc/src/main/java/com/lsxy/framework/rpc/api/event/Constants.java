package com.lsxy.framework.rpc.api.event;

/**
 * Created by liuws on 2016/8/30.
 */
public class Constants {
    /**语音呼叫事件**/
    public static final String EVENT_SYS_CALL_ON_INCOMING ="sys.call.on_incoming";//呼入呼叫事件
    public static final String EVENT_SYS_CALL_ON_DIAL_COMPLETED = "sys.call.on_dial_completed";//拨号结束事件
    public static final String EVENT_SYS_CALL_ON_START = "EVENT_SYS_CALL_ON_START";//呼叫资源创建成功事件
    public static final String EVENT_SYS_CALL_ON_FAIL = "EVENT_SYS_CALL_ON_FAIL";//呼叫资源创建失败事件
    public static final String EVENT_SYS_CALL_ON_CONF_COMPLETED = "sys.call.on_conf_completed";//呼叫加入会议结束
    public static final String EVENT_SYS_CALL_CONF_ENTER_SUCC = "EVENT_SYS_CALL_CONF_ENTER_SUCC";//将呼叫加入会议成功事件
    public static final String EVENT_SYS_CALL_CONF_ENTER_FAIL = "EVENT_SYS_CALL_CONF_ENTER_FAIL";//将呼叫加入会议失败事件
    public static final String EVENT_SYS_CALL_ON_ANSWER_COMPLETED = "sys.call.on_answer_completed";//应答调用成功事件
    public static final String EVENT_SYS_CALL_ON_RELEASE = "sys.call.on_released";//呼叫被释放事件
    public static final String EVENT_SYS_CALL_ON_RINGING = "sys.call.on_ringing";//呼叫振铃事件
    public static final String EVENT_SYS_CALL_ON_PLAY_COMPLETED = "sys.call.on_play_completed";//放音结束
    public static final String EVENT_SYS_CALL_ON_RECORD_COMPLETED = "sys.call.on_record_completed";//录音结束事件
    public static final String EVENT_SYS_CALL_ON_SEND_DTMF_COMPLETED = "sys.call.on_send_dtmf_completed";//发码结束事件
    public static final String EVENT_SYS_CALL_ON_RECEIVE_DTMF_COMPLETED = "sys.call.on_receive_dtmf_completed";//收码结束事件
    public static final String EVENT_SYS_CALL_CONNECT_ON_START = "EVENT_SYS_CALL_CONNECT_ON_START";//调用双通道连接成功事件
    public static final String EVENT_SYS_CALL_ON_CONNECT_COMPLETED="sys.call.on_connect_completed";//双通道连接结束事件
    public static final String EVENT_SYS_CALL_PLAY_ON_FAIL = "EVENT_SYS_CALL_PLAY_ON_FAIL";
    public static final String EVENT_SYS_CALL_RECORD_ON_FAIL = "EVENT_SYS_CALL_RECORD_ON_FAIL";
    public static final String EVENT_SYS_CALL_RECEIVE_DTMF_ON_FAIL = "EVENT_SYS_CALL_RECEIVE_DTMF_ON_FAIL";
    public static final String EVENT_SYS_CALL_SEND_DTMF_ON_FAIL ="EVENT_SYS_CALL_SEND_DTMF_ON_FAIL";
    /**会议相关事件**/
    public static final String EVENT_SYS_CONF_ON_START = "EVENT_SYS_CONF_ON_START";	//会议创建成功
    public static final String EVENT_SYS_CONF_ON_FAIL = "EVENT_SYS_CONF_ON_FAIL";	//会议创建失败
    public static final String EVENT_SYS_CONF_ON_TIMEOUT = "EVENT_SYS_CONF_ON_TIMEOUT";	//会议创建超时
    public static final String EVENT_SYS_CONF_ON_RELEASE = "sys.conf.on_released";//会议结束事件
    public static final String EVENT_SYS_CONF_ON_PLAY_COMPLETED = "sys.conf.on_play_completed";//会议放音结束事件
    public static final String EVENT_SYS_CONF_ON_RECORD_COMPLETED = "sys.conf.on_record_completed";//会议录音结束事件


    /**简单Api相关**/
    public static final String EVENT_EXT_DUO_CALLBACK_SUCCESS = "EVENT_EXT_DUO_CALLBACK_SUCCESS";
    public static final String EVENT_EXT_DUO_CALLBACK_ON_RELEASED = "ext.duo_callback.on_released";
    public static final String EVENT_EXT_NOTIFY_CALL_SUCCESS = "EVENT_EXT_NOTIFY_CALL_SUCCESS";
    public static final String EVENT_EXT_NOTIFY_CALL_ON_RELEASED = "ext.notify_call.on_released";
    public static final String EVENT_EXT_CAPTCHA_CALL_SUCCESS = "EVENT_EXT_CAPTCHA_CALL_SUCCESS";
    public static final String EVENT_EXT_CAPTCHA_CALL_ON_RELEASED = "ext.captcha_call.on_released";
    public static final String EVENT_EXT_VERIFY_CALL_SUCCESS = "EVENT_EXT_VERIFY_CALL_SUCCESS";
    public static final String EVENT_EXT_CALL_ON_FAIL = "EVENT_EXT_CALL_ON_FAIL";
    public static final String EVENT_EXT_CALL_ON_TIMEOUT = "EVENT_EXT_CALL_ON_TIMEOUT";

    /**
     * cdr
     */
    public static final String SYS_ON_CHAN_CLOSED = "sys.on_chan_closed";


}
