package com.lsxy.app.portal.console.cost;

import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.framework.api.consume.model.Consume;
import com.lsxy.framework.api.invoice.model.InvoiceInfo;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 发票信息
 * Created by liups on 2016/7/14.
 */
@Controller
@RequestMapping("/console/cost/invoice_info")
public class InvoiceInfoController {
    @RequestMapping("")
    public ModelAndView get(){
        Map<String,Object> model = new HashMap<>();

        return new ModelAndView("console/cost/invoice/invoice_info",model);
    }

    /**
     * 获取用户发票信息
     * @return
     */
    private InvoiceInfo getInvoiceInfo(String token){
        String url = PortalConstants.REST_PREFIX_URL + "/rest/invoice_info/get";
        RestResponse<InvoiceInfo> response = RestRequest.buildSecurityRequest(token).get(url, InvoiceInfo.class);
        response.getData();
        return null;
    }

}
