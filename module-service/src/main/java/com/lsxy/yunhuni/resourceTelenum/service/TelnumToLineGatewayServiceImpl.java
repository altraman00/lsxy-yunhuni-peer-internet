package com.lsxy.yunhuni.resourceTelenum.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.config.service.LineGatewayService;
import com.lsxy.yunhuni.api.resourceTelenum.model.TelnumToLineGateway;
import com.lsxy.yunhuni.api.resourceTelenum.service.TelnumToLineGatewayService;
import com.lsxy.yunhuni.resourceTelenum.dao.TelnumToLineGatewayDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by liups on 2016/9/2.
 */
@Service
public class TelnumToLineGatewayServiceImpl extends AbstractService<TelnumToLineGateway> implements TelnumToLineGatewayService{
    @Autowired
    TelnumToLineGatewayDao telnumToLineGatewayDao;

    @Autowired
    LineGatewayService lineGatewayService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public BaseDaoInterface<TelnumToLineGateway, Serializable> getDao() {
        return this.telnumToLineGatewayDao;
    }

    @Override
    public String getAreaIdByTelnum(String telnum){
        TelnumToLineGateway telnumToLineGateway = telnumToLineGatewayDao.findFirstByTelNumber(telnum);
        if(telnumToLineGateway == null ){
            throw new RuntimeException("数据异常，号码没有关联线路");
        }
        String lineId = telnumToLineGateway.getLineId();
        LineGateway lineGateway = lineGatewayService.findById(lineId);
        return lineGateway.getAreaId();
    }

    @Override
    public  List<TelnumToLineGateway> getDialingLinesByNumber(String number) {
        return telnumToLineGatewayDao.findDialingLine(number);
    }

    @Override
    public LineGateway getCalledLineByNumber(String number) {
        TelnumToLineGateway ttg = telnumToLineGatewayDao.findFirstByTelNumberAndIsCalled(number,"1");
        if(ttg != null){
            return lineGatewayService.findById(ttg.getLineId());
        }else{
            return null;
        }
    }

    @Override
    public void deleteByLineId(String line) {
        String sql = "UPDATE db_lsxy_bi_yunhuni.tb_oc_telnum_to_linegateway SET deleted=1 WHERE line_id='"+line+"'";
        jdbcTemplate.update(sql);
    }

    @Override
    public Page<TelnumToLineGateway> getPage(Integer pageNo,Integer pageSize,String number, String isDialing, String isCalled, String isThrough) {
        String hql = " FROM TelnumToLineGateway obj WHERE 1=1 ";
        if(StringUtils.isNotEmpty(isDialing)){
            hql += " AND obj.isDialing='"+isDialing+"' ";
        }
        if(StringUtils.isNotEmpty(isCalled)){
            hql += " AND obj.isCalled='"+isCalled+"' ";
        }
        if(StringUtils.isNotEmpty(isThrough)){
            hql += " AND obj.isThrough='"+isThrough+"' ";
        }
        if(StringUtils.isNotEmpty(number)){
            hql += " AND obj.number like '%"+number+"%' ";
        }
        hql += " ORDER BY obj.createTime DESC ";
        Page page = this.pageList(hql,pageNo,pageSize);
        return page;
    }

    @Override
    public void batchDelete(String[] ids) {
        String sql = " UPDATE db_lsxy_bi_yunhuni.tb_oc_telnum_to_linegateway SET deleted=1 WHERE id IN (";
        for(int i=0;i<ids.length;i++){
            sql+= " '"+ids[i]+"' ";
            if(i!=ids.length-1){
                sql+=" , ";
            }
        }
        sql += " ) ";
        jdbcTemplate.update(sql);
    }

}
