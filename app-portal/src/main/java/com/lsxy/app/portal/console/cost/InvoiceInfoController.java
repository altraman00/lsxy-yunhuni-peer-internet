package com.lsxy.app.portal.console.cost;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.framework.api.consume.model.Consume;
import com.lsxy.framework.api.invoice.model.InvoiceInfo;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
public class InvoiceInfoController extends AbstractPortalController {
    @RequestMapping(value = "",method = RequestMethod.GET)
    public ModelAndView get(HttpServletRequest request){
        String returView;
        Map<String,Object> model = new HashMap<>();
        String token = this.getSecurityToken(request);
        InvoiceInfo invoiceInfo = getInvoiceInfo(token);
        if(invoiceInfo != null){
            returView = "console/cost/invoice/invoice_info";
            model.put("invoiceInfo",invoiceInfo);
        }else{
            returView = "console/cost/invoice/invoice_info_edit";
        }
        return new ModelAndView(returView,model);
    }

    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public ModelAndView edit(HttpServletRequest request){
        String returView;
        Map<String,Object> model = new HashMap<>();
        String token = this.getSecurityToken(request);
        InvoiceInfo invoiceInfo = getInvoiceInfo(token);
        model.put("invoiceInfo",invoiceInfo);
        returView = "console/cost/invoice/invoice_info_edit";
        return new ModelAndView(returView,model);
    }

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ModelAndView edit(HttpServletRequest request,InvoiceInfo invoiceInfo){
        String returView;
        Map<String,Object> model = new HashMap<>();
        String token = this.getSecurityToken(request);
        saveInvoiceInfo(token,invoiceInfo);
        returView = "redirect:/console/cost/invoice_info";
        return new ModelAndView(returView,model);
    }

    private void saveInvoiceInfo(String token, InvoiceInfo invoiceInfo) {

    }

    /**
     * 获取用户发票信息
     * @return
     */
    private InvoiceInfo getInvoiceInfo(String token){
        String url = PortalConstants.REST_PREFIX_URL + "/rest/invoice_info/get";
        RestResponse<InvoiceInfo> response = RestRequest.buildSecurityRequest(token).get(url, InvoiceInfo.class);
        return response.getData();
    }

}
