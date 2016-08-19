package com.lsxy.framework.tenant.dao;


import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.tenant.model.RealnamePrivate;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangxb on 2016/6/29.
 * 实名认证 个人DAO类
 */
public interface RealnamePrivateDao extends BaseDaoInterface<RealnamePrivate, Serializable> {
    public List<RealnamePrivate> findByTenantId(String tenantId) ;
    public RealnamePrivate findByTenantIdAndStatus(String tenantId,int status);

    public List<RealnamePrivate> findByStatusAndCreateTimeBetween(Integer status, Date d1, Date d2) ;
}
