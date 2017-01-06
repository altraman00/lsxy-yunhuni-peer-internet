package com.lsxy.app.oc.rest.dashboard.vo;

import java.io.Serializable;

/**
 * Created by liuws on 2016/8/9.
 */
public class ApplicationIndicantVO implements Serializable {

    private Long total;

    private Long online;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getOnline() {
        return online;
    }

    public void setOnline(Long online) {
        this.online = online;
    }
}
