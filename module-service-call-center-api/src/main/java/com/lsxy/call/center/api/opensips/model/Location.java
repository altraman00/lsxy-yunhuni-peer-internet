package com.lsxy.call.center.api.opensips.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * opensips 注册信息
 * Created by liups on 2016/11/24.
 */
@Entity
@Table(schema="opensips",name = "location")
public class Location implements Serializable {
    private Long contactId;
    private String username;
    private String domain;
    private String contact;
    private String received;
    private String path;
    private Date expires;
    private Double q;
    private String callid;
    private Integer cseq;
    private Date lastModified;
    private Integer flags;
    private String cflags;
    private String userAgent;
    private String socket;
    private Integer methods;
    private String sipInstance;
    private String attr;

    @Id
    @Column(name="contact_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @GenericGenerator(name = "paymentableGenerator", strategy = "native")
    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    @Column(name="username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name="domain")
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Column(name="contact")
    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Column(name="received")
    public String getReceived() {
        return received;
    }

    public void setReceived(String received) {
        this.received = received;
    }

    @Column(name="path")
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Column(name="expires")
    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    @Column(name="q")
    public Double getQ() {
        return q;
    }

    public void setQ(Double q) {
        this.q = q;
    }

    @Column(name="callid")
    public String getCallid() {
        return callid;
    }

    public void setCallid(String callid) {
        this.callid = callid;
    }

    @Column(name="cseq")
    public Integer getCseq() {
        return cseq;
    }

    public void setCseq(Integer cseq) {
        this.cseq = cseq;
    }

    @Column(name="last_modified")
    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @Column(name="flags")
    public Integer getFlags() {
        return flags;
    }

    public void setFlags(Integer flags) {
        this.flags = flags;
    }

    @Column(name="cflags")
    public String getCflags() {
        return cflags;
    }

    public void setCflags(String cflags) {
        this.cflags = cflags;
    }

    @Column(name="user_agent")
    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Column(name="socket")
    public String getSocket() {
        return socket;
    }

    public void setSocket(String socket) {
        this.socket = socket;
    }

    @Column(name="methods")
    public Integer getMethods() {
        return methods;
    }

    public void setMethods(Integer methods) {
        this.methods = methods;
    }

    @Column(name="sip_instance")
    public String getSipInstance() {
        return sipInstance;
    }

    public void setSipInstance(String sipInstance) {
        this.sipInstance = sipInstance;
    }

    @Column(name="attr")
    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }
}
