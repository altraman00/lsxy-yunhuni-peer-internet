package com.lsxy.app.oc.rest.tenant;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.oc.rest.tenant.vo.*;
import com.lsxy.call.center.api.model.AppExtension;
import com.lsxy.call.center.api.model.CallCenterAgent;
import com.lsxy.call.center.api.model.Condition;
import com.lsxy.call.center.api.service.AppExtensionService;
import com.lsxy.call.center.api.service.CallCenterAgentService;
import com.lsxy.call.center.api.service.ConditionService;
import com.lsxy.framework.api.billing.model.Billing;
import com.lsxy.framework.api.billing.service.CalBillingService;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.mail.MailConfigNotEnabledException;
import com.lsxy.framework.mail.MailContentNullException;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.msg.api.model.MsgTemplate;
import com.lsxy.msg.api.service.MsgTemplateService;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;
import com.lsxy.yunhuni.api.apicertificate.service.ApiCertificateSubAccountService;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.config.service.TenantConfigService;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;
import com.lsxy.yunhuni.api.file.model.VoiceFileRecord;
import com.lsxy.yunhuni.api.file.service.VoiceFilePlayService;
import com.lsxy.yunhuni.api.file.service.VoiceFileRecordService;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourcesRent;
import com.lsxy.yunhuni.api.resourceTelenum.model.TestNumBind;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourceTelenumService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import com.lsxy.yunhuni.api.resourceTelenum.service.TestNumBindService;
import com.lsxy.yunhuni.api.statistics.service.ConsumeMonthService;
import com.lsxy.yunhuni.api.statistics.service.VoiceCdrMonthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by liups on 2017/1/11.
 */
@Api(value = "租户中心-应用", description = "租户中心-应用相关的接口" )
@RestController
@RequestMapping("/tenant")
public class TenantAppController {

    @Autowired
    private TestNumBindService testNumBindService;
    @Autowired
    private VoiceFilePlayService voiceFilePlayService;
    @Autowired
    private VoiceFileRecordService voiceFileRecordService;
    @Autowired
    private AppService appService;
    @Autowired
    private ConsumeMonthService consumeMonthService;
    @Autowired
    private VoiceCdrMonthService voiceCdrMonthService;
    @Autowired
    private TenantConfigService tenantConfigService;
    @Reference(timeout = 3000,check = false,lazy = true)
    AppExtensionService appExtensionService;
    @Autowired
    private CalBillingService calBillingService;
    @Autowired
    ResourcesRentService resourcesRentService;
    @Autowired
    ResourceTelenumService resourceTelenumService;
    @Reference(timeout=3000,check = false,lazy = true)
    private CallCenterAgentService callCenterAgentService;
    @Autowired
    private ApiCertificateSubAccountService apiCertificateSubAccountService;
    @Reference(timeout=3000,check = false,lazy = true)
    private ConditionService conditionService;
    @Reference(timeout=3000,check = false,lazy = true)
    private MsgTemplateService msgTemplateService;
    @ApiOperation(value = "租户的app列表，以及app上个月指标")
    @RequestMapping(value="/tenants/{id}/apps",method = RequestMethod.GET)
    public RestResponse apps(@PathVariable String id) throws MailConfigNotEnabledException, MailContentNullException {
        List<TenantAppVO> dto = new ArrayList<>();
        List<App> apps = appService.getAppsByTenantId(id);
        if(apps != null && apps.size()>0){
            //获取app上个月的指标
            Date preMonth = DateUtils.getPrevMonth(new Date());
            for (App app : apps) {
                TenantAppVO vo = new TenantAppVO(app);
                vo.setConsume(consumeMonthService.getAmongAmountByDateAndApp(preMonth,app.getId()));
                vo.setAmongDuration(voiceCdrMonthService.getAmongCostTimeByDateAndApp(preMonth,app.getId())/60);
                vo.setSessionCount(voiceCdrMonthService.getAmongCallByDateAndApp(preMonth,app.getId()));
                dto.add(vo);
            }
        }
        return RestResponse.success(dto);
    }

    @ApiOperation(value = "获取租户的app信息")
    @RequestMapping(value="/tenants/{tenant}/apps/{appId}",method = RequestMethod.GET)
    public RestResponse app(@PathVariable String tenant,@PathVariable String appId) {
        App app = appService.findById(appId);

        if(app == null || app.getTenant() == null ||
                !app.getTenant().getId().equals(tenant)){
            return RestResponse.success(null);
        }
        TenantAppVO vo = new TenantAppVO(app);
        List<TestNumBind> tests = testNumBindService.findByTenant(tenant,appId);
        vo.setTestPhone(tests.stream().parallel().map(t -> t.getNumber()).collect(Collectors.toList()));
//        AreaSip areaSip = app.getAreaSip();
//        if(areaSip!=null){
//            vo.setSipRegistrar(app.getAreaSip().getRegistrarIp()+":"+app.getAreaSip().getRegistrarPort());
//        }else{
//            vo.setSipRegistrar("");
//        }
        vo.setSipRegistrar(SystemConfig.getProperty("app.cc.opensips.domain"));
        int re = tenantConfigService.getRecordingTimeByTenantIdAndAppId(app.getTenant().getId(),app.getId());
        String temp = "";
        if(re==7){
            temp="7天免费存储";
        }else if(re%30==0){
            temp = (re/30)+"个月";
        }
        vo.setRecordingTime( temp);
        return RestResponse.success(vo);
    }

    @ApiOperation(value = "获取租户的文件容量和剩余")
    @RequestMapping(value="/tenants/{tenant}/file/totalSize",method = RequestMethod.GET)
    public RestResponse fileTotalSize(@PathVariable String tenant){
        Map map = new HashMap();
        Billing billing = calBillingService.getCalBilling(tenant);
        if(billing!=null){
            Long fileTotalSize = billing.getFileTotalSize();
            map.put("fileRemainSize",fileTotalSize-billing.getFileRemainSize());
            map.put("fileTotalSize",fileTotalSize);
        }else{
            map.put("fileRemainSize",0);
            map.put("fileTotalSize",0);
        }
        return RestResponse.success(map);
    }

    private String getSubIdsByCerbId(String cerbId){
        StringBuilder stringBuilder = new StringBuilder();
        List<ApiCertificateSubAccount> list = apiCertificateSubAccountService.getListByCerbId(cerbId);
        for (int i = 0; i < list.size(); i++) {
            stringBuilder.append("'" + list.get(i).getId() + "'");
            if ((list.size() - 1) != i) {
                stringBuilder.append(",");
            }
        }
        return stringBuilder.toString();
    }
    public Map<String,ApiCertificateSubAccount> getMapSubAccountList(String appId){
        List<ApiCertificateSubAccount> list = apiCertificateSubAccountService.findByAppId(appId);
        Map<String ,ApiCertificateSubAccount> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            map.put(list.get(i).getId(),list.get(i));
        }
        return map;
    }

    @ApiOperation(value = "获取租户的app录音文件列表")
    @RequestMapping(value = "/tenants/{tenant}/apps/{appId}/records",method = RequestMethod.GET)
    public RestResponse records(
            @PathVariable String tenant,@PathVariable String appId,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize){
        Page<VoiceFileRecord> page = voiceFileRecordService.pageList(pageNo,pageSize,appId,tenant);
        return RestResponse.success(page);
    }

    @ApiOperation(value = "批量下载租户的app录音文件")
    @RequestMapping(value="/tenants/{tenant}/apps/{appId}/records/batch/download",method = RequestMethod.GET)
    public RestResponse batchDownload(
            @PathVariable String tenant,@PathVariable String appId,
            @ApiParam(name = "startTime",value = "格式:yyyy-MM")
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") Date startTime,
            @ApiParam(name = "endTime",value = "格式:yyyy-MM")
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") Date endTime){
        return RestResponse.success("url");
    }

    @ApiOperation(value = "下载单个录音文件")
    @RequestMapping(value = "/tenants/{tenant}/apps/{appId}/records/{record}",method = RequestMethod.GET)
    public RestResponse records(
            @PathVariable String tenant,@PathVariable String appId,@PathVariable String record){
        return RestResponse.success("url");
    }

    @ApiOperation(value = "租户的app列表")
    @RequestMapping(value="/tenants/{id}/app/list",method = RequestMethod.GET)
    public RestResponse appList(@PathVariable String id) throws MailConfigNotEnabledException, MailContentNullException {
        return RestResponse.success(appService.getAppsByTenantId(id));
    }


    @ApiOperation(value = "获取应用分机")
    @RequestMapping(value = "/tenants/{tid}/app/{appId}/callcenter/extension",method = RequestMethod.GET)
    public RestResponse<Page<ExtensionVO>> listExtensions(
            HttpServletRequest request,@PathVariable String appId,
            @RequestParam(defaultValue = "1",required = false) Integer  pageNo,
            @RequestParam(defaultValue = "20",required = false)  Integer pageSize,
            @ApiParam(name = "extensionNum",value = "分机号码") @RequestParam(required = false) String extensionNum,
            @ApiParam(name = "subId",value = "子账号ID-鉴权账号ID") @RequestParam(required = false) String subId
    ) throws YunhuniApiException {
            if(org.apache.commons.lang3.StringUtils.isNotEmpty(subId)){
                String subId1 = getSubIdsByCerbId(subId);
                if(org.apache.commons.lang3.StringUtils.isEmpty(subId1)){
                    return RestResponse.success(new Page((pageNo-1)*pageSize,pageNo*pageSize,pageSize,null));
                }else{
                    subId = subId1;
                }
            }
        Page page = appExtensionService.getPageByCondition(appId,extensionNum,subId,pageNo,pageSize);
        if(page != null){
            List<ExtensionVO> returnResult = new ArrayList<>();
            List<AppExtension> result = page.getResult();
            Map<String,ApiCertificateSubAccount> map = getMapSubAccountList(appId);
            if(result != null){
                result.stream().forEach(exception -> returnResult.add(ExtensionVO.changeAppExtensionToExtensionVO(exception,map.get(exception.getSubaccountId()))));
                page.setResult(returnResult);
            }
        }
        return RestResponse.success(page);
    }
    @ApiOperation(value = "获取应用座席")
    @RequestMapping(value = "/tenants/{tid}/app/{appId}/callcenter/agent",method = RequestMethod.GET)
    public RestResponse<Page<AgentVO>> page(
            HttpServletRequest request,
            @PathVariable String appId,
            @RequestParam(defaultValue = "1",required = false) Integer  pageNo,
            @RequestParam(defaultValue = "20",required = false)  Integer pageSize,
            @ApiParam(name = "agentName",value = "坐席名称") @RequestParam(required = false) String agentName,
            @ApiParam(name = "subId",value = "关联子账号（鉴权账号）")@RequestParam(required = false) String subId
    ) throws YunhuniApiException {
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(subId)){
            String subId1 = getSubIdsByCerbId(subId);
            if(org.apache.commons.lang3.StringUtils.isEmpty(subId1)){
                return RestResponse.success(new Page((pageNo-1)*pageSize,pageNo*pageSize,pageSize,null));
            }else{
                subId = subId1;
            }
        }
        Page page  = callCenterAgentService.getPageForPotal(appId,pageNo,pageSize,agentName,subId);
        if(page != null){
            List<AgentVO> returnResult = new ArrayList<>();
            List<CallCenterAgent> result = page.getResult();
            Map<String,ApiCertificateSubAccount> map = getMapSubAccountList(appId);
            if(result != null){
                result.stream().forEach(agent -> returnResult.add(AgentVO.changeCallCenterAgentToAgentVO(agent,map.get(agent.getSubaccountId()))));
                page.setResult(returnResult);
            }
        }
        return RestResponse.success(page);
    }

    @ApiOperation(value = "获取租户的app排队条件列表")
    @RequestMapping(value = "/tenants/{tenant}/apps/{appId}/callcenter/queue",method = RequestMethod.GET)
    public RestResponse listQueueCondition(
            HttpServletRequest request,@PathVariable String tenant,
            @ApiParam(name = "appId",value = "应用标识") @PathVariable String appId,
            @ApiParam(name = "pageNo",value = "第几页") @RequestParam(defaultValue = "1",required = false) Integer  pageNo,
            @ApiParam(name = "pageSize",value = "每页记录数") @RequestParam(defaultValue = "20",required = false)  Integer pageSize,
            @ApiParam(name = "queueId",value = "排队条件ID") @RequestParam(required = false) String queueId,
            @ApiParam(name = "subId",value = "子账号ID") @RequestParam(required = false) String subId
    ) throws YunhuniApiException {
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(subId)){
            String subId1 = getSubIdsByCerbId(subId);
            if(org.apache.commons.lang3.StringUtils.isEmpty(subId1)){
                return RestResponse.success(new Page((pageNo-1)*pageSize,pageNo*pageSize,pageSize,null));
            }else{
                subId = subId1;
            }
        }
        Page<Condition> page = conditionService.getPageByCondition(pageNo,pageSize,tenant,appId,subId,queueId);
        if(page != null){
            List<ConditionVo> returnResult = new ArrayList<>();
            List<Condition> result = page.getResult();
            Map<String,ApiCertificateSubAccount> map = getMapSubAccountList(appId);
            if(result != null){
                for (int i = 0; i < result.size(); i++) {
                    returnResult.add(ConditionVo.changeConditionToConditionVO( result.get(i), map.get(result.get(i).getSubaccountId()) ));
                }
                page.setResult(returnResult);
            }
        }
        return RestResponse.success(page);
    }


    @ApiOperation(value = "获取租户的app放音文件列表")
    @RequestMapping(value = "/tenants/{tenant}/apps/{appId}/plays",method = RequestMethod.GET)
    public RestResponse plays(
            @PathVariable String tenant,@PathVariable String appId,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String subId
    ){
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(subId)){
            String subId1 = getSubIdsByCerbId(subId);
            if(org.apache.commons.lang3.StringUtils.isEmpty(subId1)){
                return RestResponse.success(new Page((pageNo-1)*pageSize,pageNo*pageSize,pageSize,null));
            }else{
                subId = subId1;
            }
        }
        Page<VoiceFilePlay> page = voiceFilePlayService.pageList(pageNo,pageSize,name,appId,new String[]{tenant},1,null,null,subId);
        if(page != null){
            List<VoiceFilePlayVo> returnResult = new ArrayList<>();
            List<VoiceFilePlay> result = page.getResult();
            Map<String,ApiCertificateSubAccount> map = getMapSubAccountList(appId);
            if(result != null){
                result.stream().forEach(exception -> returnResult.add(VoiceFilePlayVo.changeAppVoiceFileToVoiceFileVO(exception,map.get(exception.getSubaccountId()))));
                page.setResult(returnResult);
            }
        }
        return RestResponse.success(page);
    }
    @ApiOperation(value = "租户的app绑定的号码列表")
    @RequestMapping(value = "/tenants/{tid}/res_rent/app/{appId}/res_rent/list" ,method = RequestMethod.GET)
    public RestResponse<AppNumVO> findByAppId(@PathVariable String tid,@PathVariable String appId,
                                              @RequestParam(defaultValue = "1")Integer pageNo,  @RequestParam(defaultValue = "10")Integer pageSize){
        Page<ResourcesRent> page = resourcesRentService.findByAppId(appId, pageNo, pageSize);
        List<ResourcesRent> result = page.getResult();
        if(result != null && result.size() > 0){
            List<AppNumVO> appNumVOs = new ArrayList<>();
            Map<String,ApiCertificateSubAccount> map = getMapSubAccountList(appId);
            for(ResourcesRent rent : result){
                AppNumVO vo = new AppNumVO();
                vo.setRentId(rent.getId());
                vo.setNum(rent.getResourceTelenum().getTelNumber());
                vo.setStatus((rent.getRentExpire() == null || rent.getRentExpire().compareTo(new Date()) == 1 )? "1" : "0");
                vo.setIsCalled(StringUtils.isBlank(rent.getResourceTelenum().getIsCalled())?ResourceTelenum.ISCALLED_FALSE:rent.getResourceTelenum().getIsCalled());
                if(ResourceTelenum.ISDIALING_TRUE.equals(rent.getResourceTelenum().getIsDialing()) || ResourceTelenum.ISTHROUGH_TRUE.equals(rent.getResourceTelenum().getIsThrough())){
                    vo.setIsDialing("1");
                }else{
                    vo.setIsDialing("0");
                }
                vo.setAreaCode(rent.getResourceTelenum().getAreaCode());
                vo.setExpireTime(rent.getRentExpire());
                ApiCertificateSubAccount apiCertificateSubAccount = map.get(rent.getResourceTelenum().getSubaccountId());
                String certId = apiCertificateSubAccount!=null ? apiCertificateSubAccount.getCertId() :"";
                vo.setSubaccountId(rent.getResourceTelenum().getSubaccountId());
                vo.setCertId(certId);
                appNumVOs.add(vo);
            }
            page.setResult(appNumVOs);
        }

        return RestResponse.success(page);
    }
    @ApiOperation(value = "租户的app解绑号码")
    @RequestMapping(value = "/tenants/{tid}/res_rent/app/{appId}/unbind/{rentId}" ,method = RequestMethod.DELETE)
    public RestResponse unbind(@PathVariable String tid,@PathVariable String appId,@PathVariable String rentId){
        resourcesRentService.unbind(tid,appId,rentId);
        return RestResponse.success();
    }

    @ApiOperation(value = "租户的app解绑所有号码")
    @RequestMapping(value = "/tenants/{tid}/res_rent/app/{appId}/unbind_all" ,method = RequestMethod.DELETE)
    public RestResponse unbindAll(@PathVariable String tid,@PathVariable String appId){
        resourcesRentService.appUnbindAll(tid,appId);
        return RestResponse.success();
    }

    @ApiOperation(value = "租户获取能被应用绑定的号码")
    @RequestMapping(value = "/tenants/{tid}/res_rent/num/unused/app/{appId}" ,method = RequestMethod.GET)
    public RestResponse<AppNumVO> findOwnUnusedNum(@PathVariable String tid,@PathVariable String appId,
                                                   @RequestParam(defaultValue = "1")Integer pageNo,  @RequestParam(defaultValue = "10")Integer pageSize){
        App app = appService.findById(appId);
        if(!tid.equals(app.getTenant().getId())){
            return RestResponse.failed("0000","应用不属于用户");
        }
        if(app == null || StringUtils.isBlank(app.getOnlineAreaId())){
            return RestResponse.failed("0000","应用不存在");
        }
        Page page = resourceTelenumService.findOwnUnusedNum(tid, app.getOnlineAreaId(), pageNo, pageSize);
        List<ResourceTelenum> result = page.getResult();
        if(result != null && result.size() > 0){
            List<AppNumVO> appNumVOs = new ArrayList<>();
            for(ResourceTelenum num:result){
                AppNumVO vo = new AppNumVO();
                vo.setNum(num.getTelNumber());
                vo.setIsCalled(StringUtils.isBlank(num.getIsCalled())?ResourceTelenum.ISCALLED_FALSE:num.getIsCalled());
                if(ResourceTelenum.ISDIALING_TRUE.equals(num.getIsDialing()) || ResourceTelenum.ISTHROUGH_TRUE.equals(num.getIsThrough())){
                    vo.setIsDialing("1");
                }else{
                    vo.setIsDialing("0");
                }
                vo.setAreaCode(num.getAreaCode());
                appNumVOs.add(vo);
            }
            page.setResult(appNumVOs);
        }
        return RestResponse.success(page);
    }

    @ApiOperation(value = "租户App绑定号码")
    @RequestMapping(value = "/tenants/{tid}/res_rent/num/bind/app/{appId}" ,method = RequestMethod.POST)
    public RestResponse bindNumToApp(@PathVariable String tid,@PathVariable String appId,@RequestBody AppNumBindVO bindNumVO){
        App app = appService.findById(appId);
        if(!tid.equals(app.getTenant().getId())){
            throw new RuntimeException("应用不属于租户");
        }
        String[] nums = bindNumVO.getNums();
        List numList = null;
        if(nums != null && nums.length > 0){
            numList = Arrays.asList(nums);
        }else{
            return RestResponse.failed("0000","没有选定绑定的号码");
        }
        if(numList != null && numList.size() > 0){
            resourcesRentService.bindNumToAppAndGetAreaId(app,numList,false);
        }
        return RestResponse.success();
    }
    @ApiOperation(value = "获取租户的app子账号列表")
    @RequestMapping(value = "/tenants/{tenant}/apps/{appId}/subs",method = RequestMethod.GET)
    public RestResponse subs(
            @PathVariable String tenant,@PathVariable String appId,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam(name = "subId",value = "子账号ID")@RequestParam(required = false) String subId
    ){
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(subId)){
            String subId1 = getSubIdsByCerbId(subId);
            if(org.apache.commons.lang3.StringUtils.isEmpty(subId1)){
                return RestResponse.success(new Page((pageNo-1)*pageSize,pageNo*pageSize,pageSize,null));
            }else{
                subId = subId1;
            }
        }
        Page<ApiCertificateSubAccount> page = apiCertificateSubAccountService.pageListWithQuotaByCondition(appId,pageNo,pageSize,subId,null,null);
        List<SubAccountVo> list = new ArrayList<>();
        for (int i = 0; i < page.getResult().size(); i++) {
            list.add( new SubAccountVo( page.getResult().get(i)) );
        }
        Page page2 = new Page(page.getStartIndex(),page.getTotalCount(),page.getPageSize(),list);
        return RestResponse.success(page2);
    }
    @ApiOperation(value = "获取租户的app子账号详情")
    @RequestMapping(value = "/tenants/{tenant}/apps/{appId}/subs/{id}",method = RequestMethod.GET)
    public RestResponse subdetail(HttpServletRequest request,@ApiParam(name = "id",value = "子账号ID")@PathVariable String id){
        ApiCertificateSubAccount apiCertificateSubAccount = apiCertificateSubAccountService.findByIdWithQuota(id);
        if(apiCertificateSubAccount != null){
            return RestResponse.success(new SubAccountVo(apiCertificateSubAccount,""));
        }else{
            return RestResponse.failed("","找不到对象");
        }
    }
    @ApiOperation(value = "获取租户的app子账号详情中的绑定号码列表")
    @RequestMapping(value = "/tenants/{tenant}/apps/{appId}/subs/{id}/nums",method = RequestMethod.GET)
    public RestResponse subsnums(
            @PathVariable String tenant,@PathVariable String appId,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam(name = "id",value = "子账号ID")@RequestParam(required = false) String id
    ){
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(id)){
            String subId1 = getSubIdsByCerbId(id);
            if(org.apache.commons.lang3.StringUtils.isEmpty(subId1)){
                return RestResponse.success(new Page((pageNo-1)*pageSize,pageNo*pageSize,pageSize,null));
            }else{
                id = subId1;
            }
        }
        Page<ResourcesRent> page = resourcesRentService.findBySubaccount(appId,id,pageNo,pageSize);
        List<ResourcesRent> result = page.getResult();
        if(result != null && result.size() > 0){
            List<AppNumVO> appNumVOs = new ArrayList<>();
            Map<String,ApiCertificateSubAccount> map = getMapSubAccountList(appId);
            for(ResourcesRent rent : result){
                AppNumVO vo = new AppNumVO();
                vo.setRentId(rent.getResourceTelenum().getId());
                vo.setNum(rent.getResourceTelenum().getTelNumber());
                vo.setStatus((rent.getRentExpire() == null || rent.getRentExpire().compareTo(new Date()) == 1 )? "1" : "0");
                vo.setIsCalled(StringUtils.isBlank(rent.getResourceTelenum().getIsCalled())? ResourceTelenum.ISCALLED_FALSE:rent.getResourceTelenum().getIsCalled());
                if(ResourceTelenum.ISDIALING_TRUE.equals(rent.getResourceTelenum().getIsDialing()) || ResourceTelenum.ISTHROUGH_TRUE.equals(rent.getResourceTelenum().getIsThrough())){
                    vo.setIsDialing("1");
                }else{
                    vo.setIsDialing("0");
                }
                vo.setAreaCode(rent.getResourceTelenum().getAreaCode());
                vo.setExpireTime(rent.getRentExpire());
                ApiCertificateSubAccount apiCertificateSubAccount = map.get(rent.getResourceTelenum().getSubaccountId());
                String certId = apiCertificateSubAccount!=null ? apiCertificateSubAccount.getCertId() :"";
                vo.setSubaccountId(rent.getResourceTelenum().getSubaccountId());
                vo.setCertId(certId);
                appNumVOs.add(vo);
            }
            page.setResult(appNumVOs);
        }
        return RestResponse.success(page);
    }
    @ApiOperation(value = "获取租户的app消息模板列表")
    @RequestMapping(value = "/tenants/{tenant}/apps/{appId}/templates",method = RequestMethod.GET)
    public RestResponse templates(
            @PathVariable String tenant,@PathVariable String appId,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam(name = "subId",value = "模板名称")@RequestParam(required = false) String name,
            @ApiParam(name = "subId",value = "子账号ID")@RequestParam(required = false) String subId
    ){
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(subId)){
            String subId1 = getSubIdsByCerbId(subId);
            if(org.apache.commons.lang3.StringUtils.isEmpty(subId1)){
                return RestResponse.success(new Page((pageNo-1)*pageSize,pageNo*pageSize,pageSize,null));
            }else{
                subId = subId1;
            }
        }
        Page page = msgTemplateService.getPageByCondition(pageNo,pageSize,appId,subId,name);
        if(page != null){
            List<MsgTemplateVo> returnResult = new ArrayList<>();
            List<MsgTemplate> result = page.getResult();
            Map<String,ApiCertificateSubAccount> map = getMapSubAccountList(appId);
            if(result != null){
                result.stream().forEach(msgTemplate -> returnResult.add(MsgTemplateVo.changeMsgTemplateToVo(msgTemplate,map.get(msgTemplate.getSubaccountId()))));
                page.setResult(returnResult);
            }
        }
        return RestResponse.success(page);
    }

}
