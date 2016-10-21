package com.lsxy.yunhuni.config.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.config.service.LineGatewayService;
import com.lsxy.yunhuni.api.resourceTelenum.service.TelnumToLineGatewayService;
import com.lsxy.yunhuni.config.dao.LineGatewayDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liups on 2016/8/24.
 */
@Service
public class LineGatewayServiceImpl extends AbstractService<LineGateway> implements LineGatewayService {
    @Autowired
    LineGatewayDao lineGatewayDao;
    @Autowired
    TelnumToLineGatewayService telnumToLineGatewayService;

    @Override
    public BaseDaoInterface<LineGateway, Serializable> getDao() {
        return this.lineGatewayDao;
    }

    @Override
    public LineGateway getBestLineGatewayByNumber(String number){
        //TODO 获取最优的线路
        List<String> lineIds =  telnumToLineGatewayService.getDialingLineIdsByNumber(number);
        String lineId = lineIds.get(0);
        return this.findById(lineId);
    }
}
