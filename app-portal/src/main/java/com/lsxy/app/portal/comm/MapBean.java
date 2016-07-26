package com.lsxy.app.portal.comm;

/**
 * ajax返回统一格式
 * Created by zhangxb on 2016/7/26.
 */
public class MapBean  {
    //代号 0000成功，1111失败 ,0010登陆超时
    private String code;
    //消息
    private String msg;
    //可提供返回对象，也可以不提供
    private Object data;

    public MapBean(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public MapBean(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public MapBean() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
