package com.lsxy.area.server.service;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.framework.core.utils.JSONUtil2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by liuws on 2016/8/29.
 * 目前业务都是异步的
 * 所以用redis保存业务数据状态
 */
@Component
public class BusinessStateServiceImpl implements BusinessStateService {

    @Autowired
    private RedisCacheService redisCacheService;

    private int EXPIRE_START = 60 * 60 * 12;

    private int EXPIRE_RELEASE = 60 * 30;

    private String getKey(String id){
        return "business_state_" + id;
    }

    @Override
    public void save(BusinessState state) {
        redisCacheService.set(getKey(state.getId()), JSONUtil.objectToJson(state),EXPIRE_START);
    }

    @Override
    public BusinessState get(String id) {
        return JSONUtil2.fromJson(redisCacheService.get(getKey(id)),BusinessState.class);
    }

    @Override
    public void delete(String id) {
        try{
            redisCacheService.expire(getKey(id),EXPIRE_RELEASE);
        }catch (Throwable t){
        }
    }
}
