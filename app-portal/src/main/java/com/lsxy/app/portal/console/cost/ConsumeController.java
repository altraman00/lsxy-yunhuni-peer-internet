package com.lsxy.app.portal.console.cost;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.framework.api.consume.model.Consume;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 *消费记录控制器
 * Created by liups on 2016/7/1.
 */
@Controller
@RequestMapping("/console/cost/consume")
public class ConsumeController extends AbstractPortalController {
    private static final Logger logger = LoggerFactory.getLogger(ConsumeController.class);
    private String restPrefixUrl = SystemConfig.getProperty("portal.rest.api.url");
    /**
     * 返回消费记录
     * @param request
     * @return
     */
    @RequestMapping(value = "/index")
    public ModelAndView index(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize){
        RestResponse<Page<Consume>> restResponse = getPageList(request,pageNo,pageSize);
        Page<Consume> pageObj = restResponse.getData();
        ModelAndView mav = new ModelAndView();
        mav.addObject("pageObj",pageObj);
        mav.setViewName("/console/cost/consume/index");
        return mav;
    }
    /**
     * 获取消费记录的分页信息
     * @param request
     * @param pageNo 第几页
     * @param pageSize 一页多少数据
     * @return
     */
    private RestResponse getPageList(HttpServletRequest request, Integer pageNo, Integer pageSize){
        String token = getSecurityToken(request);
        String uri = restPrefixUrl + "/rest/consume/page?pageNo={1}&pageSize={2}";
        return RestRequest.buildSecurityRequest(token).getPage(uri,Consume.class ,pageNo,pageSize);
    }
}
