package com.lsxy.yunhuni.resourceTelenum.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourcesRent;

import java.io.Serializable;
import java.util.List;

/**
 * 呼入号码DAO
 * Created by zhangxb on 2016/7/1.
 */
public interface ResourcesRentDao extends BaseDaoInterface<ResourcesRent, Serializable> {

    ResourcesRent findByAppId(String appId);
}
