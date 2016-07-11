package com.lsxy.app.portal.rest;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.api.consume.model.BillMonth;
import com.lsxy.framework.api.consume.service.BillMonthService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.web.rest.RestResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liups on 2016/7/11.
 */
@RestController
@RequestMapping("/rest/bill_month")
public class BillMonthController extends AbstractRestController {
    @Autowired
    BillMonthService billMonthService;

    @RequestMapping("/get")
    public RestResponse get(String appId,String month){
        if(StringUtils.isBlank(month)){
            String curMonth = DateUtils.getDate("yyyy-MM");
            month = DateUtils.getPrevMonth(curMonth,"yyyy-MM");
        }
        String userName = getCurrentAccountUserName();
        BillMonth billMonth = billMonthService.getBillMonth(userName, appId, month);
        return RestResponse.success(billMonth);
    }
}
