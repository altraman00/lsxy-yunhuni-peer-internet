package com.lsxy.app.oc.rest.account;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.app.oc.rest.config.vo.IdsVo;
import com.lsxy.app.oc.rest.file.VoiceFilePlayVo;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.oc.VoiceFilePlayAuditCompletedEvent;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.msg.api.model.MsgSupplier;
import com.lsxy.msg.api.model.MsgSupplierTemplate;
import com.lsxy.msg.api.model.MsgTemplate;
import com.lsxy.msg.api.service.MsgSupplierService;
import com.lsxy.msg.api.service.MsgSupplierTemplateService;
import com.lsxy.msg.api.service.MsgTemplateService;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;
import com.lsxy.yunhuni.api.apicertificate.service.ApiCertificateSubAccountService;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;
import com.lsxy.yunhuni.api.file.service.VoiceFilePlayService;
import com.lsxy.yunhuni.api.message.model.AccountMessage;
import com.lsxy.yunhuni.api.message.service.AccountMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.*;


/**
 * 模板审核
 * Created by zhangxb on 2016/7/21.
 */
@Api(value = "消息模板审核", description = "审核管理相关的接口" )
@RequestMapping("/demand/member/msgtemplate")
@RestController
public class MsgTemptaleController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(MsgTemptaleController.class);
    @Reference(timeout=3000,check = false,lazy = true)
    private MsgTemplateService msgTemplateService;
    @Reference(timeout=3000,check = false,lazy = true)
    private MsgSupplierService msgSupplierService;
    @Reference(timeout=3000,check = false,lazy = true)
    private MsgSupplierTemplateService msgSupplierTemplateService;
    @Autowired
    private TenantService tenantService;
    @Autowired
    private AppService appService;
    @Autowired
    AccountMessageService accountMessageService;
    @Autowired
    private ApiCertificateSubAccountService apiCertificateSubAccountService;
    @ApiOperation(value = "根据id查看详情")
    @RequestMapping(value = "/detail/{id}",method = RequestMethod.GET)
    public RestResponse detail(
            @ApiParam(name = "id",value = "模板ID")
            @PathVariable String id

    ){
        MsgTemplate msgTemplate = msgTemplateService.findById(id);
        if(msgTemplate!=null){
            App app = appService.findById(msgTemplate.getAppId());
            MsgTemplateDetailVo msgTemplateDetailVo = new MsgTemplateDetailVo(msgTemplate,app.getTenant().getTenantName(),app.getName());
            List<MsgSupplierTemplate> msgSupplierTemplateList = msgSupplierTemplateService.findByTempId(msgTemplate.getTempId());
            if(msgSupplierTemplateList!=null) {
                List<Map> mapList = new ArrayList<>();
                for (int j = 0; j < msgSupplierTemplateList.size(); j++) {
                    MsgSupplier msgSupplier = msgSupplierService.findByCode(msgSupplierTemplateList.get(j).getSupplierCode());
                    if(msgSupplier!=null) {
                        Map<String, String> map = new HashMap<String, String>() {{
                            put("msgSupplierId", msgSupplier.getId());
                            put("msgSupplierName", msgSupplier.getSupplierName());
                        }};
                        mapList.add(map);
                    }
                }
                msgTemplateDetailVo.setList(mapList);
            }
            return RestResponse.success(msgTemplateDetailVo);
        }

        return RestResponse.failed("","记录不存在");
    }

    @ApiOperation(value = "根据名字和时间查询模板审核的分页信息")
    @RequestMapping(value = "/{type}/list",method = RequestMethod.GET)
    public RestResponse pageList(
            @ApiParam(name = "type",value = "状态await待处理auditing审核通过unauth不通过")
            @PathVariable String type,
            @ApiParam(name = "name",value = "会员名")
            @RequestParam(required=false)String name,
            @ApiParam(name = "startTime",value = "开始时间 yyyy-MM-dd")
            @RequestParam(required=false)String startTime,
            @ApiParam(name = "endTime",value = "结束时间 yyyy-MM-dd")
            @RequestParam(required=false)String endTime,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "20")Integer pageSize
    ){
        int state = 0;
        if("await".equals(type)){
            state = 0;
        }else if("auditing".equals(type)){
            state = 1;
        }else if("unauth".equals(type)){
            state = -1;
        }else{
            return RestResponse.failed("","类型错误");
        }
        Date date1 = null;
        Date date2 = null;
        try{
            if(StringUtils.isNotEmpty(startTime)) {
                date1 = DateUtils.parseDate(startTime+" 00:00:00", "yyyy-MM-dd hh:mm:ss");
            }
            if(StringUtils.isNotEmpty(endTime)) {
                date2 = DateUtils.parseDate(endTime+" 23:59:59", "yyyy-MM-dd hh:mm:ss");
            }
        }catch (Exception e){
            return RestResponse.failed("","日期格式错误");
        }
        Page<MsgTemplate> page = null;
        if (StringUtil.isNotEmpty(name)) {
            List<Tenant> tList = tenantService.pageListByUserName(name);
            if (tList.size() == 0) {
                page = new Page((pageNo-1)*pageSize ,0,pageSize,null);
            } else {
                String[] tenantId = new String[tList.size()];
                for (int i = 0; i < tList.size(); i++) {
                    tenantId[i] = tList.get(i).getId();
                }
                page = msgTemplateService.getPageByCondition(pageNo,pageSize,state,date1,date2,tenantId);
            }
        }else{
            page = msgTemplateService.getPageByCondition(pageNo,pageSize,state,date1,date2,new String[]{});
        }
        List<MsgTemplateVo> list = new ArrayList<>();
        for (int i = 0; i < page.getResult().size(); i++) {
            MsgTemplate msgTemplate = page.getResult().get(i);
            App app = appService.findById(msgTemplate.getAppId());
            String certId = "";
            if(msgTemplate.getSubaccountId()!=null){
                ApiCertificateSubAccount apiCertificateSubAccount = apiCertificateSubAccountService.findById(msgTemplate.getSubaccountId());
                if(apiCertificateSubAccount!=null){
                    certId = apiCertificateSubAccount.getCertId();
                }
            }
            MsgTemplateVo templateVo = new MsgTemplateVo( page.getResult().get(i),app.getTenant().getTenantName(),app.getName());
            templateVo.setCertId(certId);
            if(msgTemplate.getTempId()!=null) {
                List<MsgSupplierTemplate> msgSupplierTemplateList = msgSupplierTemplateService.findByTempId(msgTemplate.getTempId());
                if (msgSupplierTemplateList != null) {
                    List<Map> mapList = new ArrayList<>();
                    for (int j = 0; j < msgSupplierTemplateList.size(); j++) {
                        MsgSupplier msgSupplier = msgSupplierService.findByCode(msgSupplierTemplateList.get(j).getSupplierCode());
                        if (msgSupplier != null) {
                            Map<String, String> map = new HashMap<String, String>() {{
                                put("msgSupplierId", msgSupplier.getId());
                                put("msgSupplierName", msgSupplier.getSupplierName());
                            }};
                            mapList.add(map);
                        }
                    }
                    templateVo.setList(mapList);
                }
            }
            list.add(templateVo);
        }
        page.setResult(list);
        return RestResponse.success(page);
    }
    @ApiOperation(value = "根据模板ID修改模板状态为审核通过")
    @RequestMapping(value = "/pass/{id}",method = RequestMethod.PUT)
    public RestResponse pass(
            @ApiParam(name = "id",value = "模板ID")
            @PathVariable String id,@RequestBody MsgTemplateUpdateVo idsVo
    ){
        MsgTemplate msgTemplate = msgTemplateService.findById(id);
        if(msgTemplate!=null){
            msgTemplate.setStatus(MsgTemplate.STATUS_PASS);
            msgTemplateService.save(msgTemplate);
            List<MsgEdit> ids= idsVo.getIds();
            for (int i = 0; i < ids.size(); i++) {
                MsgEdit edit = ids.get(i);
                List list = msgSupplierTemplateService.findBySupplierTempId(edit.getTempId());
                if(list.size()>0){
                    return RestResponse.failed("",edit.getTempId()+"上游模板编号已存在");
                }
            }
            for (int i = 0; i < ids.size(); i++) {
                MsgEdit edit = ids.get(i);
                MsgSupplier msgSupplier = msgSupplierService.findById(edit.getId());
                if(msgSupplier!=null) {
                    MsgSupplierTemplate msgSupplierTemplate = new MsgSupplierTemplate(msgTemplate.getTempId(), msgTemplate.getTenantId(), msgTemplate.getAppId(), msgTemplate.getSubaccountId(), msgSupplier.getCode(), edit.getTempId(), getCurrentUser().getUserName());
                    msgSupplierTemplateService.save(msgSupplierTemplate);
                }
            }
            accountMessageService.sendTenantTempletMessage(null,msgTemplate.getTenantId(), AccountMessage.MESSAGE_MSG_TEMPLATE_SUCCESS );
            return RestResponse.success();
        }else{
            return RestResponse.failed("","模板不存在");
        }
    }
    @ApiOperation(value = "根据模板ID修改模板状态为审核不通过")
    @RequestMapping(value = "/nopass/{id}",method = RequestMethod.PUT)
    public RestResponse pageList(
            @ApiParam(name = "id",value = "模板ID")
            @PathVariable String id,
            @ApiParam(name="reason",value = "原因格式：{\"reason\":\"内容\"}")
            @RequestBody Map map
            ){
        MsgTemplate msgTemplate = msgTemplateService.findById(id);
        if(msgTemplate!=null){
            msgTemplate.setStatus(MsgTemplate.STATUS_FAIL);
            msgTemplate.setReason((String)map.get("reason"));
            msgTemplateService.save(msgTemplate);
            accountMessageService.sendTenantTempletMessage(null,msgTemplate.getTenantId(), AccountMessage.MESSAGE_MSG_TEMPLATE_FAIL );
            return RestResponse.success();
        }else{
            return RestResponse.failed("","模板不存在");
        }
    }
    @ApiOperation(value = "获取消息供应商列表")
    @RequestMapping(value = "/supplier/list",method = RequestMethod.GET)
    public RestResponse supplierList(){
        List<MsgSupplier> list = (List<MsgSupplier>) msgSupplierService.list();
        return RestResponse.success(list);
    }

}
