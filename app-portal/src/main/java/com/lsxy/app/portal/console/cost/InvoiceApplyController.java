package com.lsxy.app.portal.console.cost;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.framework.api.invoice.model.InvoiceApply;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 发票申请
 * Created by liups on 2016/7/21.
 */
@Controller
@RequestMapping("/console/cost/invoice_apply")
public class InvoiceApplyController extends AbstractPortalController {

    @RequestMapping("/page")
    public ModelAndView page(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "5") Integer pageSize){
        Map<String,Object> model = new HashMap<>();
        String token = this.getSecurityToken(request);
        Map startInfo = getStartInfo(token);
        Page<InvoiceApply> page = getPage(token, pageNo, pageSize);
        model.putAll(startInfo);
        Object amountStr = startInfo.get("amount");
        BigDecimal amount = new BigDecimal((Double) amountStr);
        //余额整数部分
        model.put("amountInt",amount.intValue());
        //余额小数部分
        DecimalFormat df   = new DecimalFormat("#0.00");
        String format = df.format(amount);
        model.put("amountDec",format.substring(format.indexOf('.') + 1, format.length()));
        model.put("pageObj",page);
        return new ModelAndView("console/cost/invoice/invoice_record",model);
    }

    private Map getStartInfo(String token){
        String url = PortalConstants.REST_PREFIX_URL + "/rest/invoice_apply/start_info";
        return RestRequest.buildSecurityRequest(token).get(url, Map.class).getData();
    }

    private Page<InvoiceApply> getPage(String token, Integer pageNo, Integer pageSize){
        String url = PortalConstants.REST_PREFIX_URL + "/rest/invoice_apply/page?pageNo={1}&pageSize={2}";
        return RestRequest.buildSecurityRequest(token).getPage(url, InvoiceApply.class,pageNo,pageSize).getData();
    }

    @RequestMapping("/apply_info")
    @ResponseBody
    public Map applyInfo(HttpServletRequest request,String start,String end){
        Map result = new HashMap();
        if(StringUtils.isBlank(start) ){
            result.put("flag",false);
            result.put("msg","暂无可开发票！");
        }else if(StringUtils.isBlank(end)){
            result.put("flag",false);
            result.put("msg","请选择结束时间！");
        }else{
            String token = this.getSecurityToken(request);
            RestResponse<BigDecimal> response = applyAmount(token, start, end);
            if(response.isSuccess()){
                double amount = response.getData().doubleValue();
                result.put("flag",true);
                result.put("applyAmount",amount);
            }else{
                result.put("flag",false);
                result.put("msg","请选择结束时间！");
            }
        }
        return result;
    }

    private RestResponse<BigDecimal> applyAmount(String token,String start,String end){
        String url = PortalConstants.REST_PREFIX_URL + "/rest/invoice_apply/apply_amount?start={1}&end={2}";
        return RestRequest.buildSecurityRequest(token).get(url, BigDecimal.class,start,end);
    }

}
