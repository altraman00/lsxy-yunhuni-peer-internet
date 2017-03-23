package com.lsxy.msg.api.result;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liups on 2017/3/23.
 */
public class MsgSendMassResult implements Serializable {
    private String msgKey;
    private boolean state;
    private List<String> invalidMobiles;

    public MsgSendMassResult() {
    }

    public MsgSendMassResult(String msgKey, boolean state, List<String> invalidMobiles) {
        this.msgKey = msgKey;
        this.state = state;
        this.invalidMobiles = invalidMobiles;
    }

    public String getMsgKey() {
        return msgKey;
    }

    public void setMsgKey(String msgKey) {
        this.msgKey = msgKey;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public List<String> getInvalidMobiles() {
        return invalidMobiles;
    }

    public void setInvalidMobiles(List<String> invalidMobiles) {
        this.invalidMobiles = invalidMobiles;
    }
}
