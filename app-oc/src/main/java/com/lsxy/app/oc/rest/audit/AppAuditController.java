package com.lsxy.app.oc.rest.audit;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppOnlineActionService;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.message.model.AccountMessage;
import com.lsxy.yunhuni.api.message.service.AccountMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxb on 2017/3/15.
 */
@Api(value = "应用审核",description = "审核管理相关的接口" )
@RequestMapping("/demand/member/app")
@RestController
public class AppAuditController extends AbstractRestController {
    @Autowired
    private AppService appService;
    @Autowired
    AppOnlineActionService appOnlineActionService;
    @Autowired
    private TenantService tenantService;
    @Autowired
    AccountMessageService accountMessageService;

    @ApiOperation(value = "根据名字和日期查询应用审核的分页信息(1上线（通过），2没上线，3待审核，4不通过)")
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
            state = App.STATUS_WAIT;
        }else if("auditing".equals(type)){
            state = App.STATUS_ONLINE;
        }else if("unauth".equals(type)){
            state = App.STATUS_FAIl;
        }else{
            return RestResponse.failed("","类型错误");
        }
        Date date1 = null;
        Date date2 = null;
        try{
            if(StringUtils.isNotEmpty(startTime)) {
                date1 = DateUtils.parseDate(startTime, "yyyy-MM-dd");
            }
            if(StringUtils.isNotEmpty(endTime)) {
                date2 = DateUtils.parseDate(endTime, "yyyy-MM-dd");
            }
        }catch (Exception e){
            return RestResponse.failed("","日期格式错误");
        }
        Page page = null;
        if (StringUtil.isNotEmpty(name)) {
            List<Tenant> tList = tenantService.pageListByUserName(name);
            if (tList.size() == 0) {
                page = new Page((pageNo-1)*pageSize ,0,pageSize,null);
            } else {
                String[] tenantId = new String[tList.size()];
                for (int i = 0; i < tList.size(); i++) {
                    tenantId[i] = tList.get(i).getId();
                }
                page = appService.pageList(tenantId,date1,date2, state,pageNo,pageSize);
            }
        }else{
            page = appService.pageList(new String[]{},date1,date2, state,pageNo,pageSize);
        }
        return RestResponse.success(page);
    }
    @ApiOperation(value = "根据应用ID修改审核状态为审核通过-即应用上线")
    @RequestMapping(value = "/pass/{id}",method = RequestMethod.PUT)
    public RestResponse pass(
            @ApiParam(name = "id",value = "应用ID")
            @PathVariable String id
    ){
        App app = appService.findById(id);
        if(app!=null){
            app.setStatus(App.STATUS_ONLINE);
            appService.save(app);
            accountMessageService.sendTenantTempletMessage(null,app.getTenant().getId(), AccountMessage.MESSAGE_TYPE_APP_SUCCESS );
            return RestResponse.success();
        }else{
            return RestResponse.failed("","应用不存在");
        }
    }
    @ApiOperation(value = "根据应用ID修改应用状态为审核不通过")
    @RequestMapping(value = "/nopass/{id}",method = RequestMethod.PUT)
    public RestResponse pageList(
            @ApiParam(name = "id",value = "应用ID")
            @PathVariable String id,
            @ApiParam(name="reason",value = "原因格式：{\"reason\":\"内容\"}")
            @RequestBody Map map
    ){
        App app = appService.findById(id);
        if(app!=null){
            app = appOnlineActionService.offline(app.getId());
            app.setReason((String)map.get("reason"));
            appService.save(app);
            accountMessageService.sendTenantTempletMessage(null,app.getTenant().getId(), AccountMessage.MESSAGE_TYPE_APP_FAIL );
            return RestResponse.success();
        }else{
            return RestResponse.failed("","应用不存在");
        }
    }

}
