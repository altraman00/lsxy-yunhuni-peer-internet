package com.lsxy.app.oc.rest.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/9.
 */
public class RegMemberStatisticDTO implements Serializable {

    @JsonProperty("member_count")
    private List<Integer> memberCount;

    public List<Integer> getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(List<Integer> memberCount) {
        this.memberCount = memberCount;
    }
}
