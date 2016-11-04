package com.lsxy.yunhuni.config.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.config.model.LineGatewayToPublic;
import com.lsxy.yunhuni.api.config.service.LineGatewayToPublicService;
import com.lsxy.yunhuni.config.dao.LineGatewayToPublicDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    public LineGatewayToPublic findById(String id) {
        return getDao().findOne(id);
    }

    @Override
    public int getMaxPriority() {
        String sql = " SELECT IFNULL(MAX(priority),0) FROM db_lsxy_bi_yunhuni.tb_oc_linegateway_to_public WHERE deleted=0 ";
        int result = jdbcTemplate.queryForObject(sql,Integer.class);
        return result;
    }

    @Override
    public int findByLindId(String lindId) {
        String sql = " SELECT COUNT(id) FROM db_lsxy_bi_yunhuni.tb_oc_linegateway_to_public where deleted=0 AND line_id='"+lindId+"'";
        int result = jdbcTemplate.queryForObject(sql,Integer.class);
        return result;
    }

    @Override
    public Page<LineGatewayToPublic> getPage(Integer pageNo, Integer pageSize, String operator, String isThrough, String status, String isPublicLine, String order) {
        String hql = " FROM LineGatewayToPublic obj WHERE obj.deleted=0  ";
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
                    if("priority".equals(oo[0])){
                        temp += " obj." + oo[0] + " DESC ";
                    }else {
                        temp += " obj.lineGateway." + oo[0] + " DESC ";
                    }
                }else if("0".equals(oo[1])){
                    if("priority".equals(oo[0])){
                        temp += " obj." + oo[0] + " ";
                    }else {
                        temp += " obj.lineGateway." + oo[0] + " ";
                    }
                }
                if(i!=o.length-1){
                    temp += " , ";
                }
            }
            hql += temp;
        }else{
            hql += " ORDER BY obj.priority  ";
        }
        Page page = this.pageList(hql,pageNo,pageSize);
        return page;
    }

    @Override
    public List<LineGateway> findAllLineGatewayByAreaId(String areaId) {
        List<LineGateway> lineGateways = new ArrayList<>();
        List<LineGatewayToPublic> ltps = lineGatewayToPublicDao.findByLineGateway_AreaId(areaId);
        for (LineGatewayToPublic ltp:ltps){
            LineGateway lineGateway = ltp.getLineGateway();
            //TODO 判断线路是否可用
            if("1".equals(lineGateway.getStatus())){
                lineGateway.setPriority(ltp.getPriority());
                lineGateways.add(lineGateway);
            }
        }
        return lineGateways;
    }

    @Override
    public void deleteLineGateWay(String line) {

    }

    @Override
    public void deleteLine(String line) {
        String sql =  " update  db_lsxy_bi_yunhuni.tb_oc_linegateway_to_public  set deleted=1 where deleted=0 and line_id='"+line+"'  ";
        jdbcTemplate.update(sql);
    }
}
