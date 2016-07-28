package com.lsxy.app.portal.loginandregister;

import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.app.portal.security.AvoidDuplicateSubmission;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 修改邮箱
 * Created by zhangxb on 2016/7/28.
 */
@Controller
@RequestMapping("/modify")
public class ModifyEmailController {
    @Autowired
    private RedisCacheService cacheManager;

    @RequestMapping(value = "/reset_password",method = RequestMethod.POST)
    @AvoidDuplicateSubmission(needRemoveToken = true) //需要检验token防止重复提交的方法用这个
    public ModelAndView resetPassword(HttpServletRequest request,String id,String code){
        String key = "account_modify_email_"+id;
        String re = cacheManager.get(key);
        if(!StringUtils.isEmpty(code)&&!StringUtils.isEmpty(re)){
            Map map = JSONUtil.parseObject(re);
            if(code.equals(map.get("code"))){//验证成功，进行邮箱修改
                String email = (String)map.get("email");
                String url = PortalConstants.REST_PREFIX_URL + "/rest/account/safety/modify/email?id={1}&email={2}";
                RestResponse restResponse = RestRequest.buildRequest().get(url,null,id,email);
                if(restResponse.isSuccess()){//修改成功

                }else{//修改失败

                }
            }
        }
        cacheManager.del(key);
        return null;
    }

}
