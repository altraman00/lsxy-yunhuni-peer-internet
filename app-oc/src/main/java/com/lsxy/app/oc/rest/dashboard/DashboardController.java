package com.lsxy.app.oc.rest.dashboard;

import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.app.oc.rest.dashboard.vo.*;
import com.lsxy.yunhuni.api.statistics.service.ConsumeDayService;
import com.lsxy.yunhuni.api.statistics.service.ConsumeMonthService;
import com.lsxy.yunhuni.api.statistics.service.VoiceCdrDayService;
import com.lsxy.yunhuni.api.statistics.service.VoiceCdrMonthService;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.app.service.AppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2016/8/9.
 */
@Api(value = "dashboard", description = "dashboard 相关的接口" )
@RestController
@RequestMapping("/dashboard")
public class DashboardController extends AbstractRestController {

    @Autowired
    private TenantService tenantService;

    @Autowired
    private AppService appService;

    @Autowired
    private VoiceCdrDayService voiceCdrDayService;

    @Autowired
    private VoiceCdrMonthService voiceCdrMonthService;

    @Autowired
    private ConsumeDayService consumeDayService;

    @Autowired
    private ConsumeMonthService consumeMonthService;


    @ApiOperation(value = "租户数据")
    @RequestMapping(value = "/member/indicant",method = RequestMethod.GET)
    public RestResponse member(){
        MemberIndicantVO dto = new MemberIndicantVO();
        //注册会员总数
        dto.setRegistTotal(tenantService.countValidTenant());
        dto.setRegistTatalDay(tenantService.countValidTenantToday());
        dto.setRegistTatalWeek(tenantService.countValidTenantWeek());
        dto.setRegistTatalMonth(tenantService.countValidTenantMonth());
        //已认证会员总数
        dto.setAuthTotal(tenantService.countAuthTenant());
        dto.setAuthTotalDay(tenantService.countAuthTenantToday());
        dto.setAuthTotalWeek(tenantService.countAuthTenantWeek());
        dto.setAuthTotalMonth(tenantService.countAuthTenantMonth());
        //已产生消费总数
        dto.setConsume(tenantService.countConsumeTenant());
        dto.setConsumeDay(tenantService.countConsumeTenantToday());
        dto.setConsumeWeek(tenantService.countConsumeTenantWeek());
        dto.setConsumeMonth(tenantService.countConsumeTenantMonth());
        return RestResponse.success(dto);
    }


    @ApiOperation(value = "应用数据，应用总数，在线应用数")
    @RequestMapping(value = "/app/indicant",method = RequestMethod.GET)
    public RestResponse app(){
        ApplicationIndicantVO dto = new ApplicationIndicantVO();
        dto.setOnline(appService.countOnline());
        dto.setTotal(appService.countValid());
        return RestResponse.success(dto);
    }

    @ApiOperation(value = "租户，应用，按日/月统计")
    @RequestMapping(value = "/statistic",method = RequestMethod.GET)
    public RestResponse memberStatistic(
            @RequestParam(value = "year") Integer year,
            @RequestParam(value = "month",required = false) Integer month){
        StatisticVO dto = new StatisticVO();
        if(month!=null){
            dto.setMemberCount(perDayOfMonthMemberStatistic(year,month));
            dto.setAppCount(perDayOfMonthAppStatistic(year,month));
        }else {
            dto.setMemberCount(perMonthOfYearMemberStatistic(year));
            dto.setAppCount(perMonthOfYearAppStatistic(year));
        }
        return RestResponse.success(dto);
    }

    /**
     * 统计某个月的每天的注册租户数
     * @return
     */
    private List<Integer> perDayOfMonthMemberStatistic(int year,int month){
        List<Integer> results = new ArrayList<Integer>();
        //先计算出某个月的所有天的开始和结束时间
        Date cdate = DateUtils.newDate(year,month,1);
        Date d1=DateUtils.getFirstTimeOfMonth(cdate);
        Date d2 =DateUtils.getLastTimeOfMonth(cdate);
        Date[] ds = DateUtils.getDatesBetween(d1,d2);
        if(ds!=null && ds.length>0){
            ExecutorService pool= Executors.newFixedThreadPool(ds.length);
            List<Future<Integer>> fs = new ArrayList<Future<Integer>>();
            for (final Date d: ds) {
                fs.add(pool.submit(new Callable<Integer>() {
                    @Override
                    public Integer call(){
                        return tenantService.countValidTenantDateBetween(
                                DateUtils.getFirstTimeOfDate(d),
                                DateUtils.getLastTimeOfDate(d));
                    }
                }));
            }
            pool.shutdown();
            for (Future<Integer> future : fs) {
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
     * 统计某年的每月的注册租户数
     * @return
     */
    private List<Integer> perMonthOfYearMemberStatistic(int year){
        List<Integer> results = new ArrayList<Integer>();
        int month_length = 12;
        ExecutorService pool= Executors.newFixedThreadPool(month_length);
        List<Future<Integer>> fs = new ArrayList<Future<Integer>>();
        //先计算出某年所有月的开始和结束时间
        for (int month =1;month<=month_length;month++){
            Date month_start = DateUtils.newDate(year,month,1);
            fs.add(pool.submit(new Callable<Integer>() {
                @Override
                public Integer call(){
                    return tenantService.countValidTenantDateBetween(
                            DateUtils.getFirstTimeOfMonth(month_start),
                            DateUtils.getLastTimeOfMonth(month_start));
                }
            }));
        }
        pool.shutdown();
        for (Future<Integer> future : fs) {
            try {
                results.add(future.get());
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return results;
    }

    /**
     * 统计某个月的每天的app数
     * @return
     */
    private List<Integer> perDayOfMonthAppStatistic(int year, int month){
        List<Integer> results = new ArrayList<Integer>();
        //先计算出某个月的所有天的开始和结束时间
        Date cdate = DateUtils.newDate(year,month,1);
        Date d1=DateUtils.getFirstTimeOfMonth(cdate);
        Date d2 =DateUtils.getLastTimeOfMonth(cdate);
        Date[] ds = DateUtils.getDatesBetween(d1,d2);
        if(ds!=null && ds.length>0){
            ExecutorService pool= Executors.newFixedThreadPool(ds.length);
            List<Future<Integer>> fs = new ArrayList<Future<Integer>>();
            for (final Date d: ds) {
                fs.add(pool.submit(new Callable<Integer>() {
                    @Override
                    public Integer call(){
                        return appService.countValidDateBetween(
                                DateUtils.getFirstTimeOfDate(d),
                                DateUtils.getLastTimeOfDate(d));
                    }
                }));
            }
            pool.shutdown();
            for (Future<Integer> future : fs) {
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
     * 统计某年的每月的app数
     * @return
     */
    private List<Integer> perMonthOfYearAppStatistic(int year){
        List<Integer> results = new ArrayList<Integer>();
        int month_length = 12;
        ExecutorService pool= Executors.newFixedThreadPool(month_length);
        List<Future<Integer>> fs = new ArrayList<Future<Integer>>();
        //先计算出某年所有月的开始和结束时间
        for (int month =1;month<=month_length;month++){
            Date month_start = DateUtils.newDate(year,month,1);
            fs.add(pool.submit(new Callable<Integer>() {
                @Override
                public Integer call(){
                    return appService.countValidDateBetween(
                            DateUtils.getFirstTimeOfMonth(month_start),
                            DateUtils.getLastTimeOfMonth(month_start));
                }
            }));
        }
        pool.shutdown();
        for (Future<Integer> future : fs) {
            try {
                results.add(future.get());
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return results;
    }


    /**
     * 话务量指标
     * @return
     */
    @ApiOperation(value = "话务量指标")
    @RequestMapping(value = "/duration/indicant",method = RequestMethod.GET)
    public RestResponse durationIndicant(){
        DurationIndicantVO dto = new DurationIndicantVO();
        long yesterday = getDurationIndicantOfYesterday();
        long beforeYesterday = getDurationIndicantOfBeforeYesterday();
        long beforeWeek = getDurationIndicantOfBeforeWeek();
        long before2Week = getDurationIndicantOfBefore2Week();
        long beforeMonth = getDurationIndicantOfBeforeMonth();
        long before2Month = getDurationIndicantOfBefore2Month();
        //分母加0.1是为了防止分母为0
        Map dto1 = new HashMap();
        dto1.put("rateOfDay",false);
        dto1.put("rateOfWeek",false);
        dto1.put("rateOfMonth",false);
        dto.setDuration((long)Math.round(yesterday/60));//yesterday单位为秒，转为分
        if(beforeYesterday>0){
            dto.setRateOfDay(new BigDecimal(((yesterday-beforeYesterday)*0.01)/(beforeYesterday*0.01)*100)
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());//日增长率
            dto1.put("rateOfDay",true);
        }
        if(before2Week>0){
            dto.setRateOfWeek(new BigDecimal(((beforeWeek-before2Week)*0.01)/(before2Week*0.01)*100)
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());//周增长率
            dto1.put("rateOfWeek",true);
        }
        if(before2Month>0){
            dto.setRateOfMonth(new BigDecimal(((beforeMonth-before2Month)*0.01)/(before2Month*0.01)*100)
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());//周增长率
            dto1.put("rateOfMonth",true);
        }
        Map map = new HashMap();
        map.put("dto",dto);
        map.put("dto1",dto1);
        return RestResponse.success(map);
    }

    //获取前天话务量
    private long getDurationIndicantOfBeforeYesterday(){
        Date toDay = new Date();
        Date pre_pre_date = DateUtils.getPreDate(DateUtils.getPreDate(toDay));//前天
        return voiceCdrDayService.getAmongCostTimeByDate(pre_pre_date);
    }

    //获取昨天话务量
    private long getDurationIndicantOfYesterday(){
        Date toDay = new Date();
        Date pre_date = DateUtils.getPreDate(toDay);//昨天
        return voiceCdrDayService.getAmongCostTimeByDate(pre_date);
    }

    //获取上上周的话务量
    private long getDurationIndicantOfBefore2Week(){
        Date toDay = new Date();
        Date pp_week_date = DateUtils.getPrevWeek(DateUtils.getPrevWeek(toDay));
        return voiceCdrDayService.getAmongCostTimeBetween(
                        DateUtils.getFirstDayOfWeek(pp_week_date),DateUtils.getLastDayOfWeek(pp_week_date));
    }
    //获取上周的话务量
    private long getDurationIndicantOfBeforeWeek(){
        Date toDay = new Date();
        Date p_week_date = DateUtils.getPrevWeek(toDay);
        return voiceCdrDayService.getAmongCostTimeBetween(
                DateUtils.getFirstDayOfWeek(p_week_date),DateUtils.getLastDayOfWeek(p_week_date));
    }
    //获取上上月的话务量
    private long getDurationIndicantOfBefore2Month(){
        Date toDay = new Date();
        Date pp_month_date = DateUtils.getPrevMonth(DateUtils.getPrevMonth(toDay));
        return voiceCdrMonthService.getAmongCostTimeByDate(pp_month_date);
    }
    //获取上月的话务量
    private long getDurationIndicantOfBeforeMonth(){
        Date toDay = new Date();
        Date p_month_date = DateUtils.getPrevMonth(toDay);
        return voiceCdrMonthService.getAmongCostTimeByDate(p_month_date);
    }

    /**
     * 消费额指标
     * @return
     */
    @ApiOperation(value = "消费额数据，指标")
    @RequestMapping(value = "/comsume/indicant",method = RequestMethod.GET)
    public RestResponse comsumeIndicant(){
        ConsumeIndicantVO dto = new ConsumeIndicantVO();
        double yesterday = getComsumeIndicantOfYesterday().doubleValue();
        double beforeYesterday = getComsumeIndicantOfBeforeYesterday().doubleValue();
        double beforeWeek = getComsumeIndicantOfBeforeWeek().doubleValue();
        double before2Week = getComsumeIndicantOfBefore2Week().doubleValue();
        double beforeMonth = getComsumeIndicantOfBeforeMonth().doubleValue();
        double before2Month = getComsumeIndicantOfBefore2Month().doubleValue();
        //分母加0.1是为了防止分母为0
        dto.setConsume(yesterday);
        Map dto1 = new HashMap();
        dto1.put("rateOfDay", false);
        dto1.put("rateOfWeek",false);
        dto1.put("rateOfMonth",false);
        if(beforeYesterday>0) {
            dto.setRateOfDay(new BigDecimal(((yesterday - beforeYesterday)*0.01) / (beforeYesterday*0.01)*100)
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());//日增长率
            dto1.put("rateOfDay", true);
        }
        if(before2Week>0) {
            dto.setRateOfWeek(new BigDecimal(((beforeWeek - before2Week)*0.01) / (before2Week*0.01)*100)
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());//周增长率
            dto1.put("rateOfWeek", true);
        }
        if(before2Month>0) {
            dto.setRateOfMonth(new BigDecimal(((beforeMonth - before2Month)*0.01) / (before2Month*0.01)*100)
                    .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());//周增长率
            dto1.put("rateOfMonth", true);
        }
        Map map = new HashMap();
        map.put("dto",dto);
        map.put("dto1",dto1);
        return RestResponse.success(map);
    }

    //获取前天消费额
    private BigDecimal getComsumeIndicantOfBeforeYesterday(){
        Date toDay = new Date();
        Date pre_pre_date = DateUtils.getPreDate(DateUtils.getPreDate(toDay));//前天
        return consumeDayService.getAmongAmountByDate(pre_pre_date);
    }

    //获取昨天消费额
    private BigDecimal getComsumeIndicantOfYesterday(){
        Date toDay = new Date();
        Date pre_date = DateUtils.getPreDate(toDay);//昨天
        return consumeDayService.getAmongAmountByDate(pre_date);
    }

    //获取上上周的消费额
    private BigDecimal getComsumeIndicantOfBefore2Week(){
        Date toDay = new Date();
        Date pp_week_date = DateUtils.getPrevWeek(DateUtils.getPrevWeek(toDay));
        return consumeDayService.getAmongAmountBetween(
                DateUtils.getFirstDayOfWeek(pp_week_date),DateUtils.getLastDayOfWeek(pp_week_date));
    }
    //获取上周的消费额
    private BigDecimal getComsumeIndicantOfBeforeWeek(){
        Date toDay = new Date();
        Date p_week_date = DateUtils.getPrevWeek(toDay);
        return consumeDayService.getAmongAmountBetween(
                DateUtils.getFirstDayOfWeek(p_week_date),DateUtils.getLastDayOfWeek(p_week_date));
    }
    //获取上上月的消费额
    private BigDecimal getComsumeIndicantOfBefore2Month(){
        Date toDay = new Date();
        Date pp_month_date = DateUtils.getPrevMonth(DateUtils.getPrevMonth(toDay));
        return consumeMonthService.getAmongAmountByDate(pp_month_date);
    }
    //获取上月的消费额
    private BigDecimal getComsumeIndicantOfBeforeMonth(){
        Date toDay = new Date();
        Date p_month_date = DateUtils.getPrevMonth(toDay);
        return consumeMonthService.getAmongAmountByDate(p_month_date);
    }

    /**
     * 消费额  和话务量  某月所有天的统计
     * @param year
     * @param month
     * @return
     */
    @ApiOperation(value = "消费额  和话务量  某月所有天的统计")
    @RequestMapping(value = "/consumeAnduration/statistic",method = RequestMethod.GET)
    public RestResponse consumeAndurationStatistic(
            @RequestParam(value = "year") Integer year,
            @RequestParam(value = "month") Integer month){
        ConsumeAndurationStatisticVO dto = new ConsumeAndurationStatisticVO();
        dto.setSession(perDayOfMonthDurationStatistic(year,month));
        dto.setCost(perDayOfMonthConsumeStatistic(year,month));
        return RestResponse.success(dto);
    }

    /**
     * 统计某个月的每天的话务量
     * @return
     */
    private List<Long> perDayOfMonthDurationStatistic(int year,int month){
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
                        return (long)Math.round(voiceCdrDayService.getAmongCostTimeByDate(d)/60);
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
     * 统计某个月的每天的消费额
     * @return
     */
    private List<Double> perDayOfMonthConsumeStatistic(int year,int month){
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
                        return consumeDayService.getAmongAmountByDate(d).doubleValue();
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

    /**
     * 活跃会员排名
     * @return
     */
    @ApiOperation(value = "活跃会员（租户）排名")
    @RequestMapping(value = "/member/top",method = RequestMethod.GET)
    public RestResponse memberTop(@RequestParam Integer top){
        MemberTopVO dto = new MemberTopVO();
        dto.setCallTop(voiceCdrDayService.getCallTop(top));
        dto.setDurationTop(voiceCdrDayService.getCostTimeTop(top));
        dto.setConsumeTop(consumeDayService.getConsumeTop(top));
        //上周
        Date toDay = new Date();
        Date p_week_date = DateUtils.getPrevWeek(toDay);
        Date p_week_date_start = DateUtils.getFirstDayOfWeek(p_week_date);
        Date p_week_date_end = DateUtils.getLastDayOfWeek(p_week_date);
        dto.setCallWeekTop(voiceCdrDayService.getCallTopByDateBetween(top,p_week_date_start,p_week_date_end));
        dto.setDurationWeekTop(voiceCdrDayService.getCostTimeTopByDateBetween(top,p_week_date_start,p_week_date_end));
        return RestResponse.success(dto);
    }
}
