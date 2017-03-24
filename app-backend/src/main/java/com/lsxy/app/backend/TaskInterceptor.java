package com.lsxy.app.backend;

import com.lsxy.app.backend.task.SubaccountStatistics;
import com.lsxy.framework.cache.exceptions.TransactionExecFailedException;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.StringUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 任务执行Aspectj类，确保几台机器只会有一台机器执行任务
 * 注意：
 *      定时任务的方法命名规则为：scheduled_自定义名称_时间粒度
 *
 * 只有以scheduled_开头的方法才会被切入，第二个'_'后面的为时间粒度，年执行为yyyy，月执行为yyyyMM,天执行为yyyyMMdd,小时执行为yyyyMMddHH,分钟执行为yyyyMMddHHmm
 * 秒执行时间间隔太短，不建议用这种方式切入，最好另实现redis锁机制
 * Created by liups on 2017/3/16.
 */
@Aspect
@Component
public class TaskInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(SubaccountStatistics.class);
    Map<String, Long> expireMap =
            new HashMap<String, Long>() {
                {
                    put("yyyyMM", 24*60*60L);
                    put("yyyyMMdd", 24*60*60L);
                    put("yyyyMMddHH", 60*60L);
                    put("yyyyMMddHHmm", 60L);
                    put("yyyyMMddHHmmss", 1L);
                }
            };

    @Autowired
    RedisCacheService redisCacheService;

    @Pointcut("execution(* com.lsxy.app.backend.task..*.scheduled_*(..))")
    private void scheduledMethod(){}//定义一个切入点

    @Around("scheduledMethod()")
    public Object doBasicProfiling(ProceedingJoinPoint pjp) throws Throwable{
        if(logger.isDebugEnabled()){
            logger.debug("进入环绕通知");
        }
        Signature sig = pjp.getSignature();
        String name = sig.getName(); //获取方法名
        String[] split = name.split("_"); //分析方法
        String timePattern;
        if(split.length > 2){
            timePattern = split[2];
        }else{
            timePattern = "yyyyMMddHHmm";
        }
        String date = DateUtils.getDate(new Date(), timePattern);
        String cacheKey = "scheduled_" + pjp.getTarget().getClass().getName()+"."+ name + " " + date; //组装cacheKey
        Long expire = expireMap.get(timePattern);//获取cacheKey超时时长

        if(logger.isDebugEnabled()){
            logger.debug("执行任务，cacheKey：{}",cacheKey );
        }
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
                redisCacheService.setTransactionFlag(cacheKey, SystemConfig.id,expire==null?60L:expire);
                String currentCacheValue = redisCacheService.get(cacheKey);
                if(logger.isDebugEnabled()){
                    logger.debug("["+cacheKey+"]当前cacheValue:"+currentCacheValue);
                }
                if(currentCacheValue.equals(SystemConfig.id)){
                    if(logger.isDebugEnabled()){
                        logger.debug("["+cacheKey+"]马上处理该任务："+cacheKey);
                    }
                    Object object = pjp.proceed();//执行该方法
                    return object;
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
        return null;
    }

}
