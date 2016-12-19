package com.lsxy.call.center.expression.syntax.function;

import com.lsxy.call.center.expression.tokens.DataType;
import com.lsxy.call.center.expression.tokens.Valuable;

public class Get extends Function {

	@Override
	public int getArgumentNum() {
		return 1;
	}

	@Override
	protected Object executeFunction(Valuable[] arguments) {
		return 1;
	}

	@Override
	public String getName() {
		return "get";
	}

	@Override
	public DataType[] getArgumentsDataType() {
		return new DataType[]{DataType.STRING};
	}
}
