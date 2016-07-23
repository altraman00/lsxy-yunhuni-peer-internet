package com.lsxy.framework.consume.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.consume.model.BillDay;
import com.lsxy.framework.api.consume.service.BillDayService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.consume.dao.BillDayDao;
import com.lsxy.framework.core.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by liups on 2016/7/22.
 */
@Service
public class BillDayServiceImpl extends AbstractService<BillDay> implements BillDayService {
    @Autowired
    BillDayDao billDayDao;

    @Autowired
    TenantService tenantService;

    @Override
    public BaseDaoInterface<BillDay, Serializable> getDao() {
        return this.billDayDao;
    }

    @Override
    public List<BillDay> getBillDays(String userName, String appId, String day) {
        List<BillDay> billDays = null;
        Tenant tenant = tenantService.findTenantByUserName(userName);
        if(StringUtils.isBlank(day)){
            throw new IllegalArgumentException("日期为空");
        }
        Date dt = DateUtils.parseDate(day, "yyyy-MM-dd");
        if(StringUtils.isBlank(appId)){
            try {
                String hql = "select new BillDay(b.tenantId,b.dt,b.type,sum(b.amount)) " +
                        "from BillDay b group by b.tenantId,b.dt,b.type having (b.tenantId=?1 and b.dt=?2)";
                Query query = this.getEm().createQuery(hql);
                query.setParameter(1, tenant.getId());
                query.setParameter(2,dt);
                billDays = (List<BillDay>) query.getResultList();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }else{
            billDays = billDayDao.findByTenantIdAndAppIdAndDt(tenant.getId(), appId, dt);
        }

        return billDays;
    }

}
