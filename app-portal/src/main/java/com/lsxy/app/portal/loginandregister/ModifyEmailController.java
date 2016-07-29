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

    @RequestMapping(value = "/email")
    public ModelAndView resetPassword(HttpServletRequest request,String id,String code){
        String key = "account_modify_email_"+id;
        String re = cacheManager.get(key);
        ModelAndView mav = new ModelAndView();
        if(!StringUtils.isEmpty(code)&&!StringUtils.isEmpty(re)){
            Map map = JSONUtil.parseObject(re);
            if(code.equals(map.get("code"))){//验证成功，进行邮箱修改
                String email = (String)map.get("email");
                String url = PortalConstants.REST_PREFIX_URL + "/rest/account/safety/modify/email?id={1}&email={2}";
                RestResponse restResponse = RestRequest.buildRequest().get(url,null,id,email);
                if(restResponse.isSuccess()){//修改成功
                    mav.setViewName("/register/active_result");
                }else{//修改失败
                    mav.addObject("errorCode",restResponse.getErrorCode());
                    mav.addObject("erInfo",restResponse.getErrorMsg());
                    mav.setViewName("/register/active_fail");
                }
            }else{
                mav.addObject("errorCode",-1);
                mav.addObject("erInfo","该链接已失效");
                mav.setViewName("/register/active_fail");
            }
        }else{
            mav.addObject("errorCode","该链接已失效");
            mav.addObject("erInfo","");
            mav.setViewName("/register/active_fail");
        }
        cacheManager.del(key);
        return null;
    }

}
