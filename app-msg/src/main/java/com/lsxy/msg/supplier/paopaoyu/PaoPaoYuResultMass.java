package com.lsxy.msg.supplier.paopaoyu;

import com.lsxy.msg.supplier.common.MsgConstant;
import com.lsxy.msg.supplier.common.ResultMass;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by zhangxb on 2017/1/10.
 */
public class PaoPaoYuResultMass extends ResultMass {
    public PaoPaoYuResultMass(String result,String mobiles,String supplierTempId) {
        String resultCode ;
        String resultDesc = null;
        String taskId = null;
        JSONObject resp = JSONObject.fromObject(result);
        resultCode = resp.getString("result_code");
        String[] de = mobiles.split(PaoPaoYuConstant.PaoPaoYuNumRegexStr);
        this.sumNum = de.length;
        if("0".equals(resultCode)) {//调用接口成功
            taskId = resp.getString("taskId");
            this.pendingPhones = new ArrayList<>(Arrays.asList( de ));
            try {
                JSONArray jsonArray = resp.getJSONArray("badPhones");
                this.badPhones = new ArrayList<>(jsonArray);
                this.pendingPhones.removeAll(this.badPhones);
            }catch (Exception e){}
        }else{
            resultDesc = resp.getString("result_desc");
            //翻译为中文
            resultDesc = PaoPaoYuResultCode.getPaoPaoYuResult( resultCode , resultDesc );
            resultCode = MsgConstant.OTHER_ERROR_CODE;
            this.badPhones = new ArrayList<String>(Arrays.asList(de));
        }
        this.failNum = this.badPhones != null ? this.badPhones.size() : 0;
        this.pendingNum = this.pendingPhones != null ? this.pendingPhones.size() : 0;

        this.resultCode = resultCode;
        this.resultDesc = resultDesc;
        this.taskId = taskId;
        this.handlers = PaoPaoYuConstant.PaopaoyuCode;
        this.supplierTempId = supplierTempId;
    }
}
