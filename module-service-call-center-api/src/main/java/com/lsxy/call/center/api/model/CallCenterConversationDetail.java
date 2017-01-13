package com.lsxy.call.center.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

/**
 * 呼叫中心交谈
 * Created by zhangxb on 2016/11/11.
 */
public class CallCenterConversationDetail{

    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private String type;

    @JsonProperty("state")
    private String state;

    @JsonProperty("channel")
    private String channelId;

    @JsonProperty("queue_id")
    private String queueId;

    @JsonProperty("begin_time")
    private Date startTime;//发起时间

    @JsonProperty("end_time")
    private Date endTime;//结束时间

    @JsonProperty("end_reason")
    private String endReason;

    @JsonProperty("members")
    private List<MemberDetail> members;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getEndReason() {
        return endReason;
    }

    public void setEndReason(String endReason) {
        this.endReason = endReason;
    }

    public List<MemberDetail> getMembers() {
        return members;
    }

    public void setMembers(List<MemberDetail> members) {
        this.members = members;
    }

    public static class MemberDetail{

        @JsonProperty("name")
        private String agentName;

        @JsonProperty("extension_id")
        private String extensionId;

        @JsonProperty("call_id")
        private String callId;

        @JsonProperty("begin_time")
        private Date startTime;//发起时间

        @JsonProperty("end_time")
        private Date endTime;//结束时间

        @JsonProperty("mode")
        private Integer mode;//说听模式

        public String getAgentName() {
            return agentName;
        }

        public void setAgentName(String agentName) {
            this.agentName = agentName;
        }

        public String getExtensionId() {
            return extensionId;
        }

        public void setExtensionId(String extensionId) {
            this.extensionId = extensionId;
        }

        public String getCallId() {
            return callId;
        }

        public void setCallId(String callId) {
            this.callId = callId;
        }

        public Date getStartTime() {
            return startTime;
        }

        public void setStartTime(Date startTime) {
            this.startTime = startTime;
        }

        public Date getEndTime() {
            return endTime;
        }

        public void setEndTime(Date endTime) {
            this.endTime = endTime;
        }

        public Integer getMode() {
            return mode;
        }

        public void setMode(Integer mode) {
            this.mode = mode;
        }
    }
}
