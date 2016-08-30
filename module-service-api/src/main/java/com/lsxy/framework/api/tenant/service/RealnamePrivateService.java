package com.lsxy.framework.api.tenant.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.tenant.model.RealnamePrivate;
import com.lsxy.framework.core.utils.Page;

import java.util.List;

public interface RealnamePrivateService extends BaseService<RealnamePrivate> {

    /**
     * 根据租户id查找认证信息
     * @param tenantId 租户信息
     * @return
     */
    public List<RealnamePrivate> findByTenantId(String tenantId) ;
    /**
     * 根据租户和状态换取记录
     * @param tenantId 租户id
     * @param status 状态
     * @return
     */
    public List<RealnamePrivate> list(String tenantId,int status) ;
    /**
     * 根据组合id和状态查找认证信息
     * @param tenantId 租户id
     * @param status 状态
     * @return
     */
    public RealnamePrivate findByTenantIdAndStatus(String tenantId,int status);

    public RealnamePrivate findByTenantIdNewest(String tenantId);


    /**
     * 查找用户下的分页信息
     * @param authStatus await|auditing|unauth
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param type 0个人认证 1企业认证
     * @param search 会员名
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @return
     */
    Page pageListAuthInfo(Integer authStatus,String startTime, String endTime, Integer type, String search, Integer pageNo, Integer pageSize);
}
