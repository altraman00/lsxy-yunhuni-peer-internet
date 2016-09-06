package com.lsxy.app.portal.loginandregister;

import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.web.rest.RestResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 登录处理器
 * Created by liups on 2016/7/6.
 */
@Controller
public class LoginController {
    @RequestMapping("/login")
    public ModelAndView login(HttpServletRequest request,HttpServletResponse response){
        String xRequestedWith = request.getHeader("X-Requested-With");
        //处理ajax异常的时候返回登陆页面
        if (StringUtils.isNotBlank(xRequestedWith) && "XMLHttpRequest".equals(xRequestedWith)) {
            String result = JSONUtil.objectToJson(RestResponse.failed("0010","登录超时"));
            try {
                response.getWriter().write(result);
            }catch (Exception e){}
            return null;
        }else {
            String toUrl = "login";
            return new ModelAndView(toUrl);
        }
    }
}
