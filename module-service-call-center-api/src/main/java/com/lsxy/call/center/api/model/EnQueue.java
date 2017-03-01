package com.lsxy.call.center.api.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

/**
 * Created by liuws on 2016/10/29.
 */
@XStreamAlias("enqueue")
public class EnQueue implements Serializable{

    public static final String CHOICE_LRU = "lru";

    public static final String CHOICE_RANDOM = "random";

    @XStreamAsAttribute
    private Integer conversation_level;

    @XStreamAsAttribute
    private Integer conversation_timeout;

    @XStreamAsAttribute
    private String choice;

    @XStreamAsAttribute
    private String reserve_state;

    @XStreamAsAttribute
    private boolean fail_overflow;

    @XStreamAsAttribute
    private String wait_voice;

    @XStreamAsAttribute
    private Integer ring_mode;

    private Integer voice_mode;

    @XStreamAsAttribute
    private String ring_voice;

    @XStreamAsAttribute
    private String hold_voice;

    @XStreamAsAttribute
    private boolean play_num;

    @XStreamAsAttribute
    private String pre_num_voice;

    @XStreamAsAttribute
    private String post_num_voice;

    @XStreamAsAttribute
    private String user_data;

    private Route route;

    public EnQueue(){
        
    }

    public Integer getConversation_level() {
        return conversation_level;
    }

    public void setConversation_level(Integer conversation_level) {
        this.conversation_level = conversation_level;
    }

    public Integer getConversation_timeout() {
        return conversation_timeout;
    }

    public void setConversation_timeout(Integer conversation_timeout) {
        this.conversation_timeout = conversation_timeout;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public String getReserve_state() {
        return reserve_state;
    }

    public void setReserve_state(String reserve_state) {
        this.reserve_state = reserve_state;
    }

    public boolean isFail_overflow() {
        return fail_overflow;
    }

    public void setFail_overflow(boolean fail_overflow) {
        this.fail_overflow = fail_overflow;
    }

    public String getWait_voice() {
        return wait_voice;
    }

    public void setWait_voice(String wait_voice) {
        this.wait_voice = wait_voice;
    }

    public Integer getRing_mode() {
        return ring_mode;
    }

    public void setRing_mode(Integer ring_mode) {
        this.ring_mode = ring_mode;
    }

    public Integer getVoice_mode() {
        return voice_mode;
    }

    public void setVoice_mode(Integer voice_mode) {
        this.voice_mode = voice_mode;
    }

    public String getRing_voice() {
        return ring_voice;
    }

    public void setRing_voice(String ring_voice) {
        this.ring_voice = ring_voice;
    }

    public String getHold_voice() {
        return hold_voice;
    }

    public void setHold_voice(String hold_voice) {
        this.hold_voice = hold_voice;
    }

    public boolean isPlay_num() {
        return play_num;
    }

    public void setPlay_num(boolean play_num) {
        this.play_num = play_num;
    }

    public String getPre_num_voice() {
        return pre_num_voice;
    }

    public void setPre_num_voice(String pre_num_voice) {
        this.pre_num_voice = pre_num_voice;
    }

    public String getPost_num_voice() {
        return post_num_voice;
    }

    public void setPost_num_voice(String post_num_voice) {
        this.post_num_voice = post_num_voice;
    }

    public String getUser_data() {
        return user_data;
    }

    public void setUser_data(String user_data) {
        this.user_data = user_data;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public class Route  implements Serializable{
        private Condition condition;

        private Agent agent;

        public Route(){

        }
        public Condition getCondition() {
            return condition;
        }

        public void setCondition(Condition condition) {
            this.condition = condition;
        }

        public Agent getAgent() {
            return agent;
        }

        public void setAgent(Agent agent) {
            this.agent = agent;
        }
    }
    public class Condition  implements Serializable{
        @XStreamAsAttribute
        private String id;

        public Condition(){

        }
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public class Agent  implements Serializable{

        @XStreamAsAttribute
        private String name;

        @XStreamAsAttribute
        private Integer priority;

        @XStreamAsAttribute
        private Integer queue_timeout;

        @XStreamAsAttribute
        private Integer fetch_timeout;

        public Agent(){

        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getPriority() {
            return priority;
        }

        public void setPriority(Integer priority) {
            this.priority = priority;
        }

        public Integer getQueue_timeout() {
            return queue_timeout;
        }

        public void setQueue_timeout(Integer queue_timeout) {
            this.queue_timeout = queue_timeout;
        }

        public Integer getFetch_timeout() {
            return fetch_timeout;
        }

        public void setFetch_timeout(Integer fetch_timeout) {
            this.fetch_timeout = fetch_timeout;
        }
    }
}


