package com.lsxy.yunhuni.api.resourceTelenum.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;

import java.util.Date;
import java.util.List;

/**
 * 全局号码资源service
 * Created by zhangxb on 2016/7/1.
 */
public interface ResourceTelenumService extends BaseService<ResourceTelenum> {
    /**
     * 从号码池中获取闲置的号码
     * @param count
     * @return
     */
    List<String> getFreeTeleNum(int count);

    /**
     * 根据号码从号码池中获取号资源
     * @param telNumber
     * @return
     */
    ResourceTelenum findByTelNumber(String telNumber);

    /**
     * 清除过期的号码资源和租户的关系
     * @param expireTime 过期时间
     */
    void cleanExpireResourceTelnum(Date expireTime);
}
