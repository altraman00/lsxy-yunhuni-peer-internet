package com.lsxy.msg.api.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.msg.api.model.MsgTemplate;

/**
 * Created by liups on 2017/3/1.
 */
public interface MsgTemplateService extends BaseService<MsgTemplate> {
    Page<MsgTemplate> getPageByCondition(Integer pageNo, Integer pageSize, String appId, String subaccountId, String name);
}
