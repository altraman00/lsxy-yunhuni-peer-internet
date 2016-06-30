package com.lsxy.framework.tenant.service;

import com.lsxy.framework.api.tenant.model.RealnamePrivate;
import com.lsxy.framework.api.tenant.service.RealnamePrivateService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.persistence.BaseDaoInterface;
import com.lsxy.framework.tenant.dao.RealnamePrivateDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by zhangxb on 2016/6/29.
 * 租户实现类
 */
@Service
public class RealnamePrivateServiceImpl extends AbstractService<RealnamePrivate> implements RealnamePrivateService {


    @Autowired
    private RealnamePrivateDao realnamePrivaateDao;

    @Override
    public BaseDaoInterface<RealnamePrivate, Serializable> getDao() {
        return realnamePrivaateDao;
    }


    @Override
    public RealnamePrivate findByTenantId(String tenantId) {
        try {
            RealnamePrivate realnamePrivate = realnamePrivaateDao.findByTenantId(tenantId);
            return realnamePrivate;
        }catch(Exception e){
            return null;
        }
    }

}
