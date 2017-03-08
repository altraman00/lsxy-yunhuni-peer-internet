package com.lsxy.msg.supplier.model;

import com.lsxy.framework.core.utils.JSONUtil;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangxb on 2017/1/10.
 */
public class BaseResult implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int MaxMsMLength = 70;//普通短信长度扣费
    public static final int MaxOneMsMLength = 67;//普通短信长度扣费
    public static final int MaxNum = 10000;//目前最大支持10000个群发
    public static final String NumRegexStr = ",";//多手机号码用逗号分割
    public static final String ParamRegexStr = ";";//多参数用分号分割
    public static final int PaoPaoYuMaxNum = 10000;//泡泡鱼最大群发数10000
    public static final String PaoPaoYuNumRegexStr = ",";//泡泡鱼多号码已，分割
    public static final int QiXunTongMaxNum = 10000;//企讯通最大群发数10000
    public static final String QiXunTongNumRegexStr = ",";//企讯通多号码已，分割
    public static final String OTHER_ERROR_CODE = "-1";
    public static final String SUCCESS = "0";
    public  static final int SENDTYPE_TEMP_USSD = 0;//闪印
    public static final int SENDTYPE_TEMP_MSM = 1;//普通短信
    public static final int SENDTYPE_MSM = 2;//普通短信
    public static final int ChinaMobile  = 1;//"中国移动";
    public static final int ChinaUnicom = 2;//"中国联通";
    public static final int ChinaTelecom = 3;//"中国电信";
    public static final String handler_paopaoyu = "PaoPaoYu";
    public static final String handler_qixuntong = "QiXunTong";
    public static final String replace_symbol_paopaoyu = "#*#";
    public static final String replace_symbol_qixuntong = "#P#";
    public static final int SEND_FIAL_MAX_NUM = 3;
    protected String resultCode;
    protected String resultDesc;
    protected String handlers;
    protected String taskId;
    public BaseResult() {
    }
    public BaseResult(ResultCode result) {
        this.resultCode = result.getResultCode();
        this.resultDesc = result.getResultDesc();
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

    public String getHandlers() {
        return handlers;
    }

    public void setHandlers(String handlers) {
        this.handlers = handlers;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Override
    public String toString() {
        Map map = new HashMap();
        map.put("result_code",this.resultCode);
        if(StringUtils.isNotEmpty(this.resultDesc)) {
            map.put("result_desc",this.resultDesc );
        }
        return JSONUtil.objectToJson(map);
    }
    public String toString2() {
        return JSONUtil.objectToJson(this);
    }

}
