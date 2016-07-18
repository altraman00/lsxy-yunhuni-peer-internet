package com.lsxy.app.portal.rest.app;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.model.AppOnlineAction;
import com.lsxy.yunhuni.api.app.service.AppOnlineActionService;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.exceptions.NotEnoughMoneyException;
import com.lsxy.yunhuni.api.exceptions.TeleNumberBeOccupiedException;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourceTelenumService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 应用上线动作
 * Created by liups on 2016/7/15.
 */
@RequestMapping("/rest/app_online")
@RestController
public class AppOnlineActionControlller extends AbstractRestController {
    public static final String ALTERNATIVE_IVR_PREFIX = "alternative_ivr_";     //个人备选IVR号存在redis中的前缀

    @Autowired
    AppOnlineActionService appOnlineActionService;
    @Autowired
    AppService appService;
    @Autowired
    TenantService tenantService;
    @Autowired
    RedisCacheService redisCacheService;
    @Autowired
    ResourceTelenumService resourceTelenumService;

    @RequestMapping("/{appId}")
    public RestResponse<AppOnlineAction> getOnlineAction(@PathVariable("appId")String appId){
        AppOnlineAction action = appOnlineActionService.findActiveActionByAppId(appId);
        return RestResponse.success(action);
    }

    /**
     * 用户应用进入选号步骤，返回选号池
     * @param appId 应用ID
     * @return
     */
    @RequestMapping("/select_ivr/{appId}")
    public RestResponse<String[]> getSelectIvr(@PathVariable("appId") String appId){
        String[] result;
        String userName = getCurrentAccountUserName();
        Tenant tenant = tenantService.findTenantByUserName(userName);
        boolean isBelong = appService.isAppBelongToUser(userName, appId);
        if(isBelong){
            appOnlineActionService.actionOfSelectNum(appId);
            //从号码池中选出5个空闲的号码放到Redis供用户选择(若Redis已有，则直接取Redis中的值)
            String temStr = redisCacheService.get(ALTERNATIVE_IVR_PREFIX + tenant.getId());
            if(StringUtils.isBlank(temStr)) {
                temStr = getFreeNumber(5);
                redisCacheService.set(ALTERNATIVE_IVR_PREFIX + tenant.getId(),temStr,30*60);
            }
            result = temStr.split(",");
            return RestResponse.success(result);
        }else{
            return RestResponse.failed("0000","应用不属于用户");
        }
    }

    @RequestMapping("/get_pay")
    public RestResponse getPay(String appId,String ivr){
        String userName = getCurrentAccountUserName();
        boolean isBelong = appService.isAppBelongToUser(userName, appId);
        Tenant tenant = tenantService.findTenantByUserName(userName);
        if(isBelong){
            String temStr = redisCacheService.get(ALTERNATIVE_IVR_PREFIX + tenant.getId());
            boolean contains = temStr.contains(ivr.trim());//可选号码池中是否存在该ivr
            AppOnlineAction action = appOnlineActionService.actionOfInPay(appId,ivr,contains);
            return RestResponse.success(action);
        }else{
            return RestResponse.failed("0000","应用不属于用户");
        }
    }

    @RequestMapping("/pay")
    public RestResponse pay(String appId){
        String userName = getCurrentAccountUserName();
        Tenant tenant = tenantService.findTenantByUserName(userName);
        boolean isBelong = appService.isAppBelongToUser(userName, appId);
        if(isBelong){
            AppOnlineAction action = null;
            try {
                action = appOnlineActionService.actionOfOnline(userName,appId);
                return RestResponse.success(action);
            } catch (NotEnoughMoneyException e) {
                e.printStackTrace();
                return RestResponse.failed("0000","余额不足");
            } catch (TeleNumberBeOccupiedException e) {
                //号码资源被占用，则清空redis缓存的号码，重新从号码池中取新的号码
                e.printStackTrace();
                String temStr = getFreeNumber(5);
                redisCacheService.set(ALTERNATIVE_IVR_PREFIX + tenant.getId(),temStr,30*60);
                return RestResponse.failed("0000","IVR号码已被占用，请重新选择号码！");
            }
        }else{
            return RestResponse.failed("0000","应用不属于用户");
        }
    }
    @RequestMapping("/direct_online")
    public RestResponse directOnline(String appId){
        String userName = getCurrentAccountUserName();
        boolean isBelong = appService.isAppBelongToUser(userName, appId);
        if(isBelong){
            AppOnlineAction action = appOnlineActionService.actionOfDirectOnline(userName,appId);
            return RestResponse.success(action);
        }else{
            return RestResponse.failed("0000","应用不属于用户");
        }
    }

    @RequestMapping("/reset_ivr")
    public RestResponse resetIvr(String appId){
        String userName = getCurrentAccountUserName();
        boolean isBelong = appService.isAppBelongToUser(userName, appId);
        if(isBelong){
            AppOnlineAction action = appOnlineActionService.resetIvr(userName,appId);
            return RestResponse.success(action);
        }else{
            return RestResponse.failed("0000","应用不属于用户");
        }
    }

    @RequestMapping("/offline")
    public RestResponse offline(String appId){
        String userName = getCurrentAccountUserName();
        boolean isBelong = appService.isAppBelongToUser(userName, appId);
        if(isBelong){
            App app = appOnlineActionService.offline(appId);
            return RestResponse.success(app);
        }else{
            return RestResponse.failed("0000","应用不属于用户");
        }
    }

    /**
     * 从号码池中选出n个空闲的号码
     * @param n
     * @return
     */
    private String getFreeNumber(Integer n){
        //--start
        //TODO  从号码池中选出5个空闲的号码
        List<String> numbers = resourceTelenumService.getFreeTeleNum(5);
        //--end
        //生成字符串
        StringBuilder builder = new StringBuilder();
        boolean flag=false;
        for(String tem:numbers){
            if (flag) {
                builder.append(",");
            }else {
                flag=true;
            }
            builder.append(tem);
        }

        return builder.toString();
    }


}


