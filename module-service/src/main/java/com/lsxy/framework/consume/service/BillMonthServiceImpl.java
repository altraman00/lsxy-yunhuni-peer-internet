package com.lsxy.framework.consume.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.consume.model.BillMonth;
import com.lsxy.framework.api.consume.service.BillMonthService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.consume.dao.BillMonthDao;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
    public List<BillMonth> getBillMonths(String userName, String appId, String month) {
        List<BillMonth> billMonths = null;
        Tenant tenant = tenantService.findTenantByUserName(userName);
        if(StringUtils.isBlank(month)){
            throw new IllegalArgumentException("月份为空");
        }
        Date dt = DateUtils.parseDate(month, "yyyy-MM");
        if(StringUtils.isBlank(appId)){
            try {
                String hql = "select new BillMonth(b.tenantId,b.dt,b.type,sum(b.amount)) " +
                         "from BillMonth b group by b.tenantId,b.dt,b.type having (b.tenantId=?1 and b.dt=?2)";
                Query query = this.getEm().createQuery(hql);
                query.setParameter(1, tenant.getId());
                query.setParameter(2,dt);
                billMonths = (List<BillMonth>) query.getResultList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            billMonths = billMonthDao.findByTenantIdAndAppIdAndDt(tenant.getId(), appId, dt);
        }

        return billMonths;
    }
}

