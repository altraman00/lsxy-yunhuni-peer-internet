package com.lsxy.app.backend.task;

import com.lsxy.framework.api.statistics.service.AppDayService;
import com.lsxy.framework.api.statistics.service.AppHourService;
import com.lsxy.framework.api.statistics.service.AppMonthService;
import com.lsxy.framework.core.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * App定时统计任务
 * Created by zhangxb on 2016/7/29.
 */
@Component
public class AppStatisticsTask {
    private static final Logger logger = LoggerFactory.getLogger(AppStatisticsTask.class);
    @Autowired
    AppHourService appHourService;
    @Autowired
    AppDayService appDayService;
    @Autowired
    AppMonthService appMonthService;
    /**
     * 每月1号2：00：00触发执行
     */
    @Scheduled(cron="0 0 2 1 * ? ")
    public void month(){
        long startTime = System.currentTimeMillis();
        logger.info("应用指标月统计任务开启，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
        //租户 应用 运营商 地区 业务类型 应用上线个数增量/总量 应用未上线个数增量/总量 应用总个数增量/总量
        String partten = "yyyy-MM";
        Date date=new Date();
        Calendar cale = Calendar.getInstance();
        cale.setTime(date);
        cale.set(Calendar.DATE, cale.get(Calendar.DATE) - 1);
        date = cale.getTime();
        int month = (cale.get(Calendar.MONTH)) + 1;
        Date date1 = DateUtils.parseDate(DateUtils.getDate(date,partten),partten);
        try{
            logger.info("子任务：月统计租户全局任务开启，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
            //月统计租户全局
            appMonthService.monthStatistics(date1,month,new String[]{});
            logger.info("子任务：月天统计运营全局任务开启，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
            //月统计运营全局
            appMonthService.monthStatistics(date1,month,new String[]{"tenant_id"});
            long endTime = System.currentTimeMillis();
            logger.info("应用指标月统计任务结束，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")+" ,花费时间为："+(endTime-startTime)+"毫秒");
        }catch (Exception e){
            long endTime = System.currentTimeMillis();
            logger.error("应用指标月统计任务异常结束，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")+" ,花费时间为："+(endTime-startTime)+"毫秒");
            logger.error("失败原因",e);
        }
    }
    /**
     * 每天02：00：00触发执行
     */
    @Scheduled(cron="0 0 1 * * ?")
    public void days(){
        long startTime = System.currentTimeMillis();
        logger.info("应用指标日统计任务开启，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
        //租户 应用 运营商 地区 业务类型 应用上线个数增量/总量 应用未上线个数增量/总量 应用总个数增量/总量
        String partten = "yyyy-MM-dd";
        Date date=new Date();
        Calendar cale = Calendar.getInstance();
        cale.setTime(date);
        cale.set(Calendar.DATE, cale.get(Calendar.DATE) - 1);
        date = cale.getTime();
        int day = cale.get(Calendar.DATE);
        Date date1 = DateUtils.parseDate(DateUtils.getDate(date,partten),partten);
        try{
            logger.info("子任务：日统计租户全局任务开启，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
            //日统计租户全局
            appDayService.dayStatistics(date1,day,new String[]{});
            logger.info("子任务：日统计运营全局任务开启，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
            //日统计运营全局
            appDayService.dayStatistics(date1,day,new String[]{"tenant_id"});
            long endTime = System.currentTimeMillis();
            logger.info("应用指标日统计任务结束，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")+" ,花费时间为："+(endTime-startTime)+"毫秒");
        }catch (Exception e){
            long endTime = System.currentTimeMillis();
            logger.error("应用指标日统计任务异常结束，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")+" ,花费时间为："+(endTime-startTime)+"毫秒");
            logger.error("失败原因",e);
        }
    }
    /**
     *每个小时的执行一次
     */
    @Scheduled(cron="0 30 0/1 * * ?")
    public void hour(){
        long startTime = System.currentTimeMillis();
        logger.info("应用指标小时统计任务开启，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
        //租户 应用 运营商 地区 业务类型 应用上线个数增量/总量 应用未上线个数增量/总量 应用总个数增量/总量
        String partten = "yyyy-MM-dd HH";
        Date date=new Date();
        Calendar cale = Calendar.getInstance();
        cale.set(Calendar.HOUR_OF_DAY, cale.get(Calendar.HOUR_OF_DAY) - 1);
        cale.setTime(date);
        int hour24 = cale.get(Calendar.HOUR_OF_DAY);
        Date date1 = DateUtils.parseDate(DateUtils.getDate(date,partten),partten);
        try{
            logger.info("子任务：小时统计租户全局任务开启，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
            //小时统计租户全局
            appHourService.hourStatistics(date1,hour24,new String[]{});
            logger.info("子任务：小时统计运营全局任务开启，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
            //小时统计运营全局
            appHourService.hourStatistics(date1,hour24,new String[]{"tenant_id"});
            long endTime = System.currentTimeMillis();
            logger.info("应用指标小时统计任务结束，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")+" ,花费时间为："+(endTime-startTime)+"毫秒");
        }catch (Exception e){
            long endTime = System.currentTimeMillis();
            logger.error("应用指标小时统计任务异常结束，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")+" ,花费时间为："+(endTime-startTime)+"毫秒");
            logger.error("失败原因",e);
        }

    }
}
