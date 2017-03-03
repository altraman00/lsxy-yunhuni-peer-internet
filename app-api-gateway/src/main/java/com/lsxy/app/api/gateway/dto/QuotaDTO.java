package com.lsxy.app.api.gateway.dto;

import java.io.Serializable;

/**
 * Created by liuws on 2017/2/16.
 */
public class QuotaDTO extends CommonDTO{

    private String type;

    private Long value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

}
