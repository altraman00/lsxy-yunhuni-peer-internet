package com.lsxy.app.portal.tenant.event;

import com.hesyun.framework.tenant.model.Tenant;
import org.springframework.context.ApplicationEvent;

/**
 * 创建租户事件
 * @author T420
 *
 */
public class TenantCreateEvent extends ApplicationEvent {

	private Tenant tenant;
	
	public Tenant getTenant() {
		return tenant;
	}

	public TenantCreateEvent(Object source, Tenant tenant) {
		super(source);
		this.tenant = tenant;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 955142460368541358L;

}
