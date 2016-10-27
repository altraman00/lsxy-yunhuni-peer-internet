package com.lsxy.yunhuni.config.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.config.model.LineGatewayToTenant;
import com.lsxy.yunhuni.api.config.service.LineGatewayToTenantService;
import com.lsxy.yunhuni.config.dao.LineGatewayToTenantDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liups on 2016/10/27.
 */
public class LineGatewayToTenantServiceImpl extends AbstractService<LineGatewayToTenant> implements LineGatewayToTenantService {

    @Autowired
    LineGatewayToTenantDao lineGatewayToTenantDao;
    @Override

    public BaseDaoInterface<LineGatewayToTenant, Serializable> getDao() {
        return this.lineGatewayToTenantDao;
    }


    @Override
    public List<LineGateway> findByTenantId(String tenantId) {
        List<LineGateway> lineGateways = new ArrayList<>();
        List<LineGatewayToTenant> list =  lineGatewayToTenantDao.findByTenantId(tenantId);
        if(list != null && list.size() > 0){
            for(LineGatewayToTenant ltt:list){
                lineGateways.add(ltt.getLineGateway());
            }
            return lineGateways;
        }else{
            return null;
        }
    }
}
