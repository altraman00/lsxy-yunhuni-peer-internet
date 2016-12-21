package com.lsxy.area.server.mq.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.lsxy.call.center.api.service.CallCenterConversationMemberService;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.events.portal.VoiceFileRecordSyncEvent;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.server.ServerSessionContext;
import com.lsxy.yunhuni.api.config.model.Area;
import com.lsxy.yunhuni.api.config.service.AreaService;
import com.lsxy.yunhuni.api.file.model.VoiceFileRecord;
import com.lsxy.yunhuni.api.file.service.VoiceFileRecordService;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import com.lsxy.yunhuni.api.session.model.MeetingMember;
import com.lsxy.yunhuni.api.session.model.VoiceCdr;
import com.lsxy.yunhuni.api.session.service.MeetingMemberService;
import com.lsxy.yunhuni.api.session.service.VoiceCdrService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxb on 2016/11/14.
 */
@Component
public class VoiceFileRecordSyncEventHandler implements MQMessageHandler<VoiceFileRecordSyncEvent> {
    private static final Logger logger = LoggerFactory.getLogger(VoiceFileRecordSyncEventHandler.class);
    @Autowired
    private ServerSessionContext sessionContext;
    @Autowired
    private RPCCaller rpcCaller;
    @Autowired
    private VoiceFileRecordService voiceFileRecordService;
    @Autowired
    private VoiceCdrService voiceCdrService;
    @Autowired
    private MeetingMemberService meetingMemberService;
    @Autowired
    private AreaService areaService;
    @Reference(timeout=3000,check = false,lazy = true)
    private CallCenterConversationMemberService callCenterConversationMemberService;
    @Override
    public void handleMessage(VoiceFileRecordSyncEvent event) throws JMSException {
        if(logger.isDebugEnabled()){
            logger.debug("录音文件同步开启");
        }
        if(StringUtil.isEmpty(event.getKey())){
            logger.debug("录音文件同步失败，没有key值，[{}]",event);
            return;
        }
        String areaId = "";
        List<VoiceFileRecord> list = null;
        if(VoiceFileRecordSyncEvent.TYPE_CDR.equals(event.getType())) {
            VoiceCdr voiceCdr = voiceCdrService.findById(event.getKey());
            if (voiceCdr == null || StringUtil.isEmpty(voiceCdr.getId())) {
                logger.debug("录音文件同步失败,无对应cdr存在，event[{}]",event);
                return;
            }
            areaId = voiceCdr.getAreaId();
            list = getFile(voiceCdr.getId());
            if (list == null || list.size() == 0) {
                logger.debug("录音文件同步失败:无对应的录音文件记录");
                return;
            }
        }else if(VoiceFileRecordSyncEvent.TYPE_FILE.equals(event.getType())){
            VoiceFileRecord voiceFileRecord = voiceFileRecordService.findById(event.getKey());
            if (voiceFileRecord == null || StringUtil.isEmpty(voiceFileRecord.getId())) {
                logger.debug("录音文件同步失败,无对应录音记录存在，event[{}]",event);
                return;
            }
            areaId = voiceFileRecord.getAreaId();
            list = voiceFileRecordService.getListBySessionId(voiceFileRecord.getSessionId());
            if (list == null || list.size() == 0) {
                logger.debug("录音文件同步失败:无对应的录音文件记录");
                return;
            }
        }else{
            logger.debug("录音文件同步失败:对应类型错误,event[{}]",event);
            return;
        }
        //从文件出发，等到的session应该对应一个oss文件；从cdr出发，得到的文件，也只有对应一个oss文件
        List<String> files = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            VoiceFileRecord voiceFileRecord = list.get(i);
//            if(1!=voiceFileRecord.getStatus()){
                files.add(voiceFileRecord.getUrl());
//            }
        }
        //设置需要串过去的参数
        Map<String, Object> map = new HashMap();
        map.put("key",event.getKey());
        map.put("type",event.getType());
        map.put("appId",event.getAppId());
        map.put("tenantId", event.getTenantId());
        map.put("files", files);
        //转换成json
        String param = JSON.toJSON(map).toString();
        //设置区域的参数
        Map<String, Object> params = new HashMap<>();
        Area area = areaService.findById(areaId);
        if(area != null){
            areaId = area.getId();
        }else{
            logger.error("录音文件同步失败:无对应区域[{}]"+areaId);
            return;
//            areaId = SystemConfig.getProperty("area.server.test.area.id", "area001");
        }
        params.put("areaId",areaId);
        RPCRequest request = RPCRequest.newRequest(ServiceConstants.MN_CH_RF_SYNC,params);
        request.setBody(param);
        try {
            rpcCaller.invoke(sessionContext,request);
            logger.info("发送录音文件同步指令成功");
        } catch (Exception ex) {
            logger.error("发送录音文件同步指令失败:"+request,ex);
        }
    }

    private List<VoiceFileRecord> getFile(String id){
        //根据cdr获取业务类型，和业务id，根据业务id和业务类型获取录音文件列表，
        VoiceCdr voiceCdr = voiceCdrService.findById(id);
        if(voiceCdr!=null&& StringUtils.isNotEmpty(voiceCdr.getId())) {
            ProductCode p1 = ProductCode.valueOf(voiceCdr.getType());
            switch(p1){
                case sys_conf:{
                    //获取会议操作者
                    MeetingMember meetingMember = meetingMemberService.findById(voiceCdr.getSessionId());
                    //使用会议id
                    List list = voiceFileRecordService.getListBySessionId(meetingMember.getMeeting().getId());
                    return list;
                }
                case ivr_call:{
                    //使用ivr的id
                    List list = voiceFileRecordService.getListBySessionId(voiceCdr.getSessionId());
                    return list;
                }
                case duo_call:{
                    //使用双向回拨的id
                    List list = voiceFileRecordService.getListBySessionId(voiceCdr.getSessionId());
                    return list;
                }
                case call_center:{
                    //根据sessionid获取呼叫中心交互成员，在获取呼叫中心交谈，在获取文件
                    List<String> temp = callCenterConversationMemberService.getListBySessionId(voiceCdr.getSessionId());
                    if (temp==null||temp.size() == 0) {
                        return null;
                    }
                    String te = "";
                    for (int i = 0; i < temp.size(); i++) {
                        te += "'" + temp.get(i) + "'";
                        if (i != temp.size() - 1) {
                            te += ",";
                        }
                    }
                    //使用ivr的id
                    List list = voiceFileRecordService.getListBySessionId( te);
                    return list;
                }
            }

        }
        return null;
    }
}
