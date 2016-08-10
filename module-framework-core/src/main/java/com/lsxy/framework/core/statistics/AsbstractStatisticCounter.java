package com.lsxy.framework.core.statistics;

import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.framework.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tandy on 16/8/10.
 * 统计计数器
 */
@Profile(value = {"local","development"})
public abstract class AsbstractStatisticCounter {
    private static final Logger logger = LoggerFactory.getLogger(AsbstractStatisticCounter.class);

    public abstract String getStatisticName();
    /**
     * 重置
     */
    public void reset(){
        if(logger.isDebugEnabled()){
            logger.debug("重置所有计数器");
        }
        List<Field> fields =  BeanUtils.getFieldsByType(this, AtomicInteger.class);
        for (Field field:fields) {
            try {
                field.setAccessible(true);
                AtomicInteger  ai = (AtomicInteger) field.get(this);
                ai.set(0);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 输出日志
     */
    public void log(){
        List<Field> fields =  BeanUtils.getFieldsByType(this, AtomicInteger.class);
        if(logger.isDebugEnabled()){
            logger.debug("=========="+getStatisticName()+"==========",fields.size());
        }else{
            return;
        }
        for (Field field:fields) {
            try {
                MarkField x = field.getAnnotation(MarkField.class);
                String fieldName = field.getName();
                if(null != x && StringUtil.isNotEmpty(x.value())){
                    fieldName = x.value();
                }
                field.setAccessible(true);
                AtomicInteger  ai = (AtomicInteger) field.get(this);
                int value = ai.get();
                if(logger.isDebugEnabled()){
                    logger.debug("{}:\t{}",fieldName,value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if(logger.isDebugEnabled()){
            logger.debug("============================================================\r\n\r\n");
        }
    }
}
