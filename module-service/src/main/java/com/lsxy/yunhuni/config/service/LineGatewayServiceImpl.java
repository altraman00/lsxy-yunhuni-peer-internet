package com.lsxy.yunhuni.config.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.config.service.LineGatewayService;
import com.lsxy.yunhuni.config.dao.LineGatewayDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2016/8/24.
 */
@Service
public class LineGatewayServiceImpl extends AbstractService<LineGateway> implements LineGatewayService {
    @Autowired
    LineGatewayDao lineGatewayDao;

    @Override
    public BaseDaoInterface<LineGateway, Serializable> getDao() {
        return this.lineGatewayDao;
    }
}
