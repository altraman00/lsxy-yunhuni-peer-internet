package com.lsxy.app.oc.rest.tenant.vo;

import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by zhangxb on 2017/3/1.
 */
@ApiModel
public class VoiceFilePlayVo {
    @ApiModelProperty(name = "id",value = "放音文件ID")
    private String id;
    @ApiModelProperty(name = "name",value = "放音文件名字")
    private String name;
    @ApiModelProperty(name = "status",value = "状态")
    private Integer status;
    @ApiModelProperty(name = "size",value = "大小，单位byte")
    private Long size;
    @ApiModelProperty(name = "remark",value = "备注")
    private String remark;
    @ApiModelProperty(name = "reason",value = "原因")
    private String reason;
    @ApiModelProperty(name = "sync",value = "是否同步")
    private Integer sync;
    @ApiModelProperty(name = "certId",value = "关联子账号-鉴权账户")
    private String certId;
    @ApiModelProperty(name = "subaccountId",value = "关联子账号")
    private String subaccountId;
    @ApiModelProperty(name = "fileKey",value = "文件下载使用")
    private String fileKey;

    public VoiceFilePlayVo(String id, String name, Integer status, Long size, String remark, String reason, Integer sync, String certId, String subaccountId, String fileKey) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.size = size;
        this.remark = remark;
        this.reason = reason;
        this.sync = sync;
        this.certId = certId;
        this.subaccountId = subaccountId;
        this.fileKey = fileKey;
    }
    public static VoiceFilePlayVo changeAppVoiceFileToVoiceFileVO(VoiceFilePlay ext, ApiCertificateSubAccount apiCertificateSubAccount){
        String certId = apiCertificateSubAccount!=null ? apiCertificateSubAccount.getCertId() :"";
        return new VoiceFilePlayVo(ext.getId(),ext.getName(),ext.getStatus(),ext.getSize(),ext.getRemark(),ext.getReason(),ext.getSync(),certId,ext.getSubaccountId(), ext.getFileKey());
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public String getSubaccountId() {
        return subaccountId;
    }

    public void setSubaccountId(String subaccountId) {
        this.subaccountId = subaccountId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getSync() {
        return sync;
    }

    public void setSync(Integer sync) {
        this.sync = sync;
    }

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }
}
