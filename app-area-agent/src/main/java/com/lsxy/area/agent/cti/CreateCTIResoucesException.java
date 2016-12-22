package com.lsxy.area.agent.cti;

import java.util.Map;

/**
 * Created by tandy on 16/12/22.
 */
public class CreateCTIResoucesException extends Exception {
    public CreateCTIResoucesException(String name, Map<String, Object> params, CTINode ctiNode) {
        super("创建CTI资源异常："+name+"("+params+")==>"+ctiNode);
    }
}
