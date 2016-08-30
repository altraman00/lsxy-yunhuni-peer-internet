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
 */
@Component
public class BusinessStateServiceImpl implements BusinessStateService {

    @Autowired
    private RedisCacheService redisCacheService;

    private String getKey(String id){
        return "business_state_" + id;
    }
    @Override
    public void save(BusinessState state) {
        redisCacheService.set(getKey(state.getId()), JSONUtil.objectToJson(state),5 * 60 * 60);
    }

    @Override
    public BusinessState get(String id) {
        return JSONUtil2.fromJson(redisCacheService.get(getKey(id)),BusinessState.class);
    }

    @Override
    public void delete(String id) {
        try{
            redisCacheService.del(getKey(id));
        }catch (Throwable t){
        }
    }
}
