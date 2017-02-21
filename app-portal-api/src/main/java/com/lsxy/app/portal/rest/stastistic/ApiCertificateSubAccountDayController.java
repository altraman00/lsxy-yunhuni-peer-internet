package com.lsxy.app.portal.rest.stastistic;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.statistics.service.SubaccountDayService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

/**
 * Created by zhangxb on 2017/2/21.
 */
@RequestMapping("/rest/api_sub_account_day")
@RestController
public class ApiCertificateSubAccountDayController extends AbstractRestController {
    @Autowired
    private SubaccountDayService subaccountDayService;

    @RequestMapping("/plist")
    public RestResponse list(int pageNo,int pageSize,String tenantId, String appId, String subId, String startTime, String endTime ){
        Date date1 = DateUtils.parseDate(startTime+" 00:00:00","yyyy-MM-dd HH:mm:ss");
        if(StringUtils.isEmpty(endTime)){
            endTime = startTime;
        }
        Date date2 =  DateUtils.parseDate(endTime+" 23:59:59","yyyy-MM-dd HH:mm:ss");
        Page page = subaccountDayService.getPageByConditions(pageNo,pageSize,date1,date2,tenantId,appId,subId);
        return RestResponse.success(page);
    }
    @RequestMapping("/sum")
    public RestResponse sum(String tenantId, String appId,String subId, String startTime, String endTime ){
        Date date1 = DateUtils.parseDate(startTime+" 00:00:00","yyyy-MM-dd HH:mm:ss");
        if(StringUtils.isEmpty(endTime)){
            endTime = startTime;
        }
        Date date2 =  DateUtils.parseDate(endTime+" 23:59:59","yyyy-MM-dd HH:mm:ss");
        Map map = subaccountDayService.sum(date1,date2,tenantId,appId,subId);
        return RestResponse.success(map);
    }
}
