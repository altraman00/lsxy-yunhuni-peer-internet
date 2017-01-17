package com.oneyun.sapi;

import java.io.Serializable;

/**
 * Created by liups on 2016/11/16.
 */
public class AgentSkill implements Serializable {
    private String name;
    private Integer score;
    private Boolean enabled;

    public AgentSkill() {
    }

    public AgentSkill(String name, Integer score, Boolean enabled) {
        this.name = name;
        this.score = score;
        this.enabled = enabled;
    }

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
