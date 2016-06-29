package com.lsxy.app.portal.rest.console.home.vo;

import com.lsxy.app.portal.rest.comm.PortalConstants;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yuhuni.apicertificate.model.ApiCertificate;
import com.lsxy.yuhuni.billing.model.Billing;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liups on 2016/6/27.
 */
public class HomeVOBuilder {
    public final static String CERT_REST_PREFIX = "http://api.yunhuni.com";

    public static HomeVO getHomeVO(String token){
        HomeVO vo = new HomeVO();
        //此处调用账务restApi
        String billingUrl = PortalConstants.REST_PREFIX_URL + "/rest/billing/get";
        RestResponse<Billing> billingResponse = RestRequest.buildSecurityRequest(token).get(billingUrl,Billing.class);

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
        String certUrl = PortalConstants.REST_PREFIX_URL + "/rest/api_cert/get";
        RestResponse<ApiCertificate> certResponse = RestRequest.buildSecurityRequest(token).get(certUrl,ApiCertificate.class);
        ApiCertificate cert = certResponse.getData();
        vo.setRestApi(CERT_REST_PREFIX + "/" + cert.getCertId() + "/");
        vo.setSecretKey(cert.getSecretKey());

        //调用应用RestApi
        //app列表url
        String appListUrl = PortalConstants.REST_PREFIX_URL + "/rest/app/list";
        //应用统计
        String appStatisticsUrl = PortalConstants.REST_PREFIX_URL + "/rest/callrecord/current_record_statistics?appId={a}";
        RestResponse<List> appResponse = RestRequest.buildSecurityRequest(token).get(appListUrl, List.class);
        List<Map> appList = appResponse.getData();
        List<AppStateVO> appStateVOs = new ArrayList<>();
        for(Map app:appList){
            AppStateVO appStateVO = new AppStateVO();
            String appId = (String) app.get("id");
            appStateVO.setAppId(appId);
            appStateVO.setName((String) app.get("name"));
            appStateVO.setStatus((Integer) app.get("status"));
            RestResponse<Map> statisticsResponse = RestRequest.buildSecurityRequest(token).get(appStatisticsUrl, Map.class,appId);
            Map map = statisticsResponse.getData();
            appStateVO.setCallOfDay((Integer) map.get("dayCount"));
            appStateVO.setCallOfHour((Integer) map.get("hourCount"));
            appStateVO.setCurrentCall((Integer) map.get("currentSession"));
            appStateVOs.add(appStateVO);
        }

        return vo;
    }

}
