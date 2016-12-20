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
	public static final String MN_CH_SYS_CALL_PLAY_START = "MN_CH_SYS_CALL_PLAY_START";  //呼叫放音
	public static final String MN_CH_SYS_CALL_PLAY_STOP = "MN_CH_SYS_CALL_PLAY_STOP";  //停止呼叫放音
	public static final String MN_CH_SYS_CALL_RECORD_START = "MN_CH_SYS_CALL_RECORD_START";  //呼叫录音
	public static final String MN_CH_SYS_CALL_SEND_DTMF_START = "MN_CH_SYS_CALL_SEND_DTMF_START";  //开始发送 DTMF 码
	public static final String MN_CH_SYS_CALL_RECEIVE_DTMF_START = "MN_CH_SYS_CALL_RECEIVE_DTMF_START";//开始接收DTMF码
	public static final String MN_CH_SYS_CALL_ANSWER = "MN_CH_SYS_CALL_ANSWER";//应答
	public static final String MN_CH_SYS_CALL_DROP = "MN_CH_SYS_CALL_DROP";//挂断
	public static final String MN_CH_SYS_CALL_REJECT = "MN_CH_SYS_CALL_REJECT";//拒接
	public static final String MN_CH_SYS_CALL_CONNECT_START = "MN_CH_SYS_CALL_CONNECT_START";//双通道拨号

	public static final String MN_CH_SYS_CALL_CONF_ENTER = "MN_CH_SYS_CALL_CONF_ENTER";//将呼叫加入会议
	public static final String MN_CH_SYS_CALL_CONF_EXIT = "MN_CH_SYS_CALL_CONF_EXIT";//将呼叫退出会议
	public static final String MN_CH_SYS_CONF = "MN_CH_SYS_CONF";	//会议
	public static final String MN_CH_SYS_CONF_RELEASE = "MN_CH_SYS_CONF_RELEASE"; //删除会议
	public static final String MN_CH_SYS_CONF_PLAY = "MN_CH_SYS_CONF_PLAY";//会议放音
	public static final String MN_CH_SYS_CONF_PLAY_STOP = "MN_CH_SYS_CONF_PLAY_STOP";//停止会议放音
	public static final String MN_CH_SYS_CONF_RECORD = "MN_CH_SYS_CONF_RECORD";//会议录音
	public static final String MN_CH_SYS_CONF_RECORD_STOP = "MN_CH_SYS_CONF_RECORD_STOP";//停止会议录音
	public static final String MN_CH_SYS_CONF_SET_PART_VOICE_MODE = "MN_CH_SYS_CONF_SET_PART_VOICE_MODE";//设置与会者的声音收放模式

	public static final String MN_CH_EXT_DUO_CALLBACK = "MN_CH_EXT_DUO_CALLBACK";  //双向回拔
	public static final String MN_CH_EXT_DUO_CALLBACK_CANCEL = "MN_CH_EXT_DUO_CALLBACK_CANCEL";  //取消双向回拔
	public static final String MN_CH_EXT_NOTIFY_CALL = "MN_CH_EXT_NOTIFY_CALL";  //外呼通知
	public static final String MN_CH_EXT_CAPTCHA_CALL = "MN_CH_EXT_CAPTCHA_CALL";  //语音验证码/高级
	public static final String MN_CH_EXT_VERIFY_CALL = "MN_CH_EXT_VERIFY_CALL";  //语音验证码

	public static final String CH_MN_CTI_EVENT = "CH_MN_CTI_EVENT" ; 	//CTI 事件通知

	public static final String MN_CH_CTI_API = "MN_CH_CTI_API";		//区域管理器向CTI发送API指令

    public static final String MN_CH_TEST_STASTICS_RESET = "MN_CH_TEST_STASTICS_RESET";//测试使用,重置计数器

	public static final String MN_CH_TEST_ECHO= "MN_CH_TEST_ECHO";//压力测试使用

	public static final String CH_MN_HEARTBEAT_ECHO = "CH_MN_HEARTBEAT_ECHO";		//心跳请求

	public static final String MN_CH_VF_SYNC = "MN_CH_VF_SYNC";		//放音文件同步指令
	public static final String MN_CH_VF_SYNC_OK = "MN_CH_VF_SYNC_OK";		//放音文件同步成功指令
	public static final String MN_CH_VF_DELETED = "MN_CH_VF_DELETED";		//放音文件删除指令
	public static final String MN_CH_VF_DELETED_OK = "MN_CH_VF_DELETED_OK";		//放音文件删除成功指令
	public static final String MN_CH_RF_SYNC = "MN_CH_RF_SYNC";//录音文件同步指令
	public static final String MN_CH_RF_SYNC_OK = "MN_CH_RF_SYNC_OK";		//录音文件同步成功指令
	public static final String MN_CH_RF_DELETED = "MN_CH_RF_DELETED";		//录音文件删除指令
	public static final String MN_CH_RF_DELETED_OK = "MN_CH_RF_DELETED_OK";		//录音文件删除成功指令

}
