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
}
