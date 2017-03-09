package com.lsxy.msg.supplier.paopaoyu;

import com.lsxy.msg.supplier.common.MsgConstant;
import com.lsxy.msg.supplier.common.ResultOne;
import net.sf.json.JSONObject;

/**
 * 泡泡鱼单发结果处理
 * Created by zhangxb on 2017/1/10.
 */
public class PaoPaoYuResultOne extends ResultOne {

    public PaoPaoYuResultOne(String result) {
        //result_code":"0","result_desc":"Successfully.","taskId":"355d3a807165415785e5487fcce929b6"
        String resultCode ;
        String resultDesc = null;
        String taskId = null;
        JSONObject resp = JSONObject.fromObject(result);
        resultCode = resp.getString("result_code");
        if("0".equals(resultCode)) {//调用接口成功
            taskId = resp.getString("taskId");
        }else{
            resultDesc = resp.getString("result_desc");
            //翻译为中文
            resultDesc = PaoPaoYuResultCode.getPaoPaoYuResult( resultCode , resultDesc );
            resultCode = MsgConstant.OTHER_ERROR_CODE;
        }
        this.resultCode = resultCode;
        this.resultDesc = resultDesc;
        this.taskId = taskId;
        this.handlers = PaoPaoYuConstant.PaopaoyuCode;
    }

}
