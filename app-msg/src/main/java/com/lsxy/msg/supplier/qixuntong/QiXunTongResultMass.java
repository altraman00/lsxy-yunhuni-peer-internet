package com.lsxy.msg.supplier.qixuntong;

import com.lsxy.msg.api.model.MsgConstant;
import com.lsxy.msg.supplier.common.ResultMass;
import com.msg.qixuntong.QiXunTongConstant;
import com.msg.qixuntong.QiXunTongResultCode;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 企讯通接口发送处理
 * Created by zhangxb on 2017/1/10.
 */
public class QiXunTongResultMass extends ResultMass {
    public QiXunTongResultMass(String result,String mobiles) {
        String resultCode ;
        String resultDesc = null;
        String taskId = null;
        JSONObject resp = JSONObject.fromObject(result);
        //{"resultcode":0,"resultmsg":"成功","taskid":"C945316932025847"}
        resultCode = resp.getString("resultcode");
        String[] de = mobiles.split(QiXunTongConstant.QiXunTongNumRegexStr);
        this.sumNum = de.length;
        if("0".equals(resultCode)) {//调用接口成功
            taskId = resp.getString("taskid");
            this.pendingPhones = new ArrayList<>(Arrays.asList( de ));
        }else{
            resultDesc = resp.getString("resultmsg");
            //翻译为中文
            resultDesc = QiXunTongResultCode.getQiXunTongResult( resultCode , resultDesc );
            resultCode = MsgConstant.OTHER_ERROR_CODE;
            this.badPhones = new ArrayList<String>(Arrays.asList(de));
        }
        this.failNum = this.badPhones != null ? this.badPhones.size() : 0;
        this.pendingNum = this.pendingPhones != null ? this.pendingPhones.size() : 0;
        this.resultCode = resultCode;
        this.resultDesc = resultDesc;
        this.taskId = taskId;
        this.handlers = QiXunTongConstant.QixuntongCode;
    }
}
