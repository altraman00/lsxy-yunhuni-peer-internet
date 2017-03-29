package com.lsxy.app.oc.rest.tenant.vo;

import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;
import com.lsxy.yunhuni.api.apicertificate.model.CertAccountQuota;
import com.lsxy.yunhuni.api.apicertificate.model.CertAccountQuotaType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by zhangxb on 2017/2/16.
 */
@ApiModel
public class SubAccountVo {
    @ApiModelProperty(name = "id",value = "子账号ID")
    private String id;
    @ApiModelProperty(name = "certId",value = "子账号鉴权账号")
    private String certId;//
    @ApiModelProperty(name = "secretKey",value = "子账号密钥")
    private String secretKey;//
    @ApiModelProperty(name = "enabled",value = "状态：1，可用0，不可用")
    private Integer enabled;//
    @ApiModelProperty(name = "remark",value = "备注")
    private String remark;//
    @ApiModelProperty(name = "voiceNum",value = "语音用量 /总量（分钟）")
    private String voiceNum; //
    @ApiModelProperty(name = "seatNum",value = "坐席用量 /总量（个）")
    private String seatNum; //
    @ApiModelProperty(name = "smsNum",value = "短信用量/配额（条）")
    private String smsNum;//
    @ApiModelProperty(name = "ussdNum",value = "闪印用量 /配额（条）")
    private String ussdNum;//
    @ApiModelProperty(name = "url",value = "回调地址")
    private String url;//

    public SubAccountVo(ApiCertificateSubAccount apiCertificateSubAccount) {
        this.id= apiCertificateSubAccount.getId();
        this.certId = apiCertificateSubAccount.getCertId();
        this.secretKey = apiCertificateSubAccount.getSecretKey();
        this.enabled = apiCertificateSubAccount.getEnabled();
        this.remark = apiCertificateSubAccount.getRemark();
        List<CertAccountQuota> list = apiCertificateSubAccount.getQuotas();
        if(list != null){
            for (int i = 0; i < list.size(); i++) {
                CertAccountQuota certAccountQuota = list.get(i);
                if(CertAccountQuotaType.AgentQuota.name().equals( certAccountQuota.getType() )){
                    this.seatNum = ( null == certAccountQuota.getCurrentUsed() ? "0": (certAccountQuota.getCurrentUsed()/60) ) +"/" + ( 0 > certAccountQuota.getValue() ? "∞": (certAccountQuota.getValue()/60) );
                }else if(CertAccountQuotaType.CallQuota.name().equals( certAccountQuota.getType())){
                    this.voiceNum = ( null == certAccountQuota.getCurrentUsed() ? "0": (certAccountQuota.getCurrentUsed()/60) )+"/" + ( 0 > certAccountQuota.getValue() ? "∞": (certAccountQuota.getValue()/60) );
                }else if(CertAccountQuotaType.UssdQuota.name().equals( certAccountQuota.getType())){
                    this.ussdNum = ( null == certAccountQuota.getCurrentUsed() ? "0": (certAccountQuota.getCurrentUsed()) )+"/" + ( 0 > certAccountQuota.getValue() ? "∞": (certAccountQuota.getValue()) );
                }else if(CertAccountQuotaType.SmsQuota.name().equals( certAccountQuota.getType())){
                    this.smsNum = ( null == certAccountQuota.getCurrentUsed() ? "0": (certAccountQuota.getCurrentUsed()) )+"/" + ( 0 > certAccountQuota.getValue() ? "∞": (certAccountQuota.getValue()) );
                }
            }
        }
    }
    public SubAccountVo(ApiCertificateSubAccount apiCertificateSubAccount, String type) {
        this.id= apiCertificateSubAccount.getId();
        this.certId = apiCertificateSubAccount.getCertId();
        this.secretKey = apiCertificateSubAccount.getSecretKey();
        this.enabled = apiCertificateSubAccount.getEnabled();
        this.remark = apiCertificateSubAccount.getRemark();
        this.url = apiCertificateSubAccount.getCallbackUrl();
        List<CertAccountQuota> list = apiCertificateSubAccount.getQuotas();
        if(list != null){
            for (int i = 0; i < list.size(); i++) {
                CertAccountQuota certAccountQuota = list.get(i);
                if(CertAccountQuotaType.AgentQuota.name().equals( certAccountQuota.getType() )){
                    this.seatNum = ( certAccountQuota.getValue()/60 )+"" ;
                }else if(CertAccountQuotaType.CallQuota.name().equals( certAccountQuota.getType())){
                    this.voiceNum = ( certAccountQuota.getValue()/60 ) +"";
                }else if(CertAccountQuotaType.UssdQuota.name().equals( certAccountQuota.getType())){
                    this.ussdNum = ( certAccountQuota.getValue() ) +"";
                }else if(CertAccountQuotaType.SmsQuota.name().equals( certAccountQuota.getType())){
                    this.smsNum = ( certAccountQuota.getValue() ) +"";
                }
            }
        }
    }

    public String getSmsNum() {
        return smsNum;
    }

    public void setSmsNum(String smsNum) {
        this.smsNum = smsNum;
    }

    public String getUssdNum() {
        return ussdNum;
    }

    public void setUssdNum(String ussdNum) {
        this.ussdNum = ussdNum;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getVoiceNum() {
        return voiceNum;
    }

    public void setVoiceNum(String voiceNum) {
        this.voiceNum = voiceNum;
    }

    public String getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(String seatNum) {
        this.seatNum = seatNum;
    }
}
