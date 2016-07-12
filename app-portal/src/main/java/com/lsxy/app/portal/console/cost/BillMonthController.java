package com.lsxy.app.portal.console.cost;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.framework.api.consume.model.BillMonth;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.app.model.App;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liups on 2016/7/11.
 */
@Controller
@RequestMapping("/console/cost/bill_month")
public class BillMonthController extends AbstractPortalController {

    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public ModelAndView get(HttpServletRequest request, String appId, String month){
        Map<String,Object> model = new HashMap<>();
        if(StringUtils.isBlank(month)){
            String curMonth = DateUtils.getDate("yyyy-MM");
            month = DateUtils.getPrevMonth(curMonth,"yyyy-MM");
        }
        String token = this.getSecurityToken(request);
        BillMonth billMonth = getBillMonthRest(token, appId, month);
        model.put("billMonth",billMonth);
        model.put("appList",getAppList(token));
        model.put("month",month);
        model.put("appId",appId);
        return new ModelAndView("console/cost/bill/bill_month",model);
    }

    /**
     * 获取月结账单RestApi调用
     * @param token
     * @param appId 应用ID
     * @param month 查询时间（月份）
     * @return
     */
    private BillMonth getBillMonthRest(String token,String appId, String month){
        String getUrl = PortalConstants.REST_PREFIX_URL + "/rest/bill_month/get?appId={1}&month={2}";
        RestResponse<BillMonth> response = RestRequest.buildSecurityRequest(token).get(getUrl, BillMonth.class, appId, month);
        return response.getData();
    }

    /**
     * 获取租户下的全部应用
     * @param token
     * @return
     */
    private List<App> getAppList(String token){
        String uri = PortalConstants.REST_PREFIX_URL +   "/rest/app/list";
        return RestRequest.buildSecurityRequest(token).getList(uri, App.class).getData();
    }
}
