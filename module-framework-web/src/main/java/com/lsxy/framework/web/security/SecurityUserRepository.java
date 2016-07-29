package com.lsxy.framework.web.security;

import com.lsxy.framework.core.security.SecurityUser;

/**
 * Created by Tandy on 2016/6/8.
 */
public interface SecurityUserRepository {
    public SecurityUser loadSecurityUser(String token,String userName);
}
