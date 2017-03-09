package com.lsxy.msg.supplier.common;

import com.lsxy.framework.core.utils.JSONUtil;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxb on 2017/1/10.
 */
public class ResultAllMass extends BaseResult {
    private int sumNum;//操作号码总数
    private int failNum;//失败次数
    private int pendingNum;//待发送数
    private List<String> pendingPhones;
    private List<String> no;
    private List<String> badPhones;
    public ResultAllMass() {
    }

    public ResultAllMass(List<ResultMass> list,List<String> uNo) {
        this.sumNum = 0;
        this.failNum = 0;
        this.pendingNum = 0;
        this.badPhones = new ArrayList<>();
        this.no = new ArrayList<>();
        if(uNo != null && uNo.size() > 0){
            this.no .addAll(uNo);
        }
        for (int i = 0; i < list.size(); i++) {
            ResultMass resultMass = list.get(i);
            this.sumNum += resultMass.getSumNum();
            this.failNum += resultMass.getFailNum();
            this.pendingNum += resultMass.getPendingNum();
            if (resultMass.getPendingPhones() != null && resultMass.getPendingPhones() .size() > 0) {//接口成功，存在部分失败。成功失败记录都存了数据库。
                this.badPhones.addAll(resultMass.getBadPhones());
            } else {
                this.no.addAll(resultMass.getBadPhones());//接口失败。全部号码失败。没存数据库。
            }
            if( !MsgConstant.SUCCESS.equals( resultMass.getResultCode() )) {
                this.resultDesc = resultMass.getResultDesc();
            }
        }

        if( this.pendingNum > 0){
            this.resultCode = MsgConstant.SUCCESS;
        }else{
            this.resultCode = "-1";
        }
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

    public List<String> getNo() {
        return no;
    }

    public void setNo(List<String> no) {
        this.no = no;
    }

    @Override
    public String toString() {
        Map map = new HashMap();
        map.put("result_code",this.resultCode);
        if(StringUtils.isNotEmpty(this.resultDesc)) {
            map.put("result_desc",this.resultDesc );
        }
        if(no != null && no.size() > 0){
            map.put("badPhones",this.no);
        }
        return JSONUtil.objectToJson(map);
    }

}
