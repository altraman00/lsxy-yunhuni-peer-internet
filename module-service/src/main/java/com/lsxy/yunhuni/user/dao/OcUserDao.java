package com.lsxy.yunhuni.user.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.user.model.OcUser;

import java.io.Serializable;

/**
 * 运营中心用户
 * Created by liups on 2016/8/9.
 */
public interface OcUserDao extends BaseDaoInterface<OcUser, Serializable> {
    /**
     * 根据用户名来查找账号
     * @param userName 用户名
     * @param status 账号状态
     * @return
     */
    OcUser findByUserNameAndStatus(String userName,Integer status);
}
