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
    private int score;
    private boolean enabled;

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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
