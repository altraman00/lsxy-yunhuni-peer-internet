package com.lsxy.app.portal.rest.stastistic;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.statistics.service.SubaccountMonthService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxb on 2017/2/21.
 */
@RequestMapping("/rest/api_sub_account_month")
@RestController
public class ApiCertificateSubAccountMonthController extends AbstractRestController {
    @Autowired
    private SubaccountMonthService subaccountMonthService;

    @RequestMapping("/plist")
    public RestResponse plist(int pageNo,int pageSize,String tenantId, String appId, String subId, String startTime, String endTime ){
        Date date1 = DateUtils.parseDate(startTime,"yyyy-MM");
        if(StringUtils.isEmpty(endTime)){
            endTime = startTime;
        }
        Date date2 =  DateUtils.parseDate(DateUtils.getMonthLastTime(DateUtils.parseDate(endTime,"yyyy-MM")),"yyyy-MM-dd HH:mm:ss");
        Page page = subaccountMonthService.getPageByConditions(pageNo,pageSize,date1,date2,tenantId,appId,subId);
        return RestResponse.success(page);
    }
    @RequestMapping("/list")
    public RestResponse list(String tenantId, String appId, String subId, String startTime, String endTime ){
        Date date1 = DateUtils.parseDate(startTime,"yyyy-MM");
        if(StringUtils.isEmpty(endTime)){
            endTime = startTime;
        }
        Date date2 =  DateUtils.parseDate(DateUtils.getMonthLastTime(DateUtils.parseDate(endTime,"yyyy-MM")),"yyyy-MM-dd HH:mm:ss");
        List list = subaccountMonthService.getListByConditions(date1,date2,tenantId,appId,subId);
        return RestResponse.success(list);
    }
    @RequestMapping("/sum")
    public RestResponse sum(String tenantId, String appId, String subId, String startTime, String endTime ){
        Date date1 = DateUtils.parseDate(startTime,"yyyy-MM");
        if(StringUtils.isEmpty(endTime)){
            endTime = startTime;
        }
        Date date2 =  DateUtils.parseDate(DateUtils.getMonthLastTime(DateUtils.parseDate(endTime,"yyyy-MM")),"yyyy-MM-dd HH:mm:ss");
        Map map = subaccountMonthService.sum(date1,date2,tenantId,appId,subId);
        return RestResponse.success(map);
    }
}
