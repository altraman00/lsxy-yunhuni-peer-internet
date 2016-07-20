package com.lsxy.yunhuni.resourceTelenum.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourcesRent;

import java.io.Serializable;
import java.util.List;

/**
 * 呼入号码DAO
 * Created by zhangxb on 2016/7/1.
 */
public interface ResourcesRentDao extends BaseDaoInterface<ResourcesRent, Serializable> {

    ResourcesRent findByAppId(String appId);

    /**
     * 根据资源ID和状态获取资源租用关系
     * @param id
     * @param status
     * @return
     */
    ResourcesRent findByResourceTelenumIdAndRentStatus(String id, int status);

    /**
     * 根据状态获取租户的号码
     * @param id
     * @param status
     * @return
     */
    List<ResourcesRent> findByTenantIdAndRentStatus(String id, int status);
}
