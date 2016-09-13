package com.lsxy.oc.api.user.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 运营后台用户
 * Created by liups on 2016/8/9.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema = "db_lsxy_bi_yunhuni", name = "tb_oc_user")
public class OcUser extends IdEntity {
    public static final int STATUS_NOT_ACTIVE = 0; 	//账号未激活
    public static final int STATUS_LOCK = 1; 		//账号锁定
    public static final int STATUS_NORMAL = 2; 		//账号正常
    public static final int STATUS_ABNORMAL = 3; 	//账号异常
    public static final int STATUS_EXPIRE = 4; 		//账号过期

    private String userName;
    private String realName;
    private String mobile;
    private String email;
    private String password;
    private String mm;
    private Integer status;

    @Column(name = "username")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "real_name")
    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Column(name = "mobile")
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "mm")
    public String getMm() {
        return mm;
    }

    public void setMm(String mm) {
        this.mm = mm;
    }

    @Column(name = "status")
    public Integer getStatus(){
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
