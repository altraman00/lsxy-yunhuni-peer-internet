package com.lsxy.framework.consume.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.consume.model.ConsumeMonth;
import com.lsxy.framework.api.consume.service.ConsumeMonthService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.consume.dao.ConsumeMonthDao;
import com.lsxy.framework.core.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 消费月统计serviceimpl
 * Created by zhangxb on 2016/7/6.
 */
@Service
public class ConsumeMonthServiceImpl extends AbstractService<ConsumeMonth> implements ConsumeMonthService{
    @Autowired
    ConsumeMonthDao consumeMonthDao;
    @Autowired
    TenantService tenantService;
    @Override
    public BaseDaoInterface<ConsumeMonth, Serializable> getDao() {
        return consumeMonthDao;
    }
    @Override
    public Page<ConsumeMonth> pageList(String userName, String appId, String startTime, String endTime, Integer pageNo, Integer pageSize) {
        Tenant tenant = tenantService.findTenantByUserName(userName);
        Page<ConsumeMonth> page = null;
        if("0".equals(appId)){//表示查询全部
            String hql = "from ConsumeMonth obj where obj.tenantId=?1 and ( DATE_FORMAT(obj.dt,'%Y')=?2 or DATE_FORMAT(obj.dt,'%Y')=?3 ) ORDER BY obj.dt,obj.month";
            page = this.pageList(hql,pageNo,pageSize,tenant.getId(),endTime,startTime);
        }else{
            String hql = "from ConsumeMonth obj where obj.tenantId=?1 and obj.appId=?2 and ( DATE_FORMAT(obj.dt,'%Y')=?3  or DATE_FORMAT(obj.dt,'%Y')=?4 )ORDER BY obj.dt,obj.month";
            page = this.pageList(hql,pageNo,pageSize,tenant.getId(),appId,endTime,startTime);
        }
        return page;
    }

    @Override
    public List<ConsumeMonth> list(String userName, String appId, String startTime) {
        Tenant tenant = tenantService.findTenantByUserName(userName);
        List<ConsumeMonth> list = null;
        if("0".equals(appId)){//表示查询全部
            String hql = "from ConsumeMonth obj where obj.tenantId=?1 and DATE_FORMAT(obj.dt,'%Y')=?2 ORDER BY obj.month";
            list = this.findByCustomWithParams(hql, tenant.getId(),startTime);
        }else{
            String hql = "from ConsumeMonth obj where obj.tenantId=?1 and obj.appId=?2 and DATE_FORMAT(obj.dt,'%Y')=?3 ORDER BY obj.month";
            list = this.findByCustomWithParams(hql, tenant.getId(),appId,startTime);
        }
        return list;
    }
}
