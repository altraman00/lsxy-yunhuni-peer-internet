package com.lsxy.area.server.event.handler.call;

import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantServiceSwitchService;
import com.lsxy.framework.core.exceptions.api.BalanceNotEnoughException;
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
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import com.lsxy.yunhuni.api.product.service.CalCostService;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourcesRent;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;
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

    @Autowired
    private CalCostService calCostService;

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
        ResourceTelenum to = resourceTelenumService.findNumByCallUri(to_uri);//被叫号码
        if(to ==null){
            logger.info("被叫号码不存在{}",request);
            return null;
        }
        LineGateway calledLine = telnumToLineGatewayService.getCalledLineByNumber(to.getTelNumber());
        if(calledLine == null){
            logger.info("线路不存在{}",request);
            return null;
        }
        String from = resolveFromTelNum(from_uri,calledLine);//主叫号码

        Tenant tenant = null;
        App app = null;
        String subaccount = null;
        if(testNum.equals(to.getTelNumber())){
            //被叫是公共测试号,根据主叫号查出应用
            TestNumBind testNumBind = testNumBindService.findByNumber(from);
            if(testNumBind == null){
                logger.error("公共测试号={}找不到对应的app，from={}",testNum,from);
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
            subaccount = to.getSubaccountId(); //根据号码找到对应的子账号(如果是子账号的号码)
            //不是公共测试号，从号码资源池中查出被叫号码的应用
            if(StringUtils.isBlank(to.getAppId())){
                logger.error("呼入号码没有绑定应用：{}",params);
            }
            app = appService.findById(to.getAppId());
            if(app == null){
                logger.error("号码资源池中找不到被叫号码对应的应用：{}",params);
                return res;
            }
            tenant = app.getTenant();
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
        boolean isCallCenter = false;
        if(app.getServiceType().equals(App.PRODUCT_CALL_CENTER)){
            if(!appService.enabledService(tenant.getId(),app.getId(), ServiceType.CallCenter)){
                logger.info("[{}][{}]没有开通呼叫中心",tenant.getId(),app.getId());
                return res;
            }
            isCallCenter = true;
        }else{
            if(!appService.enabledService(tenant.getId(),app.getId(), ServiceType.IvrService)){
                logger.info("[{}][{}]没有开通ivr",tenant.getId(),app.getId());
                return res;
            }
        }
        boolean isAmountEnough = calCostService.isCallTimeRemainOrBalanceEnough(isCallCenter ?
                ProductCode.call_center.getApiCmd():ProductCode.ivr_call.getApiCmd(), app.getTenant().getId());
        if(!isAmountEnough){
            logger.info("[{}][{}]欠费，不能呼入",app.getId(),tenant.getId());
            return res;
        }

        if(logger.isDebugEnabled()){
            logger.debug("[{}][{}]开始处理ivr",tenant.getId(),app.getId());
        }
        ivrActionService.doActionIfAccept(subaccount,app,tenant,res_id,from,to.getTelNumber(),calledLine.getId(),isCallCenter);
        return res;
    }

    private String resolveFromTelNum(String from,LineGateway lineGateway){
        if(logger.isDebugEnabled()){
            logger.debug("开始处理呼入号码{}",from);
        }
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
        //TODO 用于座席呼入压力测试,所以把"system"排除掉
        if(!from.contains("10000")){
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
        }
        boolean isBlackNum = apiGwRedBlankNumService.isBlackNum(checkBlackNum);
        if(isBlackNum){
            throw new RuntimeException(new NumberNotAllowToCallException());
        }
        return from;
    }

}

