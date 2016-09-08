package com.lsxy.app.api.gateway.dto;

import com.lsxy.framework.core.utils.JSONUtil2;

import java.io.Serializable;

/**
 * Created by liuws on 2016/9/1.
 */
public class CommonDTO implements Serializable{
    @Override
    public String toString(){
        return JSONUtil2.objectToJson(this);
    }
}
