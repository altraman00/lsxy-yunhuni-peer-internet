package com.lsxy.area.server.service.callcenter;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.call.center.api.model.CallCenterAgent;
import com.lsxy.call.center.api.service.CallCenterAgentService;
import com.lsxy.framework.core.utils.MapBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by liuws on 2016/12/3.
 */
@Component
public class CallCenterUtil {

    private static final Logger logger = LoggerFactory.getLogger(CallCenterUtil.class);

    /**交谈产生类型**/
    public static final String CONVERSATION_TYPE_QUEUE = "queue";
    public static final String CONVERSATION_TYPE_CALL_OUT= "call_out";
    public static final String CONVERSATION_TYPE_CALL_AGENT = "call_agent";

    /**排队类型**/
    public static final String QUEUE_TYPE_IVR = "ivr";
    public static final String QUEUE_TYPE_CALL_AGENT = "call_agent";
    public static final String QUEUE_TYPE_FWD_AGENT="fwd_agent";
    public static final String QUEUE_TYPE_XFER_AGENT="xfer_agent";
    public static final String QUEUE_TYPE_INVITE_AGENT = "invite_agent";

    /**排队失败原因**/
    public static final String QUEUE_FAIL_ERROR = "error";
    public static final String QUEUE_FAIL_TIMEOUT = "timeout";
    public static final String QUEUE_FAIL_HANGUP = "hangup";
    public static final String QUEUE_FAIL_CANCEL = "cancel";
    public static final String QUEUE_FAIL_CALLFAIL = "call_fail";
    public static final String QUEUE_FAIL_FORWARD = "forward";

    /**交谈发起者的call_id的存放属性**/
    public static final String INITIATOR_FIELD = "CONVERSATION_INITIATOR";
    /**CALLCENTER ID存放字段**/
    public static final String CALLCENTER_FIELD = "CALLCENTER_ID";
    /**呼叫所属的交谈的id的存放属性**/
    public static final String CONVERSATION_FIELD = "CONVERSATION_ID";
    /**交谈中所使用的平台号码**/
    public static final String CONVERSATION_SYSNUM_FIELD = "CONVERSATION_SYSNUM";
    /**交谈类型**/
    public static final String CONVERSATION_TYPE_FIELD = "CONVERSATION_TYPE";
    /**ivr呼叫是否呼叫中心标记存放的字段**/
    public static final String ISCC_FIELD = "ISCC";
    public static final String ISCC_TRUE = "1";
    /**正在播放排队等待音存放的字段**/
    public static final String PLAYWAIT_FIELD = "PLAYWAIT";
    /**呼叫对应的排队id存放的字段**/
    public static final String QUEUE_ID_FIELD = "QUEUE_ID";
    /**呼叫对应的排队类型存放的字段**/
    public static final String QUEUE_TYPE_FIELD = "QUEUE_TYPE";
    /**坐席挂机后的状态存放的字段**/
    public static final String RESERVE_STATE_FIELD = "RESERVE_STATE";
    /**坐席呼叫对应的坐席id存放的字段**/
    public static final String AGENT_ID_FIELD = "AGENT_ID";
    /**坐席呼叫对应的坐席名称存放的字段**/
    public static final String AGENT_NAME_FIELD = "AGENT_NAME";
    /**坐席呼叫对应的坐席分机存放的字段**/
    public static final String AGENT_EXTENSION_FIELD = "AGENT_EXTENSION";
    /**坐席工号存放的字段**/
    public static final String AGENT_NUM_FIELD = "AGENT_NUM";
    /**坐席播放工号前的音存放的字段**/
    public static final String AGENT_PRENUMVOICE_FIELD = "AGENT_PRENUMVOICE";
    /**坐席播放工号后的音存放的字段**/
    public static final String AGENT_POSTNUMVOICE_FIELD = "AGENT_POSTNUMVOICE";
    /**条件id存放的字段**/
    public static final String CONDITION_ID_FIELD = "CONDITION_ID";

    public static final String CONVERSATION_STARTED_FIELD = "CONVERSATION_STARTED";
    public static final String CONVERSATION_STARTED_TRUE = "1";

    /**记录排队开始时间字段**/
    public static final String ENQUEUE_START_TIME_FIELD = "ENQUEUE_START_TIME_FIELD";

    /**交谈成员收放音模式**/
    public static final String VOICE_MODE_FIELD = "VOICE_MODE";

    /**交谈holding 提示音**/
    public static final String HOLD_VOICE_FIELD = "HOLD_VOICE";

    /**直拨热线标记**/
    public static final String DIRECT_HOT_FIELD = "DIRECT_HOT";

    /**直拨热线-等待收码**/
    public static final String DIRECT_RECEIVE_ING_FIELD = "DIRECT_RECEIVE_ING";

    /**分机前缀**/
    public static final String DIRECT_EXTENSIONPREFIX_FIELD = "DIRECT_EXTENSION_PREFIX";


    /**直拨被叫-坐席**/
    public static final String DIRECT_AGENT_FIELD = "DIRECT_AGENT";

    /**直拨主叫**/
    public static final String DIRECT_FROM_FIELD = "DIRECT_FROM";

    /**直拨被叫-外线**/
    public static final String DIRECT_OUT_FIELD = "DIRECT_OUT";

    /**被邀请方**/
    public static final String INVITETO_FIELD = "INVITE_TO";

    /**邀请方**/
    public static final String INVITEFROM_FIELD = "INVITE_FROM";

    /**被邀请方-排队邀请-xml**/
    public static final String ENQUEUEXML_FIELD = "ENQUEUE_XML";

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;

    @Reference(lazy = true,check = false,timeout = 3000)
    private CallCenterAgentService callCenterAgentService;


    /**
     * 排队失败事件
     */
    public void sendQueueFailEvent(String subaccountId,String url,String queueId,String type,String conditionId,
                                   String cause,String origin_call_id,String agent_call_id,
                                   String userData){
       try{
            CallCenterAgent agent = new CallCenterAgent();
            if(agent_call_id != null){
                BusinessState agentState = businessStateService.get(agent_call_id);
                if(agentState != null){
                    agent.setName(agentState.getBusinessData().get(CallCenterUtil.AGENT_NAME_FIELD));
                    agent.setExtension(agentState.getBusinessData().get(CallCenterUtil.AGENT_EXTENSION_FIELD));
                }
            }
            Map<String,Object> notify_data = new MapBuilder<String,Object>()
                    .putIfNotEmpty("event","callcenter.queue.fail")
                    .putIfNotEmpty("id",queueId)
                    .putIfNotEmpty("subaccount_id",subaccountId)
                    .putIfNotEmpty("type",type)
                    .putIfNotEmpty("condition_id",conditionId)
                    .putIfNotEmpty("agent_name",agent.getName())
                    .putIfNotEmpty("cause",cause)
                    .putIfNotEmpty("origin_call_id",origin_call_id)
                    .putIfNotEmpty("agent_extension_id",agent.getExtension())
                    .putIfNotEmpty("agent_call_id",agent_call_id)
                    .putIfNotEmpty("user_data",userData)
                    .build();
            notifyCallbackUtil.postNotify(url,notify_data,null,3);
        }catch (Throwable t){
            logger.error("发送排队选中坐席事件失败",t);
        }
    }

    /**
     * 排队成功事件
     */
    public void sendQueueSuccessEvent(String subaccountId,String url,String queueId,String type,String conditionId,
                                   String origin_call_id,String agent_call_id,
                                   String userData){
        try{
            CallCenterAgent agent = new CallCenterAgent();
            if(agent_call_id != null){
                BusinessState agentState = businessStateService.get(agent_call_id);
                if(agentState != null){
                    agent.setName(agentState.getBusinessData().get(CallCenterUtil.AGENT_NAME_FIELD));
                    agent.setExtension(agentState.getBusinessData().get(CallCenterUtil.AGENT_EXTENSION_FIELD));
                }
            }
            Map<String,Object> notify_data = new MapBuilder<String,Object>()
                    .putIfNotEmpty("event","callcenter.queue.success")
                    .putIfNotEmpty("id",queueId)
                    .putIfNotEmpty("subaccount_id",subaccountId)
                    .putIfNotEmpty("type",type)
                    .putIfNotEmpty("condition_id",conditionId)
                    .putIfNotEmpty("agent_name",agent.getName())
                    .putIfNotEmpty("origin_call_id",origin_call_id)
                    .putIfNotEmpty("agent_extension_id",agent.getExtension())
                    .putIfNotEmpty("agent_call_id",agent_call_id)
                    .putIfNotEmpty("user_data",userData)
                    .build();
            notifyCallbackUtil.postNotify(url,notify_data,null,3);
        }catch (Throwable t){
            logger.error("发送排队选中坐席事件失败",t);
        }
    }

    /**
     * 排队选中坐席事件
     */
    public void sendQueueSelectedAgentEvent(String subaccountId,String url,String queueId,String type,String conditionId,
                                      String origin_call_id,String agent_call_id,
                                      String userData){
        try{
            CallCenterAgent agent = new CallCenterAgent();
            if(agent_call_id != null){
                BusinessState agentState = businessStateService.get(agent_call_id);
                if(agentState != null){
                    agent.setName(agentState.getBusinessData().get(CallCenterUtil.AGENT_NAME_FIELD));
                    agent.setExtension(agentState.getBusinessData().get(CallCenterUtil.AGENT_EXTENSION_FIELD));
                }
            }
            Map<String,Object> notify_data = new MapBuilder<String,Object>()
                    .putIfNotEmpty("event","callcenter.queue.select")
                    .putIfNotEmpty("id",queueId)
                    .putIfNotEmpty("subaccount_id",subaccountId)
                    .putIfNotEmpty("type",type)
                    .putIfNotEmpty("condition_id",conditionId)
                    .putIfNotEmpty("agent_name",agent.getName())
                    .putIfNotEmpty("origin_call_id",origin_call_id)
                    .putIfNotEmpty("agent_extension_id",agent.getExtension())
                    .putIfNotEmpty("agent_call_id",agent_call_id)
                    .putIfNotEmpty("user_data",userData)
                    .build();
            notifyCallbackUtil.postNotify(url,notify_data,null,3);
        }catch (Throwable t){
            logger.error("发送排队选中坐席事件失败",t);
        }
    }

    /**
     * 坐席状态改变事件
     */
    public void agentStateChangedEvent(String subaccountId,String url,String agent_id,String agentName,String previous_state,String latest_state,String userData){
        try{
            if(latest_state == null){
                latest_state = callCenterAgentService.getState(agent_id);
            }
            Map<String,Object> notify_data = new MapBuilder<String,Object>()
                    .putIfNotEmpty("event","callcenter.agent.state_changed")
                    .putIfNotEmpty("name",agentName)
                    .putIfNotEmpty("subaccount_id",subaccountId)
                    .putIfNotEmpty("previous_state",previous_state)
                    .putIfNotEmpty("latest_state",latest_state)
                    .putIfNotEmpty("current_time",System.currentTimeMillis())
                    .putIfNotEmpty("user_data",userData)
                    .build();
            notifyCallbackUtil.postNotify(url,notify_data,null,3);
        }catch (Throwable t){
            logger.error("发送坐席状态变化事件失败",t);
        }
    }

    /**
     * 坐席进入交谈事件
     */
    public void agentEnterConversationEvent(String subaccountId,String url,String agent_id,String agentName,String conversation,String userData){
        try{
            String latest_state = null;
            try{
                latest_state = callCenterAgentService.getState(agent_id);
            }catch (Throwable t){
                logger.error(String.format("获取坐席状态失败,agentId=%s",agent_id),t);
            }
            Map<String,Object> notify_data = new MapBuilder<String,Object>()
                    .putIfNotEmpty("event","callcenter.agent.conversation_changed")
                    .putIfNotEmpty("name",agentName)
                    .putIfNotEmpty("subaccount_id",subaccountId)
                    .putIfNotEmpty("type","enter")
                    .putIfNotEmpty("conversation_id",conversation)
                    .putIfNotEmpty("latest_state",latest_state)
                    .putIfNotEmpty("current_time",System.currentTimeMillis())
                    .putIfNotEmpty("user_data",userData)
                    .build();
            notifyCallbackUtil.postNotify(url,notify_data,null,3);
        }catch (Throwable t){
            logger.error("发送坐席进入交谈事件失败",t);
        }
    }

    /**
     * 坐席退出交谈事件
     */
    public void agentExitConversationEvent(String subaccountId,String url,String agent_id,String agentName,String conversation,String userData){
        try{
            String latest_state = null;
            try{
                latest_state = callCenterAgentService.getState(agent_id);
            }catch (Throwable t){
                logger.error(String.format("获取坐席状态失败,agentId=%s",agent_id),t);
            }
            Map<String,Object> notify_data = new MapBuilder<String,Object>()
                    .putIfNotEmpty("event","callcenter.agent.conversation_changed")
                    .putIfNotEmpty("name",agentName)
                    .putIfNotEmpty("subaccount_id",subaccountId)
                    .putIfNotEmpty("type","exit")
                    .putIfNotEmpty("conversation_id",conversation)
                    .putIfNotEmpty("latest_state",latest_state)
                    .putIfNotEmpty("current_time",System.currentTimeMillis())
                    .putIfNotEmpty("user_data",userData)
                    .build();
            notifyCallbackUtil.postNotify(url,notify_data,null,3);
        }catch (Throwable t){
            logger.error("发送坐席退出交谈事件失败",t);
        }
    }

    /**
     * 交谈开始事件
     */
    public void conversationBeginEvent(String subaccountId,String url,String conversation,String type,String queue_id,String agent_call_id,String userData){
        try{
            Map<String,Object> notify_data = new MapBuilder<String,Object>()
                    .putIfNotEmpty("event","callcenter.conversation.begin")
                    .putIfNotEmpty("id",conversation)
                    .putIfNotEmpty("subaccount_id",subaccountId)
                    .putIfNotEmpty("type",type)
                    .putIfNotEmpty("queue_id",queue_id)
                    .putIfNotEmpty("agent_call_id",agent_call_id)
                    .putIfNotEmpty("user_data",userData)
                    .build();
            notifyCallbackUtil.postNotify(url,notify_data,null,3);
        }catch (Throwable t){
            logger.error("发送交谈开始事件失败",t);
        }
    }

    /**
     * 交谈结束事件
     * @param url
     * @param conversation
     * @param type
     * @param queue_id
     * @param agent_call_id
     */
    public void conversationEndEvent(String subaccountId,String url,String conversation,String type,Long begin_time,
                                      String record_file,String record_duration,String end_reason,
                                      String queue_id,String agent_call_id,String userData){
        try{
            Map<String,Object> notify_data = new MapBuilder<String,Object>()
                    .putIfNotEmpty("event","callcenter.conversation.end")
                    .putIfNotEmpty("id",conversation)
                    .putIfNotEmpty("subaccount_id",subaccountId)
                    .putIfNotEmpty("type",type)
                    .putIfNotEmpty("begin_time",begin_time)
                    .putIfNotEmpty("end_time",System.currentTimeMillis())
                    .putIfNotEmpty("record_file",record_file)
                    .putIfNotEmpty("record_duration",record_duration)
                    .putIfNotEmpty("end_reason",end_reason)
                    .putIfNotEmpty("queue_id",queue_id)
                    .putIfNotEmpty("agent_call_id",agent_call_id)
                    .putIfNotEmpty("user_data",userData)
                    .build();
            notifyCallbackUtil.postNotify(url,notify_data,null,3);
        }catch (Throwable t){
            logger.error("发送交谈结束事件失败",t);
        }
    }

    public void conversationPartsChangedEvent(String subaccountId,String url,String conversation,String userData){
        try{
            Map<String,Object> notify_data = new MapBuilder<String,Object>()
                    .putIfNotEmpty("event","callcenter.conversation.parts_changed")
                    .putIfNotEmpty("id",conversation)
                    .putIfNotEmpty("subaccount_id",subaccountId)
                    .putIfNotEmpty("user_data",userData)
                    .build();
            notifyCallbackUtil.postNotify(url,notify_data,null,3);
        }catch (Throwable t){
            logger.error("发送交谈成员变化事件失败",t);
        }
    }
}
