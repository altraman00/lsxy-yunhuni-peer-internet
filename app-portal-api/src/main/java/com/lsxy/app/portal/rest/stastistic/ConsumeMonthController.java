package com.lsxy.app.portal.rest.stastistic;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.api.statistics.model.ConsumeMonth;
import com.lsxy.framework.api.statistics.service.ConsumeMonthService;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 消费月统计
 * Created by zhangxb on 2016/7/6.
 */
@RequestMapping("/rest/consume_month")
@RestController
public class ConsumeMonthController extends AbstractRestController {
    @Autowired
    ConsumeMonthService consumeMonthService;
    /**
     * 根据时间和应用获取列表数据
     * @param appId 应用id
     * @param startTime 时间
     * @return
     */
    @RequestMapping("/list")
    public RestResponse list(String tenantId, String appId, String type, String startTime, String endTime ){
        Date date1 = DateUtils.parseDate(startTime,"yyyy");
        if(StringUtils.isEmpty(endTime)){
            endTime = startTime;
        }
        Date date2  = DateUtils.parseDate(DateUtils.getLastYearByDate(endTime)+" 23:59:59","yyyy-MM-dd HH:mm:ss");
        List<ConsumeMonth> list =  consumeMonthService.list(tenantId,appId,type,date1,date2);
        return RestResponse.success(list);
    }

    /**
     * 获取分页数据
     * @param appId 应用id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @return
     */
    @RequestMapping("/page")
    public RestResponse pageList(String tenantId, String appId,String type,String startTime, String endTime,Integer pageNo,Integer pageSize){
        Date date1 = DateUtils.parseDate(startTime,"yyyy");
        if(StringUtils.isEmpty(endTime)){
            endTime = startTime;
        }
        Date date2  = DateUtils.parseDate(DateUtils.getLastYearByDate(endTime)+" 23:59:59","yyyy-MM-dd HH:mm:ss");
        Page<ConsumeMonth> page =  consumeMonthService.pageList(tenantId,appId,type,date1,date2,pageNo,pageSize);
        return RestResponse.success(page);
    }

    @RequestMapping("/get")
    public RestResponse get(String appId,String month){
        if(org.apache.commons.lang.StringUtils.isBlank(month)){
            String curMonth = DateUtils.getDate("yyyy-MM");
            month = DateUtils.getPrevMonth(curMonth,"yyyy-MM");
        }
        Account account = getCurrentAccount();
        List<ConsumeMonth> consumeMonths = consumeMonthService.getConsumeMonths(account.getTenant().getId(),appId,month);
        return RestResponse.success(consumeMonths);
    }

}
