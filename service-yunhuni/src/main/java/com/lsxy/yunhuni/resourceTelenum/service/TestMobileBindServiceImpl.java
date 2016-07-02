package com.lsxy.yunhuni.resourceTelenum.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.yuhuni.api.resourceTelenum.model.TestMobileBind;
import com.lsxy.yuhuni.api.resourceTelenum.service.TestMobileBindService;
import com.lsxy.yunhuni.resourceTelenum.dao.TestMobileBindDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * 测试绑定号码service
 * Created by zhangxb on 2016/7/2.
 */
@Service
public class TestMobileBindServiceImpl extends AbstractService<TestMobileBind> implements TestMobileBindService {
    @Autowired
    private TestMobileBindDao testMobileBindDao;
    @Autowired
    private TenantService tenantService;
    @Override
    public BaseDaoInterface<TestMobileBind, Serializable> getDao() {
       return this.testMobileBindDao;
    }

    @Override
    public List<TestMobileBind> findByNumber(String userName,String number) throws MatchMutiEntitiesException{
        Tenant tenant = tenantService.findTenantByUserName(userName) ;
        String hql = "from TestMobileBind obj where obj.number=?1 ";
        List<TestMobileBind> list = this.findByCustomWithParams(hql, number);
        return list;
    }

    @Override
    public List<TestMobileBind> findAll(String userName) throws MatchMutiEntitiesException{
        Tenant tenant = tenantService.findTenantByUserName(userName) ;
        String hql = "from TestMobileBind obj where obj.tenant.id=?1 ";
        List<TestMobileBind> list = this.findByCustomWithParams(hql, tenant.getId());
        return list;
    }
}
