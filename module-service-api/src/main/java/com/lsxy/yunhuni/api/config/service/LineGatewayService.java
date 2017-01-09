package com.lsxy.yunhuni.api.config.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.config.model.LineGateway;

import java.util.Collection;
import java.util.List;

/**
 * Created by liups on 2016/8/24.
 */
public interface LineGatewayService extends BaseService<LineGateway> {
    /**
     * 根据条件获取分页数据
     * @param operator 运营商
     * @param isThrough 是否透传
     * @param status 状态
     * @param isPublicLine 是否全局线路
     * @return
     */
    Page<LineGateway> getPage(Integer pageNo,Integer pageSize,String operator, String isThrough, String status, String isPublicLine,String order);
    Page<LineGateway> getNotTenantPage(Integer pageNo,Integer pageSize,String tenantId,String operator, String line);
    int batchModify(String[] sql);

    List<LineGateway> findByIds(Collection<String> ids);

    /**
     * 根据ip或域名查找线路
     * 注：当是用opensip线路时，返回个id为0的线路
     * @param host
     * @return
     */
    LineGateway findByHost(String host);
}
