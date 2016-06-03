package com.hesyun.core.exceptions;

public class MailContentNullException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4739799642428542225L;

	public MailContentNullException(){
		super("邮件内容为空！！");
	}
}
