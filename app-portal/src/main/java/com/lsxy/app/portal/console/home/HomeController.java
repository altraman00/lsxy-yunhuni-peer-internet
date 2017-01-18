package com.lsxy.app.portal.console.home;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificate;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.framework.api.billing.model.Billing;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourcesRent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 登录后的首页
 * Created by liups on 2016/6/27.
 */
@Controller
@RequestMapping("/console/home")
public class HomeController extends AbstractPortalController {

    public final static String CERT_REST_PREFIX = SystemConfig.getProperty("api.gateway.url","http://api.yunhuni.com");
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    /**
     * 返回首页页面
     */
    @RequestMapping(value = "",method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request){
        Map<String,Object> model = new HashMap<>();
        String token = getSecurityToken(request);
        HomeVO homeVO = buildHomeVO(token);
        model.put("homeVO",homeVO);
        return new ModelAndView("console/home/index",model);
    }
    /**
     * 用户变更secretKey所调用的接口
     */
    @RequestMapping(value = "/change_sk",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse changeSecretKey(HttpServletRequest request){
        String token = getSecurityToken(request);
        //调用restApi改变secretKey
        String url = PortalConstants.REST_PREFIX_URL + "/rest/api_cert/change_sk";
        RestResponse<String> response = RestRequest.buildSecurityRequest(token).get(url,String.class);
        return response;
    }

    /**
     * 获取今日数据和本月数据
     * @param request
     * @param type
     * @return
     */
    @RequestMapping(value = "/get_avg_der/{type}",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse getAvgDer(HttpServletRequest request,@PathVariable String type){
        String token = getSecurityToken(request);
        String searchType;
        if("today".equals(type)) {
            searchType = "today";
        }else {
            searchType = "month";
        }
        String url = PortalConstants.REST_PREFIX_URL +   "/rest/day_statics/get?type={1}";
        return RestRequest.buildSecurityRequest(token).get(url, Map.class,searchType);
    }

    /**
     * 根据token构建首页vo
     * @param token
     * @return
     */
    private HomeVO buildHomeVO(String token){
        HomeVO vo = new HomeVO();
        RestResponse<Billing> billingResponse = getBilling(token);
        //获取未读消息
        Long  messageNum = (Long)countMessage(token).getData();
        vo.setMessageNum(messageNum);
        //获取账务
        Billing billing = billingResponse.getData();
        if(billing != null){
            //取出套餐剩余数据
            //余额正数部分
            BigDecimal balance = billing.getBalance();
            if(balance.compareTo(BigDecimal.ZERO) == -1){
                vo.setArrearage("欠费");
            }
            int vTemp = balance.intValue();
            vTemp = Math.abs(vTemp);
            vo.setBalanceInt(vTemp+"");
            //余额小数部分
            DecimalFormat df   = new DecimalFormat("######0.000");
            String format = df.format(billing.getBalance());
            vo.setBalanceDec(format.substring(format.indexOf('.') + 1, format.length()));
            //语音剩余量
            vo.setVoiceRemain(billing.getVoiceRemain()/60);
            //短信剩余量
            vo.setSmsRemain(billing.getSmsRemain());
            //会议剩余量
            vo.setConferenceRemain(billing.getConferenceRemain()/60);

            Long fileTotalSize = billing.getFileTotalSize()/(1024 * 1024);
            Long fileRemainSize = billing.getFileRemainSize()/(1024 * 1024);
            vo.setFileUsedSize(fileTotalSize - fileRemainSize);
            vo.setFileTotalSize(fileTotalSize);
        }else{
            throw new RuntimeException("系统出错，账务表不存在，请联系管理员");
        }

        //TODO 获取当前线路状况，从redis里取
        //当前线路情况(暂时给个数字)
        vo.setLineNum(1);

        //获取通话状况
        Map callStatus = getAvgDdr(token,"all");
        //总平均通话时长
        vo.setLineAverageCallTime(callStatus.get("avgCostTime"));
        //总接通率
        vo.setLineLinkRate(callStatus.get("avgCall"));

        //此处调用鉴权账号（凭证）RestApi
        ApiCertificate cert = getApiCertificate(token);
        if(cert != null){
            vo.setRestApi(CERT_REST_PREFIX + "/"+SystemConfig.getProperty("api.gateway.version","v1")+"/account/" + cert.getCertId() + "/");
            vo.setCertId(cert.getCertId());
            vo.setSecretKey(cert.getSecretKey());
        }
        List<App> appList = getApps(token);

        vo.setAppSize(appList.size());
        int onlineApp = 0;
        if(appList != null){
            for(App app:appList){
                if(app.getStatus() == App.STATUS_ONLINE){
                    onlineApp++;
                }
            }
        }
        vo.setOnLineApp(onlineApp);
        vo.setTime(org.apache.tools.ant.util.DateUtils.format(new Date(),"yyyy-MM"));
        return vo;
    }

    private Map getCallStatus(String token){
        String url = PortalConstants.REST_PREFIX_URL +   "/rest/voice_cdr_hour/call_status";
        return RestRequest.buildSecurityRequest(token).get(url, Map.class).getData();
    }

    private Map getAvgDdr(String token,String type){
        String url = PortalConstants.REST_PREFIX_URL +   "/rest/day_statics/get?type={1}";
        return RestRequest.buildSecurityRequest(token).get(url, Map.class,type).getData();
    }
    /**
     * 获取未读消息
     * @return
     */
    private RestResponse countMessage(String token){
        String uri = PortalConstants.REST_PREFIX_URL +   "/rest/message/account_message/count?status=0";
        return  RestRequest.buildSecurityRequest(token).get(uri, Long.class);
    }
    /**
     * 获取账务信息
     * @param token
     * @return
     */
    private RestResponse<Billing> getBilling(String token) {
        //此处调用账务restApi
        String billingUrl = PortalConstants.REST_PREFIX_URL + "/rest/billing/get";
        return RestRequest.buildSecurityRequest(token).get(billingUrl,Billing.class);
    }

    /**
     * 获取App统计数据
     * @param token
     * @param app
     * @return
     */
    private Map getStatistics(String token, App app) {
        //获取当前App的统计数据
        String appStatisticsUrl = PortalConstants.REST_PREFIX_URL + "/rest/voice_cdr/current_record_statistics?appId={1}";
        RestResponse<Map> statisticsResponse = RestRequest.buildSecurityRequest(token).get(appStatisticsUrl, Map.class,app.getId());
        return statisticsResponse.getData();
    }

    /**
     * 获取应用列表
     * @param token
     * @return
     */
    private List<App> getApps(String token) {
        //调用应用RestApi
        //app列表url
        String appListUrl = PortalConstants.REST_PREFIX_URL + "/rest/app/list";
        //应用统计
        RestResponse<List<App>> appResponse = RestRequest.buildSecurityRequest(token).getList(appListUrl, App.class);
        return appResponse.getData();
    }

    /**
     * 获取鉴权账号（凭证）
     * @param token
     * @return
     */
    private ApiCertificate getApiCertificate(String token) {
        String certUrl = PortalConstants.REST_PREFIX_URL + "/rest/api_cert/get";
        RestResponse<ApiCertificate> certResponse = RestRequest.buildSecurityRequest(token).get(certUrl,ApiCertificate.class);
        return certResponse.getData();
    }

}
