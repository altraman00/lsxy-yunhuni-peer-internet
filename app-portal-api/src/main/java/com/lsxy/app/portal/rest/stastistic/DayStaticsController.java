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
            map.put("avgCall",new BigDecimal(statics.getCallConnect()).divide(new BigDecimal(statics.getCallSum())).multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        map.put("cost",statics.getConsume());
        map.put("session",statics.getCallSum());
        map.put("costTime",Math.round(statics.getCallCostTime()/60.0));
        return RestResponse.success(map);
    }
}
