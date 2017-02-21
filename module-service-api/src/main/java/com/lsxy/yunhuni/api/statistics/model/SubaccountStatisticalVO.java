package com.lsxy.yunhuni.api.statistics.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 子账户统计vo
 * Created by zhangxb on 2017/2/14.
 */
@Entity
@SqlResultSetMapping(name="subaccountStatisticalVO",
        entities= {
                @EntityResult(
                        entityClass=SubaccountStatisticalVO.class,
                        fields= {
                                @FieldResult(name="id", column="id"),
                                @FieldResult(name="certId", column="certId"),
                                @FieldResult(name="secretKey", column="secretKey"),
                                @FieldResult(name="appId", column="appId"),
                                @FieldResult(name="appName", column="appName"),
                                @FieldResult(name="voiceNum", column="voiceNum"),
                                @FieldResult(name="seatNum", column="seatNum"),
                                @FieldResult(name="amongAmount", column="amongAmount"),
                                @FieldResult(name="amongDuration", column="amongDuration")
                        })
        }
)
public class SubaccountStatisticalVO implements Serializable {
    private String id;
    private String certId;//子账号鉴权账号
    private String secretKey;//子账号密钥
    private String appId;//应用id
    private String appName;//应用名字
    private String voiceNum; //语音用量 /总量（分钟）
    private String seatNum; //坐席用量 /总量（个）
    private BigDecimal amongAmount;//消费金额
    private Long amongDuration;//话务量

    @Id
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

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
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