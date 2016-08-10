package com.lsxy.app.oc.rest.dashboard;

import com.lsxy.app.oc.rest.dashboard.vo.ApplicationIndicantVO;
import com.lsxy.app.oc.rest.dashboard.vo.DurationIndicantVO;
import com.lsxy.app.oc.rest.dashboard.vo.MemberIndicantVO;
import com.lsxy.app.oc.rest.dashboard.vo.StatisticVO;
import com.lsxy.framework.api.statistics.service.VoiceCdrDayService;
import com.lsxy.framework.api.statistics.service.VoiceCdrMonthService;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.app.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2016/8/9.
 */
@RestController
@RequestMapping("/dashboard")
public class IndexController {

    @Autowired
    private TenantService tenantService;

    @Autowired
    private AppService appService;

    @Autowired
    private VoiceCdrDayService voiceCdrDayService;

    @Autowired
    private VoiceCdrMonthService voiceCdrMonthService;


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


    @RequestMapping(value = "/app/indicant",method = RequestMethod.GET)
    public RestResponse app(){
        ApplicationIndicantVO dto = new ApplicationIndicantVO();
        dto.setOnline(appService.countOnline());
        dto.setTotal(appService.countValid());
        return RestResponse.success(dto);
    }

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
        Date cdate = new Date(year-1900,month-1,1);
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
        for (int month =0;month<month_length;month++){
            Date month_start = new Date(year-1900,month,1);
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
        Date cdate = new Date(year-1900,month-1,1);
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
        for (int month =0;month<month_length;month++){
            Date month_start = new Date(year-1900,month,1);
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
    @RequestMapping(value = "/duration/indicant",method = RequestMethod.GET)
    public RestResponse durationIndicant(){
        DurationIndicantVO dto = new DurationIndicantVO();
        dto.setpDay(getDurationIndicantOfYesterday());
        dto.setPpDay(getDurationIndicantOfBeforeYesterday());
        dto.setpWeek(getDurationIndicantOfBeforeWeek());
        dto.setPpWeek(getDurationIndicantOfBefore2Week());
        dto.setpMonth(getDurationIndicantOfBeforeMonth());
        dto.setPpMonth(getDurationIndicantOfBefore2Month());
        return RestResponse.success(dto);
    }

    //获取前天话务量
    private long getDurationIndicantOfBeforeYesterday(){
        Date toDay = new Date();
        Date pre_pre_date = DateUtils.getPreDate(DateUtils.getPreDate(toDay));//前天
        return voiceCdrDayService.getAmongDurationByDate(pre_pre_date);
    }

    //获取昨天话务量
    private long getDurationIndicantOfYesterday(){
        Date toDay = new Date();
        Date pre_date = DateUtils.getPreDate(toDay);//昨天
        return voiceCdrDayService.getAmongDurationByDate(pre_date);
    }

    //获取上上周的话务量
    private long getDurationIndicantOfBefore2Week(){
        Date toDay = new Date();
        Date pp_week_date = DateUtils.getPrevWeek(DateUtils.getPrevWeek(toDay));
        return voiceCdrDayService.getAmongDurationBetween(
                        DateUtils.getFirstDayOfWeek(pp_week_date),DateUtils.getLastDayOfWeek(pp_week_date));
    }
    //获取上周的话务量
    private long getDurationIndicantOfBeforeWeek(){
        Date toDay = new Date();
        Date p_week_date = DateUtils.getPrevWeek(toDay);
        return voiceCdrDayService.getAmongDurationBetween(
                DateUtils.getFirstDayOfWeek(p_week_date),DateUtils.getLastDayOfWeek(p_week_date));
    }
    //获取上上月的话务量
    private long getDurationIndicantOfBefore2Month(){
        Date toDay = new Date();
        Date pp_month_date = DateUtils.getPrevMonth(DateUtils.getPrevMonth(toDay));
        return voiceCdrDayService.getAmongDurationBetween(
                DateUtils.getFirstTimeOfMonth(pp_month_date),DateUtils.getLastTimeOfMonth(pp_month_date));
    }
    //获取上月的话务量
    private long getDurationIndicantOfBeforeMonth(){
        Date toDay = new Date();
        Date p_month_date = DateUtils.getPrevMonth(toDay);
        return voiceCdrDayService.getAmongDurationBetween(
                DateUtils.getFirstTimeOfMonth(p_month_date),DateUtils.getLastTimeOfMonth(p_month_date));
    }
}
