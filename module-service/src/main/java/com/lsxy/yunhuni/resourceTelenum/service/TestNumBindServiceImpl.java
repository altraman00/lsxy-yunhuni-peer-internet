package com.lsxy.yunhuni.resourceTelenum.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.resourceTelenum.model.TestNumBind;
import com.lsxy.yunhuni.api.resourceTelenum.service.TestNumBindService;
import com.lsxy.yunhuni.resourceTelenum.dao.TestNumBindDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试绑定号码service
 * Created by zhangxb on 2016/7/2.
 */
@Service
public class TestNumBindServiceImpl extends AbstractService<TestNumBind> implements TestNumBindService {
    @Autowired
    private TestNumBindDao testNumBindDao;
    @Autowired
    private TenantService tenantService;
    @Override
    public BaseDaoInterface<TestNumBind, Serializable> getDao() {
       return this.testNumBindDao;
    }

    @Override
    public List<TestNumBind> findByNumber(String userName, String number)  {
        Tenant tenant = tenantService.findTenantByUserName(userName) ;
        String hql = "from TestNumBind obj where obj.number=?1 ";
        List<TestNumBind> list = this.findByCustomWithParams(hql, number);
        return list;
    }

    @Override
    public List<TestNumBind> findAll(String userName)  {
        Tenant tenant = tenantService.findTenantByUserName(userName) ;
        String hql = "from TestNumBind obj where obj.tenant.id=?1 ";
        List<TestNumBind> list = this.findByCustomWithParams(hql, tenant.getId());
        return list;
    }

    @Override
    public List<TestNumBind> findByTenant(String tenant,String appId) {
        String hql = "from TestNumBind obj where obj.tenant.id=?1 and obj.app.id=?2 ";
        List<TestNumBind> list = this.findByCustomWithParams(hql, tenant,appId);
        return list;
    }

    @Override
    public TestNumBind findByNumber(String number)  {
        String hql = "from TestNumBind obj where obj.number=?1 ";
        List<TestNumBind> list = this.findByCustomWithParams(hql, number);
        if(list!=null && list.size()> 0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<String> findNumByAppId(String appId) {
        List<String> nums = new ArrayList<>();
        List<TestNumBind> result = testNumBindDao.findByAppId(appId);
        if(result != null && result.size()>0){
            for(TestNumBind testNumBind : result){
                nums.add(testNumBind.getNumber());
            }
        }
        return nums;
    }
}
