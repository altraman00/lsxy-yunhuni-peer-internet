package com.lsxy.yunhuni.resourceTelenum.service;


import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourcesRent;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import com.lsxy.yunhuni.resourceTelenum.dao.ResourcesRentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 租户号码租用service
 * Created by zhangxb on 2016/7/1.
 */
@Service
public class ResourcesRentServiceImpl extends AbstractService<ResourcesRent> implements ResourcesRentService {
    @Autowired
    public ResourcesRentDao resourcesRentDao;
    @Override
    public BaseDaoInterface<ResourcesRent, Serializable> getDao() {
        return this.resourcesRentDao;
    }
    @Autowired
    public TenantService tenantService;

    @Override
    public Page<ResourcesRent> pageListByTenantId(String userName,int pageNo, int pageSize)   {
        Tenant tenant = tenantService.findTenantByUserName(userName);
        String hql = "from ResourcesRent obj where obj.tenant.id=?1 and obj.rentStatus<>3 ";
        Page<ResourcesRent> page =  this.pageList(hql,pageNo,pageSize,tenant.getId());
        return page;
    }

    @Override
    public ResourcesRent findByAppId(String appId) {
        return resourcesRentDao.findByAppIdAndRentStatus(appId,ResourcesRent.RENT_STATUS_USING);
    }

    @Override
    public ResourcesRent findByResourceTelenumIdAndStatus(String id, int status) {
        return resourcesRentDao.findByResourceTelenumIdAndRentStatus(id,status);
    }

    @Override
    public String[] findOwnUnusedNum(Tenant tenant) {
        List<String> telNums = new ArrayList<>();
        List<ResourcesRent> list = resourcesRentDao.findByTenantIdAndRentStatus(tenant.getId(),ResourcesRent.RENT_STATUS_UNUSED);
        if(list != null && list.size()>0){
            for(ResourcesRent rent:list){
                String telNumber = rent.getResourceTelenum().getTelNumber();
                telNums.add(telNumber);
            }
        }
        return telNums.toArray(new String[]{});
    }
}
