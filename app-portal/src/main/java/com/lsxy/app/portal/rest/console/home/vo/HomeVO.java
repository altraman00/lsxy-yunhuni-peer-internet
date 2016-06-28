package com.lsxy.app.portal.rest.console.home.vo;

import java.util.List;


/**
 * Created by liups on 2016/6/27.
 */
public class HomeVO {

    private String balanceInt;              //余额整数

    private String balanceDec;              //余额小数

    private Integer lineNum ;               //当前线路数量

    private Integer voice_remain;           //语音剩余量（分钟）

    private Integer sms_remain;             //短信剩余量（条）

    private Integer meeting_remain;         //会议剩余量（分钟）

    private String resApi;                  //restApi

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

    public Integer getVoice_remain() {
        return voice_remain;
    }

    public void setVoice_remain(Integer voice_remain) {
        this.voice_remain = voice_remain;
    }

    public Integer getSms_remain() {
        return sms_remain;
    }

    public void setSms_remain(Integer sms_remain) {
        this.sms_remain = sms_remain;
    }

    public Integer getMeeting_remain() {
        return meeting_remain;
    }

    public void setMeeting_remain(Integer meeting_remain) {
        this.meeting_remain = meeting_remain;
    }

    public String getResApi() {
        return resApi;
    }

    public void setResApi(String resApi) {
        this.resApi = resApi;
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
