package com.lsxy.call.center.api.operations;

/**
 * Created by liups on 2016/11/16.
 */
public class AgentSkillOperation {
    private Integer opt;
    private String name;
    private Integer score;
    private Boolean enabled;

    public Integer getOpt() {
        return opt;
    }

    public void setOpt(Integer opt) {
        this.opt = opt;
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
