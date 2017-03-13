package com.lsxy.app.portal.rest.stastistic.vo;


import java.util.Date;
import java.util.Random;

/**
 * Created by zhangxb on 2017/3/13.
 */
public class MsgStatisticsVo {
    private Long ussd;
    private Long ussdSucc;
    private Long ussdFail;
    private Long sms;
    private Long smsSucc;
    private Long smsFail;
    private Long total;
    private Long totalSucc;
    private Long totalFail;
    private Date date;
    private int num;
    public MsgStatisticsVo() {
    }

    public MsgStatisticsVo(Long ussd, Long ussdSucc, Long ussdFail, Long sms, Long smsSucc, Long smsFail, Long total, Long totalSucc, Long totalFail, Date date, int num) {
        this.ussd = ussd;
        this.ussdSucc = ussdSucc;
        this.ussdFail = ussdFail;
        this.sms = sms;
        this.smsSucc = smsSucc;
        this.smsFail = smsFail;
        this.total = total;
        this.totalSucc = totalSucc;
        this.totalFail = totalFail;
        this.date = date;
        this.num = num;
    }

    public static MsgStatisticsVo initMsgStatisticsVo(Date date,int num) {
        Integer max=1000;
        Integer min=0;
        Random random = new Random();
        Integer total = random.nextInt(max)%(max-min+1) + min;
        Integer ussd = total/2;
        Integer sms = total - ussd;
        Integer ussdSucc = random.nextInt(ussd)%(ussd-min+1) + min;
        Integer ussdFial = ussd - ussdSucc;
        Integer smsSucc = random.nextInt(sms)%(sms-min+1) + min;
        Integer smsFail = sms - smsSucc;
        Integer succ = ussdSucc + smsSucc;
        Integer fail = ussdFial + smsFail;
        return new MsgStatisticsVo(ussd.longValue(),ussdSucc.longValue(),ussdFial.longValue(),sms.longValue(),smsSucc.longValue(),smsFail.longValue(),total.longValue(),succ.longValue(),fail.longValue(), date,num);
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Long getUssd() {
        return ussd;
    }

    public void setUssd(Long ussd) {
        this.ussd = ussd;
    }

    public Long getUssdSucc() {
        return ussdSucc;
    }

    public void setUssdSucc(Long ussdSucc) {
        this.ussdSucc = ussdSucc;
    }

    public Long getUssdFail() {
        return ussdFail;
    }

    public void setUssdFail(Long ussdFail) {
        this.ussdFail = ussdFail;
    }

    public Long getSms() {
        return sms;
    }

    public void setSms(Long sms) {
        this.sms = sms;
    }

    public Long getSmsSucc() {
        return smsSucc;
    }

    public void setSmsSucc(Long smsSucc) {
        this.smsSucc = smsSucc;
    }

    public Long getSmsFail() {
        return smsFail;
    }

    public void setSmsFail(Long smsFail) {
        this.smsFail = smsFail;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getTotalSucc() {
        return totalSucc;
    }

    public void setTotalSucc(Long totalSucc) {
        this.totalSucc = totalSucc;
    }

    public Long getTotalFail() {
        return totalFail;
    }

    public void setTotalFail(Long totalFail) {
        this.totalFail = totalFail;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
