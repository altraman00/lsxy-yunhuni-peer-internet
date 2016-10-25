package com.lsxy.yunhuni.api.config.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.config.model.LineGateway;

import java.util.List;

/**
 * Created by liups on 2016/8/24.
 */
public interface LineGatewayService extends BaseService<LineGateway> {
    LineGateway getBestLineGatewayByNumber(String number);

    /**
     * 根据条件获取分页数据
     * @param operator 运营商
     * @param isThrough 是否透传
     * @param status 状态
     * @param isPublicLine 是否全局线路
     * @return
     */
    Page<LineGateway> getPage(Integer pageNo,Integer pageSize,String operator, String isThrough, String status, String isPublicLine,String order);
}
