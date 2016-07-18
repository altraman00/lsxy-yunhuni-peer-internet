package com.lsxy.app.portal.base;

import com.lsxy.framework.core.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
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
        if(StringUtils.isNotBlank(s)){return null;}
        long ltime = 0L;
        Date date = null;
        if(StringUtils.isNotBlank(s)){
            try{
                ltime = Long.parseLong(s);
                date = new Date(ltime);
            }catch (Exception e){
                try {
                    date = DateUtils.parseDate(s);
                }catch (Exception e1){
                    logger.error("日期转换失败，转换参数{1}，错误信息{2}",s,e1);
                }
            }
        }
        return date;
    }
}
