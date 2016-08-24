package com.lsxy.oc.api.user.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.oc.api.user.model.OcUser;

/**
 * 运营后台用户Service接口
 * Created by liups on 2016/8/9.
 */
public interface OcUserService extends BaseService<OcUser> {
    /**
     * 根据登录用户名和密码获取用户
     * @param userName 登录用户名（只能是用户名）
     * @param password 密码
     * @return
     */
    OcUser findUserByLoginNameAndPassword(String userName,String password);

    OcUser findUserByLoginName(String userName);
}
