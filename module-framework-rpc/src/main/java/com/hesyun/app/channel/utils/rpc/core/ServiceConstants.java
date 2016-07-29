package com.hesyun.app.channel.utils.rpc.core;

/**
 * socket通讯命令常量
 * 
 * @author tandy
 * 
 */
public class ServiceConstants {

	//公用命令
	public static final String MN_CH_SENDMESSAGE = "MN_CH_SENDMESSAGE"; // 渠道管理器调用渠道的发送消息命令
	public static final String MN_CH_GETMENU  = "MN_CH_GETMENU"; // 渠道管理器获得渠道菜单命令
	public static final String MN_CH_CREATEMENU  = "MN_CH_CREATEMENU"; // 渠道管理器创建客户菜单命令
	public static final String MN_CH_MASS_SEND  = "MN_CH_MASS_SEND"; // 渠道群发指令
	public static final String MN_CH_CHANNEL_DELETE  = "MN_CH_CHANNEL_DELETE"; // 删除渠道

	public static final String CH_MN_CONNECT = "CH_MN_CONNECT"; // 渠道连接渠道管理器
	public static final String CH_MN_RECIVEDMESSAGE = "CH_MN_RECIVEDMESSAGE"; // 渠道调用渠道管理器收到消息命令
	public static final String CH_MN_SENDMESSAGE_FAILED = "CH_MN_SENDMESSAGE_FAILED"; // 渠道给客户发送消息失败

	
	//WebChat渠道专用命令
	public static final String WC_MN_INCOMING = "WC_MN_INCOMING"; // webchat渠道向渠道管理器发送incoming命令
	public static final String WC_MN_STATUSCHANGE = "WC_MN_STATUSCHANGE"; // 通知渠道管理器会话状态变化命令
	public static final String MN_WC_QUEUEINFO = "MN_WC_QUEUEINFO"; // 渠道管理器发送给渠道的队列变更信息
	public static final String MN_WC_STATUSCHANGE = "MN_WC_STATUSCHANGE"; // 通知渠道会话状态变化命令

	//微信渠道专用命令
	public static final String WX_MN_WXINFO  = "WX_MN_WXINFO"; // 根据渠道clientId获取指定渠道对应的商户的微信配置信息

	public static final String WX_MN_THIRD_PARTY_INFO  = "WX_MN_THIRD_PARTY_INFO"; // 微信第三方授权账户信息

	public static final String WX_MN_THIRD_PARTY_TOKEN  = "WX_MN_THIRD_PARTY_TOKEN"; // 微信第三方平台Token
	public static final String WX_MN_THIRD_PARTY_AUTH  = "WX_MN_THIRD_PARTY_AUTH"; // 微信第三方授权账户信息
	public static final String WX_MN_THIRD_PARTY_UNAUTH  = "WX_MN_THIRD_PARTY_UNAUTH"; // 微信第三方授权账户取消授权信息
	public static final String WX_MN_WEIXIN_SNS_INFO  = "WX_MN_WEIXIN_SNS_INFO"; // 根据publicId获取指定微信公众号信息
	public static final String MN_WX_KHINFO  = "MN_WX_KHINFO"; // 渠道管理器获取微信客户信息命令
	public static final String MN_WX_GETMENU  = "MN_WX_GETMENU"; // 渠道管理器获取微信客户菜单命令
	public static final String MN_WX_CHECKINFO  = "MN_WX_CHECKINFO"; // 渠道管理器验证客户信息命令
	public static final String MN_WX_ADDNEWTENANT  = "MN_WX_ADDNEWTENANT"; // 添加新租户命令
    public static final String MN_WX_SENDMESSAGE  = "MN_WX_SENDMESSAGE"; // 微信渠道发消息命令
    public static final String MN_WX_REPLYMESSAGE  = "MN_WX_REPLYMESSAGE"; // 微信渠道发自动回复命令
    public static final String MN_WX_STAFFMESSAGE  = "MN_WX_STAFFMESSAGE"; // 微信渠道发自动回复命令
    public static final String MN_WX_GETQRCODE  = "MN_WX_GETQRCODE"; // 获得微信二维码

    // 微博渠道专用命令
    public static final String WB_MN_WBINFO = "WB_MN_WBINFO"; // 根据渠道clientId获取指定渠道对应的商户的微博配置信息
    public static final String MN_WB_ADDNEWTENANT = "MN_WB_ADDNEWTENANT"; // 添加新租户命令
    public static final String MN_WB_GETMENU  = "MN_WB_GETMENU"; // 渠道管理器获取微博客户菜单命令
    public static final String MN_WB_REPLYMESSAGE  = "MN_WB_REPLYMESSAGE"; // 微博渠道发自动回复命令
    public static final String MN_WB_STAFFMESSAGE  = "MN_WB_STAFFMESSAGE"; // 微博渠道发自动回复命令
    public static final String MN_WB_DELETE_CHANNEL_WB  = "MN_WB_DELETE_CHANNEL_WB"; // 删除微博渠道命令
    public static final String MN_WB_GETQRCODE  = "MN_WB_GETQRCODE"; // 获得微博二维码
    
}
