package com.lsxy.msg.supplier.model;

import com.lsxy.framework.core.utils.JSONUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangxb on 2017/1/10.
 */
public enum ResultCode {
    /**************** 发送短信失败原因 *****************/
    //手机号码检验
    ERROR_20001("20001","手机号码不合法"),
    //普通短信
    ERROR_20002("20002","消息内容的长度不符合要求或模板不存在或模板为空"),
    //模板短信
    ERROR_20003("20003","任务名不能为空"),
    ERROR_20004("20004","模板参数不正确"),
    //群发
    ERROR_20005("20005","手机号码集合数量大于最大限制"),
    ERROR_20006("20006","余量不足"),
    ERROR_20007("20007","手机号码集合中的手机号码不符合规范"),
    //普通失败
    ERROR_20008("20008","发送消息失败"),
    ERROR_20009("20009","本平台对号码所属运营商暂时不支持该发送类型"),
   //日期格式校验
   ERROR_200010("200010","发送时间格式错误，格式必须是：yyyyMMddHHmmss"),
    ERROR_10007("10007","系统错误")
    ;

    private String resultCode;
    private String resultDesc;

    ResultCode(String resultCode, String resultDesc) {
        this.resultCode = resultCode;
        this.resultDesc = resultDesc;
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

    @Override
    public String toString() {
        Map<String,String> map = new HashMap<String,String>(){
            {
                put("result_code",resultCode);
                put("result_desc",resultDesc);
            }
        };
        return JSONUtil.objectToJson(map);
    }
}
