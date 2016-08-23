package com.lsxy.app.oc.rest.tenant.vo;

public class PrivateAuthInfoVO extends AuthInfoVO {

    private String name;//姓名
    private String idNumber;//'身份证号
    private String idPhoto;//身份证照片
    private String idType;//认证证件类型

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getIdPhoto() {
        return idPhoto;
    }

    public void setIdPhoto(String idPhoto) {
        this.idPhoto = idPhoto;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }
}
