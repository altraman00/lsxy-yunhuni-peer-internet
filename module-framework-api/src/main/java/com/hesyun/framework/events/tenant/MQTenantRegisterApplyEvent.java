package com.hesyun.framework.events.tenant;

import com.hesyun.framework.events.AbstractTenantEvent;

/**
 * 客户注册商户事件
 * @author 30
 * 消息体:Tenant对象的序列化属性
 */
public class MQTenantRegisterApplyEvent extends AbstractTenantEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tenantId;
	
	public MQTenantRegisterApplyEvent(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getTenantId() {
		return tenantId;
	}
	
}
