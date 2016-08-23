package com.lsxy.app.oc.rest.tenant.vo;

import java.io.Serializable;

/**
 * Created by liuws on 2016/8/12.
 */
public class AuthInfoVO implements Serializable{

    private String status;
    private String type;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
