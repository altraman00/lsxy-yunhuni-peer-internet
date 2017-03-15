package com.lsxy.app.api.gateway.rest.msg.dto;

import com.lsxy.app.api.gateway.dto.CommonDTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Created by liups on 2017/3/14.
 */
public class TemplateDTO extends CommonDTO {
    @NotNull
    private String name;
    @Pattern(regexp = "^msg_sms|msg_ussd$")
    private String type;
    @NotNull
    private String content;
    @NotNull
    private String remark;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
