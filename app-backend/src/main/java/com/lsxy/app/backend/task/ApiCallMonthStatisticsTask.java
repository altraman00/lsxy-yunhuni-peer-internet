package com.lsxy.app.backend.task;

import com.lsxy.framework.api.statistics.service.ApiCallMonthService;
import com.lsxy.framework.core.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * api请求定时统计任务-月统计
 * Created by zhangxb on 2016/7/29.
 */
@Component
public class ApiCallMonthStatisticsTask {
    private static final Logger logger = LoggerFactory.getLogger(ApiCallMonthStatisticsTask.class);
    @Autowired
    ApiCallMonthService apiCallMonthService;

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
        List<String[]> list = new ArrayList();
        list.add(new String[]{});
        list.add(new String[]{"tenant_id"});
        list.add(new String[]{"app_id"});
        list.add(new String[]{"type"});
        list.add(new String[]{"tenant_id","app_id"});
        list.add(new String[]{"tenant_id","type"});
        list.add(new String[]{"app_id","type"});
        list.add(new String[]{"tenant_id","app_id","type"});
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
