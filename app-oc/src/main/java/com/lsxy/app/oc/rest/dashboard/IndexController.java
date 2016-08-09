package com.lsxy.app.oc.rest.dashboard;

import com.lsxy.app.oc.rest.dashboard.dto.ApplicationIndicantDTO;
import com.lsxy.app.oc.rest.dashboard.dto.MemberIndicantDTO;
import com.lsxy.app.oc.rest.dashboard.dto.RegMemberStatisticDTO;
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
import java.util.concurrent.*;

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


    @RequestMapping(value = "/member/indicant",method = RequestMethod.GET)
    public RestResponse member(){
        MemberIndicantDTO dto = new MemberIndicantDTO();
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
        ApplicationIndicantDTO dto = new ApplicationIndicantDTO();
        dto.setOnline(appService.countOnline());
        dto.setTotal(appService.countValid());
        return RestResponse.success(dto);
    }

    @RequestMapping(value = "/member/statistic",method = RequestMethod.GET)
    public RestResponse memberStatistic(
            @RequestParam(value = "year",required = true) Integer year,
            @RequestParam(value = "month",required = false) Integer month){
        if(month!=null){
            return RestResponse.success(perDayOfMonth(year,month));
        }
        return RestResponse.success(perMonthOfYear(year));

    }

    /**
     * 统计某个月的每天的注册租户数
     * @return
     */
    private RegMemberStatisticDTO perDayOfMonth(int year,int month){
        RegMemberStatisticDTO dto = new RegMemberStatisticDTO();
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
        dto.setMemberCount(results);
        return dto;
    }

    /**
     * 统计某年的每月的注册租户数
     * @return
     */
    private RegMemberStatisticDTO perMonthOfYear(int year){
        RegMemberStatisticDTO dto = new RegMemberStatisticDTO();
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
        dto.setMemberCount(results);
        return dto;
    }
}
