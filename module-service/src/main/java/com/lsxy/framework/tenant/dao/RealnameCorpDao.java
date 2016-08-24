package com.lsxy.framework.tenant.dao;


import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.tenant.model.RealnameCorp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by zhagnxb on 2016/6/29.
 * 实名认证 企业DAO类
 */
public interface RealnameCorpDao extends BaseDaoInterface<RealnameCorp, Serializable> {
    public List<RealnameCorp> findByTenantId(String tenantId) ;
    public RealnameCorp findByTenantIdAndStatus(String tenantId,int status);

    public List<RealnameCorp> findByStatusAndCreateTimeBetween(Integer status, Date d1, Date d2) ;
}
