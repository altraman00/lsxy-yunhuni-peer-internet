package com.lsxy.area.server.event.handler.call;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.api.ConfService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.exceptions.InvalidParamException;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.session.model.Meeting;
import com.lsxy.yunhuni.api.session.model.MeetingMember;
import com.lsxy.yunhuni.api.session.service.CallSessionService;
import com.lsxy.yunhuni.api.session.service.MeetingMemberService;
import com.lsxy.yunhuni.api.session.service.MeetingService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * Created by liuws on 2016/8/29.
 */
@Component
public class Handler_EVENT_SYS_CALL_CONF_ENTER_SUCC extends EventHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_SYS_CALL_CONF_ENTER_SUCC.class);

    @Autowired
    private AppService appService;

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private MeetingMemberService meetingMemberService;

    @Autowired
    private CallSessionService callSessionService;

    @Autowired
    private ConfService confService;

    @Override
    public String getEventName() {
        return Constants.EVENT_SYS_CALL_CONF_ENTER_SUCC;
    }

    /**
     * 接收到加入会议成功事件，需要向开发者发送通知
     * @param request
     * @param session
     * @return
     */
    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        RPCResponse res = null;
        Map<String,Object> params = request.getParamMap();
        if(MapUtils.isEmpty(params)){
            throw new InvalidParamException("request params is null");
        }
        String call_id = (String)params.get("user_data");
        if(StringUtils.isBlank(call_id)){
            throw new InvalidParamException("call_id=null");
        }
        BusinessState state = businessStateService.get(call_id);
        if(state == null){
            throw new InvalidParamException("businessstate is null");
        }

        if(logger.isDebugEnabled()){
            logger.debug("call_id={},state={}",call_id,state);
        }

        String appId = state.getAppId();
        String user_data = state.getUserdata();
        Map<String,Object> businessData = state.getBusinessData();
        String conf_id = null;
        if(businessData!=null){
            conf_id = (String)businessData.get("conf_id");
        }
        if(StringUtils.isBlank(conf_id)){
            throw new InvalidParamException("没有找到对应的会议信息callid={},confid={}",call_id,conf_id);
        }

        if(StringUtils.isBlank(appId)){
            throw new InvalidParamException("没有找到对应的app信息appId={}",appId);
        }
        App app = appService.findById(state.getAppId());
        if(app == null){
            throw new InvalidParamException("没有找到对应的app信息appId={}",appId);
        }
        //会议成员增加
        confService.incrPart(conf_id,call_id);

        Meeting meeting = meetingService.findById(conf_id);
        if(meeting!=null){
            String callSessionId = (String)businessData.get("sessionid");
            MeetingMember meetingMember = new MeetingMember();
            meetingMember.setId(call_id);
            meetingMember.setNumber((String)businessData.get("to"));
            meetingMember.setJoinTime(new Date());
            if(state.getType().equalsIgnoreCase("ivr_incoming")){
                meetingMember.setJoinType(MeetingMember.JOINTYPE_CALL);
            }else{
                meetingMember.setJoinType(MeetingMember.JOINTYPE_INVITE);
            }
            meetingMember.setMeeting(meeting);
            if(callSessionId!=null){
                meetingMember.setSession(callSessionService.findById(callSessionId));
            }
            meetingMember.setResId(state.getResId());
            meetingMemberService.save(meetingMember);
        }

        //开始通知开发者
        if(logger.isDebugEnabled()){
            logger.debug("开始发送会议加入通知给开发者");
        }
        if(StringUtils.isNotBlank(app.getUrl())){
            Map<String,Object> notify_data = new MapBuilder<String,Object>()
                    .putIfNotEmpty("event","conf.joined")
                    .putIfNotEmpty("id",conf_id)
                    .putIfNotEmpty("join_time",System.currentTimeMillis())
                    .putIfNotEmpty("call_id",call_id)
                    .putIfNotEmpty("part_uri",null)
                    .putIfNotEmpty("user_data",user_data)
                    .build();
            notifyCallbackUtil.postNotify(app.getUrl(),notify_data,3);
        }
        if(logger.isDebugEnabled()){
            logger.debug("会议加入通知发送成功");
        }
        if(logger.isDebugEnabled()){
            logger.debug("处理{}事件完成",getEventName());
        }
        return res;
    }


}
