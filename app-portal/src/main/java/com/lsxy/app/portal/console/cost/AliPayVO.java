package com.lsxy.app.portal.console.cost;

import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipayCore;
import com.alipay.util.AlipaySubmit;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;


/**
 * 阿里支付传参VO，涉及到签名的生成，请不要随便添加属性
 * Created by liups on 2016/7/1.
 */
public class AliPayVO {
    //支付宝各个配置参数
    private String service;
    private String partner;
    private String seller_id;
    private String _input_charset;
    private String payment_type;
    private String notify_url;
    private String return_url;
    private String anti_phishing_key;
    private String exter_invoke_ip;

    private String out_trade_no;        //商户订单号，商户网站订单系统中唯一订单号，必填

    private String subject;             //订单名称，必填

    private String total_fee;            //付款金额，必填

    private String body;                //商品描述，可空

    private String sign;                //校验签名
    private String sign_type;           //签名方式

    private AliPayVO(){

    }

    /**
     * 构建支付宝传参VO
     * @param orderId 订单ID
     * @param amount 金额
     */
    private AliPayVO(String orderId, String amount) {
        this.service = AlipayConfig.service;
        this.partner = AlipayConfig.partner;
        this.seller_id = AlipayConfig.seller_id;
        this._input_charset = AlipayConfig.input_charset;
        this.payment_type = AlipayConfig.payment_type;
        this.notify_url = AlipayConfig.notify_url;
        this.return_url = AlipayConfig.return_url;
        this.anti_phishing_key = AlipayConfig.anti_phishing_key;
        this.exter_invoke_ip = AlipayConfig.exter_invoke_ip;
        this.out_trade_no = orderId;
        this.subject = "充值";
        this.total_fee = amount;
        this.body = "充值";
    }

    /**
     * 构建支付宝传参VO,带签名
     * @param orderId
     * @param amount
     */
    public static AliPayVO buildAliPayVO(String orderId, String amount) throws InvocationTargetException, IllegalAccessException {
        AliPayVO aliPayVO = new AliPayVO(orderId,amount);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> sParaTemp = mapper.convertValue(aliPayVO, Map.class);
        //除去数组中的空值和签名参数
        Map<String, String> sPara = AlipayCore.paraFilter(sParaTemp);
        //生成签名结果
        String mySign = AlipaySubmit.buildRequestMysign(sPara);
        //签名结果与签名方式加入请求提交参数组中
        sPara.put("sign", mySign);
        sPara.put("sign_type", AlipayConfig.sign_type);
        AliPayVO returnVO = new AliPayVO();
        BeanUtils.populate(returnVO, sPara);
        return returnVO;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String get_input_charset() {
        return _input_charset;
    }

    public void set_input_charset(String _input_charset) {
        this._input_charset = _input_charset;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public String getAnti_phishing_key() {
        return anti_phishing_key;
    }

    public void setAnti_phishing_key(String anti_phishing_key) {
        this.anti_phishing_key = anti_phishing_key;
    }

    public String getExter_invoke_ip() {
        return exter_invoke_ip;
    }

    public void setExter_invoke_ip(String exter_invoke_ip) {
        this.exter_invoke_ip = exter_invoke_ip;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }
}
