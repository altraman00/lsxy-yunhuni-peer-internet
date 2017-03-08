package com.lsxy.area.server.event.handler.call;

import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantServiceSwitchService;
import com.lsxy.framework.core.exceptions.api.BalanceNotEnoughException;
import com.lsxy.framework.core.exceptions.api.ExceptionContext;
import com.lsxy.framework.core.exceptions.api.NumberNotAllowToCallException;
import com.lsxy.framework.core.exceptions.api.QuotaNotEnoughException;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.exceptions.InvalidParamException;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;
import com.lsxy.yunhuni.api.apicertificate.service.ApiCertificateSubAccountService;
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

    @Autowired
    private ApiCertificateSubAccountService apiCertificateSubAccountService;

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

        if(isExtensionNum(from_uri)){//是坐席分机呼入
            //判断主叫分机是否存在，不合法直接拒绝

            //根据分机找到坐席，找不到坐席直接拒绝

            //坐席加锁，加锁失败直接拒绝

            //判断坐席状态是否是空闲，非空闲直接拒绝

            //设置坐席状态为fetching

            if(isShortNum(to_uri)){//被叫是分机短号
                //判断被叫分机是否存在

            }else if(isOut(to_uri)){//被叫是外线

            }else if(isHotNum(to_uri)){//被叫是热线号码

            }
            //finally 坐席解锁
        }else{
            //是外线呼入,进入ivr流程
            doIvrAction(res_id,from_uri,to_uri,params);
        }
        return res;
    }


    private void doIvrAction(String res_id,String from_uri,String to_uri,Map<String,Object> params){
        ResourceTelenum to = resourceTelenumService.findNumByCallUri(to_uri);//被叫号码
        if(to ==null){
            logger.info("被叫号码不存在{}",params);
            return;
        }
        LineGateway calledLine = telnumToLineGatewayService.getCalledLineByNumber(to.getTelNumber());
        if(calledLine == null){
            logger.info("线路不存在{}",params);
            return;
        }
        String from = resolveFromTelNum(from_uri,calledLine);//主叫号码

        Tenant tenant = null;
        App app = null;
        String subaccountId = null;
        if(testNum.equals(to.getTelNumber())){
            //被叫是公共测试号,根据主叫号查出应用
            TestNumBind testNumBind = testNumBindService.findByNumber(from);
            if(testNumBind == null){
                logger.info("公共测试号={}找不到对应的app，from={}",testNum,from);
                return;
            }
            tenant = testNumBind.getTenant();
            app = testNumBind.getApp();
            //已上线的应用不允许呼叫测试号码
            if(app != null && app.getStatus() != null && app.getStatus() == App.STATUS_ONLINE){
                logger.info("已上线应用不允许呼叫测试号码,appId={}",app.getId());
                return;
            }
        }else{
            subaccountId = to.getSubaccountId(); //根据号码找到对应的子账号(如果是子账号的号码)
            //不是公共测试号，从号码资源池中查出被叫号码的应用
            if(StringUtils.isBlank(to.getAppId())){
                logger.info("呼入号码没有绑定应用：{}",params);
            }
            app = appService.findById(to.getAppId());
            if(app == null){
                logger.info("号码资源池中找不到被叫号码对应的应用：{}",params);
                return;
            }
            tenant = app.getTenant();
            if(app.getStatus() == null || app.getStatus() == App.STATUS_OFFLINE){
                logger.info("应用未上线appId={}",app.getId());
                return;
            }
        }
        if(tenant == null){
            logger.info("找不到对应的租户:{}",params);
            return;
        }
        if(app == null){
            logger.info("找不到对应的APP:{}", params);
            return;
        }
        boolean isCallCenter = false;
        if(app.getServiceType().equals(App.PRODUCT_CALL_CENTER)){
            if(!appService.enabledService(tenant.getId(),app.getId(), ServiceType.CallCenter)){
                logger.info("[{}][{}]没有开通呼叫中心",tenant.getId(),app.getId());
                return;
            }
            isCallCenter = true;
        }else{
            if(!appService.enabledService(tenant.getId(),app.getId(), ServiceType.IvrService)){
                logger.info("[{}][{}]没有开通ivr",tenant.getId(),app.getId());
                return;
            }
        }
        if(subaccountId!=null){
            ApiCertificateSubAccount subAccount = apiCertificateSubAccountService.findById(subaccountId);
            if(subAccount == null){
                logger.info("没有找到子账号{}",subaccountId);
                return;
            }
            if(!ApiCertificateSubAccount.ENABLED_TRUE.equals(subAccount.getEnabled())){
                logger.info("子账号被禁用{}",subaccountId);
                return;
            }
        }
        try {
            calCostService.isCallTimeRemainOrBalanceEnough(subaccountId,isCallCenter ?
                    ProductCode.call_center.getApiCmd():ProductCode.ivr_call.getApiCmd(), app.getTenant().getId());
        } catch (BalanceNotEnoughException e) {
            logger.info("[{}][{}]欠费，不能呼入",app.getId(),tenant.getId());
            return;
        } catch (QuotaNotEnoughException e) {
            logger.info("[{}][{}]配额不足，不能呼入",app.getId(),tenant.getId());
            return;
        }

        if(logger.isDebugEnabled()){
            logger.debug("[{}][{}]开始处理ivr",tenant.getId(),app.getId());
        }
        ivrActionService.doActionIfAccept(subaccountId,app,tenant,res_id,from,to.getTelNumber(),calledLine.getId(),isCallCenter);
    }

    public static String extractTelnum(String sip){
        if(StringUtil.isBlank(sip)){
            return "";
        }
        int index = sip.indexOf("@");
        if(index <=0){
            index = sip.length();
        }
        if(logger.isDebugEnabled()){
            logger.info("{}====》{}",sip,sip.substring(0,index).replace("sip:",""));
        }
        return sip.substring(0,index).replace("sip:","");
    }

    /***
     * 判断是不是分机号
     * @param uri
     * @return
     */
    private boolean isExtensionNum(String uri){
        boolean result = false;
        String telnum = extractTelnum(uri);
        if(telnum.length()>=6){

        }
        return false;
    }

    /**
     * 判断是否是分机短号
     * @param uri
     * @return
     */
    private boolean isShortNum(String uri){
        return false;
    }

    /**
     * 判断是不是热线号码
     * @param uri
     * @return
     */
    private boolean isHotNum(String uri){
        return false;
    }

    /**
     * 判断是不是外线号码
     * @param uri
     * @return
     */
    private boolean isOut(String uri){
        return false;
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
                            throw new RuntimeException(new NumberNotAllowToCallException(
                                    new ExceptionContext().put("from",from)
                                    .put("linegateway",lineGateway)
                            ));
                        }
                    }else if(from.startsWith("1")){
                        if(from.length() == 11){
                            checkBlackNum = from;
                        }else{
                            throw new RuntimeException(new NumberNotAllowToCallException(
                                    new ExceptionContext().put("from",from)
                                            .put("linegateway",lineGateway)
                            ));
                        }
                    } else{
                        String areaCode = telNumLocationService.getAreaCodeOfTelephone(from);
                        if(StringUtils.isNotBlank(areaCode)){
                            checkBlackNum = from.substring(areaCode.length());
                        }else{
                            throw new RuntimeException(new NumberNotAllowToCallException(
                                    new ExceptionContext().put("from",from)
                                            .put("linegateway",lineGateway)
                            ));
                        }
                    }
                }else{
                    throw new RuntimeException(new NumberNotAllowToCallException(
                            new ExceptionContext().put("from",from)
                                    .put("linegateway",lineGateway)
                    ));
                }
            }else{
                throw new RuntimeException(new NumberNotAllowToCallException(
                        new ExceptionContext().put("from",from)
                                .put("linegateway",lineGateway)
                ));
            }
        }
        boolean isBlackNum = apiGwRedBlankNumService.isBlackNum(checkBlackNum);
        if(isBlackNum){
            throw new RuntimeException(new NumberNotAllowToCallException(
                    new ExceptionContext().put("from",from)
                            .put("linegateway",lineGateway)
            ));
        }
        return from;
    }

}

