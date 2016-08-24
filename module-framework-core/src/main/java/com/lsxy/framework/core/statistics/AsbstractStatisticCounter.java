package com.lsxy.framework.core.statistics;

import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.framework.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

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
            logger.debug("重置所有["+getStatisticName()+"]计数器");
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
            logger.debug("=========="+getStatisticName()+"[{}]==========",fields.size());
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

    class SumCounter {

        public static final int SC_UNIT_SEC=1;

        private int unit;
        private long startTime = 0L;

        public SumCounter(int unit,long startTime){
            this.unit = unit;
            this.startTime = startTime;
        }

        private SumCounter last = null;

        private AtomicInteger sum = new AtomicInteger(0);
        private AtomicInteger count = new AtomicInteger(0);

        public void add(int value){
            sum.addAndGet(value);
            count.incrementAndGet();
        }

        public int getAvg() {
            return this.sum.get()/this.count.get();
        }

        public int getsum(){
            return this.sum.get();
        }

        public void reset(){
            SumCounter sc = new SumCounter(this.unit,this.startTime);
            sc.sum = new AtomicInteger((sc.sum.get()));
            sc.count = new AtomicInteger((sc.count.get()));
            this.last = sc;
            this.sum.set(0);
            this.count.set(0);
        }

    }
}
