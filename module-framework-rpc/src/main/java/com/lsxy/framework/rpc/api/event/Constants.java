package com.lsxy.framework.rpc.api.event;

/**
 * Created by liuws on 2016/8/30.
 */
public class Constants {
    public static final String EVENT_SYS_CALL_ON_INCOMING ="sys.call.on_incoming";//呼入呼叫事件
    public static final String EVENT_SYS_CALL_ON_DIAL_COMPLETED = "sys.call.on_dial_completed";//拨号结束事件
    public static final String EVENT_SYS_CALL_ON_START = "EVENT_SYS_CALL_ON_START";//呼叫资源创建成功事件
    public static final String EVENT_SYS_CALL_ON_FAIL = "EVENT_SYS_CALL_ON_FAIL";//呼叫资源创建失败事件
    public static final String EVENT_SYS_CALL_ON_TIMEOUT = "EVENT_SYS_CALL_ON_TIMEOUT";//呼叫资源创建超时事件
    public static final String EVENT_SYS_CALL_ON_CONF_COMPLETED = "EVENT_SYS_CALL_ON_CONF_COMPLETED";//呼叫加入会议结束
    public static final String EVENT_SYS_CALL_CONF_ENTER_SUCC = "EVENT_SYS_CALL_CONF_ENTER_SUCC";//将呼叫加入会议成功事件
    public static final String EVENT_SYS_CALL_CONF_ENTER_FAIL = "EVENT_SYS_CALL_CONF_ENTER_FAIL";//将呼叫加入会议失败事件
    public static final String EVENT_SYS_CALL_CONF_ENTER_TIMEOUT = "EVENT_SYS_CALL_CONF_ENTER_TIMEOUT";//将呼叫加入会议超时事件

    /**会议相关事件**/
    public static final String EVENT_SYS_CONF_ON_START = "EVENT_SYS_CONF_ON_START";	//会议创建成功
    public static final String EVENT_SYS_CONF_ON_FAIL = "EVENT_SYS_CONF_ON_FAIL";	//会议创建失败
    public static final String EVENT_SYS_CONF_ON_TIMEOUT = "EVENT_SYS_CONF_ON_TIMEOUT";	//会议创建超时
    public static final String EVENT_SYS_CONF_ON_RELEASE = "sys.conf.on_released";//会议结束事件
    public static final String EVENT_SYS_CONF_ON_PLAY_COMPLETED = "sys.conf.on_play_completed";//会议放音结束事件
    public static final String EVENT_SYS_CONF_ON_RECORD_COMPLETED = "sys.conf.on_play_completed";//会议录音结束事件


    /**简单Api相关**/
    public static final String EVENT_EXT_DUO_CALLBACK_SUCCESS = "EVENT_EXT_DUO_CALLBACK_SUCCESS";

}
