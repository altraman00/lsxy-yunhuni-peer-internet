package com.lsxy.app.portal.rest.cost;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.recharge.model.Recharge;
import com.lsxy.yunhuni.api.recharge.model.ThirdPayRecord;
import com.lsxy.yunhuni.api.recharge.service.RechargeService;
import com.lsxy.yunhuni.api.recharge.service.ThirdPayRecordService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * 充值RestApi接口
 * Created by liups on 2016/7/1.
 */
@RequestMapping("/rest/recharge")
@RestController
public class RechargeController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(RechargeController.class);

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
    public RestResponse createRecharge(String type,BigDecimal amount){
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
     * @param payRecord 付款记录
     * @return
     */
    @RequestMapping("/pay_success")
    public RestResponse paySuccess(ThirdPayRecord payRecord) throws Exception {
        Recharge recharge =  rechargeService.paySuccess(payRecord.getOrderId(), payRecord.getTotalFee());
        if(logger.isDebugEnabled()){
            logger.debug("处理订单完成！");
        }
        if(recharge != null){
            try {
                payRecord.setRecharge(recharge);
                thirdPayRecordService.save(payRecord);
                if(logger.isDebugEnabled()){
                    logger.debug("插入充值记录完成，充值记录第三方交易号：{}",payRecord.getTradeNo());
                }
            } catch (DataIntegrityViolationException e) {
                logger.error("插入付款记录失败，交易号已存在，交易号：{}",payRecord.getTradeNo());
            }
        }
        return RestResponse.success(recharge);
    }

    /**
     * 充值记录
     * @param pageNo 当前页
     * @param pageSize 每页总数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    public RestResponse list(Integer  pageNo, Integer pageSize,String startTime,String endTime) throws Exception {
        Date startDate = null;
        Date endDate = null;
        if(StringUtils.isNotBlank(startTime)){
            startDate = DateUtils.parseDate(startTime, "yyyy-MM");
        }
        if(StringUtils.isNotBlank(endTime)){
            endDate = DateUtils.parseDate(endTime, "yyyy-MM");
            Calendar cal = Calendar.getInstance();
            cal.setTime(endDate);
            //当前月＋1，即下个月
            cal.add(Calendar.MONTH,1);
            endDate = cal.getTime();
        }
        String userName = getCurrentAccountUserName();
        Page<Recharge> page = rechargeService.pageListByUserNameAndTime(userName,pageNo,pageSize,startDate,endDate);
        return RestResponse.success(page);
    }

}
