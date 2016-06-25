package com.lsxy.app.portal.rest.comm;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by liups on 2016/6/22.
 */
@RequestMapping("/mc")
@Controller
public class MCController {
    /**
     * 发送手机验证码，并存到session里
     */
    @RequestMapping(value = "/send",method = RequestMethod.GET)
    @ResponseBody
    public void getMobileCode(HttpServletRequest request,String mobile){
        if(!StringUtils.isEmpty(mobile)){

            Object obj = request.getSession().getAttribute(PortalConstants.MC_KEY);
            if(obj != null && obj instanceof MobileCodeChecker){
                MobileCodeChecker checker = (MobileCodeChecker) obj;
                //判断是否超过时间间隔(没到达时间间隔，则直接返回)
                if(System.currentTimeMillis() < (checker.getCreateTime() + MobileCodeChecker.TIME_INTERVAL)){
                    return;
                }
            }

            //此处调用发送手机验证码服务并返回验证码
            String mobileCode = "1234";
            if(!StringUtils.isEmpty(mobileCode)){
                MobileCodeChecker mobileCodeChecker = new MobileCodeChecker(mobile,mobileCode);
                //存到session里
                request.getSession().setAttribute(PortalConstants.MC_KEY,mobileCodeChecker);
            }
        }

    }

    /**
     * 验证手机验证码
     */
    @RequestMapping(value = "/check",method = RequestMethod.GET)
    @ResponseBody
    public byte checkMobileCode(HttpServletRequest request, String mc){
        //检查手机验证码
        if(!StringUtils.isEmpty(mc)){
            MobileCodeChecker checker;
            Object obj = request.getSession().getAttribute(PortalConstants.MC_KEY);
            if(obj != null && obj instanceof MobileCodeChecker){
                checker = (MobileCodeChecker) obj;
                return MobileCodeChecker.checkCode(mc,checker);
            }else{
                return MobileCodeChecker.STATUS_OVER_TIME;
            }
        }else{
            return MobileCodeChecker.STATUS_ERR_CODE;
        }
    }


}
