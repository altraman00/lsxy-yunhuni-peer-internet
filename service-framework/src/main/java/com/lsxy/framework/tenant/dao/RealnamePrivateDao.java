package com.lsxy.framework.tenant.dao;


import com.lsxy.framework.api.tenant.model.RealnamePrivate;
import com.lsxy.framework.core.persistence.BaseDaoInterface;

import java.io.Serializable;

/**
 * Created by zhangxb on 2016/6/29.
 * 实名认证 个人DAO类
 */
public interface RealnamePrivateDao extends BaseDaoInterface<RealnamePrivate, Serializable> {
    public RealnamePrivate findByTenantId(String tenantId) ;
}
