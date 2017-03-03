package com.lsxy.app.portal.console.app.vo;

import com.lsxy.call.center.api.model.AgentSkill;
import com.lsxy.call.center.api.model.CallCenterAgent;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liups on 2016/11/18.
 */
public class AgentVO {
    private String id;
    private String name;
    private String subaccountId;
    private String certId;
    private String num;
    private String state;
    private String extension;
    private List skills;

    public AgentVO() {
    }

    public AgentVO(String id,String name, String subaccountId, String num, String state, String extension, List skills,String certId) {
        this.id = id;
        this.name = name;
        this.subaccountId = subaccountId;
        this.num = num;
        this.state = state!=null?CallCenterAgent.getChineseState(state):"未知" ;
        this.extension = extension;
        this.skills = skills;
        this.certId = certId;
    }

    public static AgentVO changeCallCenterAgentToAgentVO(CallCenterAgent agent, ApiCertificateSubAccount apiCertificateSubAccount){
        List<AgentSkill> skills = agent.getSkills();
        List<AgentSkillVO> skillVos = null;
        if(skills != null){
            skillVos = new ArrayList<>();
            for(AgentSkill skill:skills){
                AgentSkillVO skillVO = AgentSkillVO.changeAgentSkillToAgentSkillVO(skill);
                skillVos.add(skillVO);
            }
        }
        String certId = apiCertificateSubAccount!=null ? apiCertificateSubAccount.getCertId() :"";
        return new AgentVO(agent.getId(),agent.getName(),agent.getSubaccountId(),agent.getNum(),agent.getState(),agent.getExtension(),skillVos,certId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubaccountId() {
        return subaccountId;
    }

    public void setSubaccountId(String subaccountId) {
        this.subaccountId = subaccountId;
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
