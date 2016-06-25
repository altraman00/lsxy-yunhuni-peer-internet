package com.lsxy.app.portal.rest.console.account;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/25.
 */
public class InformationEditVo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String industry;
    private String business;
    private String url;
    private String province;
    private String city;
    private String address;
    private String mobile;

    public InformationEditVo() {

    }

    public InformationEditVo(String industry, String business, String url, String province, String city, String address, String mobile) {
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
        return "InformationEditVo{" +
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
