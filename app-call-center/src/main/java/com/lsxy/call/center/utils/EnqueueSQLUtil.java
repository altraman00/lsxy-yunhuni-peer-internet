package com.lsxy.call.center.utils;

import com.lsxy.call.center.expression.Expression;
import com.lsxy.call.center.expression.ExpressionFactory;
import com.lsxy.framework.core.utils.StringUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liuws on 2016/11/1.
 */
public final class EnqueueSQLUtil {

    private static final ExpressionFactory factory = ExpressionFactory.getInstance();

    private static final String SQL_TEMPLATE="select a.id,a.type,a.user,a.telenum,a.agent:SHOWCOLUMS from tb_bi_app_extension a" +
            " left join tb_bi_call_center_agent b on a.agent=b.id"+
            " left join (select agent :COLUMS" +
            " from tb_bi_call_center_agent_skill where tenant_id=\":TENANTID\" and app_id=\":APPID\" and active=1 and deleted=0 group by agent :HAVING) c on a.agent = c.agent" +
            " where a.tenant_id=\":TENANTID\" and a.app_id=\":APPID\" and a.enabled = 1 and a.deleted = 0 and a.last_register_status= 200 and a.agent <> ''" +
            " and b.tenant_id=\":TENANTID\" and b.app_id=\":APPID\" and b.deleted = 0 and b.state = 'idle'" +
            " and DATE_ADD(a.last_register_time,INTERVAL a.register_expires second)>=now()" +
            ":WHERE"+
            " group by agent" +
            ":SORT";
    private static final String skill_regex="(has|get)\\(\"(.+?)\"\\)";
    private static final String id_regex = "id\\s*?==\\s*?\"(\\S+?)\"";
    private static final Pattern p  =Pattern.compile(skill_regex);

    private EnqueueSQLUtil(){}
    
    private static void putSkills(Set<String> skills,String expression){
        Matcher m =  p.matcher(expression);
        while(m.find()){
            String s =  m.group(2);
            skills.add(s);
        }
    }

    public static String genSQL(String tenantId,String appId,String whereExpression,String sortExpression){
        checkExpression(whereExpression);
        checkExpression(sortExpression);
        Set<String> skills = new HashSet<>();
        String showColums = "";
        String having = "";
        String where = "";
        String sort = "";
        String colums = "";
        if(StringUtil.isNotBlank(whereExpression)){
            where = whereExpression.replaceAll(skill_regex,"c.`$2`").replaceAll(id_regex,"c.agent = \"$1\"");
            if(StringUtil.isBlank(where)){
                where = "";
            }else{
                having = "having "+where.replaceAll("c.`","`");
                where = " and ("+where+")";
            }
            putSkills(skills,whereExpression);
        }

        if(StringUtil.isNotBlank(sortExpression)){
            sort = sortExpression.replaceAll(skill_regex,"c.`$2`").replaceAll(id_regex,"c.agent = \"$1\"");
            if(StringUtil.isBlank(sort)){
                sort = "";
            }else{
                sort = " ORDER BY ("+sort+") desc";
            }
            putSkills(skills,sortExpression);
        }
        if(skills.size()>0){
            String colum = ",sum(case when name=\"%s\" then level end) as \"%s\"";
            for (String skill : skills){
                colums+=String.format(colum,skill,skill);
                showColums += ",c.`"+skill+"`";
            }
        }
        if(StringUtil.isBlank(sort) && skills.size()>0){
            sort = "0";
            for (String skill : skills){
                sort += " + `"+skill+"`";
            }
            sort = " ORDER BY ("+sort+") desc";
        }
        String sql = SQL_TEMPLATE
                .replaceAll(":TENANTID",tenantId)
                .replaceAll(":APPID",appId)
                .replaceAll(":SHOWCOLUMS",showColums)
                .replaceAll(":COLUMS",colums)
                .replaceAll(":HAVING",having)
                .replaceAll(":WHERE",where)
                .replaceAll(":SORT",sort)
                .replaceAll("==","=")
                .replaceAll("&&","and")
                .replaceAll("\\|\\|","or");
        return sql;
    }

    private static void checkExpression(String str){
        if(StringUtil.isBlank(str)){
            return;
        }
        Expression expression = factory.getExpression(str+";");
        expression.initVariable("id","1");
        try{
            expression.reParseAndEvaluate();
        }catch (Throwable t){
            throw new IllegalArgumentException(t);
        }
    }

    /*public static void main(String[] args) {
        //String a = "has(\\\"haha1\\\") || get(\\\"haha2\\\") > 60 && id==\"1\"";
        //System.out.println(a.replaceAll("id\\s*?==\\s*?\"(\\S+?)\"","c.agent = \"$1\""));
       System.out.println(genSQL("40288ac9575612a30157561c7ff50004","40288ac957e1812e0157e18a994e0000","(get(\"haha0\") + get(\"haha1\") * 0.6)/2 > 60",""));
    }*/
}
