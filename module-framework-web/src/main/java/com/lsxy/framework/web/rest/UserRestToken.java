package com.lsxy.framework.web.rest;


import com.lsxy.framework.core.utils.UUIDGenerator;

/**
 * Created by Tandy on 2016/6/14.
 * 用户登录后的凭证
 * 用户登录凭证到期时间为生成时间起生效，在最后一次交互计算 30分钟后到期
 */
public class UserRestToken {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private UserRestToken(){

    }

    /**
     * 构建一个新的token对象
     * @return
     */
    public static UserRestToken newToken(){
        String token = UUIDGenerator.uuid();
        UserRestToken urt = new UserRestToken();
        urt.setToken(token);
        return urt;
    }


}
