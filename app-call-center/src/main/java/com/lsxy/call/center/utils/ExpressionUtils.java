package com.lsxy.call.center.utils;

import com.lsxy.call.center.expression.Expression;
import com.lsxy.call.center.expression.ExpressionFactory;
import com.lsxy.call.center.expression.tokens.DataType;
import com.lsxy.call.center.expression.tokens.Valuable;
import com.lsxy.framework.core.utils.StringUtil;
import org.springframework.util.DigestUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liuws on 2016/11/1.
 */
public final class ExpressionUtils {

    private static final ExpressionFactory factory = ExpressionFactory.getInstance();

    private static final String skill_regex="(has|get)\\(\"(.+?)\"\\)";
    private static final Pattern pattern  =Pattern.compile(skill_regex);
    private static final String VAR_PREFIX = "VAR_";
    private static final int VAR_DEFAULT_VALUE = 0;//没有该技能时，技能分为0
    private static final int VAR_MIN_VALUE = 1;
    /**默认的排序表达式**/
    private static final String DEFAULT_SORT_EXPRESSION = "1;";
    private ExpressionUtils(){}

    private static String getVarName(String varName){
        return VAR_PREFIX + DigestUtils.md5DigestAsHex(varName.getBytes());
    }

    private static void setVariable(Expression expression,Map<String,Integer> vars){
        expression.lexicalAnalysis();
        Set<String> vs = expression.getVariableNames();
        for (String v: vs) {
            expression.initVariable(v,VAR_DEFAULT_VALUE);
        }
        if(vars != null && vars.size()>0){
            Iterator<String> keys = vars.keySet().iterator();
            while (keys.hasNext()){
                String key = keys.next();
                Integer v = vars.get(key);
                expression.initVariable(getVarName(key),v == null ? VAR_DEFAULT_VALUE : v);
            }
        }
    }

    private static String expression(String ex){
        Matcher m =  pattern.matcher(ex);
        while(m.find()){
            String key = m.group(1);
            String val =  m.group(2);
            String varName = getVarName(val);
            String rel = varName;
            if(key.equals("has")){
                rel = rel + ">=" + VAR_MIN_VALUE;
            }
            ex = ex.replaceAll(key + "\\(\"("+val+")\"\\)",rel);
        }
        return ex;
    }

    public static boolean execWhereExpression(String str, Map<String,Integer> vars){
        if(StringUtil.isBlank(str)){
            return false;
        }
        Valuable value = null;
        DataType type = null;
        try{
            Expression expression = factory.getExpression(expression(str));
            setVariable(expression,vars);
            value = expression.evaluate();
            type = value.getDataType();
        }catch (Throwable t){
            throw new IllegalArgumentException(t);
        }
        if(type == null || type != DataType.BOOLEAN){
            throw new IllegalArgumentException("表达式错误");
        }
        return value.getBooleanValue();
    }

    public static long execSortExpression(String str, Map<String,Integer> vars){
        if(StringUtil.isBlank(str)){
            str = DEFAULT_SORT_EXPRESSION;
        }
        Valuable value = null;
        DataType type = null;
        try{
            Expression expression = factory.getExpression(expression(str));
            setVariable(expression,vars);
            value = expression.evaluate();
            type = value.getDataType();
        }catch (Throwable t){
            throw new IllegalArgumentException(t);
        }
        if(type == null || type != DataType.NUMBER){
            throw new IllegalArgumentException("表达式错误");
        }
        return value.getNumberValue().longValue();
    }

    public static boolean validWhereExpression(String str){
        if(StringUtil.isBlank(str)){
            return false;
        }
        Valuable value = null;
        DataType type = null;
        try{
            Expression expression = factory.getExpression(str);
            value = expression.reParseAndEvaluate();
            type = value.getDataType();
        }catch (Throwable t){
            return false;
        }
        if(type == null || type != DataType.BOOLEAN ){
            return false;
        }
        return true;
    }

    public static boolean validSortExpression(String str){
        if(StringUtil.isBlank(str)){
            str = DEFAULT_SORT_EXPRESSION;
        }
        Valuable value = null;
        DataType type = null;
        try{
            Expression expression = factory.getExpression(str);
            value = expression.reParseAndEvaluate();
            type = value.getDataType();
        }catch (Throwable t){
            return false;
        }
        if(type == null || type != DataType.NUMBER ){
            return false;
        }
        return true;
    }
}
