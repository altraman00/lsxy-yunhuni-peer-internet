package com.lsxy.yunhuni.config.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.config.service.LineGatewayService;
import com.lsxy.yunhuni.api.resourceTelenum.model.TelnumToLineGateway;
import com.lsxy.yunhuni.api.resourceTelenum.service.TelnumToLineGatewayService;
import com.lsxy.yunhuni.config.dao.LineGatewayDao;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by liups on 2016/8/24.
 */
@Service
public class LineGatewayServiceImpl extends AbstractService<LineGateway> implements LineGatewayService {
    private static final Logger logger = LoggerFactory.getLogger(LineGatewayServiceImpl.class);

    @Autowired
    LineGatewayDao lineGatewayDao;
    @Autowired
    TelnumToLineGatewayService telnumToLineGatewayService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public BaseDaoInterface<LineGateway, Serializable> getDao() {
        return this.lineGatewayDao;
    }
    @Value(value = "${app.cc.opensips.ip}")
    private String sipIp;
    @Value(value = "${app.cc.opensips.domain}")
    private String sipDomain;


    @Override
    public Page<LineGateway> getPage(Integer pageNo,Integer pageSize,String operator, String isThrough, String status, String isPublicLine,String order) {
        String hql = " FROM LineGateway obj WHERE obj.deleted=0 ";
        if(StringUtils.isNotEmpty(operator)){
            hql += " AND obj.operator like '%"+operator+"%' ";
        }
        if(StringUtils.isNotEmpty(isThrough)){
            hql += " AND obj.isThrough = '"+isThrough+"' ";
        }
        if(StringUtils.isNotEmpty(status)){
            hql += " AND obj.status = '"+status+"' ";
        }
        if(StringUtils.isNotEmpty(isPublicLine)){
            hql += " AND obj.isPublicLine = '"+isPublicLine+"' ";
        }
        if(StringUtils.isNotEmpty(order)){
            String temp = " ORDER BY ";
            String[] o = order.split(";");
            for(int i=0;i<o.length;i++){
                String[] oo = o[i].split(":");
                if("1".equals(oo[1])){
                    temp += " obj."+oo[0]+" DESC ";
                }else if("0".equals(oo[1])){
                    temp += " obj."+oo[0]+" ";
                }
                if(i!=o.length-1){
                    temp += " , ";
                }
            }
            hql += temp;
        }else{
            hql += " ORDER BY obj.createTime DESC ";
        }
        Page page = this.pageList(hql,pageNo,pageSize);
        return page;
    }

    @Override
    public Page<LineGateway> getNotTenantPage(Integer pageNo, Integer pageSize, String tenantId,String operator, String line) {
        String hql = " FROM LineGateway obj WHERE obj.deleted=0 ";
        if(StringUtils.isNotEmpty(operator)){
            hql += " AND obj.operator like '%"+operator+"%' ";
        }
        if(StringUtils.isNotEmpty(line)){
            hql += " AND obj.lineNumber like '%"+line+"%' ";
        }
        hql += " AND obj.id NOT IN (select a.lineGateway.id from LineGatewayToTenant a where deleted=0 AND tenantId='"+tenantId+"') order by obj.createTime desc";
        Page page = this.pageList(hql,pageNo,pageSize);
        return page;
    }

    @Override
    public int batchModify(String[] sql) {
        int result = 0;
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(
                jdbcTemplate.getDataSource());
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            jdbcTemplate.batchUpdate(sql);
        } catch (Exception ex) {
            result = -1;
            transactionManager.rollback(status);
        } finally {
            if(result == 0) {
                transactionManager.commit(status);
            }
        }
        return result;
    }

    @Override
    public List<LineGateway> findByIds(Collection<String> ids) {
        return lineGatewayDao.findByIdIn(ids);
    }

    @Override
    public LineGateway findByHost(String host) {
        if(StringUtils.isBlank(host)){
            logger.error("host为空");
            return null;
        }
        if(logger.isDebugEnabled()){
            logger.debug("处理host:{}",host);
        }
        host = resolveHost(host);
        String sipIp = resolveHost(this.sipIp);
        String sipDomain = resolveHost(this.sipDomain);
        if(host.equals(sipIp) || host.equals(sipDomain)){
            //当是用opensip线路时，返回个id为0的线路
            LineGateway lineGateway = new LineGateway();
            lineGateway.setId("0");
            return lineGateway;
        }
        String host1 = host.replace(":5060","");
        String[] hosts = new String[]{host,host1};
        return lineGatewayDao.findFirstBySipProviderIpInOrSipProviderDomainIn(hosts,hosts);
    }

    private String resolveHost(String host) {
        if(!host.contains(":")){
            return host.trim() + ":5060";
        }else{
            return host.trim();
        }
    }

}
