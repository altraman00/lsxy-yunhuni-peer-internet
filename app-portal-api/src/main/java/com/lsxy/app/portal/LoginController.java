package com.lsxy.app.portal;

import com.lsxy.framework.tenant.model.Account;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.framework.web.rest.UserRestToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Tandy on 2016/6/14.
 * 登陆入口
 */
@RestController
public class LoginController {

    @RequestMapping(path="/login",method = RequestMethod.POST)
    public RestResponse login(){
        UserRestToken token = new UserRestToken();
        return RestResponse.success(token);
    }

}
