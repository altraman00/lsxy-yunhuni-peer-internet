package com.lsxy.framework.tenant.dao;


import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.tenant.model.Tenant;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by liups on 2016/6/29.
 */
public interface TenantDao extends BaseDaoInterface<Tenant, Serializable> {
    int countByDeleted(boolean deleted);

    int countByDeletedAndCreateTimeBetween(boolean deleted, Date d1,Date d2);


    int countByIsRealAuthIn(Integer[] ss);

    int countByIsRealAuthInAndCreateTimeBetween(Integer[] ss, Date d1,Date d2);

    List<Tenant> findByIdIn(Collection<String> ids);
}
