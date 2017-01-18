package com.lsxy.app.api.gateway.dto.callcenter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsxy.app.api.gateway.dto.CommonDTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Administrator on 2017/1/16.
 */
public class RejectTaskInputDTO extends CommonDTO {

    @NotNull
    @Size(min = 32,max = 32)
    @JsonProperty("queue_id")
    private String queueId;

    @Size(max = 128)
    @JsonProperty("data")
    private String data;

    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
