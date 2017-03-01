package com.lsxy.yunhuni.api.resourceTelenum.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourcesRent;

import java.util.*;

/**
 * 全局号码资源service
 * Created by zhangxb on 2016/7/1.
 */
public interface ResourceTelenumService extends BaseService<ResourceTelenum> {

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

    /**
     * 根据线路列表获取一个空闲的，没有绑定租户的号码
     * @return
     */
    ResourceTelenum findOneFreeDialingNumber(List<String> lineIds);
    /**
     *  获取空闲号码
     */
    Page getFreeNumberPage(String tenantId,Integer pageNo, Integer pageSize, String telnum, String type, String areaCode, String order);
    /**
     * 根据呼叫URI查找号码
     * @param uri
     * @return
     */
    ResourceTelenum findNumByCallUri(String uri);

    /**
     * 获取分页信息
     * @param pageNo
     * @param pageSize
     * @param operator
     * @param number
     * @return
     */
    Page<ResourceTelenum> getPage(Integer pageNo, Integer pageSize,String number,String operator,String isThrough,String status);
    /**
     * 获取分页信息
     * @return
     */
    Page<ResourceTelenum> getPageByNotLine(String id,String areaCode,Integer pageNo, Integer pageSize, String operator, String number);

    /**
     * 用于呼叫号码选择，当from不为空时，则根据from的数量选择相应数量的呼出号码，当from为空时，择一个号码
     * @param app
     * @return
     */
    List<ResourceTelenum> findDialingTelnumber(String subaccountId,List<String> lineIds, App app, String... from);

    /**
     * 根据号码获取已租户为主体的号码列表
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<Map> getTenatPageByLine(Integer pageNo, Integer pageSize, String line,String tenantName);

    /**
     * 删除号码
     */
    void delete(String id);

    /**
     * 创建号码
     */
    void createNum(ResourceTelenum resourceTelenum,LineGateway lineGateway,Tenant tenant);

    /**
     * 修改号码
     */
    void editNum(ResourceTelenum resourceTelenum,int tenantType,boolean isEditNum,Tenant tenant,String telnum1,String telnum12);
    /**
     * 释放
     */
    void release(String id);

    /**
     * 根据Ids查找号码资源
     * @param ids
     * @return
     */
    List<ResourceTelenum> findByIds(Collection<String> ids);

    List<ResourceTelenum> findByTelNumbers(Collection<String> telNumbers);

    /**
     * 根据呼叫号码或callUrl来查找号码
     * @param num
     * @return
     */
    ResourceTelenum findByTelNumberOrCallUri(String num);

    boolean isCalledByTenantIdAndAppId(String tenantId, String appId);

    /**
     * 解除应用与号码的绑定关系
     * @param tenantId
     * @param appId
     */
    void appUnbindAll(String tenantId,String appId);

    Page<ResourceTelenum> findOwnUnusedNum(String tenantId, String areaId, int pageNo, int pageSize);

    Page<ResourceTelenum> findOwnUnusedNumOrUnbindSubaccount(String tenantId, String appId, String areaId, int pageNo, int pageSize);

    /**
     * 子账号解除所有号码
     * @param appId
     * @param subaccountId
     */
    void subaccountUnbindAll(String tenantId,String appId,String subaccountId);

    void subaccountUnbindNum(String tenantId,String appId,String subaccountId,String numId);

}
