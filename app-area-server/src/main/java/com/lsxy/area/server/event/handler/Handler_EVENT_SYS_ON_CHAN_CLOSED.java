package com.lsxy.area.server.event.handler;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import com.lsxy.yunhuni.api.product.service.CalCostService;
import com.lsxy.yunhuni.api.session.model.VoiceCdr;
import com.lsxy.yunhuni.api.session.service.VoiceCdrService;
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
public class Handler_EVENT_SYS_ON_CHAN_CLOSED extends EventHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_SYS_ON_CHAN_CLOSED.class);

    @Autowired
    private BusinessStateService businessStateService;
    @Autowired
    CalCostService calCostService;
    @Autowired
    VoiceCdrService voiceCdrService;
    @Override
    public String getEventName() {
        return Constants.SYS_ON_CHAN_CLOSED;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        logger.info("正在处理{}",getEventName());
        Object cdrObj = request.getParamMap().get("data");
        if(logger.isDebugEnabled()){
            logger.info("开始处理CDR数据：{}",cdrObj);
        }
        if(cdrObj == null){
            throw new RuntimeException("cdr数据为空");
        }
        VoiceCdr voiceCdr = getVoiceCdr((String) cdrObj);
        BusinessState businessState = businessStateService.get(voiceCdr.getCdr_additionalinfo2());
        voiceCdr.setAreaId(businessState.getAreaId());
        voiceCdr.setTenantId(businessState.getTenantId());
        voiceCdr.setAppId(businessState.getAppId());
        voiceCdr.setLineId(businessState.getLineGatewayId());
        //产品编码，可根据些判断此cdr是哪个产品的cdr

        ProductCode productCode = ProductCode.changeApiCmdToProductCode(businessState.getType());
        if(businessState.getBusinessData().get("conf_id") != null){
            productCode = ProductCode.sys_conf;
        }
        voiceCdr.setType(productCode.name());

        voiceCdr.setRelevanceId(businessState.getId());
        //TODO 录音文件路径跟大小
        voiceCdr.setRecordUrl(null);
        voiceCdr.setRecordSize(null);
        voiceCdr.setFromNum(voiceCdr.getCdr_ani());
        voiceCdr.setToNum(voiceCdr.getCdr_dnis());
        voiceCdr.setCallStartDt(getCallDate(voiceCdr.getCdr_callbegintime()));
        voiceCdr.setCallAckDt(getCallDate(voiceCdr.getCdr_connectbegintime()));
        voiceCdr.setCallEndDt(getCallDate(voiceCdr.getCdr_callendtime()));
        voiceCdr.setCallTimeLong(Long.parseLong(voiceCdr.getCdr_talkduration()));
        //扣费
        calCostService.callConsume(voiceCdr);
        //sessionId和一些与具体业务相关的信息根据不同的产品业务进行设置
        Map<String, Object> data = businessState.getBusinessData();
        if(data != null){
            switch (productCode){
                case duo_call:{
                    String sessionId = (String) data.get(voiceCdr.getToNum());
                    voiceCdr.setSessionId(sessionId);
                    break;
                }
                default:{
                    String sessionId = (String) data.get("sessionid");
                    voiceCdr.setSessionId(sessionId);
                    break;
                }
            }
        }
        if(logger.isDebugEnabled()){
            logger.info("插入cdr数据：{}", JSONUtil.objectToJson(voiceCdr));
        }
        voiceCdrService.save(voiceCdr);
        return null;
    }

    private Date getCallDate(String dateStr){
        if(StringUtils.isNotBlank(dateStr)){
            return DateUtils.parseDate(dateStr,"yyyy-MM-dd HH:mm:ss");
        }else{
            return null;
        }
    }

    private VoiceCdr getVoiceCdr(String cdr) {
        cdr = cdr.substring(1,cdr.length()-1);
        String[] split = cdr.split(",");
        VoiceCdr voiceCdr = new VoiceCdr();
        voiceCdr.setCdr_id(split[0].trim());
        voiceCdr.setCdr_nodeid(split[1].trim());
        voiceCdr.setCdr_cdrid(split[2].trim());
        voiceCdr.setCdr_processid(split[3].trim());
        voiceCdr.setCdr_callid(split[4].trim());
        voiceCdr.setCdr_ch(split[5].trim());
        voiceCdr.setCdr_devno(split[6].trim());
        voiceCdr.setCdr_ani(split[7].trim());
        voiceCdr.setCdr_dnis(split[8].trim());
        voiceCdr.setCdr_dnis2(split[9].trim());
        voiceCdr.setCdr_orgcallno(split[10].trim());
        voiceCdr.setCdr_dir(split[11].trim());
        voiceCdr.setCdr_devtype(split[12].trim());
        voiceCdr.setCdr_busitype(split[13].trim());
        voiceCdr.setCdr_callstatus(split[14].trim());
        voiceCdr.setCdr_endtype(split[15].trim());
        voiceCdr.setCdr_ipscreason(split[16].trim());
        voiceCdr.setCdr_callfailcause(split[17].trim());
        voiceCdr.setCdr_callbegintime(split[18].trim());
        voiceCdr.setCdr_connectbegintime(split[19].trim());
        voiceCdr.setCdr_callendtime(split[20].trim());
        voiceCdr.setCdr_talkduration(split[21].trim());
        voiceCdr.setCdr_projectid(split[22].trim());
        voiceCdr.setCdr_flowid(split[23].trim());
        voiceCdr.setCdr_additionalinfo1(split[24].trim());
        voiceCdr.setCdr_additionalinfo2(split[25].trim());
        voiceCdr.setCdr_additionalinfo3(split[26].trim());
        voiceCdr.setCdr_additionalinfo4(split[27].trim());
        voiceCdr.setCdr_additionalinfo5(split[28].trim());
        return voiceCdr;
    }

}
