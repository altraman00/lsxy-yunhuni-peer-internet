package com.lsxy.framework.core.statistics;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by tandy on 16/8/10.
 * 用于标记属性中文翻译
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD ,ElementType.TYPE})
public @interface MarkField {
    String value() default "";
}
