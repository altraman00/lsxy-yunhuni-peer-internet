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
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.exceptions.InvalidParamException;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.config.service.LineGatewayService;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import com.lsxy.yunhuni.api.product.service.CalCostService;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourceTelenumService;
import com.lsxy.yunhuni.api.session.model.VoiceCdr;
import com.lsxy.yunhuni.api.session.service.VoiceCdrService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    VoiceCdrService voiceCdrService;
    @Autowired
    CalBillingService calBillingService;
    @Autowired
    ConversationService conversationService;
    @Reference(lazy = true,check = false,timeout = 3000)
    private CallCenterService callCenterService;
    @Autowired
    private ResourceTelenumService resourceTelenumService;
    @Autowired
    private LineGatewayService lineGatewayService;
    @Autowired
    CalCostService calCostService;
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
        //子账号ID，如果是主账号的呼叫，此处为null
        voiceCdr.setSubaccountId(businessState.getSubaccountId());
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
            }
            voiceCdr.setIvrType(1);
        }else if(BusinessState.TYPE_IVR_DIAL.equals(businessState.getType())){
            if(conversationService.isCC(businessState)){
                productCode = ProductCode.call_center;
            }
            voiceCdr.setIvrType(2);
        }else if(BusinessState.TYPE_IVR_CALL.equals(businessState.getType())){
            if(conversationService.isCC(businessState)){
                productCode = ProductCode.call_center;
            }
            voiceCdr.setIvrType(2);
        }

        if(logger.isDebugEnabled()){
            logger.info("[{}][{}][{}]设置cdr的呼入呼出类型,isCallCenter={},state={}",
                    businessState.getTenantId(),businessState.getAppId(),call_id,productCode == ProductCode.call_center,businessState);
        }

        String callCenterId = null;
        if(productCode == ProductCode.call_center){
            callCenterId = conversationService.getCallCenter(businessState);
            if(voiceCdr.getIvrType() == null){
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
                        logger.error("设置cdr的呼入呼出类型失败"+callCenterId,t);
                    }
                }
            }
        }

        voiceCdr.setType(productCode.name());
        voiceCdr.setRelevanceId(businessState.getId());
        String host;
        if(cdrSplit[11].trim().equals("0")){//等于0时表示呼入，参考CTI CDR事件说明文档
            //相对平台是呼入 处理to
            voiceCdr.setFromNum(cdrSplit[7].trim());
            String toNum = cdrSplit[8].trim().split("@")[0];
            ResourceTelenum num = resourceTelenumService.findByTelNumberOrCallUri(toNum);
            if(num != null){
                voiceCdr.setToNum(num.getTelNumber());
            }else{
                voiceCdr.setToNum(toNum);
            }
            //TODO host根据from来获取
            String[] fromSplit = cdrSplit[7].trim().split("@");
            if(fromSplit.length > 1){
                host = fromSplit[1];
            }else{
                host = cdrSplit[10].trim().split("@")[1];
            }
        }else{
            //相对平台是呼出 处理from
            String fromNum = cdrSplit[7].trim().split("@")[0];
            ResourceTelenum num = resourceTelenumService.findByTelNumberOrCallUri(fromNum);
            if(num != null){
                voiceCdr.setFromNum(num.getTelNumber());
            }else{
                voiceCdr.setFromNum(fromNum);
            }
            voiceCdr.setToNum(cdrSplit[8].trim());
            //host根据to来获取
            String[] toSplit = cdrSplit[8].trim().split("@");
            if(toSplit.length > 1){
                host = toSplit[1];
            }else{
                host = cdrSplit[10].trim().split("@")[1];
            }
        }
        Date callStartDate = getCallDate(cdrSplit[18].trim());
        voiceCdr.setCallStartDt(callStartDate == null?new Date():callStartDate);
        voiceCdr.setCallAckDt(getCallDate(cdrSplit[19].trim()));
        Date callEndDate = getCallDate(cdrSplit[20].trim());
        voiceCdr.setCallEndDt(callEndDate == null?new Date():callEndDate);
        voiceCdr.setCallTimeLong(Long.parseLong(cdrSplit[21].trim()));

        LineGateway lineGateway = lineGatewayService.findByHost(host);
        if(lineGateway == null){
            voiceCdr.setLineId(null);
            voiceCdr.setLineCost(BigDecimal.ZERO);
        }else if(LineGateway.ID_OPENSIPS.equals(lineGateway.getId())){
            //如果线路ID是"0",则说明是opensips呼叫
            voiceCdr.setLineId(LineGateway.ID_OPENSIPS);
            voiceCdr.setLineCost(BigDecimal.ZERO);
            if(ProductCode.call_center.name().equals(voiceCdr.getType())){
                //如果是opensips呼叫，且是呼叫中心类型，则将呼叫类型设为:呼叫中心-电话线路(用于计费)
                voiceCdr.setType(ProductCode.call_center_sip.name());
            }
        }else{
            //计算线路成本价
            voiceCdr.setLineId(lineGateway.getId());
            if(voiceCdr.getCallAckDt() != null){
                Integer unit = lineGateway.getLinePriceUnit() == null ? 60:lineGateway.getLinePriceUnit();
                BigDecimal price =  lineGateway.getLinePrice() == null ? BigDecimal.ZERO : lineGateway.getLinePrice();
                BigDecimal lineCost = calCostService.calCost(voiceCdr.getCallTimeLong() + 1, unit, price , 1.0);
                voiceCdr.setLineCost(lineCost);
            }else{
                voiceCdr.setLineCost(BigDecimal.ZERO);
            }
        }

        //sessionId和一些与具体业务相关的信息根据不同的产品业务进行设置
        Map<String, String> data = businessState.getBusinessData();
        if(data != null){
            switch (productCode){
                case duo_call:{
                    String sessionId = data.get(voiceCdr.getToNum().split("@")[0]);
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

}
