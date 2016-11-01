package com.lsxy.app.portal.rest.stastistic;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.api.billing.service.CalBillingService;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.statistics.model.DayStatics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 历史总数统计
 * Created by liups on 2016/10/24.
 */
@RequestMapping("/rest/day_statics")
@RestController
public class DayStaticsController extends AbstractRestController {
    @Autowired
    CalBillingService calBillingService;

    /**
     * 获取当天，当月，或总的统计数据
     * @param type day month all
     * @return
     */
    @RequestMapping("/get")
    public RestResponse get(String type){
        DayStatics statics;
        if("today".equals(type)){
            statics = calBillingService.getIncStaticsOfCurrentDay(getCurrentAccount().getTenant().getId());
        }else  if("month".equals(type)){
            statics = calBillingService.getIncStaticsOfCurrentMonth(getCurrentAccount().getTenant().getId());
        }else{
            statics = calBillingService.getCurrentStatics(getCurrentAccount().getTenant().getId());
        }
        Map map = new HashMap<>();
        map.put("avgCostTime",Math.round((statics.getCallCostTime()/60.0)/statics.getCallConnect()));
        if(statics.getCallSum() == null || statics.getCallSum()==0){
            map.put("avgCall",0);
        }else{
            map.put("avgCall",new BigDecimal(statics.getCallConnect()).divide(new BigDecimal(statics.getCallSum()),4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).doubleValue());
        }
        map.put("cost",statics.getConsume());
        map.put("session",statics.getCallSum());
        map.put("costTime",Math.round(statics.getCallCostTime()/60.0));
        return RestResponse.success(map);
    }

    /**
     * 根据租户id，应用id，开始时间和结束时间来获取统计数据
     * @param appId
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping("/call_center/get")
    public RestResponse getAmong(String appId,String startTime,String endTime){
        Map map = new HashMap<>();
        map.put("callIn","100");//呼入量
        map.put("callOut","100");//呼出量
        map.put("transferSuccess","100");//转接成功
        map.put("formTime","1000");//排队时间
        map.put("callTime","1000");//平均通话时长
        map.put("callFail","1000");//呼入流失率
        return RestResponse.success(map);
    }

}
