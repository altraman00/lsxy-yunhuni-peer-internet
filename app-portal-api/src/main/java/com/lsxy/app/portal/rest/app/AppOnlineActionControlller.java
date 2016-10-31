package com.lsxy.app.portal.rest.app;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.model.AppOnlineAction;
import com.lsxy.yunhuni.api.app.service.AppOnlineActionService;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.exceptions.NotEnoughMoneyException;
import com.lsxy.yunhuni.api.exceptions.TeleNumberBeOccupiedException;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourceTelenumService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
//    public static final String ALTERNATIVE_IVR_PREFIX = "IVR_TELENUM_SEL_";     //个人备选IVR号存在redis中的前缀

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
        Map result = new HashMap();
//        String[] selectNum = null;
        String userName = getCurrentAccountUserName();
        Tenant tenant = tenantService.findTenantByUserName(userName);
        boolean isBelong = appService.isAppBelongToUser(userName, appId);
        if(isBelong){
            App app = appService.findById(appId);
            //获取用户拥有的空闲号
            List<Map<String,Object>> ownUnusedNum = resourcesRentService.findOwnUnusedNum(tenant);
            result.put("ownNum",ownUnusedNum);
            //从号码池中选出5个空闲的号码放到Redis供用户选择(若Redis已有，则直接取Redis中的值)
//            String temStr = redisCacheService.get(ALTERNATIVE_IVR_PREFIX + tenant.getId());
//            String areaId = null;
//            if(app.getArea() != null){
//                areaId = app.getArea().getId();
//            }
//            if(StringUtils.isBlank(temStr)) {
//                temStr = getFreeNumber(5,areaId);
//            }
//            if(StringUtils.isNotBlank(temStr)){
//                redisCacheService.set(ALTERNATIVE_IVR_PREFIX + tenant.getId(),temStr,30*60);
//                selectNum = temStr.split(",");
//            }
//
//            result.put("selectIvr",selectNum);
            //如果号码池中没有号码，用户也没有空闲的号码，则抛出异常
            if((ownUnusedNum == null || ownUnusedNum.size() <= 0)
//                    && (selectNum == null || selectNum.length <= 0)
                    ){
                return RestResponse.failed("0000","没有可用呼出号码");
            }else{
                appOnlineActionService.actionOfSelectNum(appId);
            }
            return RestResponse.success(result);
        }else{
            return RestResponse.failed("0000","应用不属于用户");
        }
    }

    /**
     * 获取或生成支付订单
     * @param appId
     * @param ivr
     * @return
     */
//    @RequestMapping("/get_pay")
//    public RestResponse getPay(String appId,String ivr){
//        String userName = getCurrentAccountUserName();
//        boolean isBelong = appService.isAppBelongToUser(userName, appId);
//        Tenant tenant = tenantService.findTenantByUserName(userName);
//        if(isBelong){
//            boolean contains = false;
//            String temStr = redisCacheService.get(ALTERNATIVE_IVR_PREFIX + tenant.getId());
//            if(StringUtils.isNotBlank(temStr)){
//                contains = temStr.contains(ivr.trim());//可选号码池中是否存在该ivr
//            }
//            AppOnlineAction action = appOnlineActionService.actionOfInPay(appId,ivr,tenant,contains);
//            return RestResponse.success(action);
//        }else{
//            return RestResponse.failed("0000","应用不属于用户");
//        }
//    }

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
            App app = appService.findById(appId);
            try {
                action = appOnlineActionService.actionOfOnline(tenant,appId,nums);
                //将号码池中的号码清掉
//                redisCacheService.del(ALTERNATIVE_IVR_PREFIX + tenant.getId());
                return RestResponse.success(action);
//            } catch (NotEnoughMoneyException e) {
//                logger.error("支付余额不足",e);
//                return RestResponse.failed("0000","余额不足");
            } catch (TeleNumberBeOccupiedException e) {
                //号码资源被占用，则清空redis缓存的号码，重新从号码池中取新的号码
//                logger.error("选定号码已被占用",e);
//                String areaId = null;
//                if(app.getArea() != null){
//                    areaId = app.getArea().getId();
//                }
//                String temStr = getFreeNumber(5,areaId);
//                if(StringUtils.isNotBlank(temStr)){
//                    redisCacheService.set(ALTERNATIVE_IVR_PREFIX + tenant.getId(),temStr,30*60);
//                }
                return RestResponse.failed("0000","IVR号码已被占用，请重新选择号码！");
            }
        }else{
            return RestResponse.failed("0000","应用不属于用户");
        }
    }

//    /**
//     * 直接上线
//     * @param appId
//     * @return
//     */
//    @RequestMapping("/direct_online")
//    public RestResponse directOnline(String appId){
//        String userName = getCurrentAccountUserName();
//        boolean isBelong = appService.isAppBelongToUser(userName, appId);
//        if(isBelong){
//            AppOnlineAction action = appOnlineActionService.actionOfDirectOnline(userName,appId);
//            return RestResponse.success(action);
//        }else{
//            return RestResponse.failed("0000","应用不属于用户");
//        }
//    }

    /**
     * 重选IVR号码
     * @param appId
     * @return
     */
//    @RequestMapping("/reset_ivr")
//    public RestResponse resetIvr(String appId){
//        String userName = getCurrentAccountUserName();
//        boolean isBelong = appService.isAppBelongToUser(userName, appId);
//        if(isBelong){
//            AppOnlineAction action = appOnlineActionService.resetIvr(userName,appId);
//            return RestResponse.success(action);
//        }else{
//            return RestResponse.failed("0000","应用不属于用户");
//        }
//    }

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

    /**
     * 从号码池中选出n个空闲的号码
     * @param n
     * @return
     */
    private String getFreeNumber(Integer n,String areaId){
        String result = null;
        //--start
        //从号码池中选出5个空闲的号码
        List<String> numbers = resourceTelenumService.getFreeTeleNum(n,areaId);
        //--end
        //生成字符串
        if(numbers != null && numbers.size()>0){
            result = StringUtil.join(numbers,",");
        }
        return result;
    }


}


