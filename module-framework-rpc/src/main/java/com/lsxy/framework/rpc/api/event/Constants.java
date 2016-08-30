package com.lsxy.framework.rpc.api.event;

/**
 * Created by liuws on 2016/8/30.
 */
public class Constants {
    public static final String EVENT_SYS_CALL_ON_INCOMING ="sys.call.on_incoming";

    /**会议相关事件**/
    public static final String EVENT_SYS_CONF_ON_START = "EVENT_SYS_CONF_ON_START";	//会议创建成功
    public static final String EVENT_SYS_CONF_ON_FAIL = "EVENT_SYS_CONF_ON_FAIL";	//会议创建失败
    public static final String EVENT_SYS_CONF_ON_TIMEOUT = "EVENT_SYS_CONF_ON_TIMEOUT";	//会议创建超时
    public static final String EVENT_SYS_CONF_ON_RELEASE = "sys.conf.on_released";//会议结束事件

    public static final String EVENT_SYS_CALL_ON_START = "EVENT_SYS_CALL_ON_START";
    public static final String EVENT_SYS_CALL_ON_FAIL = "EVENT_SYS_CALL_ON_FAIL";
    public static final String EVENT_SYS_CALL_ON_TIMEOUT = "EVENT_SYS_CALL_ON_TIMEOUT";
}
