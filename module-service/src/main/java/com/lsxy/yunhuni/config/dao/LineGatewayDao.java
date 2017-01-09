package com.lsxy.yunhuni.config.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.config.model.LineGateway;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Created by liups on 2016/8/24.
 */
public interface LineGatewayDao extends BaseDaoInterface<LineGateway, Serializable> {
    List<LineGateway> findByIdIn(Collection<String> ids);

    LineGateway findFirstBySipProviderIpInOrSipProviderDomainIn(String[] hosts, String[] hosts1);
}
