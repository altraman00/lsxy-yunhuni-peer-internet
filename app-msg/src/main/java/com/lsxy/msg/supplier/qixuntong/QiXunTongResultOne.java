package com.lsxy.msg.supplier.qixuntong;

import com.lsxy.msg.supplier.model.MsgConstant;
import com.lsxy.msg.supplier.model.ResultOne;
import net.sf.json.JSONObject;

/**
 * 企讯通接口发送处理
 * Created by zhangxb on 2017/1/10.
 */
public class QiXunTongResultOne extends ResultOne {
    public QiXunTongResultOne(String result) {
        String resultCode ;
        String resultDesc = null;
        String taskId = null;
        JSONObject resp = JSONObject.fromObject(result);
        //{"resultcode":0,"resultmsg":"成功","taskid":"C945316932025847"}
        resultCode = resp.getString("resultcode");
        if("0".equals(resultCode)) {//调用接口成功
            taskId = resp.getString("taskid");
        }else{
            resultDesc = resp.getString("resultmsg");
            //翻译为中文
            resultDesc = QiXunTongResultCode.getQiXunTongResult( resultCode , resultDesc );
            resultCode = MsgConstant.OTHER_ERROR_CODE;
        }
        this.resultCode = resultCode;
        this.resultDesc = resultDesc;
        this.taskId = taskId;
        this.handlers = QiXunTongConstant.handler_qixuntong;
    }
}
