package com.lsxy.area.server.util.ivr.act.domain;

/**
 * Created by liuws on 2016/9/2.
 */
public class Attribute {
    private String key;
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key.toLowerCase();
    }
}