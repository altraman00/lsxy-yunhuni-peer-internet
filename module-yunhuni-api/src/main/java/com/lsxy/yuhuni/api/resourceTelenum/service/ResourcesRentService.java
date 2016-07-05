package com.lsxy.yuhuni.api.resourceTelenum.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yuhuni.api.resourceTelenum.model.ResourcesRent;

/**
 * 租户号码租用service
 * Created by zhangxb on 2016/7/1.
 */
public interface ResourcesRentService extends BaseService<ResourcesRent> {
    /**
     * 根据租户id获取租户下的全部租用号码
     * @param tenantId 租户id
     * @return
     */
    public Page<ResourcesRent> pageListByTenantId(String tenantId,int pageNo, int pageSize) throws MatchMutiEntitiesException;
}
