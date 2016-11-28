package com.lsxy.app.opensips.dao;

import com.lsxy.call.center.api.opensips.model.Location;
import com.lsxy.framework.api.base.BaseDaoInterface;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liups on 2016/11/25.
 */
public interface LocationDao extends BaseDaoInterface<Location, Serializable> {
    List<Location> findByUsername(String username);
}
