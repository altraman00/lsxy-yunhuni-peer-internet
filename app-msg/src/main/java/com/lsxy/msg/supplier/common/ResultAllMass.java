package com.lsxy.msg.supplier.common;

import com.lsxy.msg.api.model.MsgConstant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxb on 2017/1/10.
 */
public class ResultAllMass extends BaseResult {
    private long sumNum; //发送总数
    private long pendingNum;//待发送数
    private long failNum;//失败次数
    private long invalidNum; //无效数
    private List<String> pendingPhones;
    private List<String> invalidPhones;
    public ResultAllMass() {
    }

    public ResultAllMass(List<ResultMass> list,List<String> invalidPhones) {
        this.sumNum = 0;
        this.failNum = 0;
        this.pendingNum = 0;
        if(invalidPhones != null){
            this.invalidPhones = invalidPhones;
        }else{
            this.invalidPhones = new ArrayList<>();
        }
        if(this.pendingPhones == null){
            this.pendingPhones = new ArrayList<>();
        }
        for (int i = 0; i < list.size(); i++) {
            ResultMass resultMass = list.get(i);
            // 一开始的所有号码为所有等待的号码
            this.sumNum +=  resultMass.getPendingNum();
            this.pendingNum += resultMass.getPendingNum();
            if(resultMass.getBadPhones() != null){
                this.invalidPhones.addAll(resultMass.getBadPhones());//上游不通过的号码也算无效号码，不存数据库
            }
            if(resultMass.getPendingPhones() != null){
                this.pendingPhones.addAll(resultMass.getPendingPhones());
            }

            if( !MsgConstant.SUCCESS.equals( resultMass.getResultCode() )) {
                this.resultDesc = resultMass.getResultDesc();
            }
        }

        invalidNum = invalidPhones.size();

        if( this.pendingNum > 0){
            this.resultCode = MsgConstant.SUCCESS;
        }else{
            this.resultCode = MsgConstant.FAIL;
        }
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

    public void setPendingNum(int pendingNum) {
        this.pendingNum = pendingNum;
    }

    public List<String> getPendingPhones() {
        return pendingPhones;
    }

    public void setPendingPhones(List<String> pendingPhones) {
        this.pendingPhones = pendingPhones;
    }

    public List<String> getInvalidPhones() {
        return invalidPhones;
    }

    public void setInvalidPhones(List<String> invalidPhones) {
        this.invalidPhones = invalidPhones;
    }

    public long getInvalidNum() {
        return invalidNum;
    }

    public void setInvalidNum(long invalidNum) {
        this.invalidNum = invalidNum;
    }
}
