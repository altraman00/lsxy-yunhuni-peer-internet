package com.lsxy.framework.core.utils;

import com.fasterxml.jackson.annotation.JsonFilter;

/**
 *  提供json串行化支持，可以支持序列化指定的字段或者指定排除哪些字段
 *  默认方法toJson串行化所有没有@jsonIgonre的字段
 *  使用JSONUtil2.objectToJsonInclude方法可以指定只需要的字段被串行化
 * @author T420
 *
 */
@JsonFilter("fieldFilter")
public interface JSONUtil2Serialization {
	public String toJson();
}
