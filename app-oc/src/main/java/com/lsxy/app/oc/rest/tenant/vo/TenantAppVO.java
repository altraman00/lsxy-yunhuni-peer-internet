package com.lsxy.app.oc.rest.tenant.vo;

import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.yunhuni.api.app.model.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by liuws on 2016/8/12.
 */
public class TenantAppVO implements Serializable {
    public static final Logger logger = LoggerFactory.getLogger(TenantAppVO.class);
    private String id;
    private String name;//应用名字
    private Integer status;//应用状态
    private String description;//应用描述
    private String type;//应用类型
    private String industry;//所属行业
    private String whiteList;//服务器白名单
    private String url;//回调URL
    private Integer isAuth;//是否鉴权,0否，1是
    private Integer isVoiceDirectly;//是否语音直拨 0否，1是
    private Integer isVoiceCallback;//是否语音回拨0否，1是
    private Integer isSessionService;//是否会议服务0否，1是
    private Integer isRecording;//是否录音服务0否，1是
    private Integer isVoiceValidate;//是否语音验证码0否，1是
    private Integer isIvrService;//是否IVR定制服务0否，1是
    private String serviceType;//服务类型
    private BigDecimal consume;
    private Long sessionCount;
    private Long amongDuration;
    private String recordingTime;
    private List<String> testPhone;
    private String sipRegistrar;//分机信息
    public TenantAppVO(){}
    public TenantAppVO(App app){
        try {
            BeanUtils.copyProperties(this,app);
        } catch (IllegalAccessException e) {
            logger.error("复制类属性异常",e);
        } catch (InvocationTargetException e) {
            logger.error("复制类属性异常",e);
        }
    }

    public String getRecordingTime() {
        return recordingTime;
    }

    public void setRecordingTime(String recordingTime) {
        this.recordingTime = recordingTime;
    }

    public String getSipRegistrar() {
        return sipRegistrar;
    }

    public void setSipRegistrar(String sipRegistrar) {
        this.sipRegistrar = sipRegistrar;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(String whiteList) {
        this.whiteList = whiteList;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(Integer isAuth) {
        this.isAuth = isAuth;
    }

    public Integer getIsVoiceDirectly() {
        return isVoiceDirectly;
    }

    public void setIsVoiceDirectly(Integer isVoiceDirectly) {
        this.isVoiceDirectly = isVoiceDirectly;
    }

    public Integer getIsVoiceCallback() {
        return isVoiceCallback;
    }

    public void setIsVoiceCallback(Integer isVoiceCallback) {
        this.isVoiceCallback = isVoiceCallback;
    }

    public Integer getIsSessionService() {
        return isSessionService;
    }

    public void setIsSessionService(Integer isSessionService) {
        this.isSessionService = isSessionService;
    }

    public Integer getIsRecording() {
        return isRecording;
    }

    public void setIsRecording(Integer isRecording) {
        this.isRecording = isRecording;
    }

    public Integer getIsVoiceValidate() {
        return isVoiceValidate;
    }

    public void setIsVoiceValidate(Integer isVoiceValidate) {
        this.isVoiceValidate = isVoiceValidate;
    }

    public Integer getIsIvrService() {
        return isIvrService;
    }

    public void setIsIvrService(Integer isIvrService) {
        this.isIvrService = isIvrService;
    }

    public BigDecimal getConsume() {
        return consume;
    }

    public void setConsume(BigDecimal consume) {
        this.consume = consume;
    }

    public Long getSessionCount() {
        return sessionCount;
    }

    public void setSessionCount(Long sessionCount) {
        this.sessionCount = sessionCount;
    }

    public Long getAmongDuration() {
        return amongDuration;
    }

    public void setAmongDuration(Long amongDuration) {
        this.amongDuration = amongDuration;
    }

    public List<String> getTestPhone() {
        return testPhone;
    }

    public void setTestPhone(List<String> testPhone) {
        this.testPhone = testPhone;
    }
}
