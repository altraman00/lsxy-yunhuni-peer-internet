package com.lsxy.yunhuni.config.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.config.model.LineGatewayToTenant;
import com.lsxy.yunhuni.api.config.service.LineGatewayToTenantService;
import com.lsxy.yunhuni.config.dao.LineGatewayToTenantDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liups on 2016/10/27.
 */
@Service
public class LineGatewayToTenantServiceImpl extends AbstractService<LineGatewayToTenant> implements LineGatewayToTenantService {

    @Autowired
    LineGatewayToTenantDao lineGatewayToTenantDao;
    @Override

    public BaseDaoInterface<LineGatewayToTenant, Serializable> getDao() {
        return this.lineGatewayToTenantDao;
    }


    @Override
    public List<LineGateway> findByTenantIdAndAreaId(String tenantId,String areaId) {
        List<LineGateway> lineGateways = new ArrayList<>();
        List<LineGatewayToTenant> list =  lineGatewayToTenantDao.findByTenantIdAndLineGateway_AreaId(tenantId,areaId);
        if(list != null && list.size() > 0){
            for(LineGatewayToTenant ltt:list){
                LineGateway lineGateway = ltt.getLineGateway();
                //TODO 判断线路是否可用
                if("1".equals(lineGateway.getStatus())){
                    lineGateway.setPriority(ltt.getPriority());
                    lineGateways.add(lineGateway);
                }
            }
            return lineGateways;
        }else{
            return null;
        }
    }
}
