package com.hesyun.framework.tenant.exceptions;

import java.util.Map;

@SuppressWarnings("serial")
public class InvalidateTenantAugumentsException extends Exception {

	@SuppressWarnings("rawtypes")
	public InvalidateTenantAugumentsException(Map jsonMap) {
		super("无效的租户创建参数："+jsonMap.toString());
	}

}
