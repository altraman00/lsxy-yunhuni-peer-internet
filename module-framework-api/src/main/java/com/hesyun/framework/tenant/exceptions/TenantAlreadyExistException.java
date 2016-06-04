package com.hesyun.framework.tenant.exceptions;

/**
 * 租户已存在
 * @author T420
 *
 */
@SuppressWarnings("unused")
public class TenantAlreadyExistException extends Exception {
	private String tenantUn;
	private String tenantName;
	public TenantAlreadyExistException(String tenantUn,String tenantName){
		super("租户["+tenantUn+"]["+tenantName+"]已存在");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 5909482581090187342L;

}
