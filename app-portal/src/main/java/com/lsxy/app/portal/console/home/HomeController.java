package com.lsxy.app.portal.console.home;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yuhuni.api.apicertificate.model.ApiCertificate;
import com.lsxy.yuhuni.api.app.model.App;
import com.lsxy.yuhuni.api.billing.model.Billing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Map changeSecretKey(HttpServletRequest request){
        Map<String,Object> result = new HashMap<>();
        String token = getSecurityToken(request);
        //调用restApi改变secretKey
        String url = PortalConstants.REST_PREFIX_URL + "/rest/api_cert/change_sk";
        RestResponse<String> response = RestRequest.buildSecurityRequest(token).get(url,String.class);
        if(response.isSuccess()){
            String secretKey = response.getData();
            if(!StringUtils.isEmpty(secretKey)){
                //成功返回新的secretKey
                result.put("secretKey",secretKey);
            }
        }else{
            //失败返回失败信息
            result.put("errorCode",response.getErrorCode());
            result.put("errorMsg",response.getErrorMsg());
        }
        return result;
    }


    /**
     * 根据token构建首页vo
     * @param token
     * @return
     */
    private HomeVO buildHomeVO(String token){
        HomeVO vo = new HomeVO();
        RestResponse<Billing> billingResponse = getBilling(token);

        //获取账务
        Billing billing = billingResponse.getData();
        //余额正数部分
        vo.setBalanceInt(billing.getBalance().intValue()+"");
        //余额小数部分
        DecimalFormat df   = new DecimalFormat("######0.00");
        String format = df.format(billing.getBalance());
        vo.setBalanceDec(format.substring(format.indexOf('.') + 1, format.length()));
        //语音剩余量
        vo.setVoiceRemain(billing.getVoiceRemain());
        //短信剩余量
        vo.setSmsRemain(billing.getSmsRemain());
        //会议剩余量
        vo.setConferenceRemain(billing.getConferenceRemain());

        //当前线路情况(暂时给个数字)
        vo.setLineNum(10);

        //此处调用鉴权账号（凭证）RestApi
        ApiCertificate cert = getApiCertificate(token);
        vo.setRestApi(CERT_REST_PREFIX + "/" + cert.getCertId() + "/");
        vo.setSecretKey(cert.getSecretKey());
        List<App> appList = getApps(token);

        List<AppStateVO> appStateVOs = new ArrayList<>();
        if(appList != null){
            for(App app:appList){
                AppStateVO appStateVO = new AppStateVO();
                try {
                    BeanUtils.copyProperties2(appStateVO,app,false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Map map = getStatistics(token, app);

                appStateVO.setCallOfDay((Integer) map.get("dayCount"));
                appStateVO.setCallOfHour((Integer) map.get("hourCount"));
                appStateVO.setCurrentCall((Integer) map.get("currentSession"));
                appStateVOs.add(appStateVO);
            }
        }
        vo.setAppStateVOs(appStateVOs);
        return vo;
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
        String appStatisticsUrl = PortalConstants.REST_PREFIX_URL + "/rest/callrecord/current_record_statistics?appId={1}";
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
