package com.lsxy.utils;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tandy on 2016/7/19.
 * 案例，不适用
 */
@Deprecated
public class MyInterceptorClassName extends EmptyInterceptor {
    public static final Logger logger = LoggerFactory.getLogger(MyInterceptorClassName.class);
    @Override
    public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (logger.isDebugEnabled()){
                logger.debug("MyInterceptorClassName>>>>>>>onLoad");
         }
        return super.onLoad(entity, id, state, propertyNames, types);
    }

    @Override
    public String onPrepareStatement(String sql) {
        sql = replaceDateHolder(sql);
        return sql;
    }

    /**
     * 替换日期占位通配符
     * @param sql
     * @return
     */
    private static String replaceDateHolder(String sql) {
        String pattern = "\\{([y|M|d]*)\\}";

        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(sql);
        while(m.find()){
            for (int i=0;i<m.groupCount();i++){
                System.out.println(m.group(i));
            }
//            String dateFrm = m.group(1);
//            if(StringUtil.isNotEmpty(dateFrm)){
//                String sDt = DateUtils.formatDate(new Date(),dateFrm);
//                sql = m.replaceAll(sDt);
//            }
        }
        return sql;
    }

    public static void main(String[] args) {
//        String tableName = "tb_base_account_{abcyyyymmdd}";
//
//        String pattern = "\\{(.*)\\}";
//
//        // 创建 Pattern 对象
//        Pattern r = Pattern.compile(pattern);
//        // 现在创建 matcher 对象
//        Matcher m = r.matcher(tableName);
//        if(m.find()){
//            System.out.println(m.group(1));
//        }
//
//        System.out.println(tableName.replaceAll("\\{.*\\}",""));
//        System.out.println(DateUtils.formatDate(new Date(),"yyyyMMdd"));

        String sql = "from tb_table01_{yyyyMMdd} A,tb_table02_{yyyyMMdd} B";
//        System.out.println(sql.replaceAll("\\{([y|M|d]*)\\}","xxxx"));
        System.out.println(replaceDateHolder(sql));
    }
}
