package com.oneyun.sapi.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhangxb on 2016/8/29.
 */
public class DateUtils {
    /**
     * 将当前时间按yyyyMMddHHmmss格式转换成字符串
     * @return
     */
    public static String getTimestamp() {
        String timestr;
        String  parrten = "yyyyMMddHHmmss";
        SimpleDateFormat sdf = new SimpleDateFormat(parrten);
        Date day = new Date();
        timestr = sdf.format(day);
        return timestr;
    }
}
