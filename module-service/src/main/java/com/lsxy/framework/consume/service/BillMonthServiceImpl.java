package com.lsxy.framework.consume.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.consume.model.BillMonth;
import com.lsxy.framework.api.consume.service.BillMonthService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.consume.dao.BillMonthDao;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by liups on 2016/7/11.
 */
@Service
public class BillMonthServiceImpl extends AbstractService<BillMonth> implements BillMonthService {
    @Autowired
    BillMonthDao billMonthDao;

    @Autowired
    TenantService tenantService;
    @Override
    public BaseDaoInterface<BillMonth, Serializable> getDao() {
        return this.billMonthDao;
    }

    @Override
    public BillMonth getBillMonth(String userName, String appId, String month) {
        Tenant tenant = tenantService.findTenantByUserName(userName);
        Date date = DateUtils.parseDate(month, "yyyy-MM");
        if(StringUtils.isBlank(month)){
            throw new IllegalArgumentException("月份为空");
        }
//        if(StringUtil)
        return billMonthDao.findByTenantIdAndAppIdAndDt(tenant.getId(), appId, date);
    }
}
