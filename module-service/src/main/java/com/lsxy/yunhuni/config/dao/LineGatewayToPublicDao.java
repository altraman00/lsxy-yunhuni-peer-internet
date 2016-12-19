package com.lsxy.yunhuni.config.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.config.model.LineGatewayToPublic;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangxb on 2016/10/25.
 */
public interface LineGatewayToPublicDao extends BaseDaoInterface<LineGatewayToPublic, Serializable> {
    List<LineGatewayToPublic> findByLineGateway_AreaId(String areaId);
}
