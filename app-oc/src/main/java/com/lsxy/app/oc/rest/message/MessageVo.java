package com.lsxy.app.oc.rest.message;

import java.util.Date;

/**
 * Created by zhangxb on 2016/8/19.
 */
public class MessageVo {
    private Integer type;//消息类型
    private Integer status;//状态
    private String content;//消息内容
    private String title;//标题
    private String name;//发布人
    private String line;//上线时间

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }
}
