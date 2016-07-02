package com.lsxy.app.portal.console.cost;

import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipayNotify;
import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.app.portal.security.AvoidDuplicateSubmission;
import com.lsxy.framework.api.recharge.model.Recharge;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yuhuni.api.billing.model.Billing;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.alipay.util.AlipaySubmit.ALIPAY_GATEWAY_NEW;

/**
 * 充值控制器
 * Created by liups on 2016/7/1.
 */
@Controller
@RequestMapping("/console/cost/recharge")
public class RechargeController extends AbstractPortalController {
    /**
     * 去往充值页面
     * @param request
     * @return
     */
    @RequestMapping(value = "",method = RequestMethod.GET)
    @AvoidDuplicateSubmission(needSaveToken = true) //需要生成防重token的方法用这个
    public ModelAndView index(HttpServletRequest request){
        Map<String,Object> model = new HashMap<>();
        String token = getSecurityToken(request);
        Billing billing = getBilling(token);
        if(billing != null){
            //金额格式化成整数和小数部分
            amountFormat(model, billing.getBalance());
        }

        return new ModelAndView("console/cost/recharge/index",model);
    }



    /**
     * 生成订单，并返回确认付款页面
     * @param request
     * @return
     */
    @RequestMapping(value = "/sure",method = RequestMethod.POST)
    @AvoidDuplicateSubmission(needRemoveToken = true) //需要检验token防止重复提交的方法用这个
    public ModelAndView sure(HttpServletRequest request,String type,Double amount) throws Exception {
        Map<String,Object> model = new HashMap<>();
        String token = getSecurityToken(request);
        Recharge recharge = createRecharge(token,type,amount);
        model.put("recharge",recharge);
        //金额格式化成整数和小数部分
        amountFormat(model,recharge.getAmount());
        return new ModelAndView("console/cost/recharge/sure",model);
    }

    /**
     * 返回跳转到支付宝付款的页面
     * @param request
     * @param orderId 充值记录的orderId
     * @return
     */
    @RequestMapping(value = "/to_alipay",method = RequestMethod.POST)
    public ModelAndView toAliPay(HttpServletRequest request,String orderId) throws Exception {
        Map<String,Object> model = new HashMap<>();
        String token = getSecurityToken(request);
        Recharge recharge = getRecharge(token, orderId);
        //支付处理调用
        AliPayVO aliPayVO = AliPayVO.buildAliPayVO(recharge.getOrderId(),recharge.getAmount()+"");
        //阿里支付网关
        String aliPayGateWay = ALIPAY_GATEWAY_NEW + "_input_charset=" + AlipayConfig.input_charset;
        model.put("aliPayVO",aliPayVO);
        model.put("aliPayGateWay",aliPayGateWay);
        return new ModelAndView("console/cost/recharge/toalipay",model);
    }


    /**
     * 支付宝支付完后的跳转页面
     * 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表
     * @param request HttpServletRequest
     * @param out_trade_no 商户订单号
     * @param trade_no 支付宝交易号
     * @param trade_status 交易状态
     * @return
     */
    @RequestMapping(value = "/pay_return",method = RequestMethod.GET)
    public ModelAndView payReturn(HttpServletRequest request,String out_trade_no,String trade_no,String trade_status){
        Map model = new HashMap();
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        //计算得出通知验证结果
        boolean verify_result = AlipayNotify.verify(params);
        if(verify_result){
            //验证成功
            if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序
            }

            //-------------------
            String token = getSecurityToken(request);
            Billing billing = getBilling(token);
            if(billing != null){
                //金额格式化成整数和小数部分
                amountFormat(model, billing.getBalance());
            }
            //-------------------
        }else{
            //验证失败
        }

        return new ModelAndView("console/cost/recharge/index",model);
    }


    /**
     * 生成订单RestApi调用
     * @param token
     * @return
     */
    private Recharge createRecharge(String token, String type, Double amount) {
        //此处调用生成订单restApi
        String orderUrl = PortalConstants.REST_PREFIX_URL + "/rest/recharge/create_recharge";
        Map<String,Object> map = new HashMap();
        map.put("type",type);
        map.put("amount",amount);
        RestResponse<Recharge> orderResponse = RestRequest.buildSecurityRequest(token).post(orderUrl,map, Recharge.class);
        return orderResponse.getData();
    }

    /**
     * 生成订单RestApi调用
     * @param token
     * @param orderId 充值记录的orderId
     * @return
     */
    private Recharge getRecharge(String token, String orderId) {
        //此处调用生成订单restApi
        String getUrl = PortalConstants.REST_PREFIX_URL + "/rest/recharge/get?orderId={1}";
        RestResponse<Recharge> orderResponse = RestRequest.buildSecurityRequest(token).get(getUrl, Recharge.class,orderId);
        return orderResponse.getData();
    }

    /**
     * 获取账务信息RestApi调用
     * @param token
     * @return
     */
    private Billing getBilling(String token) {
        //此处调用账务restApi
        String billingUrl = PortalConstants.REST_PREFIX_URL + "/rest/billing/get";
        RestResponse<Billing> billingResponse = RestRequest.buildSecurityRequest(token).get(billingUrl, Billing.class);
        return billingResponse.getData();
    }


    /**
     * 将金额分割成整数部分和小数部分
     * @param model 传进来一个装载的map
     * @param amount 金额
     */
    private void amountFormat(Map<String, Object> model, Double amount) {
        if(amount != null){
            //余额整数部分
            model.put("balanceInt",amount.intValue());
            //余额小数部分
            DecimalFormat df   = new DecimalFormat("######0.00");
            String format = df.format(amount);
            model.put("balanceDec",format.substring(format.indexOf('.') + 1, format.length()));
        }
    }

}
