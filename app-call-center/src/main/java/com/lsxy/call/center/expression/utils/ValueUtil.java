package com.lsxy.call.center.expression.utils;

import com.lsxy.call.center.expression.tokens.DataType;
import com.lsxy.call.center.expression.tokens.Valuable;

import java.math.BigDecimal;
import java.util.Calendar;


public class ValueUtil {
	public static BigDecimal getNumberValue(Valuable valuable) {
		if(valuable.getDataType() == DataType.NUMBER
				&& valuable.getValue() != null)
			return (BigDecimal) valuable.getValue();
		return null;
	}
	
	public static String getStringValue(Valuable valuable) {
		if(valuable.getDataType() == DataType.STRING
				&& valuable.getValue() != null)
			return (String) valuable.getValue();
		return null;
	}
	
	public static Character getCharValue(Valuable valuable) {
		if(valuable.getDataType() == DataType.CHARACTER 
				&& valuable.getValue() != null)
			return (Character) valuable.getValue();
		return null;
	}
	
	public static Calendar getDateValue(Valuable valuable) {
		if(valuable.getDataType() == DataType.DATE 
				&& valuable.getValue() != null)
			return (Calendar) valuable.getValue();
		return null;
	}
	
	public static Boolean getBooleanValue(Valuable valuable) {
		if(valuable.getDataType() == DataType.BOOLEAN
				&& valuable.getValue() != null)
			return (Boolean) valuable.getValue();
		return null;
	}

}
