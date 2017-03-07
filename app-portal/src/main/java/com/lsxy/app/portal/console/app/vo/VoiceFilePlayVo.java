package com.lsxy.app.portal.console.app.vo;

import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;
import sun.nio.ch.FileKey;

/**
 * Created by zhangxb on 2017/3/1.
 */
public class VoiceFilePlayVo{
    private String id;
    private String name;
    private Integer status;
    private Long size;
    private String remark;
    private String reason;
    private Integer sync;
    private String certId;
    private String subaccountId;
    private String fileKey;

    public VoiceFilePlayVo(String id, String name, Integer status, Long size, String remark, String reason, Integer sync, String certId, String subaccountId,String fileKey) {
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
