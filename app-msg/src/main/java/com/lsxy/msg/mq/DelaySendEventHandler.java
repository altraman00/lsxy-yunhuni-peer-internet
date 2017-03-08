package com.lsxy.msg.mq;

import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.mq.api.MQMessageHandler;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.Date;

/**
 * Created by zhangxb on 2017/2/10.
 */
@Component
public class DelaySendEventHandler implements MQMessageHandler<DelaySendEvent> {
    private Logger logger = Logger.getLogger(DelaySendEventHandler.class);
//    @Autowired
//    private SendMassMessageService sendMassMessageService;
//    @Autowired
//    private CostBillingService costBillingService;
    @Override
    public void handleMessage(DelaySendEvent message) throws JMSException {
//        logger.info("预计发送时间:"+ DateUtils.formatDate(DateUtils.parseDate(message.getSendTime(),"yyyyMMddhhmmss"),"yyyy-MM-dd hh:mm:ss")+" 当前时间："+ DateUtils.formatDate(new Date(),"yyyy-MM-dd hh:mm:ss")+" 延迟毫秒数："+message.getDelay());
//        QiXunTongResultMass qiXunTongResultMass = sendMassMessageService.getQiXunTongResultMass(message.getKey() , message.getUserId(), message.getUserName() , message.getMobiles() , message.getTaskName() , message.getMsg() , message.getTempId() , message.getTempArgs() , message.getSendType() , message.getOperator() , message.getSendTime() );
        //接口调用成功则不理会，接口调用失败，则进行补扣费
        //处理发送结果
//        if( BaseResult.SUCCESS.equals( qiXunTongResultMass.getResultCode() ) ) {
//        }else{
//            int msgCost = sendMassMessageService.getRealCost( message.getMsg() ,message.getTempId(),message.getTempArgs(), message.getSendType() );
//            进行扣费
//            int realCost = qiXunTongResultMass.getBadPhones()!=null?qiXunTongResultMass.getBadPhones().size():0 * msgCost;
//            costBillingService.costFail(message.getUserId(),message.getUserName(),message.getKey(), message.getSendType(), realCost);
//        }
    }
}
