package com.lsxy.framework.tenant.dao;


import com.lsxy.framework.api.tenant.model.RealnameCorp;
import com.lsxy.framework.core.persistence.BaseDaoInterface;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhagnxb on 2016/6/29.
 * 实名认证 企业DAO类
 */
public interface RealnameCorpDao extends BaseDaoInterface<RealnameCorp, Serializable> {
    public List<RealnameCorp> findByTenantId(String tenantId) ;
    public RealnameCorp findByTenantIdAndStatus(String tenantId,int status);
}
