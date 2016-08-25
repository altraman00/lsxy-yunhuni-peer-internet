package com.lsxy.app.backend.task;

import com.lsxy.framework.core.utils.DateUtils;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * 定时任务测试样例
 * Created by liups on 2016/7/27.
 */
//@Component
public class TestTask {
    /*
    格式：* * * * * * (*)
    秒	 	0-59	 	, - * /
    分	 	0-59	 	, - * /
    小时	 	0-23	 	, - * /
    日期	 	1-31	 	, - *   / L W C
    月份	 	1-12 或者 JAN-DEC	 	, - * /
    星期	 	1-7 或者 SUN-SAT	 	, - *   / L C #
    年（可选）	 	留空, 1970-2099	 	, - * /

    说明：
    “*”字符被用来指定所有的值。如：”*“在分钟的字段域里表示“每分钟”。

    “-”字符被用来指定一个范围。如：“10-12”在小时域意味着“10点、11点、12点”。

    “,”字符被用来指定另外的值。如：“MON,WED,FRI”在星期域里表示”星期一、星期三、星期五”.

    “?”字符只在日期域和星期域中使用。它被用来指定“非明确的值”。当你需要通过在这两个域中的一个来指定一些东西的时候，它是有用的

    日期（即第4个）和星期（即第6个）是互斥的，因此应该通过设置一个问号（？）来表明你不想设置的那个字段

    每一个元素都可以显式地规定一个值（如6），一个区间（如9-12），一个列表（如9，11，13）或一个通配符（如*）
    "0 0 12 * * ?"	 	每天中午12点触发
    "0 15 10 ? * *"	 	每天上午10:15触发
    "0 15 10 * * ?"	 	每天上午10:15触发
    "0 15 10 * * ? *"	 	每天上午10:15触发
    "0 15 10 * * ? 2005"	 	2005年的每天上午10:15触发
    "0 * 14 * * ?"	 	在每天下午2点到下午2:59期间的每1分钟触发
    "0 0/5 14 * * ?"	 	在每天下午2点到下午2:55期间的每5分钟触发
    "0 0/5 14,18 * * ?"	 	在每天下午2点到2:55期间和下午6点到6:55期间的每5分钟触发
    "0 0-5 14 * * ?"	 	在每天下午2点到下午2:05期间的每1分钟触发
    "0 10,44 14 ? 3 WED"	 	每年三月的星期三的下午2:10和2:44触发
    "0 15 10 ? * MON-FRI"	 	周一至周五的上午10:15触发
    "0 15 10 15 * ?"	 	每月15日上午10:15触发
    "0 15 10 L * ?"	 	每月最后一日的上午10:15触发
    "0 15 10 ? * 6L"	 	每月的最后一个星期五上午10:15触发
    "0 15 10 ? * 6L 2002-2005"	 	2002年至2005年的每月的最后一个星期五上午10:15触发
    "0 15 10 ? * 6#3"	 	每月的第三个星期五上午10:15触发
     */
    //@Scheduled(cron="0/10 * * * * ?")
    @Test
    public void init(){
        String startTime = "2016-01-01 00:00:00";
        String endTime = "2016-08-24 00:00:00";
        int days= DateUtils.compareStringTimes(endTime,startTime,"yyyy-MM-dd HH:mm:ss");
        System.out.println("days:"+days);
        Date startDate = DateUtils.parseDate(startTime,"yyyy-MM-dd HH:mm:ss");
        Calendar ca1Month = Calendar.getInstance();
        ca1Month.setTime(startDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        // 当前年
//        int year = cal.get(Calendar.YEAR);
        // 当前月
//        int month = (cal.get(Calendar.MONTH)) + 1;
        // 当前月的第几天：即当前日
//        int day = cal.get(Calendar.DAY_OF_MONTH);
        // 当前时：HOUR_OF_DAY-24小时制；HOUR-12小时制
//        int hour = cal.get(Calendar.HOUR_OF_DAY);
        ConsumeStatisticsTask consumeStatisticsTask = new ConsumeStatisticsTask();
        VoiceCdrStatisticsTask voiceCdrStatisticsTask = new VoiceCdrStatisticsTask();
        for(int i=0;i<days;i++){
            for(int j=0;j<24;j++) {
                cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + 1);
                Date date = cal.getTime();
                System.out.println("小时:"+DateUtils.getDate(date,"yyyy-MM-dd HH:mm:ss"));
                //调用费用小时统计
//                consumeStatisticsTask.hourStatistics(date);
                //调用cdr小时统计
//                voiceCdrStatisticsTask.hourStatistics(date);
            }
            Date date = cal.getTime();
            System.out.println("日:"+DateUtils.getDate(date,"yyyy-MM-dd HH:mm:ss"));
            //调用费用小时统计
//            consumeStatisticsTask.dayStatistics(date);
            //调用cdr小时统计
//            voiceCdrStatisticsTask.dayStatistics(date);
        }
        int months = 7;
        for(int k = 0;k<months;k++){
            ca1Month.set(Calendar.MONTH, ca1Month.get(Calendar.MONTH) + 1);
            Date date = ca1Month.getTime();
            System.out.println("月:"+DateUtils.getDate(date,"yyyy-MM-dd HH:mm:ss"));
            //调用费用小时统计
//            consumeStatisticsTask.monthStatistics(date);
            //调用cdr小时统计
//            voiceCdrStatisticsTask.monthStatistics(date);
        }

    }

}
