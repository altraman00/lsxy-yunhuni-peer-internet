package com.lsxy.msg.supplier.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxb on 2017/1/10.
 */
public class ResultMass  extends BaseResult {
    protected long sumNum;//发送号码总数（不包括不能发送的号码）
    protected long failNum;//失败次数
    protected long pendingNum;//待发送数
    protected List<String> pendingPhones = new ArrayList<>();
    protected List<String> badPhones = new ArrayList<>();
    public ResultMass() {
    }

    public List<String> getBadPhones() {
        return badPhones;
    }

    public void setBadPhones(List<String> badPhones) {
        this.badPhones = badPhones;
    }

    public long getSumNum() {
        return sumNum;
    }

    public void setSumNum(long sumNum) {
        this.sumNum = sumNum;
    }

    public long getFailNum() {
        return failNum;
    }

    public void setFailNum(long failNum) {
        this.failNum = failNum;
    }

    public long getPendingNum() {
        return pendingNum;
    }

    public void setPendingNum(long pendingNum) {
        this.pendingNum = pendingNum;
    }

    public List<String> getPendingPhones() {
        return pendingPhones;
    }

    public void setPendingPhones(List<String> pendingPhones) {
        this.pendingPhones = pendingPhones;
    }
}
