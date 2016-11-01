package com.lsxy.yunhuni.config.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.config.model.LineGatewayToTenant;
import com.lsxy.yunhuni.api.config.service.LineGatewayToTenantService;
import com.lsxy.yunhuni.config.dao.LineGatewayToTenantDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
    private JdbcTemplate jdbcTemplate;
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

    @Override
    public Page<LineGatewayToTenant> getPage(String tenantId,Integer pageNo, Integer pageSize) {
        String hql = " FROM LineGatewayToTenant obj WHERE obj.tenantId='"+tenantId+"' ORDER BY obj.priority ";
        Page page = this.pageList(hql,pageNo,pageSize);
        return page;
    }

    @Override
    public long findByLineIdAndTenantId(String lineId, String tenantId) {
        String hql = " FROM LineGatewayToTenant obj where  obj.lineGateway.id='"+lineId+"' AND tenantId='"+tenantId+"' ";
        return this.countByCustom(hql);
    }
    @Override
    public int getMaxPriority() {
        String sql = " SELECT IFNULL(MAX(priority),0) FROM db_lsxy_bi_yunhuni.tb_oc_linegateway_to_tenant WHERE deleted=0";
        int result = jdbcTemplate.queryForObject(sql,Integer.class);
        return result;
    }
}
