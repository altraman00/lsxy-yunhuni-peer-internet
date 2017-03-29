package com.lsxy.yunhuni.api.apicertificate.model;

/**
 * 鉴权账号配额类型
 * Created by liups on 2017/2/15.
 */
public enum  CertAccountQuotaType {
    CallQuota("通话时长配额",CertAccountQuota.CALTYPE_SUM),
    AgentQuota("坐席在线个数配额",CertAccountQuota.CALTYPE_COUNT),
    SmsQuota("短信条数配额",CertAccountQuota.CALTYPE_COUNT),
    UssdQuota("闪印条数配额",CertAccountQuota.CALTYPE_COUNT);

    private String name;
    private Integer calType;
    CertAccountQuotaType(String name,Integer calType){
        this.name = name;
        this.calType = calType;
    }

    public String getName() {
        return name;
    }

    public Integer getCalType() {
        return calType;
    }
}
