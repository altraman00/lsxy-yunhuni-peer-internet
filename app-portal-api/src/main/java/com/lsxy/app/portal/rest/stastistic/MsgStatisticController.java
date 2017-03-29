package com.lsxy.app.portal.rest.stastistic;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.app.portal.rest.stastistic.vo.MsgStatisticsVo;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.statistics.service.MsgDayService;
import com.lsxy.yunhuni.api.statistics.service.MsgMonthService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangxb on 2017/3/13.
 */
@RequestMapping("/rest/msg_statistics")
@RestController
public class MsgStatisticController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(MsgStatisticController.class);
    public static final String TYPE_MONTH = "month";//月统计类型 按年查找输出 返回按年
    public static final String TYPE_DAY = "day";//日统计类型 按月查找输出 返回按月
    @Autowired
    private MsgDayService msgDayService;
    @Autowired
    private MsgMonthService msgMonthService;
    @RequestMapping("/plist")
    public RestResponse pList(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize,@RequestParam String tenantId,@RequestParam String appId,@RequestParam String startTime,@RequestParam String type  ){
        Date date1 = getStartDate(startTime,type);
        Date date2 = getLastDate(startTime,type);
        Page page = null;//getPage(tenantId, appId , type, date1, date2, pageNo, pageSize);
        if(TYPE_MONTH.equals(type)){
            page = msgMonthService.getStatisticsPage(tenantId,appId,date1,date2,pageNo,pageSize);
        }else{
            page = msgDayService.getStatisticsPage(tenantId,appId,date1,date2,pageNo,pageSize);
        }
        return RestResponse.success(page);
    }
    @RequestMapping("/list")
    public RestResponse list(@RequestParam String tenantId,@RequestParam String appId,@RequestParam String startTime,@RequestParam String type ){
        Date date1 = getStartDate(startTime,type);
        Date date2 = getLastDate(startTime,type);
        List list = null;//getList( tenantId, appId,date1,date2,type);
        if(TYPE_MONTH.equals(type)){
            list = msgMonthService.getStatisticsList(tenantId,appId,date1,date2);
        }else{
            list = msgDayService.getStatisticsList(tenantId,appId,date1,date2);
        }
        return RestResponse.success(list);
    }
    private Page<MsgStatisticsVo> getPage(String tenantId,String appId ,String type,Date date1,Date date2,int pageNo,int pageSize){
        List<MsgStatisticsVo> list = getList(tenantId,appId,date1,date2,type);
        Page page = new Page( (pageNo-1)*pageSize ,  list.size(),  pageSize, list);
        return page;
    }
    private List<MsgStatisticsVo> getList(String tenantId, String appId, Date date1,Date date2,String type){
        int len = 0;
        if(TYPE_MONTH.equals(type)){
            len = getLong(12);
        }else{
            len = getLong(date1);
        }
        List<MsgStatisticsVo> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        for (int i = 0; i < len; i++) {
            list.add( MsgStatisticsVo.initMsgStatisticsVo(calendar.getTime(),(i+1)) );
            if(TYPE_MONTH.equals(type)){
                calendar.add(Calendar.MONTH,1);
            }else{
                calendar.add(Calendar.DAY_OF_MONTH,1);
            }
        }
        return list;
    }
    private Date getStartDate(String startTime,String type){
        Date date = null;
        if(TYPE_MONTH.equals(type)){
            date = DateUtils.parseDate(startTime,"yyyy");
        }else{
            date = DateUtils.parseDate(startTime,"yyyy-MM");
        }
        return date;
    }
    private Date getLastDate(String endTime,String type){
        Date date2 = null;
        if(TYPE_MONTH.equals(type)){
            date2  = DateUtils.parseDate(DateUtils.getLastYearByDate(endTime)+" 23:59:59","yyyy-MM-dd HH:mm:ss");
        }else{
            date2 =  DateUtils.parseDate(DateUtils.getMonthLastTime(DateUtils.parseDate(endTime,"yyyy-MM")),"yyyy-MM-dd HH:mm:ss");
        }
        return date2;
    }
    private int getLong(Object obj){
        int r = 0;
        if (obj instanceof Date) {
            r = Integer.valueOf(DateUtils.getLastDate((Date)obj).split("-")[2]);
        } else if (obj instanceof Integer) {
            r =Integer.valueOf((Integer)obj);
        }
        return r;
    }

}
