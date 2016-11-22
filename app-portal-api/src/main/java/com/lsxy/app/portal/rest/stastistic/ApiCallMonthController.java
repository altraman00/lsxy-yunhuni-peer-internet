package com.lsxy.app.portal.rest.stastistic;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.statistics.model.ApiCallMonth;
import com.lsxy.yunhuni.api.statistics.service.ApiCallMonthService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * api月统计
 * Created by zhangxb on 2016/7/6.
 */
@RequestMapping("/rest/api_call_month")
@RestController
public class ApiCallMonthController extends AbstractRestController {
    @Autowired
    ApiCallMonthService apiCallMonthService;
    /**
     * 获取用户某时间的列表数据
     * @param tenantId 对于租户
     * @param appId 应用id
     * @param type 消费类型
     * @param startTime 时间
     * @param endTime 结束时间
     * @return
     */
    @RequestMapping("/list")
    public RestResponse list(String tenantId, String appId, String type, String startTime, String endTime ){
        Date date1 = DateUtils.parseDate(startTime,"yyyy");
        if(StringUtils.isEmpty(endTime)){
            endTime = startTime;
        }
        Date date2  = DateUtils.parseDate(DateUtils.getLastYearByDate(endTime)+" 23:59:59","yyyy-MM-dd HH:mm:ss");
        List<ApiCallMonth> list =  apiCallMonthService.list(tenantId,  appId,  type,  date1,  date2 );
        return RestResponse.success(list);
    }


}
