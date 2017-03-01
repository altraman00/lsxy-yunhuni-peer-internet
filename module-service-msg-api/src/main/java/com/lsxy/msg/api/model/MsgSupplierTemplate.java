package com.lsxy.msg.api.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by liups on 2017/3/1.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema = "db_lsxy_bi_yunhuni", name = "tb_bi_msg_supplier_template")
public class MsgSupplierTemplate extends IdEntity {
}
