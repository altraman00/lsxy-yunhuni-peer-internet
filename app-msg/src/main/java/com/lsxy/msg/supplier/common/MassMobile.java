package com.lsxy.msg.supplier.common;

import java.util.List;

/**
 * Created by zhangxb on 2017/1/4.
 */
public class MassMobile {
    private List<String> allNum ;
    //联通号码
    private List<String> unicom ;
    //移动号码
    private List<String> mobile ;
    //电信号码
    private List<String> telecom ;
    //无效号码
    private List<String> no ;
    //总号码数
    private int count;
    //有效号码个数
    private int countVaild;
    public MassMobile(List<String> allNum, List<String> unicom, List<String> mobile, List<String> telecom, List<String> no) {
        this.allNum = allNum;
        this.count = allNum.size();
        this.unicom = unicom;
        this.mobile = mobile;
        this.telecom = telecom;
        this.no = no;
        this.countVaild = unicom.size() + mobile.size() + telecom.size();
    }
    public MassMobile(int countVaild) {
        this.countVaild = countVaild;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<String> getUnicom() {
        return unicom;
    }

    public void setUnicom(List<String> unicom) {
        this.unicom = unicom;
    }

    public List<String> getMobile() {
        return mobile;
    }

    public void setMobile(List<String> mobile) {
        this.mobile = mobile;
    }

    public List<String> getTelecom() {
        return telecom;
    }

    public void setTelecom(List<String> telecom) {
        this.telecom = telecom;
    }

    public List<String> getNo() {
        return no;
    }

    public void setNo(List<String> no) {
        this.no = no;
    }

    public int getCountVaild() {
        return countVaild;
    }

    public void setCountVaild(int countVaild) {
        this.countVaild = countVaild;
    }

}
