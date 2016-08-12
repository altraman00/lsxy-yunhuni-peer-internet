package com.lsxy.app.oc.rest.tenant;

import com.lsxy.app.oc.rest.tenant.vo.TenantIndicantVO;
import com.lsxy.framework.api.statistics.service.ConsumeMonthService;
import com.lsxy.framework.api.statistics.service.RechargeMonthService;
import com.lsxy.framework.api.statistics.service.VoiceCdrMonthService;
import com.lsxy.framework.api.tenant.model.TenantVO;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.apicertificate.service.ApiCertificateService;
import com.lsxy.yunhuni.api.billing.service.BillingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/10.
 */
@Api(value = "租户中心", description = "租户中心相关的接口" )
@RestController
@RequestMapping("/tenant")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ApiCertificateService apiCertificateService;

    @Autowired
    private BillingService billingService;

    @Autowired
    private VoiceCdrMonthService voiceCdrMonthService;

    @Autowired
    private ConsumeMonthService consumeMonthService;

    @Autowired
    private RechargeMonthService rechargeMonthService;

    @ApiOperation(value = "租户列表")
    @RequestMapping(value = "/tenants",method = RequestMethod.GET)
    public RestResponse tenants(
    @RequestParam(required = false) String name,
    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") Date begin,
    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") Date end,
    @ApiParam(name = "authStatus",value = "认证状态，1已认证，0未认证")
    @RequestParam(required = false) Integer authStatus,
    @ApiParam(name = "accStatus",value = "账号状态，2正常/启用，1被锁定/禁用")
    @RequestParam(required = false) Integer accStatus,
    @RequestParam(required = false,defaultValue = "1") Integer pageNo,
    @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        if(begin!=null){
            begin = DateUtils.getFirstTimeOfMonth(begin);
        }
        if(end!=null){
            end = DateUtils.getLastTimeOfMonth(end);
        }
        Page<TenantVO> list = tenantService.pageListBySearch(name,begin,end,authStatus,accStatus,pageNo,pageSize);
        return RestResponse.success(list);
    }

    @ApiOperation(value = "租户状态禁用/启用")
    @RequestMapping(value = "/tenants/{id}",method = RequestMethod.PATCH)
    public RestResponse tenants(
            @ApiParam(name = "id",value = "租户id")
            @PathVariable String id,
            @ApiParam(name = "status",value = "账号状态，2正常/启用，1被锁定/禁用")
            @RequestParam Integer status){
        return RestResponse.success(accountService.updateStatusByTenantId(id,status));
    }

    @ApiOperation(value = "租户鉴权信息")
    @RequestMapping(value = "/tenants/{id}/cert",method = RequestMethod.GET)
    public RestResponse cert(
            @ApiParam(name = "id",value = "租户id")
            @PathVariable String id){
        return RestResponse.success(apiCertificateService.findApiCertificateByTenantId(id));
    }

    @ApiOperation(value = "租户账务信息，余额/套餐/存储")
    @RequestMapping(value = "/tenants/{id}/billing",method = RequestMethod.GET)
    public RestResponse billing(
            @ApiParam(name = "id",value = "租户id")
            @PathVariable String id){
        return RestResponse.success(billingService.findBillingByTenantId(id));
    }

    @ApiOperation(value = "租户上个月数据指标")
    @RequestMapping(value = "/tenants/{id}/indicant",method = RequestMethod.GET)
    public RestResponse indicant(
            @ApiParam(name = "id",value = "租户id")
            @PathVariable String id){
        TenantIndicantVO dto = new TenantIndicantVO();
        //上个月
        Date preMonth = DateUtils.getPrevMonth(new Date());
        //上个月消费额
        double preConsume = consumeMonthService.getAmongAmountByDateAndTenant(preMonth,id).doubleValue();
        //上个月消费额
        double preRecharge = rechargeMonthService.getAmongAmountByDateAndTenant(preMonth,id).doubleValue();
        //上个月会话量
        long preAmongCall = voiceCdrMonthService.getAmongCallByDateAndTenant(preMonth,id);
        //上个月话务量 分钟
        long preAmongDuration = Math.round(voiceCdrMonthService.getAmongDurationByDateAndTenant(preMonth,id)/60);
        //上个月连通量
        long preAmongConnect = voiceCdrMonthService.getAmongConnectByDateAndTenant(preMonth,id);
        //上个月平均通话时长
        long preAvgTime = preAmongCall == 0 ? 0 : preAmongDuration/preAmongCall;
        //上个月连通率
        double preConnectRate = preAmongCall == 0 ? 0 : new BigDecimal((preAmongConnect/preAmongCall) * 100)
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        //上上个月
        Date prepreMonth = DateUtils.getPrevMonth(preMonth);
        //上上个月消费额
        double prepreConsume = consumeMonthService.getAmongAmountByDateAndTenant(prepreMonth,id).doubleValue();
        //上上个月消费额
        double prepreRecharge = rechargeMonthService.getAmongAmountByDateAndTenant(prepreMonth,id).doubleValue();
        //上上个月会话量
        long prepreAmongCall = voiceCdrMonthService.getAmongCallByDateAndTenant(prepreMonth,id);
        //上上个月话务量 分钟
        long prepreAmongDuration = Math.round(voiceCdrMonthService.getAmongDurationByDateAndTenant(prepreMonth,id)/60);
        //上上个月连通量
        long prepreAmongConnect = voiceCdrMonthService.getAmongConnectByDateAndTenant(prepreMonth,id);
        //上上个月平均通话时长
        long prepreAvgTime = prepreAmongCall == 0 ? 0 : prepreAmongDuration/prepreAmongCall;
        //上上个月连通率
        double prepreConnectRate = prepreAmongCall == 0 ? 0 : new BigDecimal((prepreAmongConnect/prepreAmongCall) * 100)
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        dto.setCostCoin(preConsume);
        dto.setRechargeCoin(preRecharge);
        dto.setSessionCount(preAmongCall);
        dto.setSessionTime(preAmongDuration);
        dto.setAvgSessionTime(preAvgTime);
        dto.setConnectedRate(preConnectRate);
        dto.setCostCoinRate(new BigDecimal(((preConsume-prepreConsume)/(prepreConsume+0.1)) * 100)
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        dto.setRechargeCoinRate(new BigDecimal(((preRecharge-prepreRecharge)/(prepreRecharge+0.1)) * 100)
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        dto.setSessionCountRate(new BigDecimal(((preAmongCall-prepreAmongCall)/(prepreAmongCall+0.1)) * 100)
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        dto.setSessionTimeRate(new BigDecimal(((preAmongDuration-prepreAmongDuration)/(prepreAmongDuration+0.1)) * 100)
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        dto.setAvgSessionTimeRate(new BigDecimal(((preAvgTime-prepreAvgTime)/(prepreAvgTime+0.1)) * 100)
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        dto.setConnectedRateRate(new BigDecimal(((preConnectRate-prepreConnectRate)/(prepreConnectRate+0.1)) * 100)
                .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        return RestResponse.success(dto);
    }
}
