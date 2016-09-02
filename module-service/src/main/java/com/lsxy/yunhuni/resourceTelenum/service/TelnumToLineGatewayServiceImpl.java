package com.lsxy.yunhuni.resourceTelenum.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.config.model.Area;
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

    @Override
    public BaseDaoInterface<TelnumToLineGateway, Serializable> getDao() {
        return this.telnumToLineGatewayDao;
    }

    @Override
    public String getAreaIdByTelnum(String telnum){
        List<TelnumToLineGateway> telnumToLineGateways = telnumToLineGatewayDao.findByTelNumber(telnum);
        if(telnumToLineGateways == null || telnumToLineGateways.size() <= 0){
            throw new RuntimeException("数据异常，号码没有关联线路");
        }
        Random random = new Random();
        Integer ranNum = random.nextInt(telnumToLineGateways.size());
        String lineId = telnumToLineGateways.get(ranNum).getLineId();
        LineGateway lineGateway = lineGatewayService.findById(lineId);
        return lineGateway.getAreaId();
    }

    @Override
    public List<String> getLineIdsByNumber(String number) {
        List<String> results = new LinkedList<>();
        List<TelnumToLineGateway> telnumToLineGateways = telnumToLineGatewayDao.findByTelNumber(number);
        for(TelnumToLineGateway ttg:telnumToLineGateways){
            results.add(ttg.getLineId());
        }
        return results;
    }

}
