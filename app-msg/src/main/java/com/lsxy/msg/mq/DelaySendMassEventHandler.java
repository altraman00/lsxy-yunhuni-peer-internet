package com.lsxy.msg.mq;

import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.msg.supplier.SupplierSelector;
import com.lsxy.msg.supplier.SupplierSendService;
import com.lsxy.msg.supplier.common.MsgConstant;
import com.lsxy.msg.supplier.common.ResultMass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
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

    @Override
    public void handleMessage(DelaySendMassEvent message) throws JMSException {
        logger.info("预计发送时间:"+ message.getSendTime() + " 当前时间："+ DateUtils.formatDate(new Date(),"yyyy-MM-dd hh:mm:ss")+" 延迟毫秒数："+message.getDelay());
        ResultMass resultMass = null;
        String[] split = message.getTempArgs().split(MsgConstant.ParamRegexStr);
        List<String> tempArgsList = Arrays.asList(split);
        List<String> mobiles = Arrays.asList(message.getMobiles().split(MsgConstant.NumRegexStr));
        Date sendTime = DateUtils.parseDate(message.getSendTime(), MsgConstant.TimePartten);
        SupplierSendService supplierSendService = supplierSelector.getSendMassService(message.getOperator(),message.getSendType());
        if(supplierSendService != null){
            resultMass = supplierSendService.sendMass(message.getKey(),message.getTaskName(),message.getTempId(),tempArgsList,message.getMsg(),mobiles,sendTime,message.getSendType());
        }

        if(resultMass != null && MsgConstant.SUCCESS.equals( resultMass.getResultCode())&& !MsgConstant.AwaitingTaskId.equals(resultMass.getTaskId())){//成功存发送记录
            //TODO 存放记录
        }else{//失败也存放发送记录
            // TODO 存放记录
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
