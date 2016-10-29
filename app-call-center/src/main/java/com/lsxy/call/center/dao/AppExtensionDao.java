package com.lsxy.call.center.dao;

import com.lsxy.call.center.api.model.AppExtension;
import com.lsxy.framework.api.base.BaseDaoInterface;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangxb on 2016/10/21.
 */
public interface AppExtensionDao extends BaseDaoInterface<AppExtension, Serializable> {
    List<AppExtension> findByAppId(String appId);

    long countByUser(String user);

    AppExtension findByTenantIdAndAppIdAndUser(String tenantId,String appId,String user);

    @Modifying
    @Query("update AppExtension a set a.enabled=:enabled,a.agent=null where a.agent=:agent")
    public void updateByAgent(@Param("enabled") Integer enabled,@Param("agent") String agent);
}
