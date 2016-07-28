package com.lsxy.app.portal.console.cost;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.framework.api.consume.model.BillDay;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liups on 2016/7/22.
 */
@Controller
@RequestMapping("/console/cost/bill_day")
public class BillDayController extends AbstractPortalController {

    /**
     * Ajax获取日结账单
     * @param request
     * @param appId
     * @param day
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse list(HttpServletRequest request, String appId, String day){
        if(StringUtils.isBlank(day)){
            Date preDate = DateUtils.getPreDate(new Date());
            day = DateUtils.getDate(preDate, "yyyy-MM-dd");
        }
        String token = this.getSecurityToken(request);
        List<BillDay> billDays = getBillDayRest(token, appId, day);
        return RestResponse.success(billDays);
    }

    /**
     * 获取日结账单RestApi调用
     * @param token
     * @param appId 应用ID
     * @param day 查询时间（日期）
     * @return
     */
    private List<BillDay> getBillDayRest(String token,String appId, String day){
        String getUrl = PortalConstants.REST_PREFIX_URL + "/rest/bill_day/get?appId={1}&day={2}";
        RestResponse<List<BillDay>> response = RestRequest.buildSecurityRequest(token).getList(getUrl, BillDay.class, appId, day);
        return response.getData();
    }

}
