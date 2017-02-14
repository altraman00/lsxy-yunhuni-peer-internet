package com.lsxy.app.api.gateway.dto.callcenter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsxy.app.api.gateway.dto.CommonDTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by liuws on 2017/1/16.
 */
public class AgentCallOutInputDTO extends CommonDTO {

    @NotNull
    @JsonProperty("to")
    private String to;

    @JsonProperty("from")
    private String from;

    @JsonProperty("max_dial_seconds")
    private Integer maxDialSeconds;

    @JsonProperty("max_answer_seconds")
    private Integer maxAnswerSeconds;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Integer getMaxDialSeconds() {
        return maxDialSeconds;
    }

    public void setMaxDialSeconds(Integer maxDialSeconds) {
        this.maxDialSeconds = maxDialSeconds;
    }

    public Integer getMaxAnswerSeconds() {
        return maxAnswerSeconds;
    }

    public void setMaxAnswerSeconds(Integer maxAnswerSeconds) {
        this.maxAnswerSeconds = maxAnswerSeconds;
    }
}
