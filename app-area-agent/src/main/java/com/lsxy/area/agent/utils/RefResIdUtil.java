package com.lsxy.area.agent.utils;

import java.util.Map;

/**
 * Created by liuws on 2016/12/27.
 */
public class RefResIdUtil {

    private static final String REF_RES_ID_FIELD = "ref_res_id";

    private static final String RES_ID_FIELD = "res_id";

    public static String get(Map<String,Object> params){
        String result = (String)params.get(REF_RES_ID_FIELD);

        if(result == null || result.length() == 0){
            result = (String)params.get(RES_ID_FIELD);
        }
        return result;
    }
}
