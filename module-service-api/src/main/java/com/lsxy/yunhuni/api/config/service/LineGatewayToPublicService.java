package com.lsxy.yunhuni.api.config.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.config.model.LineGatewayToPublic;

/**
 * Created by zhangxb on 2016/10/25.
 */
public interface LineGatewayToPublicService extends BaseService<LineGatewayToPublic> {
    int getMaxPriority();
    int findByLindId(String lindId);
    Page<LineGatewayToPublic> getPage(Integer pageNo, Integer pageSize, String operator, String isThrough, String status, String isPublicLine, String order);
}
