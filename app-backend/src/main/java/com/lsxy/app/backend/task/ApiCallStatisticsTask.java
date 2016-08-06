package com.lsxy.app.backend.task;

import com.lsxy.framework.api.statistics.service.ApiCallDayService;
import com.lsxy.framework.api.statistics.service.ApiCallHourService;
import com.lsxy.framework.api.statistics.service.ApiCallMonthService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.utils.StatisticsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * api请求定时统计任务
 * Created by zhangxb on 2016/7/29.
 */
@Component
public class ApiCallStatisticsTask {
    private static final Logger logger = LoggerFactory.getLogger(ApiCallStatisticsTask.class);
    @Autowired
    ApiCallHourService apiCallHourService;
    @Autowired
    ApiCallMonthService apiCallMonthService;
    @Autowired
    ApiCallDayService apiCallDayService;
    /**
     *每个小时的执行一次
     */
    @Scheduled(cron="0 30 0/1 * * ?")
    public void hour(){
        long startTime = System.currentTimeMillis();
        logger.info("api请求指标小时统计任务开启，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
        //租户 应用 运营商 地区 业务类型 应用上线个数增量/总量 应用未上线个数增量/总量 应用总个数增量/总量
        String partten = "yyyy-MM-dd HH";
        Date date=new Date();
        Calendar cale = Calendar.getInstance();
        cale.set(Calendar.HOUR_OF_DAY, cale.get(Calendar.HOUR_OF_DAY) - 1);
        date = cale.getTime();
        int hour1 = cale.get(Calendar.HOUR_OF_DAY);
        Date date1 = DateUtils.parseDate(DateUtils.getDate(date,partten),partten);
        //前一个小时的时间
        cale.set(Calendar.HOUR_OF_DAY, cale.get(Calendar.HOUR_OF_DAY) - 1);
        date = cale.getTime();
        int hour2 = cale.get(Calendar.HOUR_OF_DAY);
        Date date2 = DateUtils.parseDate(DateUtils.getDate(date,partten),partten);
        List<String[]> list = StatisticsUtils.getGroupBys(new String[]{"tenant_id","app_id","type"});
        try{
            logger.info("参数date1={}，hour1={}，date2={}，hour2={}",DateUtils.formatDate(date1,"yyyy-MM-dd HH:mm:ss"),hour1,DateUtils.formatDate(date2,"yyyy-MM-dd HH:mm:ss"),hour2);
            for(int i=0;i<list.size();i++){
                logger.info("子任务：小时统计维度{}任务开启，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"), Arrays.toString(list.get(i)));
                apiCallHourService.hourStatistics(date1,hour1,date2,hour2,list.get(i));
            }
            long endTime = System.currentTimeMillis();
            logger.info("api请求指标小时统计任务结束，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")+" ,花费时间为："+(endTime-startTime)+"毫秒");
        }catch (Exception e){
            long endTime = System.currentTimeMillis();
            logger.error("api请求指标小时统计任务异常结束，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")+" ,花费时间为："+(endTime-startTime)+"毫秒");
            logger.error("失败原因",e);
        }
    }


    /**
     * 每天02：00：00触发执行
     */
    @Scheduled(cron="0 0 2 * * ?")
    public void day(){
        long startTime = System.currentTimeMillis();
        logger.info("api请求指标日统计任务开启，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
        String partten = "yyyy-MM-dd";
        Date date=new Date();
        Calendar cale = Calendar.getInstance();
        cale.setTime(date);
        cale.set(Calendar.DATE, cale.get(Calendar.DATE) - 1);
        date = cale.getTime();
        //当前统计时间
        int day1 = cale.get(Calendar.DATE);
        Date date1 = DateUtils.parseDate(DateUtils.getDate(date,partten),partten);
        //前一天的时间
        cale.set(Calendar.DATE, cale.get(Calendar.DATE) - 1);
        date = cale.getTime();
        int day2 = cale.get(Calendar.DATE);
        Date date2 = DateUtils.parseDate(DateUtils.getDate(date,partten),partten);
        List<String[]> list = StatisticsUtils.getGroupBys(new String[]{"tenant_id","app_id","type"});
        try{
            logger.info("参数date1={}，hour1={}，date2={}，hour2={}",DateUtils.formatDate(date1,"yyyy-MM-dd HH:mm:ss"),day1,DateUtils.formatDate(date2,"yyyy-MM-dd HH:mm:ss"),day2);
            for(int i=0;i<list.size();i++){
                logger.info("子任务：日统计维度{}任务开启，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"), Arrays.toString(list.get(i)));
                apiCallDayService.dayStatistics(date1,day1,date2,day2,list.get(i));
            }
            long endTime = System.currentTimeMillis();
            logger.info("api请求指标日统计任务结束，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")+" ,花费时间为："+(endTime-startTime)+"毫秒");
        }catch (Exception e){
            long endTime = System.currentTimeMillis();
            logger.error("api请求指标日统计任务异常结束，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")+" ,花费时间为："+(endTime-startTime)+"毫秒");
            logger.error("失败原因",e);
        }
    }
    /**
     * 每月1号2：00：00触发执行
     */
    @Scheduled(cron="0 0 2 1 * ? ")
    public void month(){
        long startTime = System.currentTimeMillis();
        logger.info("api请求指标月统计任务开启，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
        String partten = "yyyy-MM";
        Date date=new Date();
        Calendar cale = Calendar.getInstance();
        cale.setTime(date);
        cale.set(Calendar.MONTH, cale.get(Calendar.MONTH) - 1);
        date = cale.getTime();
        int month1 = (cale.get(Calendar.MONTH)) + 1;
        Date date1 = DateUtils.parseDate(DateUtils.getDate(date,partten),partten);
        //前一天的时间
        cale.set(Calendar.MONTH, cale.get(Calendar.MONTH) - 1);
        date = cale.getTime();
        int month2 = (cale.get(Calendar.MONTH)) + 1;
        Date date2 = DateUtils.parseDate(DateUtils.getDate(date,partten),partten);
        List<String[]> list = StatisticsUtils.getGroupBys(new String[]{"tenant_id","app_id","type"});
        try{
            logger.info("参数date1={}，hour1={}，date2={}，hour2={}",DateUtils.formatDate(date1,"yyyy-MM-dd HH:mm:ss"),month1,DateUtils.formatDate(date2,"yyyy-MM-dd HH:mm:ss"),month2);
            for(int i=0;i<list.size();i++){
                logger.info("子任务：月统计维度{}任务开启，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"), Arrays.toString(list.get(i)));
                apiCallMonthService.monthStatistics(date1,month1,date2,month2,list.get(i));
            }
            long endTime = System.currentTimeMillis();
            logger.info("api请求指标月统计任务结束，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")+" ,花费时间为："+(endTime-startTime)+"毫秒");
        }catch (Exception e){
            long endTime = System.currentTimeMillis();
            logger.error("api请求指标月统计任务异常结束，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")+" ,花费时间为："+(endTime-startTime)+"毫秒");
            logger.error("失败原因",e);
        }
    }

}
