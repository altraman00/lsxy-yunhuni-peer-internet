package com.lsxy.msg.mq;

import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.msg.api.model.MsgSendDetail;
import com.lsxy.msg.api.model.MsgSendRecord;
import com.lsxy.msg.api.service.MsgSendDetailService;
import com.lsxy.msg.api.service.MsgSendRecordService;
import com.lsxy.msg.supplier.SupplierSelector;
import com.lsxy.msg.supplier.SupplierSendService;
import com.lsxy.msg.api.model.MsgConstant;
import com.lsxy.msg.supplier.common.ResultMass;
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

    @Override
    public void handleMessage(DelaySendMassEvent message) throws JMSException {
        ResultMass resultMass = null;
        String[] split = message.getTempArgs().split(MsgConstant.ParamRegexStr);
        List<String> tempArgsList = Arrays.asList(split);
        List<String> mobiles = Arrays.asList(message.getMobiles().split(MsgConstant.NumRegexStr));
        Date sendTime = DateUtils.parseDate(message.getSendTime(), MsgConstant.TimePartten);
        SupplierSendService supplierSendService = supplierSelector.getSendMassService(message.getOperator(),message.getSendType());
        if(supplierSendService != null){
            resultMass = supplierSendService.sendMass(message.getTenantId(),message.getAppId(),message.getSubaccountId(),message.getKey(),message.getTaskName(),
                    message.getTempId(),tempArgsList,message.getMsg(),mobiles,sendTime,message.getSendType(),message.getCost());
        }

        if(resultMass != null && MsgConstant.SUCCESS.equals( resultMass.getResultCode())){//成功存发送记录
            //存发送记录，
            MsgSendRecord msgSendRecord = new MsgSendRecord(message.getKey(),message.getTenantId(),message.getAppId(),message.getSubaccountId(),resultMass.getTaskId(),message.getTaskName(),message.getSendType(),resultMass.getHandlers(),message.getOperator(),message.getMsg(),
                    message.getTempId(),resultMass.getSupplierTempId(),message.getTempArgs(),sendTime,new BigDecimal(message.getCost()),true,resultMass.getSumNum(),resultMass.getPendingNum(),resultMass.getFailNum(),MsgSendRecord.STATE_WAIT);
            msgSendRecordService.save(msgSendRecord);
            msgSendDetailService.batchInsertDetail(msgSendRecord,resultMass.getPendingPhones(), MsgSendDetail.STATE_WAIT);
            msgSendDetailService.batchInsertDetail(msgSendRecord,resultMass.getBadPhones(),MsgSendDetail.STATE_FAIL);
        }else if(resultMass != null && !MsgConstant.AwaitingTaskId.equals(resultMass.getTaskId())){
            //失败也存放发送记录
            //存发送记录
            MsgSendRecord msgSendRecord = new MsgSendRecord(message.getKey(),message.getTenantId(),message.getAppId(),message.getSubaccountId(),resultMass.getTaskId(),message.getTaskName(),message.getSendType(),resultMass.getHandlers(),message.getOperator(),message.getMsg(),
                    message.getTempId(),resultMass.getSupplierTempId(),message.getTempArgs(),sendTime,new BigDecimal(message.getCost()),true,resultMass.getSumNum(),resultMass.getPendingNum(),resultMass.getFailNum(),MsgSendRecord.STATE_FAIL);
            msgSendRecordService.save(msgSendRecord);
            msgSendDetailService.batchInsertDetail(msgSendRecord,resultMass.getPendingPhones(), MsgSendDetail.STATE_WAIT);
            msgSendDetailService.batchInsertDetail(msgSendRecord,resultMass.getBadPhones(),MsgSendDetail.STATE_FAIL);
        }

//        接口调用成功则不理会，接口调用失败，则进行补扣费
//        处理发送结果
        if( MsgConstant.SUCCESS.equals( resultMass.getResultCode() ) ) {

        }else{
            //TODO 每条费用
//            int msgCost = sendMassMessageService.getRealCost( message.getMsg() ,message.getTempId(),message.getTempArgs(), message.getSendType() );
//            进行扣费
//            int realCost = resultMass.getBadPhones()!=null?resultMass.getBadPhones().size():0 * msgCost;
            //TODO 费用返还
        }
    }
}
