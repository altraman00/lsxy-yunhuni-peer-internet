package com.lsxy.framework.consume.service;


import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.consume.model.ConsumeDay;
import com.lsxy.framework.api.consume.service.ConsumeDayService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.consume.dao.ConsumeDayDao;
import com.lsxy.framework.core.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 消费日统计serviceimpl
 * Created by zhangxb on 2016/7/6.
 */
@Service
public class ConsumeDayServiceImpl extends AbstractService<ConsumeDay> implements ConsumeDayService {
    @Autowired
    ConsumeDayDao consumeDayDao;
    @Autowired
    TenantService tenantService;
    @Override
    public BaseDaoInterface<ConsumeDay, Serializable> getDao() {
        return consumeDayDao;
    }

    @Override
    public Page<ConsumeDay> pageList(String userName, String appId, String startTime, String endTime,Integer pageNo,Integer pageSize) {
        Tenant tenant = tenantService.findTenantByUserName(userName);
        Page<ConsumeDay> page = null;
        if("0".equals(appId)){//表示查询全部
            String hql = "from ConsumeDay obj where obj.tenantId=?1 and DATE_FORMAT(obj.dt,'%Y-%m')=?2 or DATE_FORMAT(obj.dt,'%Y-%m')=?3 ORDER BY obj.dt,obj.day";
            page =  this.pageList(hql,pageNo,pageSize,tenant.getId(),endTime,startTime);
        }else{
            String hql = "from ConsumeDay obj where obj.tenantId=?1 and obj.appId=?2 and DATE_FORMAT(obj.dt,'%Y-%m')=?3  or DATE_FORMAT(obj.dt,'%Y-%m')=?4 ORDER BY obj.dt,obj.day";
            page =  this.pageList(hql,pageNo,pageSize,tenant.getId(),appId,endTime,startTime);
        }
        return page;
    }

    @Override
    public List<ConsumeDay> list(String userName, String appId, String startTime) {
        Tenant tenant = tenantService.findTenantByUserName(userName);
        List<ConsumeDay> list = null;
        if("0".equals(appId)){//表示查询全部
            String hql = "from ConsumeDay obj where obj.tenantId=?1 and DATE_FORMAT(obj.dt,'%Y-%m')=?2 ORDER BY obj.day";
            list = this.findByCustomWithParams(hql, tenant.getId(),startTime);
        }else{
            String hql = "from ConsumeDay obj where obj.tenantId=?1 and obj.appId=?2 and DATE_FORMAT(obj.dt,'%Y-%m')=?3 ORDER BY obj.day";
            list = this.findByCustomWithParams(hql, tenant.getId(),appId,startTime);
        }
        return list;
    }
}
