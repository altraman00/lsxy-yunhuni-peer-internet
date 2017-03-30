package com.lsxy.area.server.mq.handler;

import com.lsxy.area.server.mq.UpdateQueueEvent;
import com.lsxy.call.center.api.model.CallCenterConversationMember;
import com.lsxy.call.center.api.model.CallCenterQueue;
import com.lsxy.call.center.api.service.CallCenterConversationMemberService;
import com.lsxy.call.center.api.service.CallCenterQueueService;
import com.lsxy.framework.core.exceptions.api.ConversationMemberNotExistException;
import com.lsxy.framework.core.exceptions.api.ExceptionContext;
import com.lsxy.framework.core.exceptions.api.QueueTaskNotExistException;
import com.lsxy.framework.core.utils.JSONUtil2;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.events.callcenter.ConversationMemberExitEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.Date;

@Component
public class UpdateQueueEventHandler implements MQMessageHandler<UpdateQueueEvent> {

    private static final Logger logger = LoggerFactory.getLogger(UpdateQueueEventHandler.class);

    /**超过半个小时的消息直接丢弃**/
    private static final long expired = 1000 * 60 * 30;

    @Autowired
    private CallCenterQueueService callCenterQueueService;

    @Override
    public void handleMessage(UpdateQueueEvent message) throws JMSException {
        if(logger.isDebugEnabled()){
            logger.debug("处理更新排队记录事件{}",message.toJson());
        }
        if(System.currentTimeMillis() - message.getTimestamp() <= expired){
            CallCenterQueue update = null;
            try{
                update = JSONUtil2.fromJson(message.getQueue(),CallCenterQueue.class);
            }catch (Throwable t){
                logger.error(String.format("queue反序列化失败,queue=%s",message.getQueue()),t);
            }
            if(update == null){
                return;
            }
            CallCenterQueue queue = callCenterQueueService.findById(message.getId());
            if(queue == null){
                //queue为null，消息消费失败，会重新消费
                throw new RuntimeException(
                        new QueueTaskNotExistException(new ExceptionContext().put("排队记录尚未就绪",message.toJson())));
            }

            callCenterQueueService.update(queue.getId(),update);
        }
    }
}
