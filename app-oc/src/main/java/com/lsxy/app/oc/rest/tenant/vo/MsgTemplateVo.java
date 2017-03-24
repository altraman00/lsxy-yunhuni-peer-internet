package com.lsxy.app.oc.rest.tenant.vo;

import com.lsxy.msg.api.model.MsgTemplate;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by zhangxb on 2017/3/8.
 */
@ApiModel
public class MsgTemplateVo {
    @ApiModelProperty(name = "id",value = "子账号ID")
    private String id;
    @ApiModelProperty(name = "tempId",value = "模板ID")
    private String tempId;
    @ApiModelProperty(name = "subaccountId",value = "子账号ID")
    private String subaccountId;
    @ApiModelProperty(name = "certId",value = "子账号-鉴权账号")
    private String certId;
    @ApiModelProperty(name = "name",value = "模板名字")
    private String name;
    @ApiModelProperty(name = "type",value = "模板类型sms短信；ussd闪印")
    private String type;
    @ApiModelProperty(name = "content",value = "模板内容")
    private String content;
    @ApiModelProperty(name = "state",value = "状态-1审核不通过，0待审核，1审核通过")
    private Integer state;
    @ApiModelProperty(name = "reason",value = "原因")
    private String reason;
    @ApiModelProperty(name = "remark",value = "备注")
    private String remark;

    public MsgTemplateVo(String id, String tempId, String subaccountId, String certId, String name, String type, String content, Integer state, String reason, String remark) {
        this.id = id;
        this.tempId = tempId;
        this.subaccountId = subaccountId;
        this.certId = certId;
        this.name = name;
        this.type = type;
        this.content = content;
        this.state = state;
        this.reason = reason;
        this.remark = remark;
    }

    public static MsgTemplateVo changeMsgTemplateToVo(MsgTemplate msg, ApiCertificateSubAccount apiCertificateSubAccount){
        String certId = apiCertificateSubAccount!=null ? apiCertificateSubAccount.getCertId() :"";
        return new MsgTemplateVo(msg.getId(),msg.getTempId(),msg.getSubaccountId(),certId,msg.getName(),msg.getType(),msg.getContent(),msg.getStatus(),msg.getReason(),msg.getRemark());
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }


    public String getSubaccountId() {
        return subaccountId;
    }

    public void setSubaccountId(String subaccountId) {
        this.subaccountId = subaccountId;
    }

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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
