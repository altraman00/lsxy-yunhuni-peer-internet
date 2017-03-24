package com.lsxy.msg.mq;

import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.JSONUtil2;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.msg.api.model.MsgSendDetail;
import com.lsxy.msg.api.model.MsgSendRecord;
import com.lsxy.msg.api.service.MsgSendDetailService;
import com.lsxy.msg.api.service.MsgSendRecordService;
import com.lsxy.msg.api.service.MsgSendService;
import com.lsxy.msg.supplier.SupplierSelector;
import com.lsxy.msg.supplier.SupplierSendService;
import com.lsxy.msg.api.model.MsgConstant;
import com.lsxy.msg.supplier.common.ResultMass;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangxb on 2017/2/10.
 */
@Component
public class DelaySendMassEventHandler implements MQMessageHandler<DelaySendMassEvent> {
    private static final Logger logger = LoggerFactory.getLogger(DelaySendMassEventHandler.class);
    @Autowired
    SupplierSelector supplierSelector;
    @Autowired
    MsgSendRecordService msgSendRecordService;
    @Autowired
    MsgSendDetailService msgSendDetailService;
    @Autowired
    MsgSendService msgSendService;

    @Override
    public void handleMessage(DelaySendMassEvent message) throws JMSException {
        if(logger.isDebugEnabled()){
            logger.debug("进入延时发送处理方法:{}",message);
        }
        ResultMass resultMass = null;
        String[] split = message.getTempArgs().split(MsgConstant.ParamRegexStr);
        List<String> tempArgsList = Arrays.asList(split);
        List<String> mobiles = Arrays.asList(message.getMobiles().split(MsgConstant.NumRegexStr));
        Date sendTime = DateUtils.parseDate(message.getSendTime(), MsgConstant.TimePartten);
        SupplierSendService supplierSendService = supplierSelector.getSendMassService(message.getOperator(),message.getSendType());
        if(supplierSendService != null){
            resultMass = supplierSendService.sendMass(message.getTenantId(),message.getAppId(),message.getSubaccountId(),message.getRecordId(),message.getKey(),message.getTaskName(),
                    message.getTempId(),tempArgsList,message.getMsg(),mobiles,sendTime,message.getSendType(),message.getCost());
        }
        List<String> ids = null;
        Date endTime = new Date();
        if(logger.isDebugEnabled()){
            logger.debug("进入延时发送完成{}", JSONUtil2.objectToJson(resultMass));
        }
        if(resultMass != null && MsgConstant.SUCCESS.equals( resultMass.getResultCode())){
            //成功发送
            //更新发送记录，
            msgSendRecordService.updateStateAndTaskIdById(message.getRecordId(),MsgSendRecord.STATE_WAIT,resultMass.getTaskId());
            msgSendDetailService.updateStateAndTaskIdAndEndTimeByRecordIdAndPhones(message.getRecordId(),resultMass.getPendingPhones(), MsgSendDetail.STATE_WAIT,resultMass.getTaskId(),null);
            ids = msgSendDetailService.updateStateAndTaskIdAndEndTimeByRecordIdAndPhones(message.getRecordId(),resultMass.getBadPhones(), MsgSendDetail.STATE_FAIL,resultMass.getTaskId(),endTime);
        }else if(resultMass == null){
            //发送失败
            //更新发送记录，
            msgSendRecordService.updateStateAndTaskIdById(message.getRecordId(),MsgSendRecord.STATE_FAIL,"");
            ids = msgSendDetailService.updateStateAndTaskIdAndEndTimeByRecordIdAndPhones(message.getRecordId(),mobiles, MsgSendDetail.STATE_FAIL,"",endTime);
        } else if( !MsgConstant.AwaitingTaskId.equals(resultMass.getTaskId())){
            //发送失败
            //更新发送记录，
            msgSendRecordService.updateStateAndTaskIdById(message.getRecordId(),MsgSendRecord.STATE_FAIL,resultMass.getTaskId());
            ids = msgSendDetailService.updateStateAndTaskIdAndEndTimeByRecordIdAndPhones(message.getRecordId(),resultMass.getBadPhones(), MsgSendDetail.STATE_FAIL,resultMass.getTaskId(),endTime);
        }

        //接口调用成功则不理会，接口调用失败，进行补扣费
        //处理发送结果
        if(ids != null && ids.size() > 0){
            BigDecimal cost = BigDecimal.ZERO.subtract(new BigDecimal(message.getCost()));
            ProductCode product = ProductCode.valueOf(message.getSendType());
            msgSendService.batchConsumeMsg(endTime,message.getSendType(),cost,product.getRemark(),message.getAppId(),message.getTenantId(),message.getSubaccountId(),ids);
        }
        if(logger.isDebugEnabled()){
            logger.debug("结束延时发送处理方法:{}",message);
        }
    }
}
