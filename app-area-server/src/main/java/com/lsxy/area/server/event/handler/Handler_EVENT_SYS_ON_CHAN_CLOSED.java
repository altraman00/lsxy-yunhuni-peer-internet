package com.lsxy.area.server.event.handler;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import com.lsxy.yunhuni.api.session.model.VoiceCdr;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by liuws on 2016/8/29.
 */
@Component
public class Handler_EVENT_SYS_ON_CHAN_CLOSED extends EventHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_SYS_ON_CHAN_CLOSED.class);

    @Autowired
    private BusinessStateService businessStateService;

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
        ProductCode productCode = ProductCode.changeApiCmdToProductCode(businessState.getType());
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
        voiceCdr.setCdr_id(split[0]);
        voiceCdr.setCdr_nodeid(split[1]);
        voiceCdr.setCdr_cdrid(split[2]);
        voiceCdr.setCdr_processid(split[3]);
        voiceCdr.setCdr_callid(split[4]);
        voiceCdr.setCdr_ch(split[5]);
        voiceCdr.setCdr_devno(split[6]);
        voiceCdr.setCdr_ani(split[7]);
        voiceCdr.setCdr_dnis(split[8]);
        voiceCdr.setCdr_dnis2(split[9]);
        voiceCdr.setCdr_orgcallno(split[10]);
        voiceCdr.setCdr_dir(split[11]);
        voiceCdr.setCdr_devtype(split[12]);
        voiceCdr.setCdr_busitype(split[13]);
        voiceCdr.setCdr_callstatus(split[14]);
        voiceCdr.setCdr_endtype(split[15]);
        voiceCdr.setCdr_ipscreason(split[16]);
        voiceCdr.setCdr_callfailcause(split[17]);
        voiceCdr.setCdr_callbegintime(split[18]);
        voiceCdr.setCdr_connectbegintime(split[19]);
        voiceCdr.setCdr_callendtime(split[20]);
        voiceCdr.setCdr_talkduration(split[21]);
        voiceCdr.setCdr_projectid(split[22]);
        voiceCdr.setCdr_flowid(split[23]);
        voiceCdr.setCdr_additionalinfo1(split[24]);
        voiceCdr.setCdr_additionalinfo2(split[25]);
        voiceCdr.setCdr_additionalinfo3(split[26]);
        voiceCdr.setCdr_additionalinfo4(split[27]);
        voiceCdr.setCdr_additionalinfo5(split[28]);
        return voiceCdr;
    }

}
