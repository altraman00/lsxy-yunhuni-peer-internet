package com.lsxy.app.portal.console.cost;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.consume.model.Consume;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 *消费记录控制器
 * Created by liups on 2016/7/1.
 */
@Controller
@RequestMapping("/console/cost/consume")
public class ConsumeController extends AbstractPortalController {
    private static final Logger logger = LoggerFactory.getLogger(ConsumeController.class);
    /**
     * 返回消费记录
     * @param request
     * @return
     */
    @RequestMapping(value = "")
    public ModelAndView index(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize,
                              String appId,String startTime, String endTime){
        if(startTime==null){startTime = DateUtils.formatDate(new Date(),"yyyy-MM");}
        if(endTime==null){endTime = DateUtils.formatDate(new Date(),"yyyy-MM");}
        RestResponse<Page<Consume>> restResponse = getPageList(request,pageNo,pageSize,startTime,endTime,appId);
        Page<Consume> pageObj = restResponse.getData();
        ModelAndView mav = new ModelAndView();
        mav.addObject("appList",getAppList(request).getData());
        mav.addObject("pageObj",pageObj);
        mav.addObject("startTime",startTime);
        mav.addObject("endTime",endTime);
        mav.addObject("appId",appId);
        mav.setViewName("/console/cost/consume/index");
        return mav;
    }
    /**
     * 返回消费记录
     * @param request
     * @return
     */
    @RequestMapping(value = "/download")
    public void download(HttpServletRequest request, HttpServletResponse response, String appId, String startTime, String endTime){
        if(startTime==null){startTime = DateUtils.formatDate(new Date(),"yyyy-MM");}
        if(endTime==null){endTime = DateUtils.formatDate(new Date(),"yyyy-MM");}
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL  + "/rest/consume/list?startTime={1}&endTime={2}&appId={3}";
        RestResponse<List<Consume>> restResponse = RestRequest.buildSecurityRequest(token).getList(uri,Consume.class ,startTime,endTime,appId);
        List<Consume> list = restResponse.getData();
        String appName = "";
        if(StringUtils.isNotEmpty(appId)){
            appName = ((App)getAppById(request,appId).getData()).getName();
        }else{
            appName = "全部";
        }
        downloadExcel("消费记录","消费记录 时间："+startTime+"至"+endTime +"  应用："+appName,new String[]{"消费时间","消费金额","消费类型"},new String[]{"dt","amount","type"},list,null,"amount",response);
    }
    /**
     * 获取消费记录的分页信息
     * @param request
     * @param pageNo 第几页
     * @param pageSize 一页多少数据
     * @return
     */
    private RestResponse getPageList(HttpServletRequest request, Integer pageNo, Integer pageSize,String startTime,String endTime,String appId){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL  + "/rest/consume/page?pageNo={1}&pageSize={2}&startTime={3}&endTime={4}&appId={5}";
        return RestRequest.buildSecurityRequest(token).getPage(uri,Consume.class ,pageNo,pageSize,startTime,endTime,appId);
    }
}
