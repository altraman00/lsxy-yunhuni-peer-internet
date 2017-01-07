package com.lsxy.yunhuni.api.resourceTelenum.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourcesRent;
import com.lsxy.yunhuni.api.resourceTelenum.model.TelenumOrder;

import java.util.List;
import java.util.Map;

/**
 * 租户号码租用service
 * Created by zhangxb on 2016/7/1.
 */
public interface ResourcesRentService extends BaseService<ResourcesRent> {
    /**
     * 根据租户id获取租户下的全部租用号码
     * @param tenantId 租户id
     * @return
     */
    Page<ResourcesRent> pageListByTenantId(String tenantId,int pageNo, int pageSize)  ;

    /**
     * 根据ID获取租用关系
     * @param appId
     * @return
     */
    List<ResourcesRent> findByAppId(String appId);

    /**
     * 根据号码和租用状态查询租用关系
     * @param id
     * @param status
     * @return
     */
    ResourcesRent findByResourceTelenumIdAndStatus(String id, int status);
    /**
     * 根据号码和租用状态查询租用关系
     * @param id
     * @return
     */
    ResourcesRent findByResourceTelenumId(String id);
    /**
     * 根据号码查询租用关系
     * @param resData
     * @param status
     * @return
     */
    ResourcesRent findByResDataAndRentStatus(String resData, int status);

    /**
     * 根据租户查询拥有的但没在使用状态的号码
     * @param tenant
     * @return
     */
    List<ResourceTelenum> findOwnUnusedNum(Tenant tenant,String lastOnlineAreaId);

    /**
     * 清除过期号资源和租户的关系
     */
    void cleanExpireTelnumResourceRent();

    /**
     * 号码租用自动扣费任务
     */
    void resourcesRentTask();


    /**
     * 释放号码
     */
    void release(String id);

    /**
     * 支付订单
     * @param id
     * @param tenant
     * @return
     */
    void telnumPlay(String id,Tenant tenant);
    /**
     * 取消订单
     */
    void telnumDelete(String id,Tenant tenant);
    /**
     * 创建订单
     */
    TelenumOrder telnumNew(Tenant tenant, String[] numIds);

}
