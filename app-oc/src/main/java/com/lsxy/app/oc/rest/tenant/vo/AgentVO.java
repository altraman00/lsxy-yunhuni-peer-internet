package com.lsxy.app.oc.rest.tenant.vo;

import com.lsxy.call.center.api.model.AgentSkill;
import com.lsxy.call.center.api.model.CallCenterAgent;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liups on 2016/11/18.
 */
public class AgentVO {
    @ApiModelProperty(name="name",value = "座席名称（ID）")
    private String name;
    @ApiModelProperty(name="channel",value = "通道ID")
    private String channel;
    @ApiModelProperty(name="num",value = "座席工号")
    private String num;
    @ApiModelProperty(name="state",value = "座席状态")
    private String state;
    @ApiModelProperty(name="extension",value = "绑定分机Id")
    private String extension;
    @ApiModelProperty(name="skills",value = "技能组")
    private List skills;

    public AgentVO() {
    }

    public AgentVO(String name, String channel, String num, String state, String extension, List skills) {
        this.name = name;
        this.channel = channel;
        this.num = num;
        this.state = state;
        this.extension = extension;
        this.skills = skills;
    }

    public static AgentVO changeCallCenterAgentToAgentVO(CallCenterAgent agent){
        List<AgentSkill> skills = agent.getSkills();
        List<AgentSkillVO> skillVos = null;
        if(skills != null){
            skillVos = new ArrayList<>();
            for(AgentSkill skill:skills){
                AgentSkillVO skillVO = AgentSkillVO.changeAgentSkillToAgentSkillVO(skill);
                skillVos.add(skillVO);
            }
        }
        return new AgentVO(agent.getName(),agent.getChannel(),agent.getNum(),agent.getState(),agent.getExtension(),skillVos);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public List getSkills() {
        return skills;
    }

    public void setSkills(List skills) {
        this.skills = skills;
    }
}
