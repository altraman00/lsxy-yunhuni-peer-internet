package com.lsxy.app.api.gateway.rest.callcenter.vo;

import com.lsxy.call.center.api.operations.AgentSkillOperationDTO;

import java.util.List;

/**
 * Created by liups on 2016/12/6.
 */
public class AgentSkillOptsVO {
    private List<AgentSkillOperationDTO> opts;

    public List<AgentSkillOperationDTO> getOpts() {
        return opts;
    }

    public void setOpts(List<AgentSkillOperationDTO> opts) {
        this.opts = opts;
    }
}
