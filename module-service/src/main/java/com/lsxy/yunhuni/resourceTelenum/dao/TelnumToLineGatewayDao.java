package com.lsxy.yunhuni.resourceTelenum.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.resourceTelenum.model.TelnumToLineGateway;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liups on 2016/9/2.
 */
public interface TelnumToLineGatewayDao extends BaseDaoInterface<TelnumToLineGateway, Serializable> {
    /**
     * 根据号码来选择线路
     * @param telnum
     * @return
     */
    List<TelnumToLineGateway> findByTelNumber(String telnum);
}
