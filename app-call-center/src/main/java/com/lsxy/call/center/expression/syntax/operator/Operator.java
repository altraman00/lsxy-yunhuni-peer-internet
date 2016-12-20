package com.lsxy.call.center.expression.syntax.operator;

import com.lsxy.call.center.expression.syntax.ArgumentsMismatchException;
import com.lsxy.call.center.expression.syntax.Executable;
import com.lsxy.call.center.expression.tokens.TokenBuilder;
import com.lsxy.call.center.expression.tokens.Valuable;

/**
 * 操作符
 * @author shanxuecheng
 *
 */
public abstract class Operator implements Executable {

	/**
	 * 操作符名
	 */
	private final String operatorName;

	public Operator(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getOperatorName() {
		return operatorName;
	}

	/**
	 * 执行操作符
	 */
	public Valuable execute(Valuable[] arguments)
			throws ArgumentsMismatchException {
		Object result = operate(arguments);
		return TokenBuilder.buildRuntimeValue(result);
	}

	/**
	 * 提供操作符执行逻辑
	 * @param arguments
	 * @return
	 * @throws ArgumentsMismatchException
	 */
	protected abstract Object operate(Valuable[] arguments)
			throws ArgumentsMismatchException;

}
