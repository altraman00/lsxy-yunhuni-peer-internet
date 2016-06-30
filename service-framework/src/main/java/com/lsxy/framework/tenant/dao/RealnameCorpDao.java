package com.lsxy.framework.tenant.dao;


import com.lsxy.framework.api.tenant.model.RealnameCorp;
import com.lsxy.framework.core.persistence.BaseDaoInterface;

import java.io.Serializable;

/**
 * Created by zhagnxb on 2016/6/29.
 * 实名认证 企业DAO类
 */
public interface RealnameCorpDao extends BaseDaoInterface<RealnameCorp, Serializable> {
    public RealnameCorp findByTenantId(String tenantId) ;
}
