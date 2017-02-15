package com.lsxy.yunhuni.api.apicertificate.model;

/**
 * 鉴权账号配额类型
 * Created by liups on 2017/2/15.
 */
public enum  CertAccountQuotaType {
    CallQuota("通话时长配额"),
    AgentQuota("坐席在线个数配额");

    private String name;
    CertAccountQuotaType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
