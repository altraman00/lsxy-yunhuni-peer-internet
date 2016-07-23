package com.lsxy.app.portal.console.cost;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.app.portal.security.AvoidDuplicateSubmission;
import com.lsxy.framework.api.consume.model.ConsumeDay;
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

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发票申请
 * Created by liups on 2016/7/21.
 */
@Controller
@RequestMapping("/console/cost/invoice_apply")
public class InvoiceApplyController extends AbstractPortalController {


    /**
     * 发票申请首页
     * @param request
     * @param pageNo
     * @param pageSize
     * @return
     */
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

    /**
     * 获取用户发票申请的开始时间rest调用
     * @param token
     * @return
     */
    private Map getStartInfo(String token){
        String url = PortalConstants.REST_PREFIX_URL + "/rest/invoice_apply/start_info";
        return RestRequest.buildSecurityRequest(token).get(url, Map.class).getData();
    }

    /**
     * 获取历史发票申请记录分页信息rest调用
     * @param token
     * @param pageNo
     * @param pageSize
     * @return
     */
    private Page<InvoiceApply> getPage(String token, Integer pageNo, Integer pageSize){
        String url = PortalConstants.REST_PREFIX_URL + "/rest/invoice_apply/page?pageNo={1}&pageSize={2}";
        return RestRequest.buildSecurityRequest(token).getPage(url, InvoiceApply.class,pageNo,pageSize).getData();
    }

    /**
     * 获取所选时间段能开的发票信息
     * @param request
     * @param start
     * @param end
     * @return
     */
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

    /**
     * 获取某时段的发票申请的金额rest调用
     * @param token
     * @param start
     * @param end
     * @return
     */
    private RestResponse<BigDecimal> applyAmount(String token,String start,String end){
        String url = PortalConstants.REST_PREFIX_URL + "/rest/invoice_apply/apply_amount?start={1}&end={2}";
        return RestRequest.buildSecurityRequest(token).get(url, BigDecimal.class,start,end);
    }

    /**
     * 到填写发票申请页
     * @param request
     * @param start
     * @param end
     * @return
     * @throws Exception
     */
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
     * 获取用户发票信息rest调用
     * @return
     */
    private InvoiceInfo getInvoiceInfo(String token){
        String url = PortalConstants.REST_PREFIX_URL + "/rest/invoice_info/get";
        RestResponse<InvoiceInfo> response = RestRequest.buildSecurityRequest(token).get(url, InvoiceInfo.class);
        return response.getData();
    }

    /**
     * 保存发票申请
     * @param request
     * @return
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @AvoidDuplicateSubmission(needSaveToken = true) //需要生成防重token的方法用这个
    public ModelAndView save(HttpServletRequest request){
        String token = this.getSecurityToken(request);
        Map<String,Object> paramsMap = WebUtils.getRequestParams(request);
        this.save(token,paramsMap);
        return new ModelAndView("redirect:/console/cost/invoice_apply/page");
    }

    /**
     * 保存rest调用
     * @param token
     * @param map
     * @return
     */
    private RestResponse save(String token,Map map){
        String url = PortalConstants.REST_PREFIX_URL + "/rest/invoice_apply/save";
        return RestRequest.buildSecurityRequest(token).post(url, map,InvoiceApply.class);
    }

    /**
     * 异常的发票申请从这个方法进发票修改页
     * @param request
     * @param id
     * @return
     */
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

    /**
     * 获取某个申请的详情rest调用
     * @param token
     * @param id
     * @return
     */
    private InvoiceApply getApply(String token,String id){
        String url = PortalConstants.REST_PREFIX_URL + "/rest/invoice_apply/get/{1}";
        return RestRequest.buildSecurityRequest(token).get(url,InvoiceApply.class,id).getData();
    }

    /**
     * 发票申请详情
     * @param request
     * @param id
     * @return
     */
    @RequestMapping(value = "/detail/{id}",method = RequestMethod.GET)
    public ModelAndView getDetail(HttpServletRequest request,@PathVariable String id){
        Map model = new HashMap();
        String token = this.getSecurityToken(request);
        InvoiceApply apply = getApply(token, id);
        model.put("apply",apply);
        return new ModelAndView("console/cost/invoice/invoice_apply_detail",model);
    }


    /**
     * 日消费统计数获取
     * @param request
     * @param start
     * @param end
     * @return
     */
    @RequestMapping(value = "/count_day_consume" ,method = RequestMethod.GET)
    @ResponseBody
    public Map countDayConsume(HttpServletRequest request,String start,String end){
        Map result = new HashMap();
        String token = this.getSecurityToken(request);
        Long count = this.countDayConsumeRest(token,start,end);
        result.put("flag",true);
        result.put("count",count);
        return result;
    }

    /**
     * 日消费统计数获取rest调用
     * @return
     */
    private Long countDayConsumeRest(String token,String start,String end) {
        String url = PortalConstants.REST_PREFIX_URL + "/rest/consume_day/count_by_time?startTime={1}&endTime={2}";
        return RestRequest.buildSecurityRequest(token).get(url,Long.class,start,end).getData();
    }

    /**
     * 日消费统计Ajax分页列表
     * @param start
     * @param end
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/list_day_consume",method = RequestMethod.GET)
    @ResponseBody
    public Map listDayConsume(HttpServletRequest request,String start,String end,Integer pageNo,Integer pageSize){
        Map model = new HashMap();
        String token = this.getSecurityToken(request);
        List result = this.listDayConsumeRest(token,start,end,pageNo,pageSize);
        model.put("flag",true);
        model.put("result",result);
        return model;
    }

    /**
     * 日消费统计Ajax分页列表rest调用
     * @param token
     * @return
     */
    private List listDayConsumeRest(String token, String start, String end,Integer pageNo,Integer pageSize) {
        String url = PortalConstants.REST_PREFIX_URL + "/rest/consume_day/list_by_time?startTime={1}&endTime={2}&pageNo={3}&pageSize={4}";
        return RestRequest.buildSecurityRequest(token).getList(url,ConsumeDay.class,start,end,pageNo,pageSize).getData();
    }

}
