package com.lsxy.framework.mail;

public class MailConfigNotEnabledException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4739799642428542225L;

	public MailConfigNotEnabledException(){
		super("邮件配置未被启用，请在config.properties中启用邮件服务器配置");
	}
}
