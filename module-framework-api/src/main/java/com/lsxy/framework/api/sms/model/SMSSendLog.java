package com.lsxy.framework.api.sms.model;

import com.lsxy.framework.api.base.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Tandy on 2016/7/7.
 */
@Entity
@Table(schema = "db_lsxy_base", name = "tb_base_sms_send_log")
public class SMSSendLog  extends IdEntity {


    private String msgContent;


    private String smsClient;


    private String sendTo;


    public SMSSendLog(){

    }
    public SMSSendLog(String to, String content, String clientName) {
        this.sendTo = to;
        this.msgContent = content;
        this.smsClient = clientName;
    }

    @Column(name="sms_content")
    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    @Column(name="sms_client")
    public String getSmsClient() {
        return smsClient;
    }

    public void setSmsClient(String smsClient) {
        this.smsClient = smsClient;
    }

    @Column(name="send_to")
    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }
}
