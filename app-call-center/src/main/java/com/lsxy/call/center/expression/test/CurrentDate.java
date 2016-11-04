package com.lsxy.call.center.expression.test;

import com.lsxy.call.center.expression.syntax.function.Function;
import com.lsxy.call.center.expression.tokens.DataType;
import com.lsxy.call.center.expression.tokens.Valuable;

import java.util.Calendar;
import java.util.Date;

public class CurrentDate extends Function {
	@Override
	public String getName() {
		return "getDate";
	}
	
	@Override
	public int getArgumentNum() {
		return 0;
	}
	
	@Override
	public DataType[] getArgumentsDataType() {
		return null;
	}
	
	@Override
	protected Object executeFunction(Valuable[] arguments) {
		Calendar date = Calendar.getInstance();
		date.setTime(new Date());
		return date;
	}
}
