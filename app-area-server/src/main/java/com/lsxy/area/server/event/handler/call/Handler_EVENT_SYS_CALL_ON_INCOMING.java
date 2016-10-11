package com.lsxy.area.server.event.handler.call;

import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.api.ConfService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.exceptions.InvalidParamException;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.config.service.LineGatewayService;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourcesRent;
import com.lsxy.yunhuni.api.resourceTelenum.model.TestNumBind;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import com.lsxy.yunhuni.api.resourceTelenum.service.TestNumBindService;
import com.lsxy.yunhuni.api.session.service.CallSessionService;
import com.lsxy.yunhuni.api.session.service.VoiceIvrService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by liuws on 2016/8/29.
 */
@Component
public class Handler_EVENT_SYS_CALL_ON_INCOMING extends EventHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_SYS_CALL_ON_INCOMING.class);

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private AppService appService;

    @Autowired
    private ConfService confService;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;

    @Autowired
    private TestNumBindService testNumBindService;

    @Autowired
    private ResourcesRentService resourcesRentService;

    @Autowired
    private IVRActionService ivrActionService;

    @Autowired
    private LineGatewayService lineGatewayService;

    @Value("${portal.test.call.number}")
    private String testNum;

    @Autowired
    private CallSessionService callSessionService;

    @Autowired
    private VoiceIvrService voiceIvrService;

    @Override
    public String getEventName() {
        return Constants.EVENT_SYS_CALL_ON_INCOMING;
    }

    /**
     * 呼叫呼入事件处理
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
        String res_id = (String)params.get("res_id");
        String from_uri = (String)params.get("from_uri");//主叫sip地址
        String to_uri = (String)params.get("to_uri");//被叫号码sip地址
        String begin_time = (String)params.get("begin_time");
        String from = resolveTelNum(from_uri);//主叫号码
        String to = resolveTelNum(to_uri);//被叫号码

        Tenant tenant = null;
        App app = null;

        if(testNum.equals(to)){
            //被叫是公共测试号,根据主叫号查出应用
            TestNumBind testNumBind = testNumBindService.findByNumber(from);
            if(testNumBind == null){
                logger.error("公共测试号={}找不到对应的app，from={}",to,from);
                return res;
            }
            tenant = testNumBind.getTenant();
            app = testNumBind.getApp();
            //已上线的应用不允许呼叫测试号码
            if(app != null && app.getStatus() != null && app.getStatus() == App.STATUS_ONLINE){
                logger.error("已上线应用不允许呼叫测试号码");
                return res;
            }
        }else{
            //不是公共测试号，从号码资源池中查出被叫号码的应用
            ResourcesRent rent = resourcesRentService.findByResDataAndRentStatus(to, ResourcesRent.RENT_STATUS_USING);
            if(rent == null){
                logger.error("号码资源池中找不到被叫号码对应的应用：{}",params);
            }
            tenant = rent.getTenant();
            app = rent.getApp();
            if(app!= null && app.getStatus() == null || app.getStatus() == App.STATUS_OFFLINE){
                logger.error("应用未上线");
                return res;
            }
        }

        if(tenant == null){
            logger.error("找不到对应的租户:{}",params);
            return res;
        }
        if(app == null){
            logger.error("找不到对应的APP:{}", params);
            return res;
        }

        ivrActionService.doActionIfAccept(app,tenant,res_id,from,to);
        return res;
    }

    private static String resolveTelNum(String sip_uri){
        int start = sip_uri.indexOf(":") + 1;
        int end = sip_uri.indexOf("@");

        if(end == -1){
            end = sip_uri.length();
        }
        return sip_uri.substring(start,end);
    }
}

