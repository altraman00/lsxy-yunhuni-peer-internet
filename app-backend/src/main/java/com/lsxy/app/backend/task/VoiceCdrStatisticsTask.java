package com.lsxy.app.backend.task;

import com.lsxy.yunhuni.api.statistics.service.VoiceCdrDayService;
import com.lsxy.yunhuni.api.statistics.service.VoiceCdrHourService;
import com.lsxy.yunhuni.api.statistics.service.VoiceCdrMonthService;
import com.lsxy.framework.cache.exceptions.TransactionExecFailedException;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.utils.StatisticsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 通话记录统计（session统计）定时统计任务
 * Created by zhangxb on 2016/7/29.
 */
@Component
public class VoiceCdrStatisticsTask {
    private static final Logger logger = LoggerFactory.getLogger(VoiceCdrStatisticsTask.class);
    @Autowired
    VoiceCdrHourService voiceCdrHourService;
    @Autowired
    VoiceCdrDayService voiceCdrDayService;
    @Autowired
    VoiceCdrMonthService voiceCdrMonthService;
    @Autowired
    RedisCacheService redisCacheService;
    /**
     * 每月1号2：00：00触发执行
     */
    @Scheduled(cron="0 0 2 1 * ? ")
    public void month(){
        Date date=new Date();
        String month = DateUtils.formatDate(date, "yyyy-MM");
        String cacheKey = "scheduled_" + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + " " + month;
        //执行互斥处理消息
        String flagValue = redisCacheService.get( cacheKey);
        if(StringUtil.isNotEmpty(flagValue)){
            if(logger.isDebugEnabled()){
                logger.debug("["+cacheKey+"]缓存中已被设置标记，该任务被"+flagValue+"处理了");
            }
        }else{
            try {
                if(logger.isDebugEnabled()){
                    logger.debug("["+cacheKey+"]准备处理该任务:"+cacheKey);
                }
                redisCacheService.setTransactionFlag(cacheKey, SystemConfig.id,30*24*60*60);
                String currentCacheValue = redisCacheService.get(cacheKey);
                if(logger.isDebugEnabled()){
                    logger.debug("["+cacheKey+"]当前cacheValue:"+currentCacheValue);
                }
                if(currentCacheValue.equals(SystemConfig.id)){
                    if(logger.isDebugEnabled()){
                        logger.debug("["+cacheKey+"]马上处理该任务："+cacheKey);
                    }
                    //执行语句
                    monthStatistics(date);
                }else{
                    if(logger.isDebugEnabled()){
                        logger.debug("["+cacheKey+"]标记位不一致"+currentCacheValue+"  vs "+ SystemConfig.id);
                    }
                }
            } catch (TransactionExecFailedException e) {
                if(logger.isDebugEnabled()){
                    logger.debug("["+cacheKey+"]设置标记位异常了，该任务被另一节点处理了");
                }
            }
        }



    }

    public void monthStatistics(Date date) {
        long startTime = System.currentTimeMillis();
        logger.info("通话记录统计（session统计）指标月统计任务开启，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
        String partten = "yyyy-MM";
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
        String[] all = new String[]{"tenant_id","app_id","type"};
        List<String[]> list = StatisticsUtils.getGroupBys(all);
        try{
            logger.info("参数date1={}，hour1={}，date2={}，hour2={}",DateUtils.formatDate(date1,"yyyy-MM-dd HH:mm:ss"),month1,DateUtils.formatDate(date2,"yyyy-MM-dd HH:mm:ss"),month2);
            for(int i=0;i<list.size();i++){
                logger.info("子任务：月统计维度{}任务开启，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"), Arrays.toString(list.get(i)));
                voiceCdrMonthService.monthStatistics(date1,month1,date2,month2,list.get(i),all);
            }
            long endTime = System.currentTimeMillis();
            logger.info("通话记录统计（session统计）指标月统计任务结束，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")+" ,花费时间为："+(endTime-startTime)+"毫秒");
        }catch (Exception e){
            long endTime = System.currentTimeMillis();
            logger.error("通话记录统计（session统计）指标月统计任务异常结束，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")+" ,花费时间为："+(endTime-startTime)+"毫秒");
            logger.error("失败原因",e);
        }
    }

    /**
     * 每天01：00：00触发执行
     */
    @Scheduled(cron="0 0 1 * * ?")
    public void day(){
        Date date=new Date();
        String day = DateUtils.formatDate(date, "yyyy-MM-dd");
        String cacheKey = "scheduled_" + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + " " + day;
        //执行互斥处理消息
        String flagValue = redisCacheService.get( cacheKey);
        if(StringUtil.isNotEmpty(flagValue)){
            if(logger.isDebugEnabled()){
                logger.debug("["+cacheKey+"]缓存中已被设置标记，该任务被"+flagValue+"处理了");
            }
        }else{
            try {
                if(logger.isDebugEnabled()){
                    logger.debug("["+cacheKey+"]准备处理该任务:"+cacheKey);
                }
                redisCacheService.setTransactionFlag(cacheKey, SystemConfig.id,24*60*60);
                String currentCacheValue = redisCacheService.get(cacheKey);
                if(logger.isDebugEnabled()){
                    logger.debug("["+cacheKey+"]当前cacheValue:"+currentCacheValue);
                }
                if(currentCacheValue.equals(SystemConfig.id)){
                    if(logger.isDebugEnabled()){
                        logger.debug("["+cacheKey+"]马上处理该任务："+cacheKey);
                    }
                    //执行语句
                    dayStatistics(date);
                }else{
                    if(logger.isDebugEnabled()){
                        logger.debug("["+cacheKey+"]标记位不一致"+currentCacheValue+"  vs "+ SystemConfig.id);
                    }
                }
            } catch (TransactionExecFailedException e) {
                if(logger.isDebugEnabled()){
                    logger.debug("["+cacheKey+"]设置标记位异常了，该任务被另一节点处理了");
                }
            }
        }

    }

    public void dayStatistics(Date date) {
        long startTime = System.currentTimeMillis();
        logger.info("通话记录统计（session统计）指标日统计任务开启，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
        String partten = "yyyy-MM-dd";
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
        String[] all = new String[]{"tenant_id","app_id","type"};
        List<String[]> list = StatisticsUtils.getGroupBys(all);
        try{
            logger.info("参数date1={}，hour1={}，date2={}，hour2={}",DateUtils.formatDate(date1,"yyyy-MM-dd HH:mm:ss"),day1,DateUtils.formatDate(date2,"yyyy-MM-dd HH:mm:ss"),day2);
            for(int i=0;i<list.size();i++){
                logger.info("子任务：日统计维度{}任务开启，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"), Arrays.toString(list.get(i)));
                voiceCdrDayService.dayStatistics(date1,day1,date2,day2,list.get(i),all);
            }
            long endTime = System.currentTimeMillis();
            logger.info("通话记录统计（session统计）指标日统计任务结束，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")+" ,花费时间为："+(endTime-startTime)+"毫秒");
        }catch (Exception e){
            long endTime = System.currentTimeMillis();
            logger.error("通话记录统计（session统计）指标日统计任务异常结束，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")+" ,花费时间为："+(endTime-startTime)+"毫秒");
            logger.error("失败原因",e);
        }
    }

    /**
     *每个小时的执行一次
     */
    @Scheduled(cron="0 30 0/1 * * ?")
    public void hour(){
        Date date=new Date();
        String hour = DateUtils.formatDate(date, "yyyy-MM-dd HH");
        String cacheKey = "scheduled_" + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName()+ " " + hour;

        //执行互斥处理消息
        String flagValue = redisCacheService.get( cacheKey);
        if(StringUtil.isNotEmpty(flagValue)){
            if(logger.isDebugEnabled()){
                logger.debug("["+cacheKey+"]缓存中已被设置标记，该任务被"+flagValue+"处理了");
            }
        }else{
            try {
                if(logger.isDebugEnabled()){
                    logger.debug("["+cacheKey+"]准备处理该任务:"+cacheKey);
                }
                redisCacheService.setTransactionFlag(cacheKey, SystemConfig.id,60*60);
                String currentCacheValue = redisCacheService.get(cacheKey);
                if(logger.isDebugEnabled()){
                    logger.debug("["+cacheKey+"]当前cacheValue:"+currentCacheValue);
                }
                if(currentCacheValue.equals(SystemConfig.id)){
                    if(logger.isDebugEnabled()){
                        logger.debug("["+cacheKey+"]马上处理该任务："+cacheKey);
                    }
                    //执行语句
                    hourStatistics(date);
                }else{
                    if(logger.isDebugEnabled()){
                        logger.debug("["+cacheKey+"]标记位不一致"+currentCacheValue+"  vs "+ SystemConfig.id);
                    }
                }
            } catch (TransactionExecFailedException e) {
                if(logger.isDebugEnabled()){
                    logger.debug("["+cacheKey+"]设置标记位异常了，该任务被另一节点处理了");
                }
            }
        }


    }

    public void hourStatistics(Date date) {
        long startTime = System.currentTimeMillis();
        logger.info("通话记录统计（session统计）指标小时统计任务开启，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
        //租户 应用 运营商 地区 业务类型 应用上线个数增量/总量 应用未上线个数增量/总量 应用总个数增量/总量
        String partten = "yyyy-MM-dd HH";
        Calendar cale = Calendar.getInstance();
        cale.setTime(date);
        cale.set(Calendar.HOUR_OF_DAY, cale.get(Calendar.HOUR_OF_DAY) - 1);
        date = cale.getTime();
        int hour1 = cale.get(Calendar.HOUR_OF_DAY);
        Date date1 = DateUtils.parseDate(DateUtils.getDate(date,partten),partten);
        //前一个小时的时间
        cale.set(Calendar.HOUR_OF_DAY, cale.get(Calendar.HOUR_OF_DAY) - 1);
        date = cale.getTime();
        int hour2 = cale.get(Calendar.HOUR_OF_DAY);
        Date date2 = DateUtils.parseDate(DateUtils.getDate(date,partten),partten);
        String[] all = new String[]{"tenant_id","app_id","type"};
        List<String[]> list = StatisticsUtils.getGroupBys(all);
        try{
            logger.info("参数date1={}，hour1={}，date2={}，hour2={}",DateUtils.formatDate(date1,"yyyy-MM-dd HH:mm:ss"),hour1,DateUtils.formatDate(date2,"yyyy-MM-dd HH:mm:ss"),hour2);
            for(int i=0;i<list.size();i++){
                logger.info("子任务：小时统计维度{}任务开启，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"), Arrays.toString(list.get(i)));
                voiceCdrHourService.hourStatistics(date1,hour1,date2,hour2,list.get(i),all);
            }
            long endTime = System.currentTimeMillis();
            logger.info("通话记录统计（session统计）指标小时统计任务结束，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")+" ,花费时间为："+(endTime-startTime)+"毫秒");
        }catch (Exception e){
            long endTime = System.currentTimeMillis();
            logger.error("通话记录统计（session统计）指标小时统计任务异常结束，当前时间" + DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss")+" ,花费时间为："+(endTime-startTime)+"毫秒");
            logger.error("失败原因",e);
        }
    }

}
