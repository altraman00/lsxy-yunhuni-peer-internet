package com.lsxy.framework.core.exceptions;
/**
 *  
 *描述：
 *	配置文件不存在异常,但在类路径中未发现config.properties文件时，出现该异常
 *时间：2010-1-12
 *作者：谭畅
 */
@SuppressWarnings("serial")
public class ConfigFileNotExistException extends RuntimeException {
	public ConfigFileNotExistException() {
		super("配置文件config.properties不存在");
	}
}
