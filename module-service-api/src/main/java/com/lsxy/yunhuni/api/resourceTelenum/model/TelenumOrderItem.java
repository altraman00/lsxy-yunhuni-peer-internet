package com.lsxy.yunhuni.api.resourceTelenum.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 号码订单子项目
 * Created by zhangxb on 2016/11/3.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema = "db_lsxy_bi_yunhuni",name="tb_bi_telenum_order_item")
public class TelenumOrderItem extends IdEntity {
    private String tenantId;//所属租户
    private String telnumOrderId;//所属订单
    private ResourceTelenum telnum;//所属号码
    private BigDecimal amount;//费用
    @Column(name = "tenant_id")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    @Column(name = "telnum_order_id")
    public String getTelnumOrderId() {
        return telnumOrderId;
    }

    public void setTelnumOrderId(String telnumOrderId) {
        this.telnumOrderId = telnumOrderId;
    }
    @OneToOne
    @JoinColumn(name = "telnum_id")
    @NotFound(action = NotFoundAction.IGNORE)
    public ResourceTelenum getTelnum() {
        return telnum;
    }

    public void setTelnum(ResourceTelenum telnum) {
        this.telnum = telnum;
    }
    @Column(name = "amount")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
