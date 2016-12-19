package com.lsxy.app.portal.console.home;

import org.apache.tools.ant.util.DateUtils;

import java.util.Date;
import java.util.List;


/**
 * 首页VO
 * Created by liups on 2016/6/27.
 */
public class HomeVO {
    private Long messageNum = 0l;//未读消息数量
    private String arrearage = "";//默认不欠费
    private String balanceInt = "0";              //余额整数

    private String balanceDec = "000";              //余额小数

    private Integer lineNum ;               //当前线路数量

    private Object lineAverageCallTime;    //平均通话时长(分钟)

    private Object lineLinkRate;            //接通率

    private Long voiceRemain;           //语音剩余量（分钟）

    private Long smsRemain;             //短信剩余量（条）

    private Long conferenceRemain;         //会议剩余量（分钟）

    private Long fileTotalSize;                //总容量

    private Long fileUsedSize;                //已使用容量

    private String restApi;                  //restApi

    private String certId;                  //鉴权账号

    private String secretKey;               //secretKey

    private String time;//当前时间

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }
    private List<AppStateVO> appStateVOs;    //应用
    private Integer onLineApp = 0;//上线应用数
    private Integer appSize = 0;//总应用数

    public Integer getAppSize() {
        return appSize;
    }

    public void setAppSize(Integer appSize) {
        this.appSize = appSize;
    }

    public Integer getOnLineApp() {
        return onLineApp;
    }

    public void setOnLineApp(Integer onLineApp) {
        this.onLineApp = onLineApp;
    }

    public String getArrearage() {
        return arrearage;
    }

    public void setArrearage(String arrearage) {
        this.arrearage = arrearage;
    }

    public Long getMessageNum() {
        return messageNum;
    }

    public void setMessageNum(Long messageNum) {
        this.messageNum = messageNum;
    }

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

    public Object getLineAverageCallTime() {
        return lineAverageCallTime;
    }

    public void setLineAverageCallTime(Object lineAverageCallTime) {
        this.lineAverageCallTime = lineAverageCallTime;
    }

    public Object getLineLinkRate() {
        return lineLinkRate;
    }

    public void setLineLinkRate(Object lineLinkRate) {
        this.lineLinkRate = lineLinkRate;
    }

    public void setLineLinkRate(Double lineLinkRate) {
        this.lineLinkRate = lineLinkRate;
    }

    public Long getVoiceRemain() {
        return voiceRemain;
    }

    public void setVoiceRemain(Long voiceRemain) {
        this.voiceRemain = voiceRemain;
    }

    public Long getSmsRemain() {
        return smsRemain;
    }

    public void setSmsRemain(Long smsRemain) {
        this.smsRemain = smsRemain;
    }

    public Long getConferenceRemain() {
        return conferenceRemain;
    }

    public void setConferenceRemain(Long conferenceRemain) {
        this.conferenceRemain = conferenceRemain;
    }

    public Long getFileTotalSize() {
        return fileTotalSize;
    }

    public void setFileTotalSize(Long fileTotalSize) {
        this.fileTotalSize = fileTotalSize;
    }

    public Long getFileUsedSize() {
        return fileUsedSize;
    }

    public void setFileUsedSize(Long fileUsedSize) {
        this.fileUsedSize = fileUsedSize;
    }

    public String getRestApi() {
        return restApi;
    }

    public void setRestApi(String restApi) {
        this.restApi = restApi;
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

    public List<AppStateVO> getAppStateVOs() {
        return appStateVOs;
    }

    public void setAppStateVOs(List<AppStateVO> appStateVOs) {
        this.appStateVOs = appStateVOs;
    }
}
