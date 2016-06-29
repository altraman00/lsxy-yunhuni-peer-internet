package com.lsxy.framework.tenant.service;

import com.lsxy.framework.api.tenant.model.RealnameCorp;
import com.lsxy.framework.api.tenant.service.RealnameCorpService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.persistence.BaseDaoInterface;
import com.lsxy.framework.tenant.dao.RealnameCorpDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by zhangxb on 2016/6/29.
 * 租户实现类
 */
@Service
public class RealnameRocpServiceImpl extends AbstractService<RealnameCorp> implements RealnameCorpService {
    @Autowired
    private RealnameCorpDao realnameCorpDao;

    @Override
    public BaseDaoInterface<RealnameCorp, Serializable> getDao() {
        return realnameCorpDao;
    }


    @Override
    public RealnameCorp findByTenantId(String tenantId) {
        try {
            RealnameCorp realnameCorp =  realnameCorpDao.findByTenantId(tenantId);
            return realnameCorp;
        }catch(Exception e){
            return null;
        }
    }
}
