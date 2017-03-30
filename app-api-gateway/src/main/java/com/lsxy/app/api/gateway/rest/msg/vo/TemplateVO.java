package com.lsxy.app.api.gateway.rest.msg.vo;

import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.msg.api.model.MsgTemplate;

/**
 * Created by liups on 2017/3/20.
 */
public class TemplateVO {
    private String name;
    private String type;
    private String tempId;
    private String content;
    private Integer status;
    private String checkTime;
    private String reason;
    private String remark;

    public TemplateVO() {
    }

    public TemplateVO(MsgTemplate template) {
        this.name = template.getName();
        this.type = template.getType();
        this.tempId = template.getTempId();
        this.content = template.getContent();
        this.status = template.getStatus();
        if(MsgTemplate.STATUS_FAIL == status || MsgTemplate.STATUS_PASS == status){
            this.checkTime = DateUtils.getTime(template.getLastTime(),"yyyy-MM-dd HH:mm:ss");
            this.reason = template.getReason();
        }
        this.remark = template.getRemark();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
