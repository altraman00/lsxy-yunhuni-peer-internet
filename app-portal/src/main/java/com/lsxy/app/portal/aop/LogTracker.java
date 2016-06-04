package com.lsxy.app.portal.aop;

import com.hesyun.framework.state.model.OperationEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 
 * @author tandy
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogTracker {
	/**
	 * 
	 * @return
	 */
	OperationEnum opType();
	/**
	 * 
	 * @return
	 */
	String value() default "";
}
 