package com.lsxy.area.server.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.lsxy.call.center.api.service.CallCenterConversationMemberService;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.mq.events.portal.VoiceFileRecordSyncEvent;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.handler.RpcRequestHandler;
import com.lsxy.framework.rpc.api.session.Session;
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

import java.util.List;
import java.util.Map;

import static com.lsxy.yunhuni.api.product.enums.ProductCode.call_center;

/**
 * Created by zhagnxb 2016-11-15
 * 录音文件同步成功
 */
@Component
public class Handler_MN_CH_RF_SYNC_OK extends RpcRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(Handler_MN_CH_RF_SYNC_OK.class);
    @Override
    public String getEventName() {
        return ServiceConstants.MN_CH_RF_SYNC_OK;
    }
    @Autowired
    private VoiceFileRecordService voiceFileRecordService;
    @Autowired
    private VoiceCdrService voiceCdrService;
    @Autowired
    private MeetingMemberService meetingMemberService;
    @Reference(timeout=3000,check = false,lazy = true)
    private CallCenterConversationMemberService callCenterConversationMemberService;
    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        if(logger.isDebugEnabled()){
            logger.debug("响应RF SYNC OK:{}",request);
        }
        String jsonList = request.getBodyAsString();
        Map map = JSON.parseObject(jsonList, Map.class);
        String key = (String)map.get("key");
        String type = (String)map.get("type");
        List<VoiceFileRecord> list = null;
        if(VoiceFileRecordSyncEvent.TYPE_CDR.equals(type)) {
            VoiceCdr voiceCdr = voiceCdrService.findById(key);
            if (voiceCdr == null || StringUtil.isEmpty(voiceCdr.getId())) {
                logger.debug("Handler_MN_CH_RF_SYNC_OK录音文件同步失败,无对应cdr存在，map[{}]",map);
                return null;
            }
            list = getFile(voiceCdr.getId());
            if (list == null || list.size() == 0) {
                logger.debug("Handler_MN_CH_RF_SYNC_OK录音文件同步失败:无对应的录音文件记录");
                return null;
            }
        }else if(VoiceFileRecordSyncEvent.TYPE_FILE.equals(type)){
            VoiceFileRecord voiceFileRecord = voiceFileRecordService.findById(key);
            if (voiceFileRecord == null || StringUtil.isEmpty(voiceFileRecord.getId())) {
                logger.debug("Handler_MN_CH_RF_SYNC_OK录音文件同步失败,无对应录音记录存在，map[{}]",map);
                return null;
            }
            list = voiceFileRecordService.getListBySessionId(voiceFileRecord.getSessionId());
            if (list == null || list.size() == 0) {
                logger.debug("Handler_MN_CH_RF_SYNC_OK录音文件同步失败:无对应的录音文件记录");
                return null;
            }
        }else{
            logger.debug("Handler_MN_CH_RF_SYNC_OK录音文件同步失败:对应类型错误,map[{}]",map);
            return null;
        }
        if(list==null||list.size()==0){
            logger.debug("Handler_MN_CH_RF_SYNC_OK录音文件同步结果处理失败:无对应的录音文件");
        }else{
            int status = (Integer) map.get("status");
            String ossUri = "";
            if (status == 1) {
                if (map.get("ossUri") != null) {
                    ossUri = (String) map.get("ossUri");
                }
            }
            for (int i = 0; i < list.size(); i++) {
                VoiceFileRecord voiceFileRecord = list.get(i);
                voiceFileRecord.setStatus(status);
                voiceFileRecord.setOssUrl(ossUri);
                voiceFileRecordService.save(voiceFileRecord);
            }
        }
        return null;
    }
    private List<VoiceFileRecord> getFile(String id){
        //根据cdr获取业务类型，和业务id，根据业务id和业务类型获取录音文件列表，
        VoiceCdr voiceCdr = voiceCdrService.findById(id);
        if(voiceCdr!=null&& StringUtils.isNotEmpty(voiceCdr.getId())) {
            ProductCode p1;
            if(ProductCode.call_center_sip.name().equals(voiceCdr.getType())){
                p1 = ProductCode.call_center;
            }else {
                p1 = ProductCode.valueOf(voiceCdr.getType());
            }

            switch(p1){
                case sys_conf:{
                    //获取会议操作者
                    MeetingMember meetingMember = meetingMemberService.findBySessionId(voiceCdr.getSessionId());
                    //使用会议id
                    List list = voiceFileRecordService.getListBySessionId(meetingMember.getMeetingId());
                    return list;
                }
                case ivr_call:{
                    //使用ivr的id
                    List list = voiceFileRecordService.getListBySessionId(voiceCdr.getRelevanceId());
                    return list;
                }
                case duo_call:{
                    //使用双向回拨的id
                    List list = voiceFileRecordService.getListBySessionId(voiceCdr.getRelevanceId());
                    return list;
                }
                case call_center:{
                    //根据sessionid获取呼叫中心交互成员，在获取呼叫中心交谈，在获取文件
                    List<String> temp = callCenterConversationMemberService.getListBySessionId(voiceCdr.getSessionId());
                    if (temp==null||temp.size() == 0) {
                        return null;
                    }
                    List list = voiceFileRecordService.getListBySessionId( temp.toArray(new String[0]));
                    return list;
                }
            }
        }
        return null;
    }
}
