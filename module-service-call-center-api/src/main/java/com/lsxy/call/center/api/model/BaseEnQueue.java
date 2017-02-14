package com.lsxy.call.center.api.model;

import java.io.Serializable;

/**
 * Created by liuws on 2016/11/23.
 */
public class BaseEnQueue implements Serializable{

    private String channel;

    private Integer conversation_level;

    private Integer conversation_timeout;

    private String choice;

    private String reserve_state;

    private boolean fail_overflow;

    private String wait_voice;

    private Integer ring_mode;

    private Integer voice_mode;

    private String ring_voice;

    private String hold_voice;

    private boolean play_num;

    private String pre_num_voice;

    private String post_num_voice;

    private String data;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
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

    public String getRing_voice() {
        return ring_voice;
    }

    public Integer getVoice_mode() {
        return voice_mode;
    }

    public void setVoice_mode(Integer voice_mode) {
        this.voice_mode = voice_mode;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
