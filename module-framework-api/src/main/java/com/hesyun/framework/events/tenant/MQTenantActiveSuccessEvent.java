package com.hesyun.framework.events.tenant;

import com.hesyun.framework.events.AbstractTenantEvent;
import com.hesyun.framework.module.mq.GlobalEvent;

/**
 * 租户通过邮件激活后发出的事件
 * @author WangYun
 * 消息体:Tenant对象的序列化属性
 */
@GlobalEvent
public class MQTenantActiveSuccessEvent extends AbstractTenantEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tenantId;
	
	public MQTenantActiveSuccessEvent(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getTenantId() {
		return tenantId;
	}
	
}
