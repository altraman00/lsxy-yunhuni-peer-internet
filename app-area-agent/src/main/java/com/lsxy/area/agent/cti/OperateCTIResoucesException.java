package com.lsxy.area.agent.cti;

import java.util.Map;

/**
 * Created by tandy on 16/12/22.
 */
public class OperateCTIResoucesException extends Exception {
    public OperateCTIResoucesException(String resId, String method, Map<String, Object> params, CTINode ctiNode) {
        super("创建CTI资源异常："+resId+"-"+method+"("+params+")==>"+ctiNode);
    }
}
