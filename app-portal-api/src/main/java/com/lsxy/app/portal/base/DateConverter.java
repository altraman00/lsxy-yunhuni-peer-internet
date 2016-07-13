package com.lsxy.app.portal.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

/**
 * 增加处理时间戳转换成日期对象配置
 * Created by zhangxb on 2016/7/13.
 */
@Configuration
public class DateConverter implements Converter<String, Date> {
    private static final Logger logger = LoggerFactory.getLogger(DateConverter.class);
    @Override
    public Date convert(String s) {
        long ltime = 0L;
        try{
            ltime = Long.parseLong(s);
            return new Date(ltime);
        }catch (Exception e){
            logger.error("日期转换失败，转换参数{1}，错误信息{2}",s,e);
        }
        return null;
    }
}
