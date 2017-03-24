package com.lsxy.msg.api.result;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liups on 2017/3/23.
 */
public class MsgSendMassResult implements Serializable {
    public static final int STATE_SUCCESS = 1;
    public static final int STATE_FAIL = 0;
    private String msgKey;
    private int state;
    private List<String> invalidMobiles;

    public MsgSendMassResult() {
    }

    public MsgSendMassResult(String msgKey, int state, List<String> invalidMobiles) {
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public List<String> getInvalidMobiles() {
        return invalidMobiles;
    }

    public void setInvalidMobiles(List<String> invalidMobiles) {
        this.invalidMobiles = invalidMobiles;
    }
}
