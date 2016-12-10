package com.lsxy.app.api.gateway.rest.callcenter.vo;

/**
 * Created by liups on 2016/11/18.
 */
public class AgentSkillVO {
    private String name;
    private Integer score;
    private Boolean enabled;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
