package com.lsxy.app.portal.tenant.event;

import com.hesyun.framework.tenant.model.Tenant;
import org.springframework.context.ApplicationEvent;


/**
 * 租户审核完成事件
 * @author tandy
 *
 */
public class TenantCheckCompleteEvent extends ApplicationEvent {

	private Tenant tenant;
	
	public Tenant getTenant() {
		return tenant;
	}

	public TenantCheckCompleteEvent(Object source,Tenant tenant) {
		super(source);
		this.tenant = tenant;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7243268468012952268L;

}
