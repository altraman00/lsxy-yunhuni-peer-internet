package com.lsxy.area.server.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Created by liuws on 2016/12/7.
 */
@Profile(value = {"local","development","test"})
@Aspect
@Component
public class HandlerExecAspect {
    private static final Logger logger = LoggerFactory.getLogger(HandlerExecAspect.class);

    private static final long ONE_MINUTE = 10;

    public static final String POINT = "target(com.lsxy.area.server.event.EventHandler)";

    /**
     * 统计方法执行耗时Around环绕通知
     * @param joinPoint
     * @return
     */
    @Around(POINT)
    public Object timeAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object obj = null;
        Object[] args = joinPoint.getArgs();
        long startTime = System.currentTimeMillis();

        obj = joinPoint.proceed(args);

        // 获取执行的方法名
        long endTime = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getDeclaringTypeName() + "." + signature.getName();

        // 打印耗时的信息
        this.printExecTime(methodName, startTime, endTime);

        return obj;
    }

    /**
     * 打印方法执行耗时的信息，如果超过了一定的时间，才打印
     * @param methodName
     * @param startTime
     * @param endTime
     */
    private void printExecTime(String methodName, long startTime, long endTime) {
        long diffTime = endTime - startTime;
        if (diffTime > ONE_MINUTE) {
            logger.info(methodName + " 方法执行耗时：" + diffTime + " ms");
        }
    }
}
