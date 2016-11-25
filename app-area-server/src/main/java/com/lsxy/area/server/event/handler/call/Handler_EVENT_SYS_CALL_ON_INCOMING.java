package com.lsxy.area.server.event.handler.call;

import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantServiceSwitchService;
import com.lsxy.framework.core.exceptions.api.NumberNotAllowToCallException;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.exceptions.InvalidParamException;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.app.service.ServiceType;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.config.service.ApiGwRedBlankNumService;
import com.lsxy.yunhuni.api.config.service.TelnumLocationService;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourcesRent;
import com.lsxy.yunhuni.api.resourceTelenum.model.TestNumBind;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourceTelenumService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import com.lsxy.yunhuni.api.resourceTelenum.service.TelnumToLineGatewayService;
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
    private AppService appService;

    @Autowired
    private TestNumBindService testNumBindService;

    @Autowired
    private ResourcesRentService resourcesRentService;

    @Autowired
    private IVRActionService ivrActionService;

    @Value("${portal.test.call.number}")
    private String testNum;

    @Autowired
    private TenantServiceSwitchService tenantServiceSwitchService;

    @Autowired
    private ResourceTelenumService resourceTelenumService;

    @Autowired
    private TelnumToLineGatewayService telnumToLineGatewayService;
    @Autowired
    private ApiGwRedBlankNumService apiGwRedBlankNumService;
    @Autowired
    private TelnumLocationService telNumLocationService;

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
        String to = resourceTelenumService.findNumByCallUri(to_uri);//被叫号码
        LineGateway calledLine = telnumToLineGatewayService.getCalledLineByNumber(to);
        String from = resolveFromTelNum(from_uri,calledLine);//主叫号码

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
                return res;
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
        Integer isCallCenter = null;
        if(app.getServiceType().equals(App.PRODUCT_CALL_CENTER)){
            if(app.getIsCallCenter() == null || app.getIsCallCenter() != 1){
                logger.info("[{}][{}]没有开通呼叫中心 ",tenant.getId(),app.getId());
                return res;
            }
            isCallCenter = 1;
        }else{
            if(!appService.enabledService(tenant.getId(),app.getId(), ServiceType.IvrService)){
                logger.info("[{}][{}]没有开通ivr",tenant.getId(),app.getId());
                return res;
            }
        }
        if(logger.isDebugEnabled()){
            logger.debug("[{}][{}]开始处理ivr",tenant.getId(),app.getId());
        }
        ivrActionService.doActionIfAccept(app,tenant,res_id,from,to,calledLine.getId(),isCallCenter);
        return res;
    }

    private String resolveFromTelNum(String from,LineGateway lineGateway){
        /*
        1、去掉前缀,去掉@后面部分
        2、1开头肯定是手机号
        3、0开头: 010 固话 01*** 手机号
        4、7到8位,固话
        5、其他(不是手机号，不是固话,可能是座席，其他)
         */
        from = from.replace("sip:","");
        String fromPrefix = lineGateway.getFromPrefix();
        int start = 0;
        if(StringUtils.isNotBlank(fromPrefix)){
            if(from.startsWith(fromPrefix)){
                start = fromPrefix.length();
            }
        }
        int end = from.indexOf("@");
        if(end == -1){
            end = from.length();
        }
        from = from.substring(start, end);
        //用于检验黑名单的号码
        String checkBlackNum = from;
        //TODO 座席呼平台多少位？
        //TODO 如何判断座席
        if(from.length() >= 7 && from.length() <= 8){
            //固话不带区号，加上区号
            from = lineGateway.getAreaCode() + from;
        }else if(from.length() >= 11 && from.length() <= 12){
            //手机或固话
            if(from.startsWith("0") || from.startsWith("1")){
                if(from.startsWith("01")){
                    if((!from.startsWith("010")) && from.length() == 12){
                        //手机号加0
                        from = from.substring(1);
                        checkBlackNum = from;
                    }else if(from.startsWith("010")){
                        checkBlackNum = from.substring(3);
                    }else{
                        throw new RuntimeException(new NumberNotAllowToCallException());
                    }
                }else if(from.startsWith("1")){
                    if(from.length() == 11){
                        checkBlackNum = from;
                    }else{
                        throw new RuntimeException(new NumberNotAllowToCallException());
                    }
                } else{
                    String areaCode = telNumLocationService.getAreaCodeOfTelephone(from);
                    if(StringUtils.isNotBlank(areaCode)){
                        checkBlackNum = from.substring(areaCode.length());
                    }else{
                        throw new RuntimeException(new NumberNotAllowToCallException());
                    }
                }
            }else{
                throw new RuntimeException(new NumberNotAllowToCallException());
            }
        }else{
            throw new RuntimeException(new NumberNotAllowToCallException());
        }
        boolean isBlackNum = apiGwRedBlankNumService.isBlackNum(checkBlackNum);
        if(isBlackNum){
            throw new RuntimeException(new NumberNotAllowToCallException());
        }
        return from;
    }

}

