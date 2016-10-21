package com.lsxy.yunhuni.resourceTelenum.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.config.service.LineGatewayService;
import com.lsxy.yunhuni.api.resourceTelenum.model.TelnumToLineGateway;
import com.lsxy.yunhuni.api.resourceTelenum.service.TelnumToLineGatewayService;
import com.lsxy.yunhuni.resourceTelenum.dao.TelnumToLineGatewayDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by liups on 2016/9/2.
 */
@Service
public class TelnumToLineGatewayServiceImpl extends AbstractService<TelnumToLineGateway> implements TelnumToLineGatewayService{
    @Autowired
    TelnumToLineGatewayDao telnumToLineGatewayDao;

    @Autowired
    LineGatewayService lineGatewayService;

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
    public List<String> getDialingLineIdsByNumber(String number) {
        List<String> results = new LinkedList<>();
        List<TelnumToLineGateway> telnumToLineGateways = telnumToLineGatewayDao.findDialingLine(number);
        for(TelnumToLineGateway ttg:telnumToLineGateways){
            results.add(ttg.getLineId());
        }
        return results;
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

}
