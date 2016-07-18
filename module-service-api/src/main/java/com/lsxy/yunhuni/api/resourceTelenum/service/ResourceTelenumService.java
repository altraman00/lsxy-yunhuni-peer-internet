package com.lsxy.yunhuni.api.resourceTelenum.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;

import java.util.List;

/**
 * 全局号码资源service
 * Created by zhangxb on 2016/7/1.
 */
public interface ResourceTelenumService extends BaseService<ResourceTelenum> {
    List<String> getFreeTeleNum(int count);

    ResourceTelenum findByTelNumber(String telNumber);
}
