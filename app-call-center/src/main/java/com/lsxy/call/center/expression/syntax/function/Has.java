package com.lsxy.call.center.expression.syntax.function;

import com.lsxy.call.center.expression.tokens.DataType;
import com.lsxy.call.center.expression.tokens.Valuable;

public class Has extends Function {

	@Override
	public String getName() {
		return "has";
	}
	
	@Override
	public int getArgumentNum() {
		return 1;
	}
	
	@Override
	public DataType[] getArgumentsDataType() {
		return new DataType[]{DataType.STRING};
	}

	@Override
	protected Object executeFunction(Valuable[] arguments) {
		return true;
	}


}
