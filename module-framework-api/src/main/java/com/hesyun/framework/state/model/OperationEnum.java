package com.hesyun.framework.state.model;

public enum OperationEnum {
	OPERATE_OTHER("其他",100), 
	OPERATE_PERSON_ADD("新增用户",1),
	OPERATE_PERSON_UPDATE("修改了个人信息.",2),
	OPERATE_PERSON_DELETE("删除用户",3),
	OPERATE_PERSON_MODIFY_PASSWROD("修改密码 ",4),
	OPERATE_PERSON_INI_PASSWROD("初始化密码",5),
	OPERATE_PERSON_RESET_PASSWROD("重置密码",6),
	OPERATE_PERSON_UNLOCK("解禁用户[<a href='/portal/tenant/admin/kf/kfinfo?id=${p.id}' target='_blank'>${p.name}</a>]",7),
	OPERATE_PERSON_LOCK("禁用用户[<a href='/portal/tenant/admin/kf/kfinfo?id=${p.id}' target='_blank'>${p.name}</a>]",8),
	OPERATE_PERSON_LIST("用户列表",9),
	OPERATE_PERSON_XIETIAO_ADD("协调新增",10),
	OPERATE_PERSON_XIETIAO_DELETE("协调删除",11),
	OPERATE_PERSON_XIETIAO_UPDATE("协调修改",12),
	OPERATE_TENANT_APPROVE("租户审核",13),
	OPERATE_TENANT_USER_APPROVE("客服审核",14),
	OPERATE_TENANT_USER_CHECKIN("客服签入",15),
	OPERATE_TENANT_USER_CHECKOUT("客服签出",16),
	OPERATE_TENANT_USER_UNSUBSCRIB("客服取消关注",34),
	OPERATE_TENANT_USER_SUBSCRIB("客服关注",35),
	OPERATE_TENANT_USER_SERVICE("客服接单服务",17),
	OPERATE_PERSON_LOGIN("用户[<a href='/portal/tenant/admin/kf/kfinfo?id=${p.id}' target='_blank'>${p.name}</a>]登录和声云平台",18),
	OPERATE_PERSON_LOGOUT("用户${currentUser.name}注销和声云平台",19),
	OPERATE_TENANT_CREATE("租户申请",24),
	
	OPERATE_TENANT_AUTH_WEIXIN("授权开通企业微信客服渠道",20), 
	OPERATE_TENANT_AUTH_WEIBO("授权开通企业微博客服渠道",21), 
	OPERATE_TENANT_UNAUTH_WEIBO("解除微博绑定",22), 
	OPERATE_TENANT_UNAUTH_TAOBAO("解除淘宝绑定",23),
	OPERATE_TENANT_WEIXIN_MENU("修改微信菜单", 25),
	OPERATE_TENANT_WEIXIN_SUBSCRIBE("修改微信关注欢迎语", 26),
	OPERATE_TENANT_WEIXIN_REPLY_MODIFY("修改微信自动回复关键词", 27),
	OPERATE_TENANT_WEIXIN_REPLY_DELETE("删除微信自动回复关键词", 28),
	OPERATE_TENANT_WEIXIN_CONFIG("修改微信参数", 29),
	
	OPERATE_TENANT_SKILL_MODIFY("修改技能", 30),
	OPERATE_TENANT_SKILL_DELETE("删除技能", 31),
	
	OPERATE_TENANT_QUICK_REPLY_MODIFY("修改快捷回复", 32),
	OPERATE_TENANT_QUICK_REPLY_DELETE("删除快捷回复", 33),

	
	OPERATE_TENANT_USER_HAS_EXPIRED("客服${number}[${name}]的微信[${channelid}#]通道过期", 34),
	OPERATE_TENANT_USER_REPLY_LEAVE_MSG("回复留言",35),
	OPERATE_TENANT_USER_CREATE_WO("创建工单",36),
	OPERATE_TENANT_USER_PROCESS_WO("处理工单",37),
	OPERATE_TENANT_ADD_ARTICLE("新增素材库",38),
	
	OPERATE_TENANT_USER_CHECKIN_PC("客服PC端签入",39),
	OPERATE_TENANT_USER_CHECKOUT_PC("客服PC端签出",40),
	OPERATE_TENANT_USER_LOGIN_PC("客服进入PC坐席",41),
	
	OPERATE_TENANT_CUSTOMER_SUBSCIBE("客户关注公众号",42),
	OPERATE_TENANT_CUSTOMER_UNSUBSCIBE("客户取消关注",43),
	OPERATE_TENANT_CUSTOMER_STAFF_SERVICE("客户发起人工",44),

	OPERATE_WX_THIRD_PARTH_UNAUTH("取消了微信渠道[${channelName}]的授权",45),
	
	OPERATE_TENANT_USER_CHECKIN_WX("客服微信端签入",46),
	OPERATE_TENANT_USER_CHECKOUT_WX("客服微信端签出",47),
	OPERATE_TENANT_USER_WX_BUILD_SESSION("微信客服受理客户",48),
	OPERATE_TENANT_USER_WX_BACKWARD_SESSION("微信客服反向受理客户",49),
	
	OPERATE_TENANT_USER_CHECKIN_QY("客服企业号端签入",50),
	OPERATE_TENANT_USER_CHECKOUT_QY("客服企业号端签出",51),
	OPERATE_TENANT_USER_QY_BUILD_SESSION("企业号客服受理客户",52),
	OPERATE_TENANT_USER_QY_BACKWARD_SESSION("企业号客服反向受理客户",53),
	
	OPERATE_UNSOVLED_QUEUE("漏接队列",60);
	
	private String opName;
	private int value;
	// 构造方法
	private OperationEnum(String opName, int value) {
		this.opName = opName;
		this.value = value;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getOpName() {
		return opName;
	}
	public void setOpName(String opName) {
		this.opName = opName;
	}
	
	
	
}
