package com.hesyun.framework.events.tenant;

import com.hesyun.framework.events.AbstractTenantEvent;
import com.hesyun.framework.tenant.model.Tenant;

/**
 * 
 * 商户集成改变事件
 * @author tandy
 *
 */
public class MQTenantSkillChangeEvent extends AbstractTenantEvent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Tenant tenant;
	
	public MQTenantSkillChangeEvent(Tenant tenant){
		this.tenant = tenant;
	}

	public Tenant getTenant() {
		return tenant;
	}
	

}
