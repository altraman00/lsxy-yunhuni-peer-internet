package com.lsxy.app.portal.rest.console.home;

import com.lsxy.app.portal.rest.base.AbstractPortalController;
import com.lsxy.app.portal.rest.comm.PortalConstants;
import com.lsxy.app.portal.rest.console.home.vo.HomeVO;
import com.lsxy.app.portal.rest.console.home.vo.HomeVOManager;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录后的首页
 * Created by liups on 2016/6/27.
 */
@Controller
@RequestMapping("/console/home")
public class HomeController extends AbstractPortalController{
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping(value = "",method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request){
        Map<String,Object> model = new HashMap<>();
        String token = getToken(request);
        HomeVO homeVO = HomeVOManager.getHomeVOByToken(token);
        model.put("homeVO",homeVO);
        return new ModelAndView("console/home/index",model);
    }

    @RequestMapping(value = "/change_sk",method = RequestMethod.GET)
    @ResponseBody
    public Map changeSecretKey(HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        String token = getToken(request);
        String url = PortalConstants.REST_PREFIX_URL + "/rest/api_cert/change_sk";
        RestResponse<String> response = RestRequest.buildSecurityRequest(token).get(url,String.class);
        if(response.isSuccess()){
            String secretKey = response.getData();
            if(!StringUtils.isEmpty(secretKey)){
                result.put("secretKey",secretKey);
            }
        }else{
            result.put("errorCode",response.getErrorCode());
            result.put("errorMsg",response.getErrorMsg());
        }
        return result;
    }

}
