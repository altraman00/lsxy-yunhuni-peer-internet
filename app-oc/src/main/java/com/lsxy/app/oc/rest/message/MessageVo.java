package com.lsxy.app.oc.rest.message;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by zhangxb on 2016/8/19.
 */
@ApiModel
public class MessageVo {
    @ApiModelProperty(name="type",value = "消息类型：1表示活动消息 0表示用户消息")
    private Integer type;//消息类型
    @ApiModelProperty(name="status",value = "消息状态-1下线0未发布1上线")
    private Integer status;//状态
    @ApiModelProperty(name="content",value = "消息内容")
    private String content;//消息内容
    @ApiModelProperty(name="title",value = "消息标题")
    private String title;//标题
    @ApiModelProperty(name="name",value = "发布人名字（非必填）")
    private String name;//发布人
    @ApiModelProperty(name="line",value = "yyyy-MM-dd HH:mm（非必填）")
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
