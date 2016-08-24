package com.lsxy.app.portal.open;

import com.lsxy.app.portal.exceptions.APIErrors;
import com.lsxy.app.portal.utils.PortalRestResponse;
import com.lsxy.framework.api.exceptions.AccountNotFoundException;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.framework.web.rest.UserRestToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Tandy on 2016/6/14.
 * 登陆入口
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);


    @Autowired
    private AccountService accountService;

    @Autowired
    private RedisCacheService cacheManager;

    /**
     * 登录接口
     * @return
     */
    @RequestMapping(path="")
    public RestResponse login(@RequestParam String username,@RequestParam String password){

        try {
            Account account = accountService.findPersonByLoginNameAndPassword(username, password);
            if (account != null) {

                //将token放入redis 与account形成映射关系
                UserRestToken token = UserRestToken.newApiToken();
                cacheManager.set(token.getToken(),account.getUserName(),35*60);
                return PortalRestResponse.success(token);
            } else {
                return PortalRestResponse.failed(APIErrors.LOGIN_ERROR_PWD_MISTAKE);
            }
        }catch (AccountNotFoundException e){
            logger.info("用户登录失败:{}:{} \r\n",username,password);
            if(logger.isDebugEnabled()){
                logger.error("用户登录失败：",e);
            }
            return PortalRestResponse.failed(APIErrors.LOGIN_ERROR_ACCOUNT_NOT_FOUND);
        }catch (Exception ex){
            logger.info("用户登录失败:{}:{} \r\n",username,password);
            if(logger.isDebugEnabled()){
                logger.error("用户登录失败：",ex);
            }
            return PortalRestResponse.failed(APIErrors.LOGIN_ERROR);
        }
    }




}
