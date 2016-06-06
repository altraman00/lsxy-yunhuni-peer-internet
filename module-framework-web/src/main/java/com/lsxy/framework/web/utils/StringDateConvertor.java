package com.lsxy.framework.web.utils;

import java.util.Date;

import org.springframework.core.convert.converter.Converter;

import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.StringUtil;

public class StringDateConvertor implements Converter<String, Date>{

	public Date convert(String source) {
		if(StringUtil.isNotEmpty(source)){
			return DateUtils.parseDate(source);
		}else{
			return new Date();
		}
		
	}
	
 
}
