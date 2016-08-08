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
	public static final String CH_MN_CTI_EVENT = "CH_MN_CTI_EVENT" ; 	//CTI 事件通知
	public static final String MN_CH_CTI_API = "MN_CH_CTI_API";		//区域管理器向CTI发送API指令
}
