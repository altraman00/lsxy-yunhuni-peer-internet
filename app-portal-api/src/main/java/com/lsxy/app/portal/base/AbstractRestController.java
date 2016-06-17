package com.lsxy.app.portal.base;

import com.lsxy.framework.tenant.model.Account;

/**
 * Created by Tandy on 2016/6/14.
 * 抽象控制器类，提供获取当前登录用户的方法
 */
public abstract class AbstractRestController {

    protected Account getCurrentAccount(){
        return null;
    }

}
