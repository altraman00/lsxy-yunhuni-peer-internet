package com.lsxy.framework.api.statistics.model;

import com.lsxy.framework.api.base.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 应用小时统计
 * Created by zhangxb on 2016/8/1.
 */
@Entity
@Table(schema="db_lsxy_base",name = "tb_base_app_hour")
public class AppHour extends IdEntity {
}
