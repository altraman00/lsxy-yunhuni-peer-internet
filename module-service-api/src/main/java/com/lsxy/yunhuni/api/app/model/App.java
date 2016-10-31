package com.lsxy.yunhuni.api.app.model;

import com.lsxy.framework.api.base.IdEntity;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.yunhuni.api.config.model.Area;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * 应用
 * Created by liups on 2016/6/29.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_app")

public class App extends IdEntity {
    public static int STATUS_ONLINE = 1;//上线
    public static int STATUS_OFFLINE = 2;//没上线
    public static String PRODUCT_VOICE = "voice";//语言产品
    public static String PRODUCT_CALL_CENTER = " call_center";//语言产品
    private Tenant tenant;//所属租户
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
    private Area area;  //所属区域（应用上线后要指定区域）
    private Integer isCallCenter;//是否启用呼叫中心服务 是否呼叫中心0否，1是',
    private String serviceType;//服务类型
    @Column(name = "is_call_center")
    public Integer getIsCallCenter() {
        return isCallCenter;
    }

    public void setIsCallCenter(Integer isCallCenter) {
        this.isCallCenter = isCallCenter;
    }
    @Column(name = "service_type")
    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    @Column(name = "industry")
    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }
    @Column(name = "white_list")
    public String getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(String whiteList) {
        this.whiteList = whiteList;
    }
    @Column(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    @Column(name = "is_auth")
    public Integer getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(Integer isAuth) {
        this.isAuth = isAuth;
    }
    @Column(name = "is_voice_directly")
    public Integer getIsVoiceDirectly() {
        return isVoiceDirectly;
    }

    public void setIsVoiceDirectly(Integer isVoiceDirectly) {
        this.isVoiceDirectly = isVoiceDirectly;
    }
    @Column(name = "is_voice_callback")
    public Integer getIsVoiceCallback() {
        return isVoiceCallback;
    }

    public void setIsVoiceCallback(Integer isVoiceCallback) {
        this.isVoiceCallback = isVoiceCallback;
    }

    @Column(name = "is_session_service")
    public Integer getIsSessionService() {
        return isSessionService;
    }

    public void setIsSessionService(Integer isSessionService) {
        this.isSessionService = isSessionService;
    }
    @Column(name = "is_recording")
    public Integer getIsRecording() {
        return isRecording;
    }

    public void setIsRecording(Integer isRecording) {
        this.isRecording = isRecording;
    }
    @Column(name = "is_voice_validate")
    public Integer getIsVoiceValidate() {
        return isVoiceValidate;
    }

    public void setIsVoiceValidate(Integer isVoiceValidate) {
        this.isVoiceValidate = isVoiceValidate;
    }
    @Column(name = "is_ivr_service")
    public Integer getIsIvrService() {
        return isIvrService;
    }

    public void setIsIvrService(Integer isIvrService) {
        this.isIvrService = isIvrService;
    }

    @ManyToOne
    @JoinColumn(name = "area_id")
    @NotFound(action = NotFoundAction.IGNORE)
    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }
}
