package com.lsxy.yunhuni.config.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.config.service.LineGatewayService;
import com.lsxy.yunhuni.api.resourceTelenum.service.TelnumToLineGatewayService;
import com.lsxy.yunhuni.config.dao.LineGatewayDao;
import org.apache.commons.lang.StringUtils;
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
        List<String> lineIds =  telnumToLineGatewayService.getLineIdsByNumber(number);
        String lineId = lineIds.get(0);
        return this.findById(lineId);
    }

    @Override
    public Page<LineGateway> getPage(Integer pageNo,Integer pageSize,String operator, String isThrough, String status, String isPublicLine) {
        String hql = " FROM LineGateway obj ";
        if(StringUtils.isNotEmpty(operator)){
            hql += " AND obj.operator = '"+operator+"' ";
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
        Page page = this.pageList(hql,pageNo,pageSize);
        return null;
    }


}
