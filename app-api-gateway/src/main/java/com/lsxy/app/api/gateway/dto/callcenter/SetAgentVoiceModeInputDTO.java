package com.lsxy.app.api.gateway.dto.callcenter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsxy.app.api.gateway.dto.CommonDTO;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by liuws on 2017/1/10.
 */
public class SetAgentVoiceModeInputDTO extends CommonDTO {

    @NotNull
    @JsonProperty("conversation_id")
    private String conversationId;

    @Min(1)
    @Max(4)
    @JsonProperty(value = "mode")
    private Integer mode;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }
}
