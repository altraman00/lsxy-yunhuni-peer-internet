package com.lsxy.app.oc.rest.tenant.vo;

import com.lsxy.call.center.api.model.AgentSkill;
import com.lsxy.call.center.api.model.CallCenterAgent;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liups on 2016/11/18.
 */
@ApiModel
public class AgentVO {
    @ApiModelProperty(name = "id",value = "坐席ID")
    private String id;
    @ApiModelProperty(name="name",value = "座席名称（ID）")
    private String name;
    @ApiModelProperty(name="num",value = "座席工号")
    private String num;
    @ApiModelProperty(name="state",value = "座席状态")
    private String state;
    @ApiModelProperty(name="extension",value = "绑定分机Id")
    private String extension;
    @ApiModelProperty(name="skills",value = "技能组")
    private List skills;
    @ApiModelProperty(name = "subaccountId",value = "关联子账号")
    private String subaccountId;
    @ApiModelProperty(name = "certId",value = "关联子账号-鉴权账户")
    private String certId;
    public AgentVO() {
    }

    public AgentVO(String name, String subaccountId, String num, String state, String extension, List skills,String certId) {
        this.name = name;
        this.subaccountId = subaccountId;
        this.num = num;
        this.state = state!=null?CallCenterAgent.getChineseState(state):"" ;
        this.extension = extension;
        this.skills = skills;
    }

    public static AgentVO changeCallCenterAgentToAgentVO(CallCenterAgent agent,ApiCertificateSubAccount apiCertificateSubAccount){
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
        return new AgentVO(agent.getName(),agent.getSubaccountId(),agent.getNum(),agent.getState(),agent.getExtension(),skillVos,certId);
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
}
