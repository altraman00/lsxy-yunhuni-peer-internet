package com.oneyun.sapi.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangxb on 2016/9/3.
 */
public class JsonUtils {
    /**
     * 将map转换成json字符串
     * @param map
     * @return
     */
    public static String toJson(Map map){
         return JSONSerializer.toJSON(map).toString();
    }

    /**
     * 将json转换成map
     * @param result
     * @return
     */
    public static Map toMap(String result){
        Map<String, Object> mapJson = null;
        try {
            JSONObject jsonObject = JSONObject.fromObject(result);
            mapJson = JSONObject.fromObject(jsonObject);
        }catch (Exception e){
            String[] temp1 = result.replace("{","").replace("}","").split(",");
            mapJson = new HashMap<>();
            for(int i=0;i<temp1.length;i++){
                String[] temp2 = temp1[i].trim().split("=");
                mapJson.put(temp2[0].trim(),temp2[1].trim());
            }
        }
        return mapJson;
    }
    public static String objectToJson(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        // Convert object to JSON string
        String jsonStr = "";
        try {
            jsonStr =  mapper.writeValueAsString(obj);
        } catch (Exception e) {
        }
        return jsonStr;
    }
}
