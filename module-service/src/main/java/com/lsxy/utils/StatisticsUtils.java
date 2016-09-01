package com.lsxy.utils;

import com.lsxy.framework.core.utils.StringUtil;

import java.util.*;

/**
 * Created by zhangxb on 2016/8/4.
 */
public class StatisticsUtils {
    /**
     * 将select数组转换成需要的条件（统计专用）
     * @param select
     * @return
     */
    public static Map<String, String> getSqlRequirements(String[] ...select) {
        Map<String,String> map = new HashMap();
        String selects = "";
        String groupbys = "";
        String wheres = "";
        for(int i=0;i<select[0].length;i++){
            if(i==select[0].length-1){
                groupbys += select[0][i] ;
            }else {
                groupbys += select[0][i] + " , ";
            }
            selects += select[0][i] + " , ";
            wheres += select[0][i]+"=a."+select[0][i] +" and ";
        }
        List isList = Arrays.asList(select[0]);
        for(int j=0;j<select[1].length;j++){
            if(!isList.contains(select[1][j])){
                wheres += select[1][j]+" is null  and ";
            }
        }
        if(StringUtil.isNotEmpty(groupbys)){
            groupbys = " group by "+groupbys;
        }
        map.put("selects",selects);
        map.put("groupbys",groupbys);
        map.put("wheres",wheres);
        return map;
    }
    /**
     * 将租户和应用和类型对为空和非为空时进行处理成sql
     * @param tenantId 租户id
     * @param appId 应用id
     * @param type 类型
     * @return
     */
    public static String getSqlIsNull2(String tenantId,String appId,String type){
        Map<String, String> map = new HashMap<String,String>();
        map.put("tenant_id",tenantId);
        map.put("app_id",appId);
        map.put("type",type);
        String sql = "";
        for(Map.Entry<String, String> entry:map.entrySet()){
            String name = entry.getKey();
            String value = entry.getValue();
            if(StringUtil.isEmpty(value)){
                sql += " "+name+" is null and ";
            }else{
                sql += " "+name+"='"+value + "' and ";
            }
        }
        return sql;
    }
    /**
     * 将租户和应用和类型对为空和非为空时进行处理成sql
     * @param tenantId 租户id
     * @param appId 应用id
     * @param type 类型
     * @return
     */
    public static String getSqlIsNull(String tenantId,String appId,String type){
        Map<String, String> map = new HashMap<String,String>();
        map.put("tenantId",tenantId);
        map.put("appId",appId);
        map.put("type",type);
        return getSqlIsNull(map);
    }
    /**
     * 将租户和应用和类型对为空和非为空时进行处理成sql
     * @param tenantId 租户id
     * @param appId 应用id
     * @param type 类型
     * @return
     */
    public static String getSqlIsNotNull(Object tenantId,Object appId,Object type){
        Map<String, Object> map = new HashMap<String,Object>();
        map.put("tenantId",tenantId);
        map.put("appId",appId);
        map.put("type",type);
        String sql = "";
        for(Map.Entry<String, Object> entry:map.entrySet()){
            String name = entry.getKey();
            Object value = entry.getValue();
            if(value == null || value instanceof String ){
                if(StringUtil.isEmpty((String)value)){
                    sql += "obj."+name+" is null and ";
                }else{
                    sql += "obj."+name+"='"+value + "' and ";
                }
            }else if( value instanceof Boolean){
                if((Boolean)value){
                    sql += "obj."+name+" is null and ";
                }else{
                    sql += "obj."+name+" is not null and ";
                }
            }
        }
        return sql;
    }

    /**
     * 将Map中的参数为空和非为空时进行处理成sql
     * @param map
     * @return
     */
    public static String getSqlIsNull(Map<String, String> map){
        String sql = "";
        for(Map.Entry<String, String> entry:map.entrySet()){
            String name = entry.getKey();
            String value = entry.getValue();
            if(StringUtil.isEmpty(value)){
                sql += "obj."+name+" is null and ";
            }else{
                sql += "obj."+name+"='"+value + "' and ";
            }
        }
        return sql;
    }

    /**
     * 根据groups生成全部不重复的组合
     * @param groups
     * @return
     */
    public static List<String[]> getGroupBys(String[] groups){
        //TODO 待实现
        List<String[]> list = new ArrayList();
        list.add(new String[]{});
        list.add(new String[]{"tenant_id"});
        list.add(new String[]{"app_id"});
        list.add(new String[]{"type"});
        list.add(new String[]{"tenant_id","app_id"});
        list.add(new String[]{"tenant_id","type"});
        list.add(new String[]{"app_id","type"});
        list.add(new String[]{"tenant_id","app_id","type"});
        return list;
    }
}
