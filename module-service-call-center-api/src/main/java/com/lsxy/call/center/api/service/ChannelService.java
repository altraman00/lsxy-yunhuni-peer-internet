package com.lsxy.call.center.api.service;

import com.lsxy.call.center.api.model.Channel;
import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;

import java.util.List;

/**
 * Created by zhangxb on 2016/10/22.
 */
public interface ChannelService extends BaseService<Channel> {

    public Channel save(String tenantId, String appId, Channel channel) throws YunhuniApiException;

    public void delete(String tenantId,String appId,String channelId) throws YunhuniApiException;

    public Channel findOne(String tenantId, String appId, String channelId) throws YunhuniApiException;

    public List<Channel> getAll(String tenantId, String appId) throws YunhuniApiException;


}
