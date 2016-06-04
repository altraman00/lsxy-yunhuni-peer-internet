package com.lsxy.app.portal.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *   
 * 防止重复提交表单的注解.
 * 
 * @author WangYun
 *  
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AvoidDuplicateSubmit {

	/**     
	 * 
	 * @return
	 */
	boolean needValidate() default false;
}
