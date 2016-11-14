package com.lsxy.call.center.api.service;

import com.lsxy.call.center.api.model.Channel;
import com.lsxy.framework.api.base.BaseService;

import java.util.List;

/**
 * Created by zhangxb on 2016/10/22.
 */
public interface ChannelService extends BaseService<Channel> {

    public void delete(String tenantId,String appId,String channelId);

    public Channel findOne(String tenantId, String appId, String channelId);

    public List<Channel> getAll(String tenantId, String appId);

}
