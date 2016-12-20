package com.lsxy.call.center.expression.tokens;

import com.lsxy.call.center.expression.syntax.Executable;

/**
 * 可执行动作
 * @author shanxuecheng
 *
 */
public final class ExecutionToken implements Token {
	
	private Executable executable;
	
	public ExecutionToken(TokenBuilder builder) {
		this.executable = builder.getExecutable();
	}
	
	public ExecutionToken(Executable executable) {
		this.executable = executable;
	}
	
	public Executable getExecutable() {
		return executable;
	}

	public final TokenType getTokenType() {
		return TokenType.EXECUTION;
	}

}
