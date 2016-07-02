package com.lsxy.app.portal.rest;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.api.recharge.model.Recharge;
import com.lsxy.framework.api.recharge.service.RechargeService;
import com.lsxy.framework.web.rest.RestResponse;
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

}
