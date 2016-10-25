package com.lsxy.yunhuni.config.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.config.model.LineGatewayToPublic;
import com.lsxy.yunhuni.api.config.service.LineGatewayToPublicService;
import com.lsxy.yunhuni.config.dao.LineGatewayToPublicDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by zhangxb on 2016/10/25.
 */
@Service
public class LineGatewayToPublicServiceImpl extends AbstractService<LineGatewayToPublic> implements LineGatewayToPublicService {
    @Autowired
    LineGatewayToPublicDao lineGatewayToPublicDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public BaseDaoInterface<LineGatewayToPublic, Serializable> getDao() {
        return this.lineGatewayToPublicDao;
    }

    @Override
    public int getMaxPriority() {
        String sql = " SELECT ISNULL(MAX(priority),0) db_lsxy_bi_yunhuni.tb_bi_linegateway_to_public ";
        int result = jdbcTemplate.queryForObject(sql,Integer.class);
        return result;
    }

    @Override
    public int findByLindId(String lindId) {
        String sql = " SELECT COUNT(id) db_lsxy_bi_yunhuni.tb_bi_linegateway_to_public where line_id='"+lindId+"'";
        int result = jdbcTemplate.queryForObject(sql,Integer.class);
        return result;
    }

    @Override
    public Page<LineGatewayToPublic> getPage(Integer pageNo, Integer pageSize, String operator, String isThrough, String status, String isPublicLine, String order) {
        String hql = " FROM LineGatewayToPublic obj ";
        if(StringUtils.isNotEmpty(operator)){
            hql += " AND obj.lineGateway.operator = '"+operator+"' ";
        }
        if(StringUtils.isNotEmpty(isThrough)){
            hql += " AND obj.lineGateway.isThrough = '"+isThrough+"' ";
        }
        if(StringUtils.isNotEmpty(status)){
            hql += " AND obj.lineGateway.status = '"+status+"' ";
        }
        if(StringUtils.isNotEmpty(isPublicLine)){
            hql += " AND obj.lineGateway.isPublicLine = '"+isPublicLine+"' ";
        }
        if(StringUtils.isNotEmpty(order)){
            String temp = " ORDER BY ";
            String[] o = order.split(";");
            for(int i=0;i<o.length;i++){
                String[] oo = o[i].split(":");
                if("1".equals(oo[1])){
                    temp += " obj.lineGateway."+oo[0]+" DESC ";
                }else if("0".equals(oo[1])){
                    temp += " obj.lineGateway."+oo[0]+" ";
                }
                if(i!=o.length-1){
                    temp += " , ";
                }
            }
        }else{
            hql += " ORDER BY obj.priority  ";
        }
        Page page = this.pageList(hql,pageNo,pageSize);
        return page;
    }
}
