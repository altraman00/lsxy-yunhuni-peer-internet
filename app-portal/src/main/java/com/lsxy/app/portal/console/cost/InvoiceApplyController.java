package com.lsxy.app.portal.console.cost;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.app.portal.security.AvoidDuplicateSubmission;
import com.lsxy.framework.api.invoice.model.InvoiceApply;
import com.lsxy.framework.api.invoice.model.InvoiceInfo;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.EntityUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.framework.web.utils.WebUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import scala.annotation.target.param;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
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

    @RequestMapping(value = "/page",method = RequestMethod.GET)

    public ModelAndView page(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "5") Integer pageSize){
        Map<String,Object> model = new HashMap<>();
        String token = this.getSecurityToken(request);
        Map startInfo = getStartInfo(token);
        Page<InvoiceApply> page = getPage(token, pageNo, pageSize);
        model.putAll(startInfo);
        Object amountObj = startInfo.get("amount");
        BigDecimal amount = new BigDecimal(0.00);
        if(amountObj instanceof String){
            amount = new BigDecimal((String) amountObj);
        }else if(amountObj instanceof Double){
            amount = new BigDecimal((Double) amountObj);
        }
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

    @RequestMapping(value = "/apply_info",method = RequestMethod.GET)
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

    @RequestMapping(value = "/to_apply",method = RequestMethod.GET)
    @AvoidDuplicateSubmission(needSaveToken = true) //需要生成防重token的方法用这个
    public ModelAndView toApply(HttpServletRequest request,String start,String end) throws Exception {
        Map<String,Object> model = new HashMap<>();
        String token = this.getSecurityToken(request);
        InvoiceInfo invoiceInfo = getInvoiceInfo(token);
        if(invoiceInfo == null){
            throw new RuntimeException("找不到发票信息");
        }
        InvoiceApply apply = new InvoiceApply();
        EntityUtils.copyProperties(apply,invoiceInfo);
        //发票信息的ID会复制过去，所以清除掉
        apply.setId(null);
        apply.setStart(DateUtils.parseDate(start,"yyyy-MM"));
        apply.setEnd(DateUtils.parseDate(end,"yyyy-MM"));
        RestResponse<BigDecimal> response = applyAmount(token, start, end);
        if(response.isSuccess()){
            double amount = response.getData().doubleValue();
            if(amount <= 0){
                throw new RuntimeException("没有可开的发票金额！");
            }
            apply.setAmount(new BigDecimal(amount));
        }
        model.put("apply",apply);
        return new ModelAndView("console/cost/invoice/invoice_apply_edit",model);
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

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @AvoidDuplicateSubmission(needSaveToken = true) //需要生成防重token的方法用这个
    public ModelAndView save(HttpServletRequest request){
        String token = this.getSecurityToken(request);
        Map<String,Object> paramsMap = WebUtils.getRequestParams(request);
        this.save(token,paramsMap);
        return new ModelAndView("redirect:/console/cost/invoice_apply/page");
    }

    private RestResponse save(String token,Map map){
        String url = PortalConstants.REST_PREFIX_URL + "/rest/invoice_apply/save";
        return RestRequest.buildSecurityRequest(token).post(url, map,InvoiceApply.class);
    }

    @RequestMapping(value = "/edit/{id}",method = RequestMethod.GET)
    @AvoidDuplicateSubmission(needSaveToken = true) //需要生成防重token的方法用这个
    public ModelAndView edit(HttpServletRequest request,@PathVariable String id){
        Map model = new HashMap();
        String token = this.getSecurityToken(request);
        InvoiceApply apply = getApply(token, id);
        if(apply == null ){
            throw new RuntimeException("找不到申请信息");
        }
        if(apply.getStatus() != InvoiceApply.STATUS_EXCEPTION){
            throw new RuntimeException("只有异常的申请才能进行修改");
        }
        model.put("apply",apply);
        return new ModelAndView("console/cost/invoice/invoice_apply_edit",model);
    }

    private InvoiceApply getApply(String token,String id){
        String url = PortalConstants.REST_PREFIX_URL + "/rest/invoice_apply/get/{1}";
        return RestRequest.buildSecurityRequest(token).get(url,InvoiceApply.class,id).getData();
    }

    @RequestMapping(value = "/detail/{id}",method = RequestMethod.GET)
    public ModelAndView getDetail(HttpServletRequest request,@PathVariable String id){
        Map model = new HashMap();
        String token = this.getSecurityToken(request);
        InvoiceApply apply = getApply(token, id);
        model.put("apply",apply);
        return new ModelAndView("console/cost/invoice/invoice_apply_detail",model);
    }

}
