package com.lsxy.area.server.util;

import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificate;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;
import com.lsxy.yunhuni.api.apicertificate.service.ApiCertificateService;
import com.lsxy.yunhuni.api.app.model.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by liuws on 2017/2/17.
 */
@Component
public class CallbackUrlUtil {

    @Autowired
    private ApiCertificateService apiCertificateService;

    /***
     * 返回app的回调地址或者子账号的回调地址
     * 如果子账号id为null或者子账号回调地址为空，就使用app的回调地址
     * @param app
     * @param subaccountId
     * @return
     */
    public String get(App app,String subaccountId){
        String url = app.getUrl();
        if(subaccountId!=null){
            ApiCertificate account= apiCertificateService.findById(subaccountId);
            if(account != null && account.getType()!=null && account.getType().equals(ApiCertificate.TYPE_SUBACCOUNT)){
                ApiCertificateSubAccount subAccount = ((ApiCertificateSubAccount)account);
                if(StringUtil.isNotBlank(subAccount.getCallbackUrl())){
                    url = subAccount.getCallbackUrl();
                }
            }
        }
        return url;
    }
}
