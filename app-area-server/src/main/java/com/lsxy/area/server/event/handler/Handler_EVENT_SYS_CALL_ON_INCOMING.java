package com.lsxy.area.server.event.handler;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.api.ConfService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.util.ivr.act.IVRActionUtil;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.resourceTelenum.model.TestNumBind;
import com.lsxy.yunhuni.api.resourceTelenum.service.TestNumBindService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
    private IVRActionUtil ivrActionUtil;

    //TODO
    @Value("")
    private String testNum ="123456789";

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
        if(logger.isDebugEnabled()){
            logger.debug("开始处理{}事件,{}",getEventName(),request);
        }
        RPCResponse res = null;
        //TODO incoming事件 cti需要生成一个user_data给我
        String call_id = (String)request.getParamMap().get("user_data");
        String res_id = (String)request.getParamMap().get("res_id");
        String from_uri = (String)request.getParamMap().get("from_uri");//主叫sip地址
        String to_uri = (String)request.getParamMap().get("to_uri");//被叫号码sip地址
        String begin_time = (String)request.getParamMap().get("begin_time");
        String from = null;//主叫号码
        String to = null;//被叫号码

        if(StringUtils.isBlank(call_id)){
            logger.info("call_id is null");
            return res;
        }
        Tenant tenant = null;
        App app = null;

        if(testNum.equals(to)){
            //被叫是公共测试号,根据主叫号查出应用
            TestNumBind testNumBind = testNumBindService.findByNumber(from);
            if(testNumBind == null){
                logger.info("公共测试号={}找不到对应的app，from={}",to,from);
                return res;
            }
            tenant = testNumBind.getTenant();
            app = testNumBind.getApp();
        }else{
            //不是公共测试号，从号码资源池中查出被叫号码的应用

        }

        if(tenant == null){
            logger.info("找不到对应的租户");
            return res;
        }
        if(app == null){
            logger.info("找不到对应的APP");
            return res;
        }
        //保存业务数据，后续事件要用到
        BusinessState callstate = new BusinessState(tenant.getId(),app.getId(),call_id,"ivr_incoming",null,
                res_id,new MapBuilder<String,Object>()
                .put("begin_time",begin_time)
                .put("from",from)
                .put("to",to)
                .build());
        businessStateService.save(callstate);
        ivrActionUtil.doActionIfAccept(call_id);
        return res;
    }

}
