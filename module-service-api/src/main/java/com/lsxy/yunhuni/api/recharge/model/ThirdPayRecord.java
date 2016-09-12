package com.lsxy.yunhuni.api.recharge.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 第三方支付记录表
 * Created by liups on 2016/7/2.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema = "db_lsxy_bi_yunhuni", name = "tb_bi_third_pay_record")
public class ThirdPayRecord extends IdEntity {
    private Recharge recharge;          //充值记录
    private String payType;             //支付类型,支付宝，银联等，参照RechargeType
    private String orderId;             //订单ID
    private String tradeNo;             //第三方支付平台的交易号
    private String tradeStatus;         //交易状态
    private BigDecimal totalFee;            //交易金额
    private String sellerId;            //卖家在第三方支付的账号ID
    private String buyerId;             //买家在第三方支付的账号ID
    private String sellerName;          //卖家在第三方支付的用户名
    private String buyerName;           //买家在第三方支付的用户名

    @OneToOne
    @JoinColumn(name = "recharge_id")
    public Recharge getRecharge() {
        return recharge;
    }

    public void setRecharge(Recharge recharge) {
        this.recharge = recharge;
    }

    @Column(name = "pay_type")
    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    @Column(name = "order_id")
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Column(name = "trade_no")
    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    @Column(name = "trade_status")
    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    @Column(name = "total_fee")
    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    @Column(name = "seller_id")
    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    @Column(name = "buyer_id")
    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    @Column(name = "seller_name")
    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    @Column(name = "buyer_name")
    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }
}
