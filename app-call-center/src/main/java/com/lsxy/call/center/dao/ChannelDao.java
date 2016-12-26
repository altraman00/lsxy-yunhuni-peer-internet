package com.lsxy.call.center.dao;

import com.lsxy.call.center.api.model.Channel;
import com.lsxy.framework.api.base.BaseDaoInterface;

import java.io.Serializable;
import java.util.List;

/**
 * 呼叫中心系统
 * Created by zhangxb on 2016/10/22.
 */
public interface ChannelDao extends BaseDaoInterface<Channel, Serializable> {

    List<Channel> findByTenantIdAndAppId(String tenantId, String appId);

}
