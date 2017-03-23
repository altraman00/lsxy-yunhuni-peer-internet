package com.lsxy.msg.api.result;

import java.io.Serializable;

/**
 * Created by liups on 2017/3/23.
 */
public class MsgSendOneResult implements Serializable {
    private String msgKey;
    private int state;

    public MsgSendOneResult() {
    }

    public MsgSendOneResult(String msgKey, int state) {
        this.msgKey = msgKey;
        this.state = state;
    }

    public String getMsgKey() {
        return msgKey;
    }

    public void setMsgKey(String msgKey) {
        this.msgKey = msgKey;
    }

    public int isState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
