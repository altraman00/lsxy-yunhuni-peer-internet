package com.lsxy.framework.tenant.dao;


import com.lsxy.framework.api.tenant.model.RealnamePrivate;
import com.lsxy.framework.core.persistence.BaseDaoInterface;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangxb on 2016/6/29.
 * 实名认证 个人DAO类
 */
public interface RealnamePrivateDao extends BaseDaoInterface<RealnamePrivate, Serializable> {
    public List<RealnamePrivate> findByTenantId(String tenantId) ;
    public RealnamePrivate findByTenantIdAndStatus(String tenantId,int status);
}
