package com.lsxy.msg.supplier.common;

import com.lsxy.framework.core.utils.JSONUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxb on 2017/1/4.
 */
public class PaoPaoYuMassNofity {
    public static final String sueccess = "0";
    //0-正在发送 2-发送成功7-待发送
    public static final int state_sueccess = 2;
    private String resultCode;
    private String resultDesc;
    private Task task;
    private JSONObject taskJson;

    public PaoPaoYuMassNofity(String result) {
        JSONObject resp = JSONObject.fromObject(result);
        this.resultCode = resp.getString("result_code");
        if( sueccess .equals( this.resultCode )){
            this.taskJson = resp.getJSONObject("task");
        }else{
            try{
                this.resultDesc = resp.getString("result_desc");
            }catch (Exception e){}
        }
    }

    public class Task{
        private String groupId;
        private String taskId;
        private int state;
        private int sendSuccNum;
        private int sendFailNum;
        private int pendingNum;
        private List<String> failPhoneList;

        public Task(JSONObject taskJson) {
            try{
                this.groupId = taskJson.getString("groupId");
            }catch (Exception e){}
            try{
                this.taskId = taskJson.getString("taskId");
            }catch (Exception e){}
            try{
                this.state = taskJson.getInt("state");
            }catch (Exception e){}
            try{
                this.sendSuccNum = taskJson.getInt("sendSuccNum");
            }catch (Exception e){}
            try{
                this.sendFailNum = taskJson.getInt("sendFailNum");
            }catch (Exception e){}
            try{
                this.pendingNum = taskJson.getInt("pendingNum");
            }catch (Exception e){}
            try{
                JSONArray failPhoneListJson = taskJson.getJSONArray("failPhoneList");
                this.failPhoneList = new ArrayList<>( failPhoneListJson );
            }catch (Exception e){}
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getSendSuccNum() {
            return sendSuccNum;
        }

        public void setSendSuccNum(int sendSuccNum) {
            this.sendSuccNum = sendSuccNum;
        }

        public int getSendFailNum() {
            return sendFailNum;
        }

        public void setSendFailNum(int sendFailNum) {
            this.sendFailNum = sendFailNum;
        }

        public int getPendingNum() {
            return pendingNum;
        }

        public void setPendingNum(int pendingNum) {
            this.pendingNum = pendingNum;
        }

        public List<String> getFailPhoneList() {
            return failPhoneList;
        }

        public void setFailPhoneList(List<String> failPhoneList) {
            this.failPhoneList = failPhoneList;
        }
        @Override
        public String toString(){
            return JSONUtil.objectToJson(this);
        }
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public Task getTask() {
        if(this.task == null){
            this.task = new Task(this.taskJson);
        }
        return this.task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public JSONObject getTaskJson() {
        return taskJson;
    }

    public void setTaskJson(JSONObject taskJson) {
        this.taskJson = taskJson;
    }

    @Override
    public String toString(){
        return JSONUtil.objectToJson(this);
    }

}
