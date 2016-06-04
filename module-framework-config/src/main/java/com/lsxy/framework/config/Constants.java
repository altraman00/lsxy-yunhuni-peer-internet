package com.lsxy.framework.config;

/**
 * @author sds 
 */
public class Constants { 

	/**
	 * 配置文件 
	 */
	public static final String DEFAULT_CONFIG_FILE="config.properties";
	public static final String DEFAULT_CONFIG_FILE_ENCODING="UTF-8";
	
	/**
	 * 消息配置文件
	 */
	public static final String MESSAGE_CONFIG_FILE="messages.properties";
	public static final String MESSAGE_CONFIG_FILE_ENCODING="UTF-8";
	
	/**
	 * 平台管理员默认的用户名称
	 */
	public static final String ADMINISTRATOR_NAME="系统管理员";
	public static final String ADMINISTRATOR_ACCOUNT= SystemConfig.getProperty("root.username","root");
	public static final String ADMINISTRATOR_ID="1";
	
	/**
	 * 定义游客,主要是在登录时候发生异常时候采用游客方式记录
	 */
	public static final String GUEST_ID="2";
	public static final String GUEST_NAME="匿名用户";
	public static final String GUEST_ACCOUNT="guest";	
	
	/**
	 * 存放在SESSION中的当前用户信息
	 */
	public static final String SESSION_CURRENT_PERSONID="SESSION_CURRENT_PERSON_ID";
	public static final String SESSION_CURRENT_PERSON_LOGINNAME="SESSION_CURRENT_PERSON_LOGINNAME";
	public static final String SESSION_CURRENT_PERSON_NAME="SESSION_CURRENT_PERSON_NAME";
	public static final String SESSION_CURRENT_PERSONROLES = "SESSION_CURRENT_PERSONROLES";
	
	public static final String SESSION_CURRENT_GROUP_NAME="SESSION_CURRENT_GROUP_NAME";
	public static final String SESSION_CURRENT_GROUP_ID="SESSION_CURRENT_GROUP_ID";
	public static final String SESSION_CURRENT_GROUP_CODE="SESSION_CURRENT_GROUP_CODE";
	public static final String SESSION_CURRENT_GROUP_TYPE="SESSION_CURRENT_GROUP_TYPE";

	
	/**
	 * 默认起始排序号
	 */
	public static final int DEFAULT_START_SORTNO = 1000;
	
	/**
	 * 佯装登录密码
	 */
	public static final String YANGZHUANGDENGLU_SPECAIL_CHAR = "$#%yangzhuangdenglu";
	
	
}
