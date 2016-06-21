package com.lsxy.app.portal;

import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.framework.web.rest.UserRestToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Created by Tandy on 2016/6/14.
 * 登陆入口
 */
@RestController
public class LoginController {
    /**
     * 登录接口
     * @return
     */
    @RequestMapping(path="/login",method = RequestMethod.POST)
    public RestResponse login(){
        //用户名密码校验

        //成功则生成并返回tocken，并将tocken存到redis里，失败则返回登录失败
        UserRestToken token = new UserRestToken();
        token.setToken(UUID.randomUUID().toString());
        return RestResponse.success(token.getToken());
    }

}
