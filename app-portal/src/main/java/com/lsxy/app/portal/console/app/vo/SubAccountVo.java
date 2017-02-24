package com.lsxy.app.portal.console.app.vo;

/**
 * Created by zhangxb on 2017/2/16.
 */
public class SubAccountVo {
    private String id;
    private String certId;//子账号鉴权账号
    private String secretKey;//子账号密钥
    private Integer enabled;//状态：1，可用0，不可用
    private String remark;//备注
    private String voiceNum; //语音用量 /总量（分钟）
    private String seatNum; //坐席用量 /总量（个）
    private String url;//回调地址
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
