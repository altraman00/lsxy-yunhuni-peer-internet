package com.lsxy.app.backend.task;

import com.lsxy.framework.api.statistics.service.VoiceCdrDayService;
import com.lsxy.framework.core.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 通话记录统计（session统计）定时统计任务-日统计
 * Created by zhangxb on 2016/7/29.
 */
@Component
public class VoiceCdrDayStatisticsTask {
    private static final Logger logger = LoggerFactory.getLogger(VoiceCdrDayStatisticsTask.class);
    @Autowired
    VoiceCdrDayService voiceCdrDayService;

    /**
     * 每天02：00：00触发执行
     */
    @Scheduled(cron="0 0 2 * * ?")
    public void day(){
        long startTime = System.currentTimeMillis();
        logger.info("通话记录统计（session统计）指标日统计任务开启，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
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
            logger.info("参数date1={}，hour1={}，date2={}，hour2={}",DateUtils.formatDate(date1,"yyyy-MM-dd HH:mm:ss"),day1,DateUtils.formatDate(date2,"yyyy-MM-dd HH:mm:ss"),day2);
            for(int i=0;i<list.size();i++){
                logger.info("子任务：日统计维度{}任务开启，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"), Arrays.toString(list.get(i)));
                voiceCdrDayService.dayStatistics(date1,day1,date2,day2,list.get(i));
            }
            long endTime = System.currentTimeMillis();
            logger.info("通话记录统计（session统计）指标日统计任务结束，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")+" ,花费时间为："+(endTime-startTime)+"毫秒");
        }catch (Exception e){
            long endTime = System.currentTimeMillis();
            logger.error("通话记录统计（session统计）指标日统计任务异常结束，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")+" ,花费时间为："+(endTime-startTime)+"毫秒");
            logger.error("失败原因",e);
        }
    }

}
