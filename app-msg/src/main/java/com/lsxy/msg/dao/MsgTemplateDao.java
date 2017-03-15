package com.lsxy.msg.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.msg.api.model.MsgTemplate;

import java.io.Serializable;

/**
 * Created by liups on 2017/3/1.
 */
public interface MsgTemplateDao extends BaseDaoInterface<MsgTemplate, Serializable> {
    MsgTemplate findByAppIdAndSubaccountIdAndTempId(String appId, String subaccountId, String tempId);

    MsgTemplate findByAppIdAndTempId(String appId, String tempId);

}
