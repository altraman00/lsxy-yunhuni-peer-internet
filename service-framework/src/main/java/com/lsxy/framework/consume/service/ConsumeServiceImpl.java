package com.lsxy.framework.consume.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.consume.model.Consume;
import com.lsxy.framework.api.consume.service.ConsumeService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.consume.dao.ConsumeDao;
import com.lsxy.framework.core.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 消费记录ServiceImpl
 * Created by zhangxb on 2016/7/8.
 */
@Service
public class ConsumeServiceImpl extends AbstractService<Consume> implements ConsumeService {
    @Autowired
    ConsumeDao consumeDao;
    @Autowired
    TenantService tenantService;
    @Override
    public BaseDaoInterface<Consume, Serializable> getDao() {
        return consumeDao;
    }

    @Override
    public Page<Consume> pageList(String userName,Integer pageNo, Integer pageSize,String startTime,String endTime) {
        Tenant tenant = tenantService.findTenantByUserName(userName);
        String hql = "from Consume obj where obj.tenant.id=?1 and ( DATE_FORMAT(obj.dt,'%Y-%m')<=?2 and DATE_FORMAT(obj.dt,'%Y-%m')>=?3 )  ORDER BY obj.dt";
        Page<Consume> page = this.pageList(hql,pageNo,pageSize,tenant.getId(),endTime,startTime);
        return page;
    }
}
