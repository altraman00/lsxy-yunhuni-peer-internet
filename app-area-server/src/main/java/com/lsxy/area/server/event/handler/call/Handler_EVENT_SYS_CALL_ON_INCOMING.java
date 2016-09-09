package com.lsxy.area.server.event.handler.call;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.api.ConfService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.area.server.util.ivr.act.IVRActionUtil;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.config.service.LineGatewayService;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourcesRent;
import com.lsxy.yunhuni.api.resourceTelenum.model.TestNumBind;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import com.lsxy.yunhuni.api.resourceTelenum.service.TestNumBindService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
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
    private IVRActionUtil ivrActionUtil;

    @Autowired
    private LineGatewayService lineGatewayService;

    @Value("${portal.test.call.number}")
    private String testNum;

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
        Map<String,Object> params = request.getParamMap();
        if(MapUtils.isEmpty(params)){
            logger.error("request params is null");
            return res;
        }
        String call_id = (String)params.get("user_data");
        String res_id = (String)params.get("res_id");
        String from_uri = (String)params.get("from_uri");//主叫sip地址
        String to_uri = (String)params.get("to_uri");//被叫号码sip地址
        String begin_time = (String)params.get("begin_time");
        String from = from_uri.substring(0,from_uri.indexOf("@"));//主叫号码
        String to = to_uri.substring(0,to_uri.indexOf("@"));//被叫号码

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
            ResourcesRent rent = resourcesRentService.findByResourceTelenumIdAndStatus(to, ResourcesRent.RENT_STATUS_USING);
            tenant = rent.getTenant();
            app = rent.getApp();
        }

        if(tenant == null){
            logger.info("找不到对应的租户");
            return res;
        }
        if(app == null){
            logger.info("找不到对应的APP");
            return res;
        }
        String oneTelnumber = appService.findOneAvailableTelnumber(app);
        LineGateway lineGateway = lineGatewayService.getBestLineGatewayByNumber(oneTelnumber);

        //保存业务数据，后续事件要用到
        BusinessState state = new BusinessState.Builder()
                .setTenantId(tenant.getId())
                .setAppId(app.getId())
                .setId(call_id)
                .setResId(res_id)
                .setType("ivr_incoming")
                .setAreaId(app.getArea().getId())
                .setLineGatewayId(lineGateway.getId())
                .setBusinessData(new MapBuilder<String,Object>()
                        .put("begin_time",begin_time)
                        .put("from",from)
                        .put("to",to)
                        .build())
                .build();
        businessStateService.save(state);
        ivrActionUtil.doActionIfAccept(call_id);
        return res;
    }

}
