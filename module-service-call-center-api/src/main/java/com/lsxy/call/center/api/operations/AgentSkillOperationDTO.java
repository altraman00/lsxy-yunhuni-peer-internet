package com.lsxy.call.center.api.operations;

import java.io.Serializable;

/**
 * Created by liups on 2016/11/16.
 */
public class AgentSkillOperationDTO implements Serializable {
    public static int OPT_SAVE = 1;
    public static int OPT_DELETE = 2;
    public static int OPT_DELETE_ALL = 0;

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
