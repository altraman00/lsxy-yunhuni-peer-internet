package com.lsxy.area.server.service;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.framework.core.utils.JSONUtil2;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by liuws on 2016/8/29.
 * 目前业务都是异步的
 * 所以用redis保存业务数据状态
 */
@Component
public class BusinessStateServiceImpl implements BusinessStateService {

    private static final Logger logger = LoggerFactory.getLogger(BusinessStateServiceImpl.class);

    @Autowired
    private RedisCacheService redisCacheService;

    private int EXPIRE_START = 60 * 60 * 12;

    private int EXPIRE_RELEASE = 60 * 30;

    private String getKey(String id){
        return "business_state_" + id;
    }

    @Override
    public void save(BusinessState state) {
        if(state == null){
            return;
        }
        if(state.getId() == null){
            return;
        }
        try{
            redisCacheService.set(getKey(state.getId()), JSONUtil.objectToJson(state),EXPIRE_START);
        }catch (Throwable t){
            logger.error("保存state失败",t);
        }
    }

    @Override
    public BusinessState get(String id) {
        BusinessState state = null;
        try{
            String str = redisCacheService.get(getKey(id));
            if(!StringUtils.isBlank(str)){
                state = JSONUtil2.fromJson(str,BusinessState.class);
            }
        }catch (Throwable t){
            logger.error("获取state失败",t);
        }
        return state;
    }

    @Override
    public void delete(String id) {
        try{
            BusinessState state = get(id);
            if(state != null){
                state.setClosed(true);
                redisCacheService.set(getKey(id), JSONUtil.objectToJson(state),EXPIRE_RELEASE);
            }
        }catch (Throwable t){
            logger.error("删除state失败",t);
        }
    }
}
