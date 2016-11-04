package com.lsxy.call.center.api.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
/**
 * Created by liuws on 2016/10/29.
 */
@XStreamAlias("enqueue")
public class EnQueue {
    @XStreamAsAttribute
    private String waitPlayFile;

    @XStreamAsAttribute
    private Boolean playAgentNum;

    @XStreamAsAttribute
    private String preAgentNumPlayFile;

    @XStreamAsAttribute
    private String postAgentNumPlayFile;

    @XStreamAsAttribute
    private String holdPlayFile;

    @XStreamAsAttribute
    private String conversationTimeout;

    @XStreamAsAttribute
    private String data;

    private Filter filter;

    public String getWaitPlayFile() {
        return waitPlayFile;
    }

    public void setWaitPlayFile(String waitPlayFile) {
        this.waitPlayFile = waitPlayFile;
    }

    public Boolean getPlayAgentNum() {
        return playAgentNum;
    }

    public void setPlayAgentNum(Boolean playAgentNum) {
        this.playAgentNum = playAgentNum;
    }

    public String getPreAgentNumPlayFile() {
        return preAgentNumPlayFile;
    }

    public void setPreAgentNumPlayFile(String preAgentNumPlayFile) {
        this.preAgentNumPlayFile = preAgentNumPlayFile;
    }

    public String getPostAgentNumPlayFile() {
        return postAgentNumPlayFile;
    }

    public void setPostAgentNumPlayFile(String postAgentNumPlayFile) {
        this.postAgentNumPlayFile = postAgentNumPlayFile;
    }

    public String getHoldPlayFile() {
        return holdPlayFile;
    }

    public void setHoldPlayFile(String holdPlayFile) {
        this.holdPlayFile = holdPlayFile;
    }

    public String getConversationTimeout() {
        return conversationTimeout;
    }

    public void setConversationTimeout(String conversationTimeout) {
        this.conversationTimeout = conversationTimeout;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public class Filter{
        @XStreamAsAttribute
        private String data;

        private Condition condition;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public Condition getCondition() {
            return condition;
        }

        public void setCondition(Condition condition) {
            this.condition = condition;
        }
    }
    public class Condition{
        @XStreamAsAttribute
        private Integer timeout;

        @XStreamAsAttribute
        private Integer priority;

        @XStreamAsAttribute
        private String data;

        private String where;

        private String sort;

        public Integer getTimeout() {
            return timeout;
        }

        public void setTimeout(Integer timeout) {
            this.timeout = timeout;
        }

        public Integer getPriority() {
            return priority;
        }

        public void setPriority(Integer priority) {
            this.priority = priority;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getWhere() {
            return where;
        }

        public void setWhere(String where) {
            this.where = where;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }
    }

}


