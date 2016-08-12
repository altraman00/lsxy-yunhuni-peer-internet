package com.lsxy.app.oc.open;

import com.lsxy.app.oc.exceptions.APIErrors;
import com.lsxy.app.oc.utils.PortalRestResponse;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.framework.web.rest.UserRestToken;
import com.lsxy.oc.api.exceptions.OcUserNotFoundException;
import com.lsxy.oc.api.user.model.OcUser;
import com.lsxy.oc.api.user.service.OcUserService;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

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
    public RestResponse login(@RequestBody LoginVO ocUser){

        try {
            OcUser user = ocUserService.findUserByLoginNameAndPassword(ocUser.getUserName(), ocUser.getPassword());
            if (user != null) {
                //将token放入redis 与OcUser形成映射关系
                UserRestToken token = UserRestToken.newOcUserToken();
                cacheManager.set(token.getToken(),user.getUserName(),35*60);
                return PortalRestResponse.success(token);
            } else {
                return PortalRestResponse.failed(APIErrors.LOGIN_ERROR_PWD_MISTAKE);
            }
        }catch (OcUserNotFoundException e){
            logger.info("用户登录失败:{}:{} \r\n",ocUser.getUserName(),ocUser.getPassword());
            if(logger.isDebugEnabled()){
                logger.error("用户登录失败：",e);
            }
            return PortalRestResponse.failed(APIErrors.LOGIN_ERROR_USER_NOT_FOUND);
        }catch (Exception ex){
            logger.info("用户登录失败:{}:{} \r\n",ocUser.getUserName(),ocUser.getPassword());
            if(logger.isDebugEnabled()){
                logger.error("用户登录失败：",ex);
            }
            return PortalRestResponse.failed(APIErrors.LOGIN_ERROR);
        }
    }

}
