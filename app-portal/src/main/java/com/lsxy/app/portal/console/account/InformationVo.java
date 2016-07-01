package com.lsxy.app.portal.console.account;

import com.lsxy.framework.api.tenant.model.Account;

import java.io.Serializable;

/**
 * Created by zhangxb on 2016/6/25.
 * 基本资料VO对象
 */
public class InformationVo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String industry; //行业
    private String business;//业务
    private String url;//网站
    private String province;//省份
    private String city;//城市
    private String address;//详细地址
    private String mobile;//手机号码

    public InformationVo() {

    }

    public InformationVo(Account account) {
        this.province = account.getProvince();
        this.industry = account.getIndustry();
        this.business = account.getBusiness();
        this.url = account.getUrl();
        this.city = account.getCity();
        this.address = account.getAddress();
        this.mobile = account.getPhone();
    }
    public InformationVo(String industry, String business, String url, String province, String city, String address, String mobile) {
        this.province = province;
        this.industry = industry;
        this.business = business;
        this.url = url;
        this.city = city;
        this.address = address;
        this.mobile = mobile;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getIndustry() {
        return industry;
    }

    public String getBusiness() {
        return business;
    }

    public String getUrl() {
        return url;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "InformationVo{" +
                "industry='" + industry + '\'' +
                ", business='" + business + '\'' +
                ", url='" + url + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
