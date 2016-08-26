package com.lsxy.yunhuni.api.config.model;

import com.lsxy.framework.api.base.IdEntity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 线路网关
 * Created by liups on 2016/8/24.
 */
@Entity
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_yy_config_line_gateway")
public class LineGateway extends IdEntity {
    private Area area;          //区域
    private String lineNumber;  //线路网关编码
    private BigDecimal lingPrice;   //线路网关单价
    private String ip;          //ip
    private String port;        //端口
    private String provider;    //线路网关提供商
    private String remark;

    @ManyToOne
    @JoinColumn(name = "area_id")
    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    @Column(name = "line_number")
    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    @Column(name = "line_price")
    public BigDecimal getLingPrice() {
        return lingPrice;
    }

    public void setLingPrice(BigDecimal lingPrice) {
        this.lingPrice = lingPrice;
    }

    @Column(name = "ip")
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Column(name = "port")
    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Column(name = "provider")
    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
