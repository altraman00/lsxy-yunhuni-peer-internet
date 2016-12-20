package com.lsxy.area.server.event.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.mq.CdrEvent;
import com.lsxy.area.server.service.callcenter.ConversationService;
import com.lsxy.call.center.api.model.CallCenter;
import com.lsxy.call.center.api.service.CallCenterService;
import com.lsxy.framework.api.billing.service.CalBillingService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.callcenter.CallCenterIncrCostEvent;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.exceptions.InvalidParamException;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import com.lsxy.yunhuni.api.product.service.CalCostService;
import com.lsxy.yunhuni.api.session.model.VoiceCdr;
import com.lsxy.yunhuni.api.session.service.VoiceCdrService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * Created by liuws on 2016/8/29.
 */
@Component
public class Handler_EVENT_SYS_ON_CHAN_CLOSED extends EventHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_SYS_ON_CHAN_CLOSED.class);

    @Autowired
    private BusinessStateService businessStateService;
    @Autowired
    CalCostService calCostService;
    @Autowired
    VoiceCdrService voiceCdrService;
    @Autowired
    CalBillingService calBillingService;
    @Autowired
    ConversationService conversationService;
    @Reference(lazy = true,check = false,timeout = 3000)
    private CallCenterService callCenterService;

    @Autowired
    private MQService mqService;

    @Override
    public String getEventName() {
        return Constants.SYS_ON_CHAN_CLOSED;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        Object cdrObj = request.getParamMap().get("data");
        if(cdrObj == null){
            throw new InvalidParamException("cdr数据为空");
        }
        VoiceCdr voiceCdr = new VoiceCdr();
        String cdrOriginal = (String) cdrObj;
        voiceCdr.setCdrOriginal(cdrOriginal);
        String cdrOriginalTemp = cdrOriginal.substring(1,cdrOriginal.length()-1);
        String[] cdrSplit = cdrOriginalTemp.split(",");

        BusinessState businessState;
        String call_id = cdrSplit[25].trim();
        if(StringUtil.isBlank(call_id)){
            //throw new InvalidParamException("CDR没有业务数据字段，可能是非法调用：{}", cdrObj);
            logger.info("CDR没有业务数据字段，可能是非法调用：{}", cdrObj);
            return null;
        }

        if(logger.isDebugEnabled()){
            logger.info("开始处理CDR数据：{}",cdrObj);
        }

        businessState = businessStateService.get(call_id);

        if(businessState == null){
            throw new InvalidParamException("返回CDR找不到关联的业务数据,cdr.id：{}",voiceCdr.getId());
        }
        voiceCdr.setAreaId(businessState.getAreaId());
        voiceCdr.setTenantId(businessState.getTenantId());
        voiceCdr.setAppId(businessState.getAppId());
        voiceCdr.setLineId(businessState.getLineGatewayId());
        //产品编码，可根据些判断此cdr是哪个产品的cdr


        ProductCode productCode = ProductCode.changeApiCmdToProductCode(businessState.getType());

        if(BusinessState.TYPE_SYS_CONF.equals(businessState.getType())){
            voiceCdr.setJoinType(1);
        }else if(businessState.getBusinessData().get("conf_id") != null){
            productCode = ProductCode.sys_conf;
            voiceCdr.setJoinType(2);
        }

        if(BusinessState.TYPE_IVR_INCOMING.equals(businessState.getType())){
            if(conversationService.isCC(businessState)){
                productCode = ProductCode.call_center;
            }else{
                voiceCdr.setIvrType(1);
            }
        }else if(BusinessState.TYPE_IVR_CALL.equals(businessState.getType())){
            voiceCdr.setIvrType(2);
        }else if(BusinessState.TYPE_IVR_DIAL.equals(businessState.getType())){
            voiceCdr.setIvrType(2);
        }

        if(logger.isDebugEnabled()){
            logger.info("[{}][{}][{}]设置cdr的呼入呼出类型,isCallCenter={},state={}",
                    businessState.getTenantId(),businessState.getAppId(),call_id,productCode == ProductCode.call_center,businessState);
        }

        String callCenterId = null;
        if(productCode == ProductCode.call_center){
            callCenterId = conversationService.getCallCenter(businessState);
            if(logger.isDebugEnabled()){
                logger.info("[{}][{}][{}]设置cdr的呼入呼出类型,callCenterId={},state={}",
                        businessState.getTenantId(),businessState.getAppId(),call_id,callCenterId,businessState);
            }
            if(callCenterId != null){
                try{
                    CallCenter callCenter = callCenterService.findById(callCenterId);
                    if(logger.isDebugEnabled()){
                        logger.info("[{}][{}][{}]设置cdr的呼入呼出类型,callCenter={},state={}",
                                businessState.getTenantId(),businessState.getAppId(),call_id,callCenter,businessState);
                    }
                    if(callCenter != null && callCenter.getType()!= null){
                        voiceCdr.setIvrType(Integer.parseInt(callCenter.getType()));
                    }
                }catch (Throwable t){
                    logger.error("设置cdr的呼入呼出类型失败",t);
                }
            }
        }

        voiceCdr.setType(productCode.name());
        voiceCdr.setRelevanceId(businessState.getId());
        voiceCdr.setFromNum(cdrSplit[7].trim());
        voiceCdr.setToNum(cdrSplit[8].trim());
        voiceCdr.setCallStartDt(getCallDate(cdrSplit[18].trim()));
        voiceCdr.setCallAckDt(getCallDate(cdrSplit[19].trim()));
        voiceCdr.setCallEndDt(getCallDate(cdrSplit[20].trim()));
        voiceCdr.setCallTimeLong(Long.parseLong(cdrSplit[21].trim()));

        //sessionId和一些与具体业务相关的信息根据不同的产品业务进行设置
        Map<String, String> data = businessState.getBusinessData();
        if(data != null){
            switch (productCode){
                case duo_call:{
                    String sessionId = data.get(voiceCdr.getToNum());
                    voiceCdr.setSessionId(sessionId);
                    break;
                }
                default:{
                    String sessionId = data.get(BusinessState.SESSIONID);
                    voiceCdr.setSessionId(sessionId);
                    break;
                }
            }
        }
        mqService.publish(new CdrEvent(JSONUtil.objectToJson(voiceCdr),callCenterId));
        return null;
    }

    private Date getCallDate(String dateStr){
        if(StringUtils.isNotBlank(dateStr)){
            return DateUtils.parseDate(dateStr,"yyyy-MM-dd HH:mm:ss");
        }else{
            return null;
        }
    }
//
//    private VoiceCdr getVoiceCdr(String cdr) {
//        cdr = cdr.substring(1,cdr.length()-1);
//        String[] split = cdr.split(",");
//        VoiceCdr voiceCdr = new VoiceCdr();
//        voiceCdr.setCdr_id(split[0].trim());
//        voiceCdr.setCdr_nodeid(split[1].trim());
//        voiceCdr.setCdr_cdrid(split[2].trim());
//        voiceCdr.setCdr_processid(split[3].trim());
//        voiceCdr.setCdr_callid(split[4].trim());
//        voiceCdr.setCdr_ch(split[5].trim());
//        voiceCdr.setCdr_devno(split[6].trim());
//        voiceCdr.setCdr_ani(split[7].trim());
//        voiceCdr.setCdr_dnis(split[8].trim());
//        voiceCdr.setCdr_dnis2(split[9].trim());
//        voiceCdr.setCdr_orgcallno(split[10].trim());
//        voiceCdr.setCdr_dir(split[11].trim());
//        voiceCdr.setCdr_devtype(split[12].trim());
//        voiceCdr.setCdr_busitype(split[13].trim());
//        voiceCdr.setCdr_callstatus(split[14].trim());
//        voiceCdr.setCdr_endtype(split[15].trim());
//        voiceCdr.setCdr_ipscreason(split[16].trim());
//        voiceCdr.setCdr_callfailcause(split[17].trim());
//        voiceCdr.setCdr_callbegintime(split[18].trim());
//        voiceCdr.setCdr_connectbegintime(split[19].trim());
//        voiceCdr.setCdr_callendtime(split[20].trim());
//        voiceCdr.setCdr_talkduration(split[21].trim());
//        voiceCdr.setCdr_projectid(split[22].trim());
//        voiceCdr.setCdr_flowid(split[23].trim());
//        voiceCdr.setCdr_additionalinfo1(split[24].trim());
//        voiceCdr.setCdr_additionalinfo2(split[25].trim());
//        voiceCdr.setCdr_additionalinfo3(split[26].trim());
//        voiceCdr.setCdr_additionalinfo4(split[27].trim());
//        voiceCdr.setCdr_additionalinfo5(split[28].trim());
//        return voiceCdr;
//    }

}
