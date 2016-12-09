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
import com.lsxy.yunhuni.api.exceptions.TeleNumberBeOccupiedException;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourceTelenumService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应用上线动作
 * Created by liups on 2016/7/15.
 */
@RequestMapping("/rest/app_online")
@RestController
public class AppOnlineActionControlller extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(AppOnlineActionControlller.class);

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
    @Autowired
    ResourcesRentService resourcesRentService;

    @RequestMapping("/{appId}")
    public RestResponse<AppOnlineAction> getOnlineAction(@PathVariable String appId){
        AppOnlineAction action = appOnlineActionService.findActiveActionByAppId(appId);
        return RestResponse.success(action);
    }

    /**
     * 用户应用进入选号步骤，返回选号池
     * @param appId 应用ID
     * @return
     */
    @RequestMapping("/select_num/{appId}")
    public RestResponse<Map> getSelectNum(@PathVariable String appId){
        Map<String,Object> result = new HashMap<>();
        String userName = getCurrentAccountUserName();
        Tenant tenant = tenantService.findTenantByUserName(userName);
        boolean isBelong = appService.isAppBelongToUser(userName, appId);
        if(isBelong){
            String lastOnlineNums = appOnlineActionService.findLastOnlineNums(appId);
            App app = appService.findById(appId);
            //获取用户拥有的空闲号
            List<ResourceTelenum> ownUnusedNums = resourcesRentService.findOwnUnusedNum(tenant);
            List<Map<String,Object>> telNums = new ArrayList<>();
            boolean hasCalled = false;
            for(ResourceTelenum telNumber:ownUnusedNums){
                if(telNumber != null){
                    Map<String,Object> map = new HashMap<>();
                    map.put("phone",telNumber.getTelNumber());
                    //获取相关属性
                    map.put("isCalled",StringUtils.isBlank(telNumber.getIsCalled())?ResourceTelenum.ISCALLED_FALSE:telNumber.getIsCalled());
                    //如果有可呼入的号码
                    if(ResourceTelenum.ISCALLED_TRUE.equals(telNumber.getIsCalled())){
                        hasCalled = true;
                    }
                    if(ResourceTelenum.ISDIALING_TRUE.equals(telNumber.getIsDialing()) || ResourceTelenum.ISTHROUGH_TRUE.equals(telNumber.getIsThrough())){
                        map.put("isDialing","1");
                    }else{
                        map.put("isDialing","0");
                    }
                    map.put("areaCode",telNumber.getAreaCode());
                    if(StringUtils.isNotBlank(lastOnlineNums) && lastOnlineNums.contains(telNumber.getTelNumber())){
                        map.put("lastUsed",true);
                    }
                    telNums.add(map);
                }
            }
            appOnlineActionService.actionOfSelectNum(appId);
            if((app.getIsIvrService() != null && app.getIsIvrService() == 1) || (app.getIsCallCenter() != null && app.getIsCallCenter() == 1)){
                //如果 没有呼出号码，而且上线的应用需要呼出号码，则将needCalledNum设为true
                if(hasCalled == false){
                    result.put("needCalledNum",true);
                }
            }
            result.put("ownNums",telNums);
            return RestResponse.success(result);
        }else{
            return RestResponse.failed("0000","应用不属于用户");
        }
    }

    /**
     * 支付上线
     * @param appId
     * @return
     */
    @RequestMapping("/online")
    public RestResponse pay(String appId,String nums){
        String userName = getCurrentAccountUserName();
        Tenant tenant = tenantService.findTenantByUserName(userName);
        boolean isBelong = appService.isAppBelongToUser(userName, appId);
        if(isBelong){
            AppOnlineAction action = null;
            try {
                action = appOnlineActionService.actionOfOnline(tenant,appId,nums);
                return RestResponse.success(action);
            } catch (TeleNumberBeOccupiedException e) {

                return RestResponse.failed("0000","IVR号码已被占用，请重新选择号码！");
            }
        }else{
            return RestResponse.failed("0000","应用不属于用户");
        }
    }



    /**
     * 下线
     * @param appId
     * @return
     */
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

}


