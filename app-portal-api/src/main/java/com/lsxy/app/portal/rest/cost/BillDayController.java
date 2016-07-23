package com.lsxy.app.portal.rest.cost;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.api.consume.model.BillDay;
import com.lsxy.framework.api.consume.service.BillDayService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.web.rest.RestResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * Created by liups on 2016/7/22.
 */
@RestController
@RequestMapping("/rest/bill_day")
public class BillDayController extends AbstractRestController {
    @Autowired
    BillDayService gillDayService;

    /**
     * 获取某一日的日结账单
     * @param appId
     * @param day
     * @return
     */
    @RequestMapping("/get")
    public RestResponse get(String appId, String day){
        if(StringUtils.isBlank(day)){
            Date preDate = DateUtils.getPreDate(new Date());
            day = DateUtils.getDate(preDate, "yyyy-MM-dd");
        }
        String userName = getCurrentAccountUserName();
        List<BillDay> billDays = gillDayService.getBillDays(userName, appId, day);
        return RestResponse.success(billDays);
    }
}
