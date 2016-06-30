package com.lsxy.framework.tenant.service;

import com.lsxy.framework.api.tenant.model.RealnamePrivate;
import com.lsxy.framework.api.tenant.service.RealnamePrivateService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.persistence.BaseDaoInterface;
import com.lsxy.framework.tenant.dao.RealnamePrivateDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangxb on 2016/6/29.
 * 租户实现类
 */
@Service
public class RealnamePrivateServiceImpl extends AbstractService<RealnamePrivate> implements RealnamePrivateService {
    private static final Logger logger = LoggerFactory.getLogger(RealnameRocpServiceImpl.class);

    @Autowired
    private RealnamePrivateDao realnamePrivaateDao;

    @Override
    public BaseDaoInterface<RealnamePrivate, Serializable> getDao() {
        return realnamePrivaateDao;
    }


    @Override
    public List<RealnamePrivate> findByTenantId(String tenantId) {
        try {
            return  realnamePrivaateDao.findByTenantId(tenantId);
        }catch(Exception e){
            logger.error("RealnamePrivateServiceImpl.findByTenantId:{}",e);
            return null;
        }
    }

    @Override
    public RealnamePrivate findByTenantIdAndStatus(String tenantId, int status) {
        try {
            return  realnamePrivaateDao.findByTenantIdAndStatus(tenantId,status);
        }catch(Exception e){
            logger.error("RealnamePrivateServiceImpl.findByTenantIdAndStatus:{}",e);
            return null;
        }
    }

}
