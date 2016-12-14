package com.lsxy.yunhuni.config.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.config.model.LineGatewayToTenant;
import com.lsxy.yunhuni.api.config.service.LineGatewayService;
import com.lsxy.yunhuni.api.config.service.LineGatewayToTenantService;
import com.lsxy.yunhuni.config.dao.LineGatewayToTenantDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
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
    @Autowired
    LineGatewayService lineGatewayService;
    @Override
    public BaseDaoInterface<LineGatewayToTenant, Serializable> getDao() {
        return this.lineGatewayToTenantDao;
    }

    @Override
    public LineGatewayToTenant findById(String id) {
        return getDao().findOne(id);
    }

    @Override
    public List<LineGateway> findByTenantIdAndAreaId(String tenantId,String areaId) {
        List<LineGatewayToTenant> list =  lineGatewayToTenantDao.findByTenantIdAndLineGateway_AreaId(tenantId,areaId);
        return getLineGateways(list);
    }

    private List<LineGateway> getLineGateways(List<LineGatewayToTenant> list) {
        List<LineGateway> lineGateways = new ArrayList<>();
        if(list != null && list.size() > 0){
            for(LineGatewayToTenant ltt:list){
                LineGateway lineGateway = ltt.getLineGateway();
                //TODO 判断线路是否可用
                if(LineGateway.STATUS_USABLE.equals(lineGateway.getStatus())){
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
    public List<LineGateway> findLineGatewayByTenantId(String tenantId) {
        List<LineGatewayToTenant> list =  lineGatewayToTenantDao.findByTenantId(tenantId);
        return getLineGateways(list);
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
    public int getMaxPriority(String tenantId) {
        String sql = " SELECT IFNULL(MAX(priority),0) FROM db_lsxy_bi_yunhuni.tb_oc_linegateway_to_tenant WHERE deleted=0 and tenant_id=?";
        int result = jdbcTemplate.queryForObject(sql,Integer.class,tenantId);
        return result;
    }

    @Override
    public void deleteLine(String line) {
        String sql =  " update  db_lsxy_bi_yunhuni.tb_oc_linegateway_to_tenant  set deleted=1 where deleted=0 and line_id='"+line+"'  ";
        jdbcTemplate.update(sql);
    }

    @Override
    public int upPriority(int o1, int o2, String line) {
        String flag = "+1";
        int begin = -1;
        int end = -1;
        if(o1<o2){
            begin = o1+1;
            end = o2;
            flag = "-1";
        }else{
            begin = o2;
            end = o1-1;
            flag = "+1";
        }
        String[] sql = new String[2];
        sql[0] = " UPDATE db_lsxy_bi_yunhuni.tb_oc_linegateway_to_tenant SET priority=priority"+flag+" WHERE deleted=0 AND priority BETWEEN "+begin+" AND "+end+" ";
        if(StringUtils.isNotEmpty(line)) {
            sql[1] = " UPDATE db_lsxy_bi_yunhuni.tb_oc_linegateway_to_tenant SET priority=" + o2 + " WHERE deleted=0 AND id ='" + line + "' ";
        }
        int re = lineGatewayService.batchModify(sql);
        return re;
    }

    @Override
    public void removeTenantLine(String id,String tenantId) {
        LineGatewayToTenant lineGatewayToTenant = this.findById(id);
        //删除线路关系
        try {
            this.delete(lineGatewayToTenant);
        } catch (Exception e) {
            throw new RuntimeException("删除失败");
        }
        //修正优先级
        int o3 = this.getMaxPriority(tenantId);
        if(o3!=lineGatewayToTenant.getPriority()){
            int re = this.upPriority(lineGatewayToTenant.getPriority(),o3,null);
            if(re==-1){
                throw new RuntimeException("删除成功，修正失败，请手动修正");
            }
        }
    }


}
