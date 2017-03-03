package com.lsxy.app.portal.rest.app;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.call.center.api.model.AppExtension;
import com.lsxy.call.center.api.model.CallCenterAgent;
import com.lsxy.call.center.api.service.AppExtensionService;
import com.lsxy.call.center.api.service.CallCenterAgentService;
import com.lsxy.call.center.api.service.ConditionService;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;
import com.lsxy.yunhuni.api.apicertificate.service.ApiCertificateSubAccountService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by zhangxb on 2016/10/21.
 */
@RequestMapping("/rest/callcenter")
@RestController
public class AppCallcenterController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(AppCallcenterController.class);

    @Reference(timeout = 3000,check = false,lazy = true)
    private AppExtensionService appExtensionService;
    @Reference(timeout=3000,check = false,lazy = true)
    private CallCenterAgentService callCenterAgentService;
    @Reference(timeout=3000,check = false,lazy = true)
    private ConditionService conditionService;
    @Autowired
    private ApiCertificateSubAccountService apiCertificateSubAccountService;

    private String getSubIdsByCerbId(String cerbId){
        StringBuilder stringBuilder = new StringBuilder();
        if(StringUtils.isNoneEmpty(cerbId)){
            List<ApiCertificateSubAccount> list = apiCertificateSubAccountService.getListByCerbId(cerbId);
            for (int i = 0; i < list.size(); i++) {
                stringBuilder.append( "'"+list.get(i).getId()+"'" );
                if( (list.size()-1) != i){
                    stringBuilder.append(",");
                }
            }
        }
        return stringBuilder.toString();
    }
    @RequestMapping("/{appId}/app_extension/page")
    public RestResponse listExtensions(HttpServletRequest request, @PathVariable String appId,
                                       @RequestParam(defaultValue = "1",required = false) Integer  pageNo,
                                       @RequestParam(defaultValue = "20",required = false)  Integer pageSize,String extensionNum,String subId) throws YunhuniApiException {
        subId = getSubIdsByCerbId(subId);
        Page page = appExtensionService.getPageByCondition(appId,extensionNum,subId,pageNo,pageSize);
        return RestResponse.success(page);
    }
    @RequestMapping("/{appId}/queue/page")
    public RestResponse listQueueCondition(HttpServletRequest request, @PathVariable String appId,
                                       @RequestParam(defaultValue = "1",required = false) Integer  pageNo,
                                       @RequestParam(defaultValue = "20",required = false)  Integer pageSize,String queueNum,String subId) throws YunhuniApiException {
        subId = getSubIdsByCerbId(subId);
        Page page = conditionService.getPageByCondition(pageNo,pageSize,getCurrentAccount().getTenant().getId(),appId,subId,queueNum);
        return RestResponse.success(page);
    }
    @RequestMapping(value = "/{appId}/agent/page",method = RequestMethod.GET)
    public RestResponse page(HttpServletRequest request, @PathVariable String appId,
                             @RequestParam(defaultValue = "1",required = false) Integer  pageNo,
                             @RequestParam(defaultValue = "20",required = false)  Integer pageSize,String agentNum,String subId) throws YunhuniApiException {
        subId = getSubIdsByCerbId(subId);
        Page page  = callCenterAgentService.getPageForPotal(appId,pageNo,pageSize,agentNum,subId);
        return RestResponse.success(page);
    }

    @RequestMapping(value = "/{appId}/agent/delete/{agentId}",method = RequestMethod.GET)
    public RestResponse agentDelete(HttpServletRequest request, @PathVariable String appId,@PathVariable String agentId) throws YunhuniApiException {
        CallCenterAgent callCenterAgent = callCenterAgentService.findById(agentId);
        if(callCenterAgent!= null && appId.equals( callCenterAgent.getAppId() )){
            //String tenantId, String appId,String subaccountId, String agentName, boolean force 强制注销
            callCenterAgentService.logout(callCenterAgent.getTenantId(), appId, callCenterAgent.getSubaccountId(),callCenterAgent.getName(),true);
            return RestResponse.success("删除成功");
        }else{
            return RestResponse.failed("","坐席不存在或者不属于该应用");
        }
    }
    @RequestMapping(value = "/{appId}/app_extension/delete/{extensionId}",method = RequestMethod.GET)
    public RestResponse extensionDelete(HttpServletRequest request, @PathVariable String appId,@PathVariable String extensionId) throws YunhuniApiException {
        AppExtension appExtension = appExtensionService.findById(extensionId);
        if(appExtension!= null && appId.equals( appExtension.getAppId() )){
            appExtensionService.delete(extensionId, appId, appExtension.getSubaccountId());
            return RestResponse.success("删除成功");
        }else{
            return RestResponse.failed("","分机不存在或者不属于该应用");
        }
    }
    @RequestMapping(value = "/{appId}/app_extension/new",method = RequestMethod.GET)
    public RestResponse page(HttpServletRequest request, @PathVariable String appId, String subId,String user,String password) throws YunhuniApiException {
        if(StringUtils.isEmpty(subId)){
            subId = null;
        }else {
           ApiCertificateSubAccount apiCertificateSubAccount =  apiCertificateSubAccountService.findByCerbId(subId);
           if(apiCertificateSubAccount != null){
               subId = apiCertificateSubAccount.getId();
           }else{
               subId = null;
           }
        }
        AppExtension appExtension = appExtensionService.create( appId, subId,new AppExtension(AppExtension.TYPE_SIP,user,password));
        if(appExtension != null) {
            return RestResponse.success("成功");
        }else{
            return RestResponse.failed("","失败");
        }
    }
}
