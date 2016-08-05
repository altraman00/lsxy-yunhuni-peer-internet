package com.lsxy.utils;

import com.lsxy.framework.core.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangxb on 2016/8/4.
 */
public class StatisticsUtils {
    /**
     * 将select数组转换成需要的条件（统计专用）
     * @param select
     * @return
     */
    public static Map<String, String> getSqlRequirements(String[] select) {
        Map<String,String> map = new HashMap();
        String selects = "";
        String groupbys = "";
        String wheres = "";
        for(int i=0;i<select.length;i++){
            if(i==select.length-1){
                groupbys += select[i] ;
            }else {
                groupbys += select[i] + " , ";
            }
            selects += select[i] + " , ";
            wheres += select[i]+"=a."+select[i] +" and ";
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
    public static String getSqlIsNull(String tenantId,String appId,String type){
        Map<String, String> map = new HashMap<String,String>();
        map.put("tenantId",tenantId);
        map.put("appId",appId);
        map.put("type",type);
        return getSqlIsNull(map);
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
}
