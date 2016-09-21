package com.lsxy.app.oc.rest.tenant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsxy.app.oc.rest.dashboard.vo.ConsumeAndurationStatisticVO;
import com.lsxy.app.oc.rest.tenant.vo.*;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.yunhuni.api.consume.enums.ConsumeCode;
import com.lsxy.yunhuni.api.consume.model.Consume;
import com.lsxy.yunhuni.api.consume.service.ConsumeService;
import com.lsxy.framework.mq.events.portal.ResetPwdVerifySuccessEvent;
import com.lsxy.yunhuni.api.statistics.model.ConsumeMonth;
import com.lsxy.yunhuni.api.statistics.service.*;
import com.lsxy.framework.api.tenant.model.*;
import com.lsxy.framework.api.tenant.service.*;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.mail.MailConfigNotEnabledException;
import com.lsxy.framework.mail.MailContentNullException;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.apicertificate.service.ApiCertificateService;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.framework.api.billing.model.Billing;
import com.lsxy.framework.api.billing.service.CalBillingService;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;
import com.lsxy.yunhuni.api.file.model.VoiceFileRecord;
import com.lsxy.yunhuni.api.file.service.VoiceFilePlayService;
import com.lsxy.yunhuni.api.file.service.VoiceFileRecordService;
import com.lsxy.yunhuni.api.recharge.service.RechargeService;
import com.lsxy.yunhuni.api.resourceTelenum.model.TestNumBind;
import com.lsxy.yunhuni.api.resourceTelenum.service.TestNumBindService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2016/8/10.
 */
@Api(value = "租户中心", description = "租户中心相关的接口" )
@RestController
@RequestMapping("/tenant")
public class TenantController {
    public static final Logger logger = LoggerFactory.getLogger(TenantController.class);
    @Autowired
    private TenantService tenantService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ApiCertificateService apiCertificateService;

    @Autowired
    private CalBillingService calBillingService;

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

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private ConsumeService consumeService;

    @Autowired
    private RealnameCorpService realnameCorpService;

    @Autowired
    private RealnamePrivateService realnamePrivateService;

    @Autowired
    private MQService mqService;

    @Autowired
    private AppService appService;

    @Autowired
    private TestNumBindService testNumBindService;

    @Autowired
    private VoiceFilePlayService voiceFilePlayService;

    @Autowired
    private VoiceFileRecordService voiceFileRecordService;

    @Autowired
    private TenantServiceSwitchService tenantServiceSwitchService;

    @Autowired
    private ApiCallMonthService apiCallMonthService;

    @ApiOperation(value = "租户列表")
    @RequestMapping(value = "/tenants",method = RequestMethod.GET)
    public RestResponse tenants(
    @RequestParam(required = false) String name,
    @ApiParam(name = "begin",value = "格式:yyyy-MM-dd")
    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date begin,
    @ApiParam(name = "end",value = "格式:yyyy-MM-dd")
    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date end,
    @ApiParam(name = "authStatus",value = "认证状态，1已认证，0未认证")
    @RequestParam(required = false) Integer authStatus,
    @ApiParam(name = "accStatus",value = "账号状态，2正常/启用，1被锁定/禁用")
    @RequestParam(required = false) Integer accStatus,
    @RequestParam(required = false,defaultValue = "1") Integer pageNo,
    @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        if(begin!=null){
            begin = DateUtils.getFirstTimeOfDate(begin);
        }
        if(end!=null){
            end = DateUtils.getLastTimeOfDate(end);
        }
        Page<TenantVO> list = null;
        Page<TenantVO> list2 = null;
        try {
            list = tenantService.pageListBySearch(name, begin, end, authStatus, accStatus, pageNo, pageSize);
        }catch (Exception e){
            logger.error("报错:{}",e);
        }
        try{
            //修改余额取值
            List<TenantVO> temp = list.getResult();
            List<TenantVO> list1 = new ArrayList();
            for(int i=0;i<temp.size();i++){
                TenantVO tenantVO = temp.get(i);
                BigDecimal bigDecimal =  calBillingService.getBalance(tenantVO.getId());
                tenantVO.setRemainCoin(bigDecimal.doubleValue());
                list1.add(tenantVO);
            }
            list2 = new Page<>(list.getStartIndex(),list.getTotalCount(),list.getPageSize(),list1);
        }catch (Exception e){
            logger.error("转换出错{}",e);
        }
        return RestResponse.success(list2);
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
        Billing billing = calBillingService.getCalBilling(id);
        ObjectMapper objectMapper = new ObjectMapper();
        Map map= objectMapper.convertValue(billing,Map.class);
        //余额正数部分
        int vTemp = billing.getBalance().intValue();
        //余额小数部分
        DecimalFormat df   = new DecimalFormat("######0.00");
        String format = df.format(billing.getBalance());
        String vTempDec = format.substring(format.indexOf('.') + 1, format.length());
        map.put("balance",vTemp);
        map.put("balanceDec",vTempDec);
        return RestResponse.success(map);
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
        double preConsume = consumeMonthService.getAmongAmountByDateAndTenant(preMonth,id,null).doubleValue();
        //上个月消费额
        double preRecharge = rechargeMonthService.getAmongAmountByDateAndTenant(preMonth,id).doubleValue();
        //上个月会话量
        long preAmongCall = voiceCdrMonthService.getAmongCallByDateAndTenant(preMonth,id,null);
        //上个月话务量 分钟
        long preAmongDuration = Math.round(voiceCdrMonthService.getAmongDurationByDateAndTenant(preMonth,id,null)/60);
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
        double prepreConsume = consumeMonthService.getAmongAmountByDateAndTenant(prepreMonth,id,null).doubleValue();
        //上上个月消费额
        double prepreRecharge = rechargeMonthService.getAmongAmountByDateAndTenant(prepreMonth,id).doubleValue();
        //上上个月会话量
        long prepreAmongCall = voiceCdrMonthService.getAmongCallByDateAndTenant(prepreMonth,id,null);
        //上上个月话务量 分钟
        long prepreAmongDuration = Math.round(voiceCdrMonthService.getAmongDurationByDateAndTenant(prepreMonth,id,null)/60);
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
        Map dto1 = new HashMap();
        dto1.put("costCoin",false);
        dto1.put("rechargeCoinRate",false);
        dto1.put("sessionCountRate",false);
        dto1.put("sessionTimeRate",false);
        dto1.put("avgSessionTimeRate",false);
        dto1.put("connectedRateRate",false);
        if(prepreConsume>0) {
            dto.setCostCoinRate(new BigDecimal(((preConsume - prepreConsume)*0.01 )/ (prepreConsume*0.01 )*100)
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            dto1.put("costCoin",true);

        }
        if(prepreRecharge>0) {
            dto.setRechargeCoinRate(new BigDecimal(((preRecharge - prepreRecharge)*0.01) / (prepreRecharge *0.01)*100)
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            dto1.put("rechargeCoinRate",true);

        }
        if(prepreAmongCall>0) {
            double b2 =  new BigDecimal(((preAmongCall - prepreAmongCall)*0.01) / (prepreAmongCall*0.01)*100)
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            dto.setSessionCountRate(b2);
            dto1.put("sessionCountRate",true);

        }
        if(prepreAmongDuration>0) {
            double b1 = new BigDecimal(((preAmongDuration - prepreAmongDuration)*0.01) / (prepreAmongDuration*0.01 )*100)
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            dto.setSessionTimeRate(b1);
            dto1.put("sessionTimeRate",true);

        }
        if(prepreAvgTime>0) {
            dto.setAvgSessionTimeRate(new BigDecimal(((preAvgTime - prepreAvgTime)*0.01) / (prepreAvgTime*0.01)*100)
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            dto1.put("avgSessionTimeRate",true);

        }
        if(prepreConnectRate>0) {
            dto.setConnectedRateRate(new BigDecimal(((preConnectRate - prepreConnectRate)*0.01) / (prepreConnectRate*0.01)*100)
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            dto1.put("connectedRateRate",true);
        }
        Map map = new HashMap();
        map.put("dto",dto);
        map.put("dto1",dto1);
        return RestResponse.success(map);
    }

    @ApiOperation(value = "租户(某月所有天/某年所有月)的（消费额和话务量）统计")
    @RequestMapping(value = "/tenants/{id}/consumeAnduration/statistic",method = RequestMethod.GET)
    public RestResponse consumeAndurationStatistic(
            @PathVariable String id,
            @RequestParam(value = "year") Integer year,
            @RequestParam(required = false,value = "month") Integer month,
            @RequestParam(required =false) String appId
    ){
        ConsumeAndurationStatisticVO dto = new ConsumeAndurationStatisticVO();
        if(month!=null){//某月所有天
            dto.setSession(perDayOfMonthDurationStatistic(year,month,id,appId));
            dto.setCost(perDayOfMonthConsumeStatistic(year,month,id,appId));
        }else{//某年所有月
            dto.setSession(perMonthOfYearDurationStatistic(year,id,appId));
            dto.setCost(perMonthOfYearConsumeStatistic(year,id,appId));
        }
        return RestResponse.success(dto);
    }

    /**
     * 统计租户某个月的每天的话务量
     * @return
     */
    private List<Long> perDayOfMonthDurationStatistic(int year, int month,String tenant,String appId){
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
                        return (long)Math.round(voiceCdrDayService.getAmongDurationByDateAndTenant(d,tenant,appId)/60);
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
     * 统计某个租户某年的每月的话务量
     * @return
     */
    private List<Long> perMonthOfYearDurationStatistic(int year,String tenant,String appId){
        List<Long> results = new ArrayList<Long>();
        int month_length = 12;
        ExecutorService pool= Executors.newFixedThreadPool(month_length);
        List<Future<Long>> fs = new ArrayList<Future<Long>>();
        //先计算出某年所有月的开始和结束时间
        for (int month =1;month<=month_length;month++){
            Date month_start = DateUtils.newDate(year,month,1);
            fs.add(pool.submit(new Callable<Long>() {
                @Override
                public Long call(){
                    return (long)Math.round(voiceCdrMonthService.getAmongDurationByDateAndTenant(month_start,tenant,appId)/60);
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
        return results;
    }

    /**
     * 统计租户某个月的每天的消费额
     * @return
     */
    private List<Double> perDayOfMonthConsumeStatistic(int year,int month,String tenant,String appId){
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
                        return consumeDayService.getAmongAmountByDateAndTenant(d,tenant,appId).doubleValue();
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


    @ApiOperation(value = "租户（某月所有天/某年所有月）的（会话量/次）统计")
    @RequestMapping(value = "/tenants/{id}/session/statistic",method = RequestMethod.GET)
    public RestResponse sessionStatistic(
            @PathVariable String id,
            @RequestParam(value = "year") Integer year,
            @RequestParam(required = false,value = "month") Integer month,
            @RequestParam(required = false) String appId
    ){
        if(month!=null){//某月所有天
            return RestResponse.success(perDayOfMonthSessionCountStatistic(year,month,id,appId));
        }
        return RestResponse.success(perMonthOfYearSessionCountStatistic(year,id,appId));
    }

    private List<Long> perDayOfMonthSessionCountStatistic(int year,int month,String tenant,String appId){
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
                        return voiceCdrDayService.getAmongCallByDateAndTenant(d,tenant,appId);
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
     * 统计某个租户某年的每月的会话量
     * @return
     */
    private List<Long> perMonthOfYearSessionCountStatistic(int year,String tenant,String appId){
        List<Long> results = new ArrayList<Long>();
        int month_length = 12;
        ExecutorService pool= Executors.newFixedThreadPool(month_length);
        List<Future<Long>> fs = new ArrayList<Future<Long>>();
        //先计算出某年所有月的开始和结束时间
        for (int month =1;month<=month_length;month++){
            Date month_start = DateUtils.newDate(year,month,1);
            fs.add(pool.submit(new Callable<Long>() {
                @Override
                public Long call(){
                    return voiceCdrMonthService.getAmongCallByDateAndTenant(month_start,tenant,appId);
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
        return results;
    }

    @ApiOperation(value = "租户（某年所有月/某月所有天）的api调用次数统计")
    @RequestMapping(value = "/tenants/{id}/interfaceInvoke/statistic",method = RequestMethod.GET)
    public RestResponse apiInvokeStatistic(
            @PathVariable String id,
            @RequestParam(value = "year") Integer year,
            @RequestParam(required = false,value = "month") Integer month,
            @RequestParam(required = false) String appId
    ){
        if(month!=null){//某月所有天
            return RestResponse.success(perDayOfMonthApiInvokeStatistic(year,month,id,appId));
        }
        return RestResponse.success(perMonthOfYearApiInvokeStatistic(year,id,appId));
    }

    /**
     * 统计某个租户某月所有天的api调用次数
     * @param year
     * @param month
     * @param tenant
     * @return
     */
    private List<Long> perDayOfMonthApiInvokeStatistic(int year,int month,String tenant,String appId){
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
                        return apiCallDayService.getInvokeCountByDateAndTenant(d,tenant,appId);
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
     * 统计某个租户某年所有月的api调用次数
     * @param year
     * @param tenant
     * @return
     */
    private List<Long> perMonthOfYearApiInvokeStatistic(int year,String tenant,String appId){
        List<Long> results = new ArrayList<Long>();
        int month_length = 12;
        ExecutorService pool= Executors.newFixedThreadPool(month_length);
        List<Future<Long>> fs = new ArrayList<Future<Long>>();
        //先计算出某年所有月的开始和结束时间
        for (int month =1;month<=month_length;month++){
            Date month_start = DateUtils.newDate(year,month,1);
            fs.add(pool.submit(new Callable<Long>() {
                @Override
                public Long call(){
                    return apiCallMonthService.getInvokeCountByDateAndTenant(month_start,tenant,appId);
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
        return results;
    }

    @ApiOperation(value = "给租户充值")
    @RequestMapping(value = "/tenants/{id}/recharge",method = RequestMethod.PUT)
    public RestResponse apiInvokeStatistic(
            @PathVariable String id,
            @RequestBody RechargeInput input){
        return RestResponse.success(rechargeService.doRecharge(id,input.getAmount()));
    }

    @ApiOperation(value = "租户消费记录")
    @RequestMapping(value = "/tenants/{id}/consumes",method = RequestMethod.GET)
    public RestResponse consumes(
            @PathVariable String id,
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize){
        ConsumesVO dto = new ConsumesVO();
        Page<Consume> page = consumeService.pageListByTenantAndDate(id,year,month,pageNo,pageSize);
        List<Consume> list  = page.getResult();
        if(logger.isDebugEnabled()){
            logger.debug("消费列表：{}", JSONUtil.objectToJson(list));
        }
        list.get(0).getTenant().setTenantName("哈哈哈");
        changeTypeToChineseOfConsume(list);
        dto.setConsumes(page);

//        BigDecimal sum  = new BigDecimal("0.00");
//        for(int i=0;i<list.size();i++){
//            sum  = sum.add(list.get(i).getAmount());
//        }
//        dto.setSumAmount(sum);
        dto.setSumAmount(consumeDayService.getSumAmountByTenant(id,year+"-"+month));
        return RestResponse.success(dto);
    }

    @ApiOperation(value = "租户基本信息,联系信息,业务信息")
    @RequestMapping(value = "/tenants/{id}/info",method = RequestMethod.GET)
    public RestResponse info(@PathVariable String id) throws InvocationTargetException, IllegalAccessException {
        TenantInfoVO dto = new TenantInfoVO();
        if(id != null){
            Tenant tenant = tenantService.findById(id);
            if(tenant != null){
                Account account = accountService.findOneByTenant(id);
                dto.setTenantName(tenant.getTenantName());
                if(account!=null){
                    BeanUtils.copyProperties(dto,account);
                }
            }
        }
        return RestResponse.success(dto);
    }

    @ApiOperation(value = "认证信息,认证状态(未认证，未审核，已认证，认证失败)")
    @RequestMapping(value = "/tenants/{id}/auth/info",method = RequestMethod.GET)
    public RestResponse authInfo(@PathVariable String id) throws InvocationTargetException, IllegalAccessException {
        AuthInfoVO info = new AuthInfoVO();
        info.setStatus("未认证");
        if(id != null){
            Tenant tenant = tenantService.findById(id);
            if(tenant != null){
                Integer status = tenant.getIsRealAuth();
                //未认证，等待审核，已认证，认证失败
                Integer[] wait_auth_status = new Integer[]{Tenant.AUTH_WAIT,Tenant.AUTH_ONESELF_WAIT,Tenant.AUTH_UPGRADE_WAIT};//等待审核
                Integer[] auth_success_status = new Integer[]{Tenant.AUTH_COMPANY_SUCCESS,Tenant.AUTH_ONESELF_SUCCESS,Tenant.AUTH_UPGRADE_WAIT,Tenant.AUTH_UPGRADE_SUCCESS,Tenant.AUTH_UPGRADE_FAIL};//已认证
                Integer[] auth_fail_status = new Integer[]{Tenant.AUTH_COMPANY_FAIL,Tenant.AUTH_ONESELF_FAIL};
                if(Arrays.asList(wait_auth_status).contains(status)){//等待审核
                    info.setStatus("未审核");
                    info = getAuthInfo(tenant.getId(),status,info);
                }else if(Arrays.asList(auth_success_status).contains(status)){//已认证
                    info.setStatus("已认证");
                    info = getAuthInfo(tenant.getId(),status,info);
                }else if(Arrays.asList(auth_fail_status).contains(status)){//认证失败
                    info.setStatus("认证失败");
                    info = getAuthInfo(tenant.getId(),status,info);
                }
            }
        }
        return RestResponse.success(info);
    }

    private AuthInfoVO getAuthInfo(String tenantId,Integer status,AuthInfoVO info){
        Integer[] privateAuth_status = new Integer[]{Tenant.AUTH_ONESELF_SUCCESS,
               Tenant.AUTH_ONESELF_WAIT,Tenant.AUTH_ONESELF_FAIL,Tenant.AUTH_UPGRADE_FAIL
        };//个人认证
        Integer[] companyAuth_status = new Integer[]{Tenant.AUTH_COMPANY_SUCCESS,
                Tenant.AUTH_COMPANY_FAIL,Tenant.AUTH_WAIT,Tenant.AUTH_UPGRADE_WAIT,
                Tenant.AUTH_UPGRADE_SUCCESS};//公司认证
        AuthInfoVO authInfo = null;
        if(Arrays.asList(privateAuth_status).contains(status)){
            authInfo = new PrivateAuthInfoVO();
            RealnamePrivate p = realnamePrivateService.findByTenantIdNewest(tenantId);
            if(p!=null){
                try {
                    BeanUtils.copyProperties(authInfo,p);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
            authInfo.setStatus(info.getStatus());
            authInfo.setType("个人");
            info = authInfo;
        }else if(Arrays.asList(companyAuth_status).contains(status)){
            authInfo = new CompanyAuthInfoVO();
            RealnameCorp p = realnameCorpService.findByTenantIdNewest(tenantId);
            if(p!=null){
                try {
                    BeanUtils.copyProperties(authInfo,p);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            }
            authInfo.setStatus(info.getStatus());
            authInfo.setType("公司");
            info = authInfo;
        }
        return info;
    }

    @ApiOperation(value = "重置租户的密码)")
    @RequestMapping(value="/tenants/{id}/resetPass",method = RequestMethod.PATCH)
    public RestResponse resetPass(@PathVariable String id) throws MailConfigNotEnabledException, MailContentNullException {
        Account account = accountService.findOneByTenant(id);
        if(account == null || account.getEmail() == null){
            return RestResponse.success(false);
        }
        mqService.publish(new ResetPwdVerifySuccessEvent(account.getEmail()));
        return RestResponse.success(true);
    }

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
                vo.setAmongDuration(voiceCdrMonthService.getAmongDurationByDateAndApp(preMonth,app.getId())/60);
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
        vo.setTestPhone(tests.parallelStream().parallel().map(t -> t.getNumber()).collect(Collectors.toList()));
        return RestResponse.success(vo);
    }

    @ApiOperation(value = "获取租户的app放音文件列表")
    @RequestMapping(value = "/tenants/{tenant}/apps/{appId}/plays",method = RequestMethod.GET)
    public RestResponse plays(
            @PathVariable String tenant,@PathVariable String appId,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name){
        Page<VoiceFilePlay> page = voiceFilePlayService.pageList(pageNo,pageSize,name,appId,new String[]{tenant},1,null,null);
        return RestResponse.success(page);
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

    @ApiOperation(value = "获取功能开关")
    @RequestMapping(value = "/tenants/{tenant}/switchs",method = RequestMethod.GET)
    public RestResponse switchs(
            @PathVariable String tenant) throws MatchMutiEntitiesException {
        TenantServiceSwitch switchs=tenantServiceSwitchService.findOneByTenant(tenant);
        if(switchs == null){
            switchs = tenantServiceSwitchService.saveOrUpdate(tenant,null);
        }
        return RestResponse.success(switchs);
    }

    @ApiOperation(value = "保存功能开关")
    @RequestMapping(value = "/tenants/{tenant}/switch",method = RequestMethod.PUT)
    public RestResponse switchs(
            @PathVariable String tenant,@RequestBody TenantServiceSwitch switchs){
        return RestResponse.success(tenantServiceSwitchService.saveOrUpdate(tenant,switchs));
    }

    @ApiOperation(value = "租户的app列表")
    @RequestMapping(value="/tenants/{id}/app/list",method = RequestMethod.GET)
    public RestResponse appList(@PathVariable String id) throws MailConfigNotEnabledException, MailContentNullException {
        return RestResponse.success(appService.getAppsByTenantId(id));
    }

    @ApiOperation(value = "租户的月结账单")
    @RequestMapping(value="/tenants/{tenant}/consume_month",method = RequestMethod.GET)
    public RestResponse get(@PathVariable String tenant,
        @RequestParam(required = false) String appId,
        @ApiParam(name = "month",value = "格式:yyyy-MM")
        @RequestParam(required = false) String month){
        if(StringUtils.isBlank(month)){
            String curMonth = DateUtils.getDate("yyyy-MM");
            month = DateUtils.getPrevMonth(curMonth,"yyyy-MM");
        }
        List<ConsumeMonth> consumeMonths = consumeMonthService.getConsumeMonths(tenant,appId,month);
        changeTypeToChineseOfConsumeMonth(consumeMonths);
        return RestResponse.success(consumeMonths);
    }



    @ApiOperation(value = "租户(某月所有天/某年所有月)的消费额统计")
    @RequestMapping(value = "/tenants/{tenant}/consume/statistic",method = RequestMethod.GET)
    public RestResponse consumeStatistic(
            @PathVariable String tenant,
            @RequestParam(value = "year") Integer year,
            @ApiParam(name = "month",value="不传month就是某年所有月的统计")
            @RequestParam(value = "month",required = false) Integer month,
            @RequestParam(required = false) String appId
    ){
        if(month!=null){
            return RestResponse.success(perDayOfMonthConsumeStatistic(year,month,tenant,appId));
        }
        return RestResponse.success(perMonthOfYearConsumeStatistic(year,tenant,appId));
    }

    /**
     * 统计某个租户某年的每月的消费额
     * @return
     */
    private List<Double> perMonthOfYearConsumeStatistic(int year,String tenant,String appId){
        List<Double> results = new ArrayList<Double>();
        int month_length = 12;
        ExecutorService pool= Executors.newFixedThreadPool(month_length);
        List<Future<Double>> fs = new ArrayList<Future<Double>>();
        //先计算出某年所有月的开始和结束时间
        for (int month =1;month<=month_length;month++){
            Date month_start = DateUtils.newDate(year,month,1);
            fs.add(pool.submit(new Callable<Double>() {
                @Override
                public Double call(){
                    BigDecimal dec = consumeMonthService.getAmongAmountByDateAndTenant(month_start,tenant,appId);
                    if(dec!=null){
                        return dec.doubleValue();
                    }
                    return null;
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
        return results;
    }

    @ApiOperation(value = "租户(某年所有月)的充值额统计")
    @RequestMapping(value = "/tenants/{tenant}/recharges/statistic",method = RequestMethod.GET)
    public RestResponse recharges(@PathVariable String tenant,@RequestParam Integer year){
        return RestResponse.success(perMonthOfYearRechargeStatistic(year,tenant));
    }

    /**
     * 统计某个租户某年的每月的充值额
     * @return
     */
    private List<BigDecimal> perMonthOfYearRechargeStatistic(int year,String tenant){
        List<BigDecimal> results = new ArrayList<BigDecimal>();
        int month_length = 12;
        ExecutorService pool= Executors.newFixedThreadPool(month_length);
        List<Future<BigDecimal>> fs = new ArrayList<Future<BigDecimal>>();
        //先计算出某年所有月的开始和结束时间
        for (int month =1;month<=month_length;month++){
            Date month_start = DateUtils.newDate(year,month,1);
            fs.add(pool.submit(new Callable<BigDecimal>() {
                @Override
                public BigDecimal call(){
                    return rechargeMonthService.getAmongAmountByDateAndTenant(month_start,tenant);
                }
            }));
        }
        pool.shutdown();
        for (Future<BigDecimal> future : fs) {
            try {
                results.add(future.get());
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return results;
    }

    @ApiOperation(value = "租户的充值额详单")
    @RequestMapping(value = "/tenants/{tenant}/recharges",method = RequestMethod.GET)
    public RestResponse recharges(
            @PathVariable String tenant,
            @RequestParam(required = false,defaultValue = "1") Integer pageNo,
            @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        return RestResponse.success(rechargeService.pageListByTenant(tenant,pageNo,pageSize));
    }


    /**
     * 将消费类型转换为中文，运用枚举
     * @param consumeMonths
     */
    private void changeTypeToChineseOfConsumeMonth(List<ConsumeMonth> consumeMonths){
        for (ConsumeMonth consumeMonth:consumeMonths){
            String type = consumeMonth.getType();
            try{
                ConsumeCode consumeCode = ConsumeCode.valueOf(type);
                consumeMonth.setType(consumeCode.getName());
            }catch(Exception e){
//                e.printStackTrace();
                consumeMonth.setType("未知项目");
            }
        }
    }


    /**
     * 将消费类型转换为中文，运用枚举
     * @param consumes
     */
    private void changeTypeToChineseOfConsume(List<Consume> consumes){
        for (Consume consume:consumes){
            String type = consume.getType();
            try{
                ConsumeCode consumeCode = ConsumeCode.valueOf(type);
                consume.setType(consumeCode.getName());
            }catch(Exception e){
//                e.printStackTrace();
                consume.setType("未知项目");
            }
        }
    }

}
