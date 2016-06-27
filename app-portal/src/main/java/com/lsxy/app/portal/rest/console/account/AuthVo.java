package com.lsxy.app.portal.rest.console.account;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/25.
 */
public class AuthVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idType;//证件类型
    private String privatename;//用户名字
    private String  id_number;//身份证号
    private String  id_photo;//身份证照片
    private String  privateenant_id;//认证租户

    private String corpName;//企业名称
    private String addr;//企业地址
    private String field_code;//所属行业

    private String auth_type;//认证类型
    private String type01_prop01;//[一照一码]营业执照照片
    private String type01_prop02;//[一照一码]统一社会信用代码
    private String type02_prop01;//[三证合一]注册号
    private String type02_prop02;//[三证合一]税务登记证号

    private String type03_prop02;//[三证分离]税务登记证照片


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getPrivatename() {
        return privatename;
    }

    public void setPrivatename(String privatename) {
        this.privatename = privatename;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getId_photo() {
        return id_photo;
    }

    public void setId_photo(String id_photo) {
        this.id_photo = id_photo;
    }

    public String getPrivateenant_id() {
        return privateenant_id;
    }

    public void setPrivateenant_id(String privateenant_id) {
        this.privateenant_id = privateenant_id;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getField_code() {
        return field_code;
    }

    public void setField_code(String field_code) {
        this.field_code = field_code;
    }

    public String getAuth_type() {
        return auth_type;
    }

    public void setAuth_type(String auth_type) {
        this.auth_type = auth_type;
    }

    public String getType01_prop01() {
        return type01_prop01;
    }

    public void setType01_prop01(String type01_prop01) {
        this.type01_prop01 = type01_prop01;
    }

    public String getType01_prop02() {
        return type01_prop02;
    }

    public void setType01_prop02(String type01_prop02) {
        this.type01_prop02 = type01_prop02;
    }

    public String getType02_prop01() {
        return type02_prop01;
    }

    public void setType02_prop01(String type02_prop01) {
        this.type02_prop01 = type02_prop01;
    }

    public String getType02_prop02() {
        return type02_prop02;
    }

    public void setType02_prop02(String type02_prop02) {
        this.type02_prop02 = type02_prop02;
    }



    public String getType03_prop02() {
        return type03_prop02;
    }

    public void setType03_prop02(String type03_prop02) {
        this.type03_prop02 = type03_prop02;
    }

    @Override
    public String toString() {
        return "AuthVo{" +
                "idType='" + idType + '\'' +
                ", privatename='" + privatename + '\'' +
                ", id_number='" + id_number + '\'' +
                ", id_photo='" + id_photo + '\'' +
                ", privateenant_id='" + privateenant_id + '\'' +
                ", corpName='" + corpName + '\'' +
                ", addr='" + addr + '\'' +
                ", field_code='" + field_code + '\'' +
                ", auth_type='" + auth_type + '\'' +
                ", type01_prop01='" + type01_prop01 + '\'' +
                ", type01_prop02='" + type01_prop02 + '\'' +
                ", type02_prop01='" + type02_prop01 + '\'' +
                ", type02_prop02='" + type02_prop02 + '\'' +
                ", type03_prop02='" + type03_prop02 + '\'' +
                '}';
    }
}
