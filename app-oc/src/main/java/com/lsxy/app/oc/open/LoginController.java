package com.lsxy.app.oc.open;

import com.lsxy.app.oc.exceptions.APIErrors;
import com.lsxy.app.oc.utils.PortalRestResponse;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.framework.web.rest.UserRestToken;
import com.lsxy.oc.api.exceptions.OcUserNotFoundException;
import com.lsxy.oc.api.user.model.OcUser;
import com.lsxy.oc.api.user.service.OcUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Tandy on 2016/6/14.
 * 登陆入口
 */
@RestController
@RequestMapping("/auth")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);


    @Autowired
    private OcUserService ocUserService;

    @Autowired
    private RedisCacheService cacheManager;

    /**
     * 登录接口
     * @return
     */
    @RequestMapping(path="/login",method = RequestMethod.POST)
    public RestResponse login(@RequestParam String username,@RequestParam String password){

        try {
            OcUser user = ocUserService.findUserByLoginNameAndPassword(username, password);
            if (user != null) {

                //将token放入redis 与OcUser形成映射关系
                UserRestToken token = UserRestToken.newOcUserToken();
                cacheManager.set(token.getToken(),user.getUserName(),35*60);
                return PortalRestResponse.success(token);
            } else {
                return PortalRestResponse.failed(APIErrors.LOGIN_ERROR_PWD_MISTAKE);
            }
        }catch (OcUserNotFoundException e){
            logger.info("用户登录失败:{}:{} \r\n",username,password);
            if(logger.isDebugEnabled()){
                logger.error("用户登录失败：",e);
            }
            return PortalRestResponse.failed(APIErrors.LOGIN_ERROR_USER_NOT_FOUND);
        }catch (Exception ex){
            logger.info("用户登录失败:{}:{} \r\n",username,password);
            if(logger.isDebugEnabled()){
                logger.error("用户登录失败：",ex);
            }
            return PortalRestResponse.failed(APIErrors.LOGIN_ERROR);
        }
    }




}
