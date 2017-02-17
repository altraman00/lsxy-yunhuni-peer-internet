package com.lsxy.call.center.service;

import com.lsxy.call.center.api.model.Channel;
import com.lsxy.call.center.api.service.ChannelService;
import com.lsxy.call.center.dao.ChannelDao;
import com.lsxy.call.center.dao.ConditionDao;
import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.exceptions.api.*;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.app.service.ServiceType;
import org.codehaus.groovy.runtime.InvokerInvocationException;
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

    @Autowired
    private ConditionDao conditionDao;

    @Autowired
    private AppService appService;

    @Override
    public BaseDaoInterface<Channel, Serializable> getDao() {
        return channelDao;
    }

    @Override
    public Channel save(String tenantId,String appId,Channel channel) throws YunhuniApiException{
        if(channel == null){
            throw new RequestIllegalArgumentException();
        }
        if(tenantId == null){
            throw new RequestIllegalArgumentException();
        }
        if(appId == null){
            throw new RequestIllegalArgumentException();
        }
        channel.setTenantId(tenantId);
        channel.setAppId(appId);
        return super.save(channel);
    }

    @Override
    public void delete(String tenantId, String appId, String channelId) throws YunhuniApiException{
        Channel channel = this.findOne(tenantId,appId,channelId);
        if(channel != null){
            if(conditionDao.countByTenantIdAndAppIdAndChannelId(tenantId, appId, channelId) > 0){
                throw new ChannelCanNotDeleteException();
            }
            try{
                this.delete(channel);
            }catch (Throwable t){
                logger.error("删除channel失败",t);
                throw new InvokeCallException();
            }
        }
    }

    @Override
    public Channel findOne(String tenantId, String appId, String channelId) throws YunhuniApiException{
        if(channelId == null){
            throw new RequestIllegalArgumentException();
        }
        if(tenantId == null){
            throw new RequestIllegalArgumentException();
        }
        if(appId == null){
            throw new RequestIllegalArgumentException();
        }
        Channel channel = this.findById(channelId);
        if(channel == null){
            throw new ChannelNotExistException();
        }
        if(!tenantId.equals(channel.getTenantId())){
            throw new ChannelNotExistException();
        }
        if(!appId.equals(channel.getAppId())){
            throw new ChannelNotExistException();
        }
        return channel;
    }

    @Override
    public List<Channel> getAll(String tenantId, String appId)  throws YunhuniApiException{
        if(tenantId == null){
            throw new RequestIllegalArgumentException();
        }
        if(appId == null){
            throw new RequestIllegalArgumentException();
        }
        return this.channelDao.findByTenantIdAndAppId(tenantId,appId);
    }
}
