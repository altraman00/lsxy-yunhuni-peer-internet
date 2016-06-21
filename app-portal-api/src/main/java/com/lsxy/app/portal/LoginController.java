package com.lsxy.app.portal;

import com.lsxy.framework.tenant.model.Account;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.framework.web.rest.UserRestToken;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Tandy on 2016/6/14.
 * 登陆入口
 */
@RestController
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(path="/login",method = RequestMethod.POST)
    public RestResponse login(@RequestParam String username,@RequestParam String password){

        UserRestToken token = new UserRestToken();
        return RestResponse.success(token);
    }

}
