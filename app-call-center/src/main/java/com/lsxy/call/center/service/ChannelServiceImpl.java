package com.lsxy.call.center.service;

import com.lsxy.call.center.api.model.Channel;
import com.lsxy.call.center.api.service.ChannelService;
import com.lsxy.call.center.dao.ChannelDao;
import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangxb on 2016/10/21.
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class ChannelServiceImpl extends AbstractService<Channel> implements ChannelService {

    private static final Logger logger = LoggerFactory.getLogger(ChannelServiceImpl.class);

    @Autowired
    private ChannelDao channelDao;

    @Override
    public BaseDaoInterface<Channel, Serializable> getDao() {
        return channelDao;
    }

    @Override
    public void delete(String tenantId, String appId, String channelId) {
        Channel channel = this.findOne(tenantId,appId,channelId);
        if(channel != null){
            try{
                this.delete(channel);
            }catch (Throwable t){
                logger.error("删除channel失败",t);
            }
        }
    }

    @Override
    public Channel findOne(String tenantId, String appId, String channelId) {
        if(channelId == null){
            throw new IllegalArgumentException("channelId 不能为null");
        }
        if(tenantId == null){
            throw new IllegalArgumentException("tenantId 不能为null");
        }
        if(appId == null){
            throw new IllegalArgumentException("appId 不能为null");
        }
        Channel channel = this.findById(channelId);
        if(channel == null){
            throw new IllegalArgumentException("channel 不存在");
        }
        if(!tenantId.equals(channel.getTenantId())){
            throw new IllegalArgumentException("channel 不存在");
        }
        if(!appId.equals(channel.getAppId())){
            throw new IllegalArgumentException("channel 不存在");
        }
        return channel;
    }

    @Override
    public List<Channel> getAll(String tenantId, String appId) {
        if(tenantId == null){
            throw new IllegalArgumentException("tenantId 不能为null");
        }
        if(appId == null){
            throw new IllegalArgumentException("appId 不能为null");
        }

        return this.channelDao.findByTenantIdAndAppId(tenantId,appId);
    }
}
