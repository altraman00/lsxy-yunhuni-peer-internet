package com.lsxy.framework.cache.exceptions;

/**
 * Redis事务执行失败
 * @author tandy
 *
 */
public class TransactionExecFailedException extends Exception {

	public TransactionExecFailedException(){}
	public TransactionExecFailedException(Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
