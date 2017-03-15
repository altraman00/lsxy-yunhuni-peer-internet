package com.lsxy.area.server.event.handler.call;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.AreaAndTelNumSelector;
import com.lsxy.area.server.batch.CallCenterBatchInserter;
import com.lsxy.area.server.batch.CallSessionBatchInserter;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.service.callcenter.CallCenterUtil;
import com.lsxy.area.server.service.callcenter.ConversationService;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.area.server.util.CallbackUrlUtil;
import com.lsxy.area.server.util.SipUrlUtil;
import com.lsxy.area.server.voicecodec.VoiceCodec;
import com.lsxy.call.center.api.model.AppExtension;
import com.lsxy.call.center.api.model.CallCenter;
import com.lsxy.call.center.api.model.CallCenterAgent;
import com.lsxy.call.center.api.model.CallCenterConversationMember;
import com.lsxy.call.center.api.service.AppExtensionService;
import com.lsxy.call.center.api.service.CallCenterAgentService;
import com.lsxy.call.center.api.states.lock.AgentLock;
import com.lsxy.call.center.api.states.state.AgentState;
import com.lsxy.call.center.api.states.state.ExtensionState;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantServiceSwitchService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.exceptions.api.BalanceNotEnoughException;
import com.lsxy.framework.core.exceptions.api.ExceptionContext;
import com.lsxy.framework.core.exceptions.api.NumberNotAllowToCallException;
import com.lsxy.framework.core.exceptions.api.QuotaNotEnoughException;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.api.session.SessionContext;
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
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;
import com.lsxy.yunhuni.api.resourceTelenum.model.TestNumBind;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourceTelenumService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import com.lsxy.yunhuni.api.resourceTelenum.service.TelnumToLineGatewayService;
import com.lsxy.yunhuni.api.resourceTelenum.service.TestNumBindService;
import com.lsxy.yunhuni.api.session.model.CallSession;
import com.lsxy.yunhuni.api.statistics.model.CallCenterStatistics;
import com.lsxy.yunhuni.api.statistics.service.CallCenterStatisticsService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
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
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

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

    @Autowired
    private ExtensionState extensionState;

    @Autowired
    private AgentState agentState;

    @Reference(timeout=3000,check = false,lazy = true)
    private AppExtensionService appExtensionService;

    @Reference(timeout=3000,check = false,lazy = true)
    private CallCenterAgentService callCenterAgentService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private CallSessionBatchInserter callSessionBatchInserter;

    @Autowired
    private CallCenterBatchInserter callCenterBatchInserter;

    @Autowired
    private CallbackUrlUtil callbackUrlUtil;

    @Autowired
    private CallCenterStatisticsService callCenterStatisticsService;

    @Autowired
    private AreaAndTelNumSelector areaAndTelNumSelector;

    @Autowired
    private BusinessStateService businessStateService;

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
        String codecs = (String)params.get("codecs");
        if(SipUrlUtil.isExtensionNum(from_uri)){
            //是坐席分机呼入，可以呼给其他坐席，热线，外线
            doDirectCall(res_id,from_uri,to_uri,params,codecs);
        }else{
            //是外线呼入,进入ivr流程
            doIvrAction(res_id,from_uri,to_uri,params,codecs);
        }
        return res;
    }

    private void doDirectCall(String res_id,String from_uri,String to_uri,Map<String,Object> params,String codecs){
        if(logger.isDebugEnabled()){
            logger.info("分机直拨{}",params);
        }
        String right_codec = VoiceCodec.selectExtensionCodec(codecs);
        if(StringUtil.isBlank(right_codec)){
            logger.info("不支持的语音编码params={},codecs={}",params,VoiceCodec.getExtensionCodecs());
            //return res;
        }
        AgentLock from_agentLock = null;
        try{
            //呼入号码为分机长号码
            String from_extensionnum = SipUrlUtil.extractTelnum(from_uri);
            //判断主叫分机是否存在，不合法直接拒绝
            AppExtension from_appExtension = appExtensionService.getByUser(from_extensionnum);
            if(from_appExtension == null){
                logger.info("分机号不存在对应的分机记录extension_num={}",from_extensionnum);
                return;
            }
            //根据分机找到坐席，找不到坐席直接拒绝
            ExtensionState.Model from_eState = extensionState.get(from_appExtension.getId());
            if(from_eState == null){
                logger.info("分机不存在state,id={}",from_appExtension.getId());
                return;
            }
            if(!from_eState.getEnable().equals(ExtensionState.Model.ENABLE_TRUE)){
                logger.info("分机不可用,state={}",from_eState);
                return;
            }
            String from_agentId = from_eState.getAgent();
            if(StringUtil.isBlank(from_agentId)){
                logger.info("坐席不存在，state={}",from_eState);
                return;
            }
            CallCenterAgent from_agent = callCenterAgentService.findById(from_agentId);
            if(from_agent == null){
                logger.info("坐席不存在，id={}",from_agentId);
                return;
            }
            AgentState.Model from_aState = agentState.get(from_agentId);
            if(from_aState == null){
                logger.info("坐席state不存在，id={}",from_agentId);
                return;
            }
            if(from_aState.getLastRegTime() + AgentState.REG_EXPIRE < System.currentTimeMillis()){
                logger.info("坐席不可用，state={}",from_aState);
                return;
            }
            //坐席加锁，加锁失败直接拒绝
            from_agentLock = new AgentLock(redisCacheService,from_agentId);
            if(!from_agentLock.lock()){
                logger.info("坐席加锁失败,id={}",from_agentId);
                return;
            }
            //判断坐席状态是否是空闲，非空闲直接拒绝
            if(!CallCenterAgent.STATE_IDLE.equals(agentState.getState(from_agentId))){
                logger.info("坐席正忙,state={}",agentState.getState(from_agentId));
                return;
            }

            //判断应用是不是呼叫中心应用，非呼叫中心应用拒绝
            App app = appService.findById(from_appExtension.getAppId());
            if(app == null){
                logger.info("app不存在,appId={}",params,from_appExtension.getAppId());
                return;
            }
            if(!appService.enabledService(app.getTenant().getId(),app.getId(), ServiceType.CallCenter)){
                logger.info("[{}][{}]没有开通呼叫中心",app.getTenant().getId(),app.getId());
                return;
            }
            ApiCertificateSubAccount subaccount = null;
            String subaccountId = from_appExtension.getSubaccountId();
            String extension_prefix = null;
            if(subaccountId!=null){
                subaccount = apiCertificateSubAccountService.findById(subaccountId);
                if(subaccount == null){
                    logger.info("没有找到子账号{}",subaccountId);
                    return;
                }
                if(!ApiCertificateSubAccount.ENABLED_TRUE.equals(subaccount.getEnabled())){
                    logger.info("子账号被禁用{}",subaccountId);
                    return;
                }
            }
            if(subaccount!=null){
                extension_prefix = subaccount.getExtensionPrefix().toString();
            }else{
                extension_prefix = app.getCallCenterNum().toString();
            }
            if(StringUtil.isBlank(extension_prefix)){
                logger.info("分机前缀不存在,app={},subaccount={}",app,subaccount);
                return;
            }
            if(!from_extensionnum.startsWith(extension_prefix)){
                logger.info("分机前缀不一致{},{}",from_extensionnum,extension_prefix);
                return;
            }
            String conversationId = UUIDGenerator.uuid();
            //设置坐席状态为fetching
            String call_id = saveSessionCall(subaccountId,app,app.getTenant(),res_id,
                    conversationId,from_agent.getId(),from_agent.getName(),from_agent.getExtension(),
                    from_uri,to_uri,right_codec,extension_prefix);
            agentState.setState(from_agentId,CallCenterAgent.STATE_FETCHING);

            //转换长号码为短号码，显示在被叫的话机上
            from_extensionnum = from_extensionnum.replace(extension_prefix,"");

            if(SipUrlUtil.isHotNum(to_uri)){//被叫是热线号码
                String to = SipUrlUtil.extractTelnum(to_uri);
                if(logger.isDebugEnabled()){
                    logger.info("直拨热线，to={}",to);
                }
                //流程，应答成功后调用收码，收码完成事件中创建会议，会议创建成功后将call加入会议，加入会议成功事件 呼叫被叫，振铃事件将被叫加入会议
                answer(res_id,call_id,areaAndTelNumSelector.getAreaId(app),right_codec);
                businessStateService.updateInnerField(
                        call_id,
                        //直拨外线-主叫分机短号
                        CallCenterUtil.DIRECT_HOT_FIELD,from_extensionnum,
                        CallCenterUtil.DIRECT_EXTENSIONPREFIX_FIELD,extension_prefix
                );
            }else if(SipUrlUtil.isShortNum(extension_prefix,to_uri)){//被叫是分机短号
                //流程：应答成功创建会议，会议创建成功后将call加入会议，加入会议成功事件 呼叫被叫，振铃事件将被叫加入会议
                AgentLock to_agentLock = null;
                try{
                    //判断被叫分机是否存在
                    String to_extensionnum = extension_prefix + SipUrlUtil.extractTelnum(to_uri);//被叫号码要为长号码
                    if(logger.isDebugEnabled()){
                        logger.info("直拨分机，to={}",to_extensionnum);
                    }
                    //不能自己呼给自己
                    if(to_extensionnum.equals(extension_prefix+from_extensionnum)){
                        logger.info("不能自己呼给自己,from={},to={}",(extension_prefix+from_extensionnum),to_extensionnum);
                        return;
                    }
                    //判断主叫分机是否存在，不合法直接拒绝
                    AppExtension to_appExtension = appExtensionService.getByUser(to_extensionnum);
                    if(to_appExtension == null){
                        logger.info("分机号不存在对应的分机记录extension_num={}",to_extensionnum);
                        return;
                    }
                    //根据分机找到坐席，找不到坐席直接拒绝
                    ExtensionState.Model to_eState = extensionState.get(to_appExtension.getId());
                    if(to_eState == null){
                        logger.info("分机不存在state,id={}",to_appExtension.getId());
                        return;
                    }
                    if(!to_eState.getEnable().equals(ExtensionState.Model.ENABLE_TRUE)){
                        logger.info("分机不可用,state={}",to_eState);
                        return;
                    }
                    String to_agentId = to_eState.getAgent();
                    if(StringUtil.isBlank(to_agentId)){
                        logger.info("坐席不存在，state={}",to_eState);
                        return;
                    }
                    CallCenterAgent to_agent = callCenterAgentService.findById(to_agentId);
                    if(to_agent == null){
                        logger.info("坐席不存在，id={}",to_agentId);
                        return;
                    }
                    AgentState.Model to_aState = agentState.get(to_agentId);
                    if(to_aState == null){
                        logger.info("坐席state不存在，id={}",to_agentId);
                        return;
                    }
                    if(to_aState.getLastRegTime() + AgentState.REG_EXPIRE < System.currentTimeMillis()){
                        logger.info("坐席不可用，state={}",to_aState);
                        return;
                    }
                    //坐席加锁，加锁失败直接拒绝
                    to_agentLock = new AgentLock(redisCacheService,to_agentId);
                    if(!to_agentLock.lock()){
                        logger.info("坐席加锁失败,id={}",to_agentId);
                        return;
                    }
                    //判断坐席状态是否是空闲，非空闲直接拒绝
                    if(!CallCenterAgent.STATE_IDLE.equals(agentState.getState(to_agentId))){
                        logger.info("坐席正忙,state={}",agentState.getState(to_agentId));
                        return;
                    }
                    answer(res_id,call_id,areaAndTelNumSelector.getAreaId(app),right_codec);

                    businessStateService.updateInnerField(
                            call_id,
                            //直拨被叫-坐席分机
                            CallCenterUtil.DIRECT_AGENT_FIELD,to_agentId,
                            //直拨主叫
                            CallCenterUtil.DIRECT_FROM_FIELD,from_extensionnum
                    );
                }catch (Throwable t){
                    logger.info("",t);
                }finally {
                    //finally 坐席解锁
                    if(to_agentLock!=null){
                        try{
                            to_agentLock.unlock();
                        }catch (Throwable t){
                            logger.info("",t);
                        }
                    }
                }
            }else if(SipUrlUtil.isOut(to_uri)){//被叫是外线
                String to = SipUrlUtil.extractTelnum(to_uri);
                if(logger.isDebugEnabled()){
                    logger.info("直拨外线，to={}",to);
                }
                boolean isRedNum = apiGwRedBlankNumService.isRedNum(to);
                if(isRedNum){
                    throw new NumberNotAllowToCallException(
                            new ExceptionContext()
                                    .put("to",to)
                                    .put("isRedNum",isRedNum)
                    );
                }
                answer(res_id,call_id,areaAndTelNumSelector.getAreaId(app),right_codec);
                businessStateService.updateInnerField(
                        call_id,
                        //直拨被叫-外线
                        CallCenterUtil.DIRECT_OUT_FIELD,to
                );
            }
        }catch (Throwable t){
            logger.info("",t);
        }finally {
            //finally 坐席解锁
            if(from_agentLock!=null){
                try{
                    from_agentLock.unlock();
                }catch (Throwable t){
                    logger.info("",t);
                }
            }
        }
    }


    private void doIvrAction(String res_id,String from_uri,String to_uri,Map<String,Object> params,String codecs){
        if(logger.isDebugEnabled()){
            logger.info("ivr呼入{}",params);
        }
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
        String right_codec = VoiceCodec.selectLineCodec(calledLine.getCodecs(),codecs);

        if(StringUtil.isBlank(right_codec)){
            logger.info("不支持的语音编码params={},codecs={}",params,calledLine.getCodecs());
            //return;
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
        ivrActionService.doActionIfAccept(subaccountId,app,tenant,res_id,from,to.getTelNumber(),
                calledLine.getId(),isCallCenter,right_codec);
    }

    public void answer(String res_id,String call_id,String areaId,String codec){
        Map<String, Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("res_id",res_id)
                .putIfNotEmpty("codec",codec)
                .putIfNotEmpty("max_answer_seconds",IVRActionService.MAX_DURATION_SEC)
                .putIfNotEmpty("user_data",call_id)
                .putIfNotEmpty("areaId",areaId)
                .build();
        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_ANSWER,params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest,true);
        } catch (Throwable e) {
            logger.error(String.format("调用应答失败,callid=%s",call_id),e);
        }
    }

    private String saveSessionCall(String subaccountId,App app, Tenant tenant, String res_id,
                                   String conversationId,String agentId,String agentName,
                                   String extension,String from, String to,String codec,String extension_prefix){
        String call_id = UUIDGenerator.uuid();
        CallSession callSession = new CallSession();
        callSession.setId(UUIDGenerator.uuid());

        if(!SipUrlUtil.isHotNum(to) && !SipUrlUtil.isOut(to) && SipUrlUtil.isShortNum(extension_prefix,to)){
            //通话记录需要补全分机号码前缀
            to = extension_prefix + to;
        }
        try{
            callSession.setStatus(CallSession.STATUS_CALLING);
            callSession.setFromNum(SipUrlUtil.extractTelnum(from));
            callSession.setToNum(SipUrlUtil.extractTelnum(to));
            callSession.setAppId(app.getId());
            callSession.setTenantId(app.getTenant().getId());
            callSession.setRelevanceId(call_id);
            callSession.setResId(res_id);
            callSession.setType(CallSession.TYPE_CALL_CENTER);
            callSessionBatchInserter.put(callSession);
            CallCenter callCenter = new CallCenter();
            callCenter.setId(call_id);
            callCenter.setTenantId(tenant.getId());
            callCenter.setAppId(app.getId());
            callCenter.setFromNum(SipUrlUtil.extractTelnum(from));
            callCenter.setToNum(SipUrlUtil.extractTelnum(to));
            callCenter.setStartTime(new Date());
            callCenter.setType(""+CallCenter.CALL_UP);
            callCenter.setCost(BigDecimal.ZERO);
            callCenterBatchInserter.put(callCenter);
            try{
                callCenterStatisticsService.incrIntoRedis(new CallCenterStatistics.Builder(tenant.getId(),app.getId(),
                        new Date()).setCallIn(1L).build());
            }catch (Throwable t){
                logger.error(String.format("incrIntoRedis失败，appId=%s",app.getId()),t);
            }
        }catch (Throwable t){
            logger.error(String.format("保存callsession失败,appId=%s,callid=%s",app.getId(),call_id),t);
        }
        String areaId = areaAndTelNumSelector.getAreaId(app);
        //保存业务数据，后续事件要用到
        BusinessState state = new BusinessState.Builder()
                .setTenantId(tenant.getId())
                .setAppId(app.getId())
                .setSubaccountId(subaccountId)
                .setId(call_id)
                .setResId(res_id)
                .setType(BusinessState.TYPE_CC_AGENT_CALL)
                .setCallBackUrl(callbackUrlUtil.get(app,subaccountId))
                .setAreaId(areaId)
                .setBusinessData(new MapBuilder<String,String>()
                        //incoming是第一个会话所以是自己引用自己
                        .putIfNotEmpty(BusinessState.REF_RES_ID,res_id)
                        .putIfNotEmpty(CallCenterUtil.CONVERSATION_FIELD,conversationId)
                        .putIfNotEmpty(CallCenterUtil.AGENT_ID_FIELD,agentId)
                        .putIfNotEmpty(CallCenterUtil.AGENT_NAME_FIELD,agentName)
                        .putIfNotEmpty(CallCenterUtil.AGENT_EXTENSION_FIELD,extension)
                        .putIfNotEmpty(CallCenterUtil.INITIATOR_FIELD,null)
                        .putIfNotEmpty(CallCenterUtil.VOICE_MODE_FIELD,CallCenterConversationMember.MODE_DEFAULT.toString())
                        .putIfNotEmpty("from", SipUrlUtil.extractTelnum(from))
                        .putIfNotEmpty("to", SipUrlUtil.extractTelnum(to))
                        .putIfNotEmpty(CallCenterUtil.CALLCENTER_FIELD,call_id)
                        .putIfNotEmpty(IVRActionService.IVR_ANSWER_WAITTING_FIELD,"1")
                        .putIfNotEmpty(IVRActionService.IVR_ANSWER_CODEC_FIELD,codec)
                        .putIfNotEmpty(BusinessState.SESSIONID,callSession.getId())
                        .build())
                .build();
        businessStateService.save(state);
        return call_id;
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

