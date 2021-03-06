package com.lsxy.msg.supplier;

import com.lsxy.framework.core.utils.JSONUtil;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangxb on 2016/12/21.
 */
public enum APIResultCode {
    /****************本系统 操作成功*****************/
    MY_SUCCESS("0","成功"),
    /****************本系统 自用错误码*****************/
    MY_ERROR_10001("10001","登录失败,用户或密码错误或者账号异常"),
    MY_ERROR_10002("10002","token检验失败，请重新登录获取"),//token检验失败，请重新登录获取
    MY_ERROR_10003("10003","该ip不在ip白名单中"),
    MY_ERROR_10004("10004","未知异常，请联系服务提供商"),//未知异常，请联系服务提供商
    MY_ERROR_10005("10005","日期格式错误"),
    MY_ERROR_10006("10006","权限不足"),
    MY_ERROR_10007("10007","系统错误"),
    /****************泡泡鱼平台 系统级错误码*****************/
    ERROR_302("302","当前请求过于频繁"),
    ERROR_10001("10001","应用帐户异常"),
    ERROR_10002("10002","应用被禁用"),
    ERROR_10003("10003","应用已删除"),
    ERROR_10004("10004","应用待审核"),
    ERROR_10005("10005","非法的client_id"),
    ERROR_10006("10006","帐户或密码错误"),
    ERROR_10007("10007","系统错误"),
    ERROR_10008("10008","数字签名校验失败"),
    ERROR_200020("200020","serialId 格式不正确"),
    /****************泡泡鱼平台 业务功能级错误码*****************/
    ERROR_20001("20001","手机号码不合法"),
    ERROR_20002("20002","参数格式不正确"),
    ERROR_20003("20003","非法参数字符串"),
    ERROR_20004("20004","模板已暂停"),
    ERROR_20005("20005","模板已删除"),
    ERROR_20006("20006","模板不存在或模板为空"),
    ERROR_20007("20007","发送数量超限"),
    ERROR_20008("20008","发送消息失败"),
    ERROR_20009("20009","企业状态不正常"),
    ERROR_200010("200010","企业套餐不存在"),
    ERROR_200011("200011","缺少必要参数"),
    ERROR_200012("200012","模板内容中的变量填写错误"),
    ERROR_200013("200013","消息发送时间格式不正确"),
    ERROR_200014("200014","手机号码集合数量大于最大限制"),
    ERROR_200015("200015","手机号码集合数量大于剩余套餐数量"),
    ERROR_200016("200016","手机号码集合中的手机号码不符合规范"),
    ERROR_200017("200017","消息内容的长度超出最大限制的长度"),
    ERROR_200018("200018","添加消息模板失败"),
    ERROR_200019("200019","对不起，查询结果为空"),
    ERROR_200021("200021","查询单发结果，部分结果查询失败"),
    ERROR_200022("200022","天发送次数超出"),
    ERROR_200023("200023","手机号码不合法"),
    ERROR_200024("200024","扣费失败")
    ;

    public static APIResultCode getApi(String code){
        for (APIResultCode s : APIResultCode.values()){
            if(s.getResultCode().equals(code)){
                return s;
            }
        }
        return null;
    }
    public static String getApiResult(String str){
        JSONObject resp = JSONObject.fromObject(str);
        String resultCode = resp.getString("result_code");
        if(!"0".equals(resultCode)){
            int code = Integer.valueOf(resultCode);
            if(code>=20001&&code<=200024){
                APIResultCode temp =  getApi(resultCode);
                if(temp!=null){
                    return temp.toString();
                }
            }else if(code==-1){
               return str;
            }
        }else{
            return MY_SUCCESS.toString();
        }
        return MY_ERROR_10004.toString();
    }
    public static String getSuccessCode(String name,Object obj){
        Map map = new HashMap();
        map.put("result_code",0);
        map.put(name,obj);
        return JSONUtil.objectToJson(map);
    }
    public static String getSuccess(Map map1){
        Map map = new HashMap();
        map.put("result_code",0);
        map.putAll(map1);
        return JSONUtil.objectToJson(map);
    }
    public static String getFail(APIResultCode apiResultCode,Map map1){
        Map map = new HashMap();
        map.put("result_code",apiResultCode.getResultCode());
        map.put("result_desc",apiResultCode.getResultDesc());
        map.putAll(map1);
        return JSONUtil.objectToJson(map);
    }
    public static String getFailCode(String name,Object obj){
        Map map = new HashMap();
        map.put("result_code",-1);
        map.put(name,obj);
        return JSONUtil.objectToJson(map);
    }
    public static String getFail(Map map1){
        Map map = new HashMap();
        map.put("result_code",-1);
        map.putAll(map1);
        return JSONUtil.objectToJson(map);
    }

    @Override
    public String toString() {
        Map map = new HashMap();
        map.put("result_code",resultCode);
        map.put("result_desc",resultDesc);
        return JSONUtil.objectToJson(map);
    }
    public Map toMap() {
        Map map = new HashMap();
        map.put("result_code",resultCode);
        map.put("result_desc",resultDesc);
        return map;
    }
    private String resultCode;
    private String resultDesc;

    APIResultCode(String resultCode, String resultDesc) {
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
}
