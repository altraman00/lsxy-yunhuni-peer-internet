package com.lsxy.yunhuni.api.statistics.model;


import com.lsxy.framework.core.utils.DateUtils;

import java.util.*;

/**
 * Created by zhangxb on 2017/3/13.
 */
public class MsgStatisticsVo {
    private Long ussd = 0L;
    private Long ussdSucc = 0L;
    private Long ussdFail = 0L;
    private Long sms = 0L;
    private Long smsSucc = 0L;
    private Long smsFail = 0L;
    private Long total = 0L;
    private Long totalSucc = 0L;
    private Long totalFail = 0L;
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

    public MsgStatisticsVo(Object obj){
        if(obj instanceof MsgDay){
            MsgDay msgDay = (MsgDay)obj;
            this.num = msgDay.getDay();
            this.date = msgDay.getDt();
            this.total = msgDay.getTotal();
            this.totalFail = msgDay.getFail();
            this.totalSucc = msgDay.getSuccess();
            if("ussd".equals(msgDay.getType())){
                this.ussd = msgDay.getTotal();
                this.ussdFail = msgDay.getFail();
                this.ussdSucc = msgDay.getSuccess();
            }else if("sms".equals(msgDay.getType())){
                this.sms = msgDay.getTotal();
                this.smsFail = msgDay.getFail();
                this.smsSucc = msgDay.getSuccess();
            }
        }else if(obj instanceof MsgMonth){
            MsgMonth msgMonth = (MsgMonth)obj;
            this.num = msgMonth.getMonth();
            this.date = msgMonth.getDt();
            this.total = msgMonth.getTotal();
            this.totalFail = msgMonth.getFail();
            this.totalSucc = msgMonth.getSuccess();
            if("ussd".equals(msgMonth.getType())){
                this.ussd = msgMonth.getTotal();
                this.ussdFail = msgMonth.getFail();
                this.ussdSucc = msgMonth.getSuccess();
            }else if("sms".equals(msgMonth.getType())){
                this.sms = msgMonth.getTotal();
                this.smsFail = msgMonth.getFail();
                this.smsSucc = msgMonth.getSuccess();
            }
        }
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
    public static List<MsgStatisticsVo> initMsgStatisticsVos(Date date, int type,Map<Object,MsgStatisticsVo> map) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int len = 0;
        if(type==1){
            len = Integer.valueOf(DateUtils.getLastDate(date).split("-")[2]);
        }else{
            len = 12;
        }
        List<MsgStatisticsVo> list = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            MsgStatisticsVo msgVo = map.get( (i+1) );
            if(msgVo==null){
                msgVo = new MsgStatisticsVo();
                msgVo.setNum( (i+1) );
                msgVo.setDate( calendar.getTime() );
            }
            if(type==1){
                calendar.add(Calendar.DAY_OF_MONTH,1);
            }else{
                calendar.add(Calendar.MONTH,1);
            }
            list.add(msgVo);
        }
        return list;
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

    public void add(Object obj){
        if(obj instanceof MsgDay){
            MsgDay msgDay = (MsgDay)obj;
            this.total = this.total + msgDay.getTotal();
            this.totalFail = this.totalFail + msgDay.getFail();
            this.totalSucc = this.totalSucc + msgDay.getSuccess();
            if("ussd".equals(msgDay.getType())){
                this.ussd = msgDay.getTotal();
                this.ussdFail = msgDay.getFail();
                this.ussdSucc = msgDay.getSuccess();
            }else if("sms".equals(msgDay.getType())){
                this.sms = msgDay.getTotal();
                this.smsFail = msgDay.getFail();
                this.smsSucc = msgDay.getSuccess();
            }
        }else if(obj instanceof MsgMonth){
            MsgMonth msgMonth = (MsgMonth)obj;
            this.total = this.total + msgMonth.getTotal();
            this.totalFail = this.totalFail + msgMonth.getFail();
            this.totalSucc = this.totalSucc + msgMonth.getSuccess();
            if("ussd".equals(msgMonth.getType())){
                this.ussd = msgMonth.getTotal();
                this.ussdFail = msgMonth.getFail();
                this.ussdSucc = msgMonth.getSuccess();
            }else if("sms".equals(msgMonth.getType())){
                this.sms = msgMonth.getTotal();
                this.smsFail = msgMonth.getFail();
                this.smsSucc = msgMonth.getSuccess();
            }
        }
    }
}
