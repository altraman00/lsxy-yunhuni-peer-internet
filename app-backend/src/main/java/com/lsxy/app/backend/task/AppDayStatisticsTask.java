package com.lsxy.app.backend.task;

import com.lsxy.framework.api.statistics.service.AppDayService;
import com.lsxy.framework.core.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * App定时统计任务-日统计
 * Created by zhangxb on 2016/7/29.
 */
@Component
public class AppDayStatisticsTask {
    private static final Logger logger = LoggerFactory.getLogger(AppDayStatisticsTask.class);
    @Autowired
    AppDayService appDayService;

    /**
     * 每天02：00：00触发执行
     */
    @Scheduled(cron="0 0 2 * * ?")
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

}
