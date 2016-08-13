package com.lsxy.third.gateway.rest;

import com.lsxy.yunhuni.api.recharge.enums.RechargeType;
import com.lsxy.yunhuni.api.recharge.model.Recharge;
import com.lsxy.yunhuni.api.recharge.model.ThirdPayRecord;
import com.lsxy.yunhuni.api.recharge.service.RechargeService;
import com.lsxy.yunhuni.api.recharge.service.ThirdPayRecordService;
import com.unionpay.acp.sdk.AcpService;
import com.unionpay.acp.sdk.SDKConstants;
import com.unionpay.acp.utils.UnionpayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liups on 2016/7/23.
 */
@RestController
@RequestMapping("/third/unionpay")
public class UnionpayController {
    private static final Logger logger = LoggerFactory.getLogger(AliPayController.class);
    @Autowired
    RechargeService rechargeService;

    @Autowired
    ThirdPayRecordService thirdPayRecordService;

    @RequestMapping("/notify")
    public String unionpayNotify(HttpServletRequest request) throws Exception {
        return handleUnionpayResult(request);
    }

    /**
     * 对银联返回的支付结果进行处理
     * @param request
     * @return 如果校验通过，返回success给支付宝(一定要是success)
     */
    private String handleUnionpayResult(HttpServletRequest request) throws Exception {
        String result = null;
        logger.info("BackRcvResponse接收后台通知开始");
        String encoding = request.getParameter(SDKConstants.param_encoding);
        logger.info("返回报文中encoding=[" + encoding + "]");
        Map<String, String> valideData = new HashMap<String, String>();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                // 在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>
                if (value != null && !"".equals(value)) {
                    value = new String(value.getBytes(encoding), encoding);
                    valideData.put(en, value);
                }
            }
        }

        //重要！验证签名前不要修改reqParam中的键值对的内容，否则会验签不过
        if (!AcpService.validate(valideData, encoding)) {
            logger.info("验证签名结果[失败].");
            //验签失败，需解决验签问题
        } else {
            logger.info("验证签名结果[成功].");
            //【注：为了安全验签成功才应该写商户的成功处理逻辑】交易成功，更新商户订单状态

//            String orderId =valideData.get("orderId"); //获取后台通知的数据，其他字段也可用类似方式获取
//            String respCode =valideData.get("respCode"); //获取应答码，收到后台通知了respCode的值一般是00，可以不需要根据这个应答码判断。
            ThirdPayRecord payRecord = new ThirdPayRecord();
            payRecord.setPayType(RechargeType.UNIONPAY.name());
            payRecord.setOrderId(valideData.get("orderId"));
            payRecord.setTradeNo(valideData.get("queryId"));
            payRecord.setTradeStatus(valideData.get("respCode"));
            //银联返回的金额是分为单位，所以要除以100
            BigDecimal amount = new BigDecimal(valideData.get("txnAmt").trim()).divide(new BigDecimal(100));
            payRecord.setTotalFee(amount);
            payRecord.setSellerId(valideData.get("merId"));
            if(UnionpayUtil.merId.equals(payRecord.getSellerId())){
                Recharge recharge = rechargeService.paySuccess(payRecord.getOrderId(),payRecord.getTotalFee());
                if(recharge != null){
                    payRecord.setRecharge(recharge);
                    try {
                        //将付款记录存到数据库
                        thirdPayRecordService.save(payRecord);
                        result = "ok";
                    } catch (DataIntegrityViolationException e) {
                        logger.error("插入付款记录失败，交易号已存在，交易号：{}",payRecord.getTradeNo());
                    }
                }
            }
        }
        logger.info("BackRcvResponse接收后台通知结束");
        //返回给银联服务器http 200  状态码
        return result;
    }
}
