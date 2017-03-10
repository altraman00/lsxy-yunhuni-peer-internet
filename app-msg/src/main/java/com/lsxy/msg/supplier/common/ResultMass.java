package com.lsxy.msg.supplier.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxb on 2017/1/10.
 */
public class ResultMass  extends BaseResult {
    protected int sumNum;//操作号码总数
    protected int failNum;//失败次数
    protected int pendingNum;//待发送数
    protected List<String> pendingPhones = new ArrayList<>();
    protected List<String> badPhones = new ArrayList<>();
    public ResultMass() {
    }

    public ResultMass(ResultCode result) {
        super(result);
    }

    public List<String> getBadPhones() {
        return badPhones;
    }

    public void setBadPhones(List<String> badPhones) {
        this.badPhones = badPhones;
    }

    public int getSumNum() {
        return sumNum;
    }

    public void setSumNum(int sumNum) {
        this.sumNum = sumNum;
    }

    public int getFailNum() {
        return failNum;
    }

    public void setFailNum(int failNum) {
        this.failNum = failNum;
    }

    public int getPendingNum() {
        return pendingNum;
    }

    public void setPendingNum(int pendingNum) {
        this.pendingNum = pendingNum;
    }

    public List<String> getPendingPhones() {
        return pendingPhones;
    }

    public void setPendingPhones(List<String> pendingPhones) {
        this.pendingPhones = pendingPhones;
    }
}
