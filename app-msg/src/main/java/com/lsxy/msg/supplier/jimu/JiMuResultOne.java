package com.lsxy.msg.supplier.jimu;

import com.lsxy.msg.api.model.MsgConstant;
import com.lsxy.msg.api.model.MsgSupplier;
import com.lsxy.msg.supplier.common.ResultOne;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 企讯通接口发送处理
 * Created by zhangxb on 2017/1/10.
 */
public class JiMuResultOne extends ResultOne {
    public JiMuResultOne(String result) {
        String resultCode ;
        String resultDesc = null;
        String taskId = null;
        JSONObject resp = JSONObject.fromObject(result);
        //{"ResultCode":"0","ResultDesc":"成功","ServerTime":"2017-03-27 14:40:23","MSMSID":"31adb1e15847566413536039bda81493","SMSID":"2f309cd1e71e4a13a05359b8d29e609f"}
        resultCode = resp.getString("ResultCode");
        if("0".equals(resultCode)) {//调用接口成功
            taskId = resp.getString("SMSID");
        }else{
            resultDesc = resp.getString("ResultDesc");
            //翻译为中文
//            resultDesc = QiXunTongResultCode.getQiXunTongResult( resultCode , resultDesc );
            resultCode = MsgConstant.OTHER_ERROR_CODE;
        }
        this.resultCode = resultCode;
        this.resultDesc = resultDesc;
        this.taskId = taskId;
        this.handlers = MsgSupplier.JiMuCode;
    }
    public static int getRequestResult(String result,String taskId){
        JSONObject resp = JSONObject.fromObject(result);
        //{"ResultCode":"0","ResultDesc":"成功","ServerTime":"2017-03-27 14:42:07",
        // "StatusReportList":[{"SMSID":"2f309cd1e71e4a13a05359b8d29e609f","MSMSID":"31adb1e15847566413536039bda81493","ObjMobile":"15360059775","GStatus":"DELIVRD"}]}
        String resultCode = resp.getString("ResultCode");
        if("0".equals(resultCode)) {//调用接口成功
            JSONArray jsonArray = resp.getJSONArray("StatusReportList");
            if(jsonArray != null && jsonArray.size() > 0){
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String mTaskId = jsonObject.getString("SMSID");
                    if(taskId.equals(mTaskId)){
                        String state = jsonObject.getString("GStatus");
                        if("DELIVRD".equals(state)){//短信发送成功
                            return 1;
                        }else{//发送失败
                            return -1;
                        }
                    }
                }
            }
        }
        return 0;
    }
}
