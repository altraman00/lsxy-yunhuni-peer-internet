package com.lsxy.app.portal.console.home;

import java.util.List;


/**
 * 首页VO
 * Created by liups on 2016/6/27.
 */
public class HomeVO {

    private String balanceInt;              //余额整数

    private String balanceDec;              //余额小数

    private Integer lineNum ;               //当前线路数量

    private Integer voiceRemain;           //语音剩余量（分钟）

    private Integer smsRemain;             //短信剩余量（条）

    private Integer conferenceRemain;         //会议剩余量（分钟）

    private String restApi;                  //restApi

    private String secretKey;               //secretKey

   private List<AppStateVO> appStateVOs;    //应用

    public String getBalanceInt() {
        return balanceInt;
    }

    public void setBalanceInt(String balanceInt) {
        this.balanceInt = balanceInt;
    }

    public String getBalanceDec() {
        return balanceDec;
    }

    public void setBalanceDec(String balanceDec) {
        this.balanceDec = balanceDec;
    }

    public Integer getLineNum() {
        return lineNum;
    }

    public void setLineNum(Integer lineNum) {
        this.lineNum = lineNum;
    }

    public Integer getVoiceRemain() {
        return voiceRemain;
    }

    public void setVoiceRemain(Integer voiceRemain) {
        this.voiceRemain = voiceRemain;
    }

    public Integer getSmsRemain() {
        return smsRemain;
    }

    public void setSmsRemain(Integer smsRemain) {
        this.smsRemain = smsRemain;
    }

    public Integer getConferenceRemain() {
        return conferenceRemain;
    }

    public void setConferenceRemain(Integer conferenceRemain) {
        this.conferenceRemain = conferenceRemain;
    }

    public String getRestApi() {
        return restApi;
    }

    public void setRestApi(String restApi) {
        this.restApi = restApi;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public List<AppStateVO> getAppStateVOs() {
        return appStateVOs;
    }

    public void setAppStateVOs(List<AppStateVO> appStateVOs) {
        this.appStateVOs = appStateVOs;
    }
}
