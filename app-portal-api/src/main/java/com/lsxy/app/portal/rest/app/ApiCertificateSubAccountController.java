package com.lsxy.app.portal.rest.app;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.app.portal.rest.app.vo.SubAccountVo;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;
import com.lsxy.yunhuni.api.apicertificate.model.CertAccountQuota;
import com.lsxy.yunhuni.api.apicertificate.model.CertAccountQuotaType;
import com.lsxy.yunhuni.api.apicertificate.service.ApiCertificateSubAccountService;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourceTelenumService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import scala.actors.threadpool.Arrays;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxb on 2017/2/16.
 */
@RestController
@RequestMapping("/rest/sub_account")
public class ApiCertificateSubAccountController extends AbstractRestController {
    private Logger logger = LoggerFactory.getLogger(ApiCertificateSubAccountController.class);
    @Autowired
    private ApiCertificateSubAccountService apiCertificateSubAccountService;
    @Autowired
    private ResourcesRentService resourcesRentService;
    @Autowired
    private ResourceTelenumService resourceTelenumService;
    @Autowired
    private AppService appService;
    @RequestMapping(value = "/new")
    public RestResponse create(HttpServletRequest request,String appId, String url, long seatNum, long voiceNum, String remark){
        List<CertAccountQuota> quotas = new ArrayList<>();
        quotas.add( new CertAccountQuota(CertAccountQuotaType.CallQuota.name(),voiceNum*60) );
        quotas.add( new CertAccountQuota(CertAccountQuotaType.AgentQuota.name(),seatNum*60) );
        apiCertificateSubAccountService.createSubAccount(new ApiCertificateSubAccount( appId,  url,  remark,  quotas));
        return RestResponse.success("成功");
    }
    @RequestMapping(value = "/get/{subId}")
    public RestResponse list(HttpServletRequest request,@PathVariable String subId){
        ApiCertificateSubAccount apiCertificateSubAccount = apiCertificateSubAccountService.findById(subId);
        if(apiCertificateSubAccount != null){
            return RestResponse.success( new SubAccountVo(apiCertificateSubAccount,""));
        }else {
            return RestResponse.failed("","对象不存在");
        }
    }
    @RequestMapping(value = "/list/{appId}")
    public RestResponse list(HttpServletRequest request,@PathVariable String appId,Integer pageNo,Integer pageSize, String certId, String remark, Integer enabled){
        Page<ApiCertificateSubAccount> page = apiCertificateSubAccountService.pageListWithQuotaByCondition(appId,pageNo,pageSize,certId,remark,enabled);
        List<SubAccountVo> list = new ArrayList<>();
        for (int i = 0; i < page.getResult().size(); i++) {
            list.add( new SubAccountVo( page.getResult().get(i)) );
        }
        Page page2 = new Page(page.getStartIndex(),page.getTotalCount(),page.getPageSize(),list);
        return RestResponse.success(page2);
    }
    @RequestMapping(value = "/delete/{subId}")
    public RestResponse delete(HttpServletRequest request,@PathVariable String subId){
        ApiCertificateSubAccount apiCertificateSubAccount = apiCertificateSubAccountService.findById(subId);
        if(apiCertificateSubAccount != null){
            try {
                apiCertificateSubAccountService.delete(apiCertificateSubAccount);
                return RestResponse.success("删除成功");
            } catch (Exception e) {
                logger.error("删除子账户失败",e);
                return RestResponse.failed("","删除失败:"+e.getMessage());
            }
        }else {
            return RestResponse.failed("", "找不到对象");
        }
    }
    @RequestMapping(value = "/enabled/{subId}")
    public RestResponse enabled(HttpServletRequest request,@PathVariable String subId){
        ApiCertificateSubAccount apiCertificateSubAccount = apiCertificateSubAccountService.findById(subId);
        if(apiCertificateSubAccount != null){
            apiCertificateSubAccount.setEnabled(1);
            apiCertificateSubAccountService.save(apiCertificateSubAccount);
            return RestResponse.success("启用成功");
        }else{
            return RestResponse.failed("","找不到对象，启用失败");
        }
    }
    @RequestMapping(value = "/disable/{subId}")
    public RestResponse disable(HttpServletRequest request,@PathVariable String subId){
        ApiCertificateSubAccount apiCertificateSubAccount = apiCertificateSubAccountService.findById(subId);
        if(apiCertificateSubAccount != null){
            apiCertificateSubAccount.setEnabled(0);
            apiCertificateSubAccountService.save(apiCertificateSubAccount);
            return RestResponse.success("禁用成功");
        }else{
            return RestResponse.failed("","找不到对象，禁用失败");
        }
    }
    @RequestMapping(value = "/detail/{subId}")
    public RestResponse detail(HttpServletRequest request,@PathVariable String subId){
        ApiCertificateSubAccount apiCertificateSubAccount = apiCertificateSubAccountService.findByIdWithQuota(subId);
        if(apiCertificateSubAccount != null){
            return RestResponse.success(new SubAccountVo(apiCertificateSubAccount,""));
        }else{
            return RestResponse.failed("","找不到对象");
        }
    }
    @RequestMapping(value = "/edit/{subId}")
    public RestResponse detail(HttpServletRequest request,@PathVariable String subId, String url, long seatNum, long voiceNum, String remark){
        ApiCertificateSubAccount apiCertificateSubAccount = apiCertificateSubAccountService.findByIdWithQuota(subId);
        if(apiCertificateSubAccount != null){
            apiCertificateSubAccount.setCallbackUrl(url);
            apiCertificateSubAccount.setRemark(remark);
            List<CertAccountQuota> quotas = new ArrayList<>();
            quotas.add( new CertAccountQuota(CertAccountQuotaType.CallQuota.name(),voiceNum*60) );
            quotas.add( new CertAccountQuota(CertAccountQuotaType.AgentQuota.name(),seatNum*60) );
            apiCertificateSubAccount.setQuotas(quotas);
            apiCertificateSubAccountService.updateSubAccount(apiCertificateSubAccount);
            return RestResponse.success("修改成功");
        }else{
            return RestResponse.failed("","找不到对象");
        }
    }
    @RequestMapping(value = "/unbind/all/{appId}/{subId}")
    public RestResponse unbindAll(HttpServletRequest request,@PathVariable String appId,@PathVariable String subId){
        Account account = getCurrentAccount();
        resourceTelenumService.subaccountUnbindAll(account.getTenant().getId(),appId,subId);
        return RestResponse.success("删除成功");
    }
    @RequestMapping(value = "/unbind/one/{appId}/{subId}/{numId}")
    public RestResponse unbindOne(HttpServletRequest request,@PathVariable String appId,@PathVariable String subId, @PathVariable String numId){
        Account account = getCurrentAccount();
        resourceTelenumService.subaccountUnbindNum(account.getTenant().getId(),appId,subId,numId);
        return RestResponse.success("删除成功");
    }

    @RequestMapping(value = "/bind/num/{appId}/{subId}")
    public RestResponse bind(HttpServletRequest request,@PathVariable String appId,@PathVariable String subId, String nums){
        Account account = getCurrentAccount();
        App app = appService.findById(appId);
        List numList = null;
        if(StringUtils.isNotBlank(nums)){
            numList = Arrays.asList(nums.split(","));
        }
        ApiCertificateSubAccount apiCertificateSubAccount = apiCertificateSubAccountService.findById(subId);
        if(numList != null && numList.size() > 0&& app != null && apiCertificateSubAccount != null && account.getTenant().getId().equals( app.getTenant().getId() ) ){
            resourcesRentService.bindNumToSubaccount(app, numList,subId);
            return RestResponse.success("绑定测试");
        }else{
            return RestResponse.failed("","应用标识或子账户标识错误或号码为空");
        }
    }
    @RequestMapping(value = "/num/list")
    public RestResponse numList(@RequestParam String appId, @RequestParam String subId, @RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "20")Integer pageSize){
        Page page = resourcesRentService.findBySubaccount(appId,subId,pageNo,pageSize);
        return RestResponse.success(page);
    }
}
