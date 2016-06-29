package com.lsxy.app.portal.rest.console.home.vo;

import com.lsxy.app.portal.rest.comm.PortalConstants;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yuhuni.billing.model.Billing;

import java.text.DecimalFormat;

/**
 * Created by liups on 2016/6/27.
 */
public class HomeVOMaker {

    public static HomeVO getHomeVO(String token){
        HomeVO vo = new HomeVO();
        //此处调用restApi
        String url = PortalConstants.REST_PREFIX_URL + "/rest/billing/get";
        RestResponse<Billing> response = RestRequest.buildSecurityRequest(token).post(url,null,Billing.class);

        //获取账务
        Billing billing = response.getData();
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
        //会议剩余量(暂时给个数字)
        vo.setConferenceRemain(billing.getConferenceRemain());

        //当前线路情况
        vo.setLineNum(10);

        return null;
    }

}
