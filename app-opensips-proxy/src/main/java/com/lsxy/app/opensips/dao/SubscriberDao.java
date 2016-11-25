package com.lsxy.app.opensips.dao;

import com.lsxy.call.center.api.opensips.model.Subscriber;
import com.lsxy.framework.api.base.BaseDaoInterface;

import java.io.Serializable;

/**
 * Created by liups on 2016/11/25.
 */
public interface SubscriberDao extends BaseDaoInterface<Subscriber, Serializable> {
    Subscriber findByUsername(String username);
}
