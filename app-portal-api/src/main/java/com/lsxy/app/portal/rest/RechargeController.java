package com.lsxy.app.portal.rest;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.yuhuni.api.billing.service.BillingService;
import com.lsxy.yuhuni.api.recharge.model.Recharge;
import com.lsxy.yuhuni.api.recharge.model.ThirdPayRecord;
import com.lsxy.yuhuni.api.recharge.service.RechargeService;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yuhuni.api.recharge.service.ThirdPayRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 充值RestApi接口
 * Created by liups on 2016/7/1.
 */
@RequestMapping("/rest/recharge")
@RestController
public class RechargeController extends AbstractRestController {
    @Autowired
    RechargeService rechargeService;

    @Autowired
    ThirdPayRecordService thirdPayRecordService;


    /**
     * 创建充值订单
     * @param type 充值类型
     * @param amount 充值金额
     * @return
     */
    @RequestMapping("/create_recharge")
    public RestResponse createRecharge(String type,Double amount){
        RestResponse result;
        try {
            Recharge recharge =  rechargeService.createRecharge(this.getCurrentAccountUserName(),type,amount);
            if(recharge != null){
                result = RestResponse.success(recharge);
            }else{
                result = RestResponse.failed("0000","生成订单失败");
            }
        } catch (Exception e) {
            result = RestResponse.failed("0000","生成订单失败");
        }
        return result;
    }

    /**
     * 根据orderId获取充值订单
     * @param orderId
     * @return
     */
    @RequestMapping("/get")
    public RestResponse getRecharge(String orderId){
        RestResponse result;
        try {
            Recharge recharge =  rechargeService.getRechargeByOrderId(orderId);
            if(recharge != null){
                result = RestResponse.success(recharge);
            }else{
                result = RestResponse.failed("0000","订单不存在");
            }
        } catch (Exception e) {
            result = RestResponse.failed("0000","获取订单失败");
        }
        return result;
    }

    /**
     * 支付成功后的处理
     * @param orderId 充值orderId
     * @return
     */
    @RequestMapping("/pay_success")
    public RestResponse paySuccess(String orderId) throws Exception {
        Recharge recharge =  rechargeService.paySuccess(orderId);
        return RestResponse.success(recharge);
    }

    /**
     * 支付付款记录
     * @param payRecord 充值orderId
     * @return
     */
    @RequestMapping("/pay_record")
    public RestResponse payRecord(ThirdPayRecord payRecord,String rechargeId) throws Exception {
        Recharge recharge = new Recharge();
        recharge.setId(rechargeId);
        payRecord.setRecharge(recharge);
        thirdPayRecordService.save(payRecord);
        return RestResponse.success(null);
    }
}
