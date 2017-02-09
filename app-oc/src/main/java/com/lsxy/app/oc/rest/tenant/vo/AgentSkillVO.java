package com.lsxy.app.oc.rest.tenant.vo;

import com.lsxy.call.center.api.model.AgentSkill;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by liups on 2016/11/18.
 */
public class AgentSkillVO {
    @ApiModelProperty(name="name",value = "技能名称")
    private String name;
    @ApiModelProperty(name="score",value = "技能分数")
    private Integer score;
    @ApiModelProperty(name="enabled",value = "是否可用")
    private Boolean enabled;

    public AgentSkillVO() {
    }

    public AgentSkillVO(String name, Integer score, Boolean enabled) {
        this.name = name;
        this.score = score;
        this.enabled = enabled;
    }

    public static AgentSkillVO changeAgentSkillToAgentSkillVO(AgentSkill skill){
        return new AgentSkillVO(skill.getName(),skill.getScore(),skill.getEnabled());
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
