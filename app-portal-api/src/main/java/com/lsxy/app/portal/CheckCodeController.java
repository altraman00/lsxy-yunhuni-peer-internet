package com.lsxy.app.portal;

import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.sms.exceptions.CheckCodeExpireException;
import com.lsxy.framework.sms.exceptions.CheckOutMaxTimesException;
import com.lsxy.framework.sms.exceptions.InvalidValidateCodeException;
import com.lsxy.framework.sms.exceptions.TooManyGenTimesException;
import com.lsxy.framework.sms.service.SmsService;
import com.lsxy.framework.web.rest.RestResponse;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * 验证码
 * Created by liups on 2016/7/7.
 */
@RequestMapping("/code")
public class CheckCodeController {
    @Autowired
    SmsService smsService;

    @Autowired
    private RedisCacheService cacheManager;

    /**
     * 发送短信验证码
     * @param mobile
     * @return
     */
    @RequestMapping("/send_mobile_code")
    public RestResponse sendMobileCode(String mobile){
        String code;
        try {
            code = smsService.genVC(mobile);
        } catch (TooManyGenTimesException e) {
            e.printStackTrace();
            return RestResponse.failed("0000",e.getMessage());
        }
        String template = "01-portal-test-num-bind.vm";
        Map<String,Object> params = new HashedMap();
        params.put("vc",code);
        boolean result = smsService.sendsms(mobile,template,params);
        return RestResponse.success(result);
    }

    /**
     * 校验手机验证码
     * @param mobile
     * @param code
     * @return
     */
    @RequestMapping("/check_mobile_code")
    public RestResponse checkMobileCode(String mobile,String code){
        RestResponse response;
        try {
            boolean flag = smsService.checkVC(mobile, code);
            if(flag){
                response = RestResponse.success(flag);
            }else{
                response = RestResponse.failed("0000","校验出错");
            }
        } catch (InvalidValidateCodeException | CheckOutMaxTimesException | CheckCodeExpireException e) {
            e.printStackTrace();
            response = RestResponse.failed("0000",e.getMessage());
        }
        return response;
    }

    /**
     * 删除Redis里的手机验证码
     * @param mobile
     * @return
     */
    @RequestMapping("remove_mobile_code")
    public RestResponse removeMobileCode(String mobile){
        if(StringUtils.isNotBlank(mobile)){
            cacheManager.del(SmsService.CODE_PREFIX + mobile);
            return RestResponse.success(null);
        }else{
            return RestResponse.failed("0000","空的手机号");
        }
    }

}
