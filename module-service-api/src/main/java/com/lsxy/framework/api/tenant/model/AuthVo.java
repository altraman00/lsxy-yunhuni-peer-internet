package com.lsxy.framework.api.tenant.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhangxb on 2016/9/9.
 */
@Entity()
@SqlResultSetMapping(name="authResult",
        entities= {
                @EntityResult(
                        entityClass=AuthVo.class,
                        fields= {
                                @FieldResult(name="uid", column="uid"),
                                @FieldResult(name="date", column="date"),
                                @FieldResult(name="name", column="name"),
                                @FieldResult(name="tenantId", column="tenantId"),
                                @FieldResult(name="email", column="email"),
                                @FieldResult(name="phone", column="phone"),
                                @FieldResult(name="type", column="type")
                        })
        }
)
public class AuthVo implements Serializable {
    @JsonProperty("uid")
    @Id
    private String uid;
    @JsonProperty("date")
    private Date date;
    @JsonProperty("name")
    private String name;
    @JsonProperty("tenantId")
    private String tenantId;
    @JsonProperty("email")
    private String email;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("type")
    private Integer type;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
