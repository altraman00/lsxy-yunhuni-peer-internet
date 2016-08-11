package com.lsxy.app.oc.rest.tenant;

import com.lsxy.app.oc.rest.dashboard.vo.ConsumeAndurationStatisticVO;
import com.lsxy.app.oc.rest.tenant.vo.TenantIndicantVO;
import com.lsxy.framework.api.statistics.service.*;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

    @Autowired
    private VoiceCdrDayService voiceCdrDayService;

    @Autowired
    private ConsumeDayService consumeDayService;

    @Autowired
    private ApiCallDayService apiCallDayService;

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

    @ApiOperation(value = "租户某月所有天的（消费额和话务量）统计")
    @RequestMapping(value = "/tenants/{id}/consumeAnduration/statistic",method = RequestMethod.GET)
    public RestResponse consumeAndurationStatistic(
            @PathVariable String id,
            @RequestParam(value = "year") Integer year,
            @RequestParam(value = "month") Integer month){
        ConsumeAndurationStatisticVO dto = new ConsumeAndurationStatisticVO();
        dto.setSession(perDayOfMonthDurationStatistic(year,month,id));
        dto.setCost(perDayOfMonthConsumeStatistic(year,month,id));
        return RestResponse.success(dto);
    }

    /**
     * 统计租户某个月的每天的话务量
     * @return
     */
    private List<Long> perDayOfMonthDurationStatistic(int year, int month,String tenant){
        List<Long> results = new ArrayList<Long>();
        //先计算出某个月的所有天的开始和结束时间
        Date cdate = DateUtils.newDate(year,month,1);
        Date d1=DateUtils.getFirstTimeOfMonth(cdate);
        Date d2 =DateUtils.getLastTimeOfMonth(cdate);
        Date[] ds = DateUtils.getDatesBetween(d1,d2);
        if(ds!=null && ds.length>0){
            ExecutorService pool= Executors.newFixedThreadPool(ds.length);
            List<Future<Long>> fs = new ArrayList<Future<Long>>();
            for (final Date d: ds) {
                fs.add(pool.submit(new Callable<Long>() {
                    @Override
                    public Long call(){
                        //转换成分钟
                        return (long)Math.round(voiceCdrDayService.getAmongDurationByDateAndTenant(d,tenant)/60);
                    }
                }));
            }
            pool.shutdown();
            for (Future<Long> future : fs) {
                try {
                    results.add(future.get());
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return results;
    }

    /**
     * 统计租户某个月的每天的消费额
     * @return
     */
    private List<Double> perDayOfMonthConsumeStatistic(int year,int month,String tenant){
        List<Double> results = new ArrayList<Double>();
        //先计算出某个月的所有天的开始和结束时间
        Date cdate = DateUtils.newDate(year,month,1);
        Date d1=DateUtils.getFirstTimeOfMonth(cdate);
        Date d2 =DateUtils.getLastTimeOfMonth(cdate);
        Date[] ds = DateUtils.getDatesBetween(d1,d2);
        if(ds!=null && ds.length>0){
            ExecutorService pool= Executors.newFixedThreadPool(ds.length);
            List<Future<Double>> fs = new ArrayList<Future<Double>>();
            for (final Date d: ds) {
                fs.add(pool.submit(new Callable<Double>() {
                    @Override
                    public Double call(){
                        return consumeDayService.getAmongAmountByDateAndTenant(d,tenant).doubleValue();
                    }
                }));
            }
            pool.shutdown();
            for (Future<Double> future : fs) {
                try {
                    results.add(future.get());
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return results;
    }


    @ApiOperation(value = "租户某月所有天的（会话量/次）统计")
    @RequestMapping(value = "/tenants/{id}/session/statistic",method = RequestMethod.GET)
    public RestResponse sessionStatistic(
            @PathVariable String id,
            @RequestParam(value = "year") Integer year,
            @RequestParam(value = "month") Integer month){
        return RestResponse.success(perDayOfMonthSessionCountStatistic(year,month,id));
    }

    private List<Long> perDayOfMonthSessionCountStatistic(int year,int month,String tenant){
        List<Long> results = new ArrayList<Long>();
        //先计算出某个月的所有天的开始和结束时间
        Date cdate = DateUtils.newDate(year,month,1);
        Date d1=DateUtils.getFirstTimeOfMonth(cdate);
        Date d2 =DateUtils.getLastTimeOfMonth(cdate);
        Date[] ds = DateUtils.getDatesBetween(d1,d2);
        if(ds!=null && ds.length>0){
            ExecutorService pool= Executors.newFixedThreadPool(ds.length);
            List<Future<Long>> fs = new ArrayList<Future<Long>>();
            for (final Date d: ds) {
                fs.add(pool.submit(new Callable<Long>() {
                    @Override
                    public Long call(){
                        return voiceCdrDayService.getAmongCallByDateAndTenant(d,tenant);
                    }
                }));
            }
            pool.shutdown();
            for (Future<Long> future : fs) {
                try {
                    results.add(future.get());
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return results;
    }

    @ApiOperation(value = "租户某月所有天的（api调用次数）统计")
    @RequestMapping(value = "/tenants/{id}/api_invoke/statistic",method = RequestMethod.GET)
    public RestResponse apiInvokeStatistic(
            @PathVariable String id,
            @RequestParam(value = "year") Integer year,
            @RequestParam(value = "month") Integer month){
        return RestResponse.success(perDayOfMonthApiInvokeStatistic(year,month,id));
    }

    private List<Long> perDayOfMonthApiInvokeStatistic(int year,int month,String tenant){
        List<Long> results = new ArrayList<Long>();
        //先计算出某个月的所有天的开始和结束时间
        Date cdate = DateUtils.newDate(year,month,1);
        Date d1=DateUtils.getFirstTimeOfMonth(cdate);
        Date d2 =DateUtils.getLastTimeOfMonth(cdate);
        Date[] ds = DateUtils.getDatesBetween(d1,d2);
        if(ds!=null && ds.length>0){
            ExecutorService pool= Executors.newFixedThreadPool(ds.length);
            List<Future<Long>> fs = new ArrayList<Future<Long>>();
            for (final Date d: ds) {
                fs.add(pool.submit(new Callable<Long>() {
                    @Override
                    public Long call(){
                        return apiCallDayService.getInvokeCountByDateAndTenant(d,tenant);
                    }
                }));
            }
            pool.shutdown();
            for (Future<Long> future : fs) {
                try {
                    results.add(future.get());
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return results;
    }
}
