package com.oneyun.sapi;

/**
 * Created by liups on 2016/11/16.
 */
public class AgentSkillOperation extends AgentSkill {
    public static int OPT_SAVE = 1;
    public static int OPT_DELETE = 2;
    public static int OPT_DELETE_ALL = 0;

    public AgentSkillOperation(String name, Integer score, Boolean enabled, Integer opt) {
        super(name, score, enabled);
        this.opt = opt;
    }

    private Integer opt;

    public Integer getOpt() {
        return opt;
    }

    public void setOpt(Integer opt) {
        this.opt = opt;
    }
}
