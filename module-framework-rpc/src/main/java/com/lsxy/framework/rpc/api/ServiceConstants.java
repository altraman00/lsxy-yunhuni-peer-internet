package com.lsxy.framework.rpc.api;

/**
 * socket通讯命令常量
 * 
 * @author tandy
 * 
 */
public class ServiceConstants {

	public static final String CH_MN_CONNECT = "CH_MN_CONNECT"; // 渠道连接渠道管理器

	public static final String MN_CH_SYS_CALL = "MN_CH_SYS_CALL";  //呼叫业务
	public static final String MN_CH_SYS_CALL_CONF_ENTER = "MN_CH_SYS_CALL_CONF_ENTER";//将呼叫加入会议
	public static final String MN_CH_SYS_CALL_CONF_EXIT = "";//将呼叫退出会议
	public static final String MN_CH_SYS_CONF = "MN_CH_SYS_CONF";	//会议
	public static final String MN_CH_SYS_CONF_RELEASE = "MN_CH_SYS_CONF_RELEASE"; //删除会议
	public static final String MN_CH_EXT_DUO_CALLBACK = "MN_CH_EXT_DUO_CALLBACK";  //双向回拔

	public static final String CH_MN_CTI_EVENT = "CH_MN_CTI_EVENT" ; 	//CTI 事件通知
	public static final String MN_CH_CTI_API = "MN_CH_CTI_API";		//区域管理器向CTI发送API指令

    public static final String MN_CH_TEST_STASTICS_RESET = "MN_CH_TEST_STASTICS_RESET";//测试使用,重置计数器

	public static final String MN_CH_TEST_ECHO= "MN_CH_TEST_ECHO";//压力测试使用

	public static final String CH_MN_HEARTBEAT_ECHO = "CH_MN_HEARTBEAT_ECHO ";		//心跳请求

}
