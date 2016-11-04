package com.lsxy.call.center.expression.test;

import com.lsxy.call.center.expression.Expression;
import com.lsxy.call.center.expression.ExpressionFactory;
import com.lsxy.call.center.expression.lexical.LexicalAnalyzer;
import com.lsxy.call.center.expression.lexical.LexicalException;
import com.lsxy.call.center.expression.tokens.TerminalToken;
import junit.framework.TestCase;

import java.util.List;


public class TestLexical extends TestCase {
	private ExpressionFactory factory = ExpressionFactory.getInstance();
	
	public void testNumber() {
		Expression expression = factory.getExpression("1 1.1 1.10 1e+2 1.1e-1");
		lexicalAnalysis(expression);
	}
	
	public void testDelimiter() {
		Expression expression = factory.getExpression("+-*/ >=<= ><,;&& ||!");
		lexicalAnalysis(expression);
	}
	
	public void testBoolean() {
		Expression expression = factory.getExpression("true false TRUE FALSE");
		lexicalAnalysis(expression);
	}
	
	public void testDate() {
		Expression expression = factory.getExpression("[2011-1-11] [2011-01-11] [2011-1-11 1:1:1] [2011-1-11 23:59:59]");
		lexicalAnalysis(expression);
	}
	
	public void testString() {
		Expression expression = factory.getExpression(" \"as\" ");
		lexicalAnalysis(expression);
	}
	
	public void testChar() {
		Expression expression = factory.getExpression(" 'a' ");
		lexicalAnalysis(expression);
	}
	
	public void testFunction() {
		Expression expression = factory.getExpression(" max abs ");
		lexicalAnalysis(expression);
	}
	
	public void testError() {
		Expression expression = factory.getExpression(" &2");
		lexicalAnalysis(expression);
	}

	public void test1111() {
		long start = System.currentTimeMillis();
		Expression expression = factory.getExpression("has(\"投\") && get(\"手机\") > 60;");
		System.out.println(expression.reParseAndEvaluate());
		System.out.println(System.currentTimeMillis() - start);
	}
	private void lexicalAnalysis(Expression expression) {
		LexicalAnalyzer la = new LexicalAnalyzer();
		try {
			List<TerminalToken> tokens = la.analysis(expression.getExpression(), expression.getFunctionDefinitions());
			PrintExpression.printTokens(tokens);
		} catch (LexicalException e) {
			e.printStackTrace();
		}
	}
}
