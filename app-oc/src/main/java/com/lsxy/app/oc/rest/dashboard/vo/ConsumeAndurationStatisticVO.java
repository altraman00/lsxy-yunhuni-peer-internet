package com.lsxy.app.oc.rest.dashboard.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liuws on 2016/8/9.
 */
public class ConsumeAndurationStatisticVO implements Serializable {

    private List<Double> cost;

    private List<Long> session;

    public List<Double> getCost() {
        return cost;
    }

    public void setCost(List<Double> cost) {
        this.cost = cost;
    }

    public List<Long> getSession() {
        return session;
    }

    public void setSession(List<Long> session) {
        this.session = session;
    }
}
