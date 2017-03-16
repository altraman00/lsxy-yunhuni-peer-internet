package com.lsxy.msg.service;

import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.msg.api.model.*;
import com.lsxy.msg.api.service.*;
import com.lsxy.msg.supplier.SupplierSelector;
import com.lsxy.msg.supplier.SupplierSendService;
import com.lsxy.msg.supplier.common.*;
import com.lsxy.yunhuni.api.apicertificate.model.CertAccountQuotaType;
import com.lsxy.yunhuni.api.apicertificate.service.CertAccountQuotaService;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.config.service.TelnumLocationService;
import com.lsxy.yunhuni.api.consume.service.ConsumeService;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import com.lsxy.yunhuni.api.product.service.CalCostService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liups on 2017/3/7.
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class MsgSendServiceImpl implements MsgSendService {
    private static final Logger logger = LoggerFactory.getLogger(MsgSendServiceImpl.class);
    @Autowired
    MsgTemplateService msgTemplateService;
    @Autowired
    TelnumLocationService telnumLocationService;
    @Autowired
    SupplierSelector supplierSelector;
    @Autowired
    AppService appService;
    @Autowired
    MsgUserRequestService msgUserRequestService;
    @Autowired
    MsgSendRecordService msgSendRecordService;
    @Autowired
    MsgSendDetailService msgSendDetailService;
    @Autowired
    ConsumeService consumeService;
    @Autowired
    CalCostService calCostService;
    @Autowired
    CertAccountQuotaService certAccountQuotaService;

    @Override
    public String sendUssd(String ip,String appId,String subaccountId,String mobile, String tempId, String tempArgs) throws YunhuniApiException{
        return sendOne(appId, subaccountId, mobile, tempId, tempArgs, MsgConstant.MSG_USSD);
    }

    @Override
    public String sendUssdMass(String ip, String appId, String subaccountId, String taskName, String tempId, String tempArgs, String mobiles, String sendTimeStr) throws YunhuniApiException {
        return sendMass(appId, subaccountId, taskName, tempId, tempArgs, mobiles, sendTimeStr,MsgConstant.MSG_SMS);
    }

    @Override
    public String sendSms(String ip,String appId,String subaccountId,String mobile, String tempId, String tempArgs) throws YunhuniApiException{
        return sendOne(appId, subaccountId, mobile, tempId, tempArgs,MsgConstant.MSG_SMS);
    }

    @Override
    public String sendSmsMass(String ip, String appId, String subaccountId, String taskName, String tempId, String tempArgs, String mobiles, String sendTimeStr) throws YunhuniApiException {
        return sendMass(appId, subaccountId, taskName, tempId, tempArgs, mobiles, sendTimeStr,MsgConstant.MSG_SMS);
    }

    @Override
    public void batchConsumeMsg(Date dt, String type, BigDecimal cost, String remark, String appId, String tenantId, String subaccountId, List<String> detailIds) {
        consumeService.batchConsume(dt, type, cost, remark, appId, tenantId, subaccountId, detailIds);
        if(StringUtils.isNotBlank(subaccountId)){
            Long d = 1L;
            if(cost.compareTo(BigDecimal.ZERO) == -1){
                d = -1L;
            }
            long use = detailIds.size() * d;
            String quotaType = null;
            if(ProductCode.msg_sms.name().equals(type)){
                quotaType = CertAccountQuotaType.SmsQuota.name();
            }else if(ProductCode.msg_ussd.name().equals(type)){
                quotaType = CertAccountQuotaType.UssdQuota.name();
            }
            if(StringUtils.isNotBlank(quotaType)){
                certAccountQuotaService.incQuotaUsed(subaccountId,dt,use, quotaType);
            }

        }
    }

    @Transactional
    private String sendOne(String appId, String subaccountId, String mobile, String tempId, String tempArgs,String sendType) throws YunhuniApiException {
        App app = appService.findById(appId);
        //TODO 判断红黑名单

        tempId = tempId.trim();
        tempArgs = tempArgs.trim();
        MsgTemplate temp = msgTemplateService.findByTempId(appId, subaccountId, tempId, true);
        String tempContent = temp.getContent();

        if(temp == null){
            //TODO 抛异常
            throw new RuntimeException("模板不存在");
        }

        //检查参数格式是否合法
        if(!checkTempArgs(tempArgs)){
            //TODO 抛异常
            return ResultCode.ERROR_20004.toString();
        }
        String msg = getMsg(tempContent, tempArgs);
        if(!checkMsgLength(msg,sendType)){
            //TODO 抛异常
        }

        //检验号码的合法性
        mobile = mobile.trim();
        if(!checkMobile(mobile)){
            //TODO 抛异常
            return ResultCode.ERROR_20001.toString();
        }
        //判断号码运营商
        String operator = telnumLocationService.getOperator(mobile);
        //判断是否支持发送
        if(!isSend( operator , MsgConstant.MSG_USSD )){
            //TODO 抛异常
            return ResultCode.ERROR_20009.toString();
        }
        //判断余额是否可以发送
        calCostService.isMsgRemainOrBalanceEnough(subaccountId,sendType,app.getTenant().getId(),1L);

        //生成任务标识，发送
        ResultOne resultOne = null;
        String key = UUIDGenerator.uuid();
        SupplierSendService smsSendOneService = supplierSelector.getSendOneService(operator,sendType);
        if(smsSendOneService == null){
            //TODO 抛异常 找不到短信服务商
        }
        String[] split = tempArgs.split(MsgConstant.ParamRegexStr);
        List<String> tempArgsList = Arrays.asList(split);
        resultOne = smsSendOneService.sendOne(tempId, tempArgsList, msg, mobile,sendType);

        if(resultOne == null){
            //TODO 抛异常
            resultOne = new ResultOne(ResultCode.ERROR_20008);
        }

        //开始发送
        //处理发送结果
        BigDecimal cost = calCostService.calCost(sendType,app.getTenant().getId());
        Date createTime = new Date();
        if(MsgConstant.SUCCESS.equals( resultOne.getResultCode() )) {
            // 计算每条费用
            //插入记录
            MsgUserRequest msgRequest = new MsgUserRequest(key,app.getTenant().getId(),appId,subaccountId,MsgConstant.MSG_USSD,mobile,msg,tempId,tempArgs,createTime,cost,MsgUserRequest.STATE_WAIT,createTime);
            msgUserRequestService.save(msgRequest);
            MsgSendRecord msgSendRecord = new MsgSendRecord(key,app.getTenant().getId(),appId,subaccountId,resultOne.getTaskId(),mobile,MsgConstant.MSG_USSD,resultOne.getHandlers(),
                    operator,msg,tempId,resultOne.getSupplierTempId(),tempArgs,createTime,cost,createTime);
            msgSendRecordService.save(msgSendRecord);
            MsgSendDetail msgSendDetail = new MsgSendDetail(key,app.getTenant().getId(),appId,subaccountId,resultOne.getTaskId(),msgSendRecord.getId(),mobile,msg,
                    tempId,resultOne.getSupplierTempId(),tempArgs,createTime,cost,MsgConstant.MSG_USSD,resultOne.getHandlers(),operator,createTime);
            msgSendDetailService.save(msgSendDetail);
            //插入消费记录
            if(msgRequest.getMsgCost().compareTo(BigDecimal.ZERO) == 1){
                //插入消费
                //批量扣费
                List<String> ids = Arrays.asList(msgSendDetail.getId());
                ProductCode product = ProductCode.valueOf(sendType);
                this.batchConsumeMsg(new Date(),sendType,cost,product.getRemark(),appId,msgRequest.getTenantId(),subaccountId,ids);
            }
        }else{
            MsgUserRequest msgRequest = new MsgUserRequest(key,app.getTenant().getId(),appId,subaccountId,MsgConstant.MSG_USSD,mobile,msg,tempId,tempArgs,new Date(),cost,MsgUserRequest.STATE_FAIL,createTime);
            msgUserRequestService.save(msgRequest);
        }
        logger.info("发送器："+resultOne.getHandlers()+"|发送类型：单发闪印|手机号码："+mobile+"|模板id："+tempId+"|模板参数："+tempArgs+"|短信内容："+msg+"|发送结果："+resultOne.toString2());
        return key;
    }

    private String sendMass(String appId, String subaccountId, String taskName, String tempId, String tempArgs, String mobiles, String sendTimeStr,String sendType) throws YunhuniApiException {
        App app = appService.findById(appId);
        if(StringUtils.isEmpty( taskName )){
            //TODO 抛异常
            return ResultCode.ERROR_20003.toString();
        }
        taskName = taskName.trim();
        Date sendTime;
        //校验群发时间
        if(StringUtils.isNotEmpty(sendTimeStr)){
            try{
                sendTime = DateUtils.parseDate(sendTimeStr, MsgConstant.TimePartten);
            }catch (Exception e){
                //TODO 抛异常
                return ResultCode.ERROR_200010.toString();
            }
        }else{
            sendTime = new Date();
        }
        tempId = tempId.trim();
        tempArgs = tempArgs.trim();
        MsgTemplate temp = msgTemplateService.findByTempId(appId, subaccountId, tempId, true);
        if(temp == null){
            //TODO 抛异常
            throw new RuntimeException("模板不存在");
        }

        String tempContent = temp.getContent();
        //检查参数格式是否合法
        if(!checkTempArgs(tempArgs)){
            //TODO 抛异常
            return ResultCode.ERROR_20004.toString();
        }
        String msg = getMsg(tempContent, tempArgs).trim();

        //检查消息长度
        if(!checkMsgLength(msg,sendType)){
            //TODO 抛异常
        }

        //检查有效号码-并按运营商处理
        MassMobile massMobile = vaildMobiles(mobiles,MsgConstant.MSG_USSD);
        if(massMobile.getCountVaild() > MsgConstant.MaxNum){
            //TODO 抛异常
            return ResultCode.ERROR_20005.toString();
        }
        if(massMobile.getCountVaild() <= 0){
            //TODO 抛异常
            return ResultCode.ERROR_20001.toString();
        }

        //判断余额是否可以发送
        calCostService.isMsgRemainOrBalanceEnough(subaccountId,sendType,app.getTenant().getId(),massMobile.getCount());

        //生成任务标识，发送
        String key = UUIDGenerator.uuid();
        //处理发送结果
        List<ResultMass> list = new ArrayList<>();
        // 单条费用
        BigDecimal cost = calCostService.calCost(sendType,app.getTenant().getId());
        Date createTime = new Date();
        if(massMobile.getMobile().size() > 0){//处理移动号码
            List<String> mobileList = massMobile.getMobile();
            sendMassByOperator(app.getTenant().getId(),appId,subaccountId,taskName, tempId, tempArgs, sendType, sendTime, msg, key, list, mobileList, MsgConstant.ChinaMobile,cost,createTime);
        }

        if(massMobile.getUnicom().size() > 0){//处理联通号码
            List<String> mobileList = massMobile.getMobile();
            sendMassByOperator(app.getTenant().getId(),appId,subaccountId,taskName, tempId, tempArgs, sendType, sendTime, msg, key, list, mobileList, MsgConstant.ChinaUnicom,cost,createTime);
        }

        if(massMobile.getTelecom().size() > 0){//处理电信号码
            List<String> mobileList = massMobile.getMobile();
            sendMassByOperator(app.getTenant().getId(),appId,subaccountId,taskName, tempId, tempArgs, sendType, sendTime, msg, key, list, mobileList, MsgConstant.ChinaTelecom,cost,createTime);
        }

        ResultAllMass resultAllMass = new ResultAllMass(list,massMobile.getNo());
        //处理发送结果
        int state = MsgUserRequest.STATE_FAIL;
        if(MsgConstant.SUCCESS.equals(resultAllMass.getResultCode())){
            state = MsgUserRequest.STATE_WAIT;
        }
        MsgUserRequest msgRequest = new MsgUserRequest(key,app.getTenant().getId(),appId,subaccountId,taskName,sendType,null,mobiles,msg,tempId,tempArgs,sendTime,cost,true,
                resultAllMass.getSumNum(),state,resultAllMass.getPendingNum(),resultAllMass.getInvalidNum(),resultAllMass.getResultDesc(),createTime);
        msgUserRequestService.save(msgRequest);

        return key;
    }

    private void sendMassByOperator(String tenantId,String appId,String subaccountId,String taskName, String tempId, String tempArgs, String sendType, Date sendTime, String msg, String key,
                                    List<ResultMass> list, List<String> mobileList, String oprator,BigDecimal cost,Date createTime) {
        SupplierSendService massService = supplierSelector.getSendMassService( oprator,sendType);
        if(massService != null){
            List<String> detailIds = new ArrayList<>();
            int uMax = massService.getMaxSendNum();
            int ulength = mobileList.size() / uMax + ( mobileList.size() % uMax == 0 ? 0 :1 );
            for(int ul = 0;ul <ulength ;ul ++ ) {
                List<String> ulMobileList = mobileList.subList(ul * uMax, ((ul + 1) * uMax) > mobileList.size() ? mobileList.size() : ((ul + 1) * uMax));
                String[] split = tempArgs.split(MsgConstant.ParamRegexStr);
                List<String> tempArgsList = Arrays.asList(split);
                String recordId = UUIDGenerator.uuid();//记录的Id提前生成
                ResultMass resultMass = massService.sendMass(recordId,tenantId,appId,subaccountId,key,taskName, tempId, tempArgsList, msg, ulMobileList, sendTime,sendType,cost.toString());
                String mobiles = StringUtils.join(ulMobileList,MsgConstant.NumRegexStr);
                if(resultMass != null && MsgConstant.SUCCESS.equals( resultMass.getResultCode() )&& !MsgConstant.AwaitingTaskId.equals(resultMass.getTaskId())){
                    //存发送记录 一开始发送总数是所有等待的号码
                    MsgSendRecord msgSendRecord = new MsgSendRecord(recordId,key,tenantId,appId,subaccountId,resultMass.getTaskId(),taskName,mobiles,sendType,resultMass.getHandlers(),oprator,msg,
                            tempId,resultMass.getSupplierTempId(),tempArgs,sendTime,cost,true,resultMass.getPendingNum(),resultMass.getPendingNum(),0L,MsgSendRecord.STATE_WAIT,createTime);
                    msgSendRecordService.save(msgSendRecord);
                    List<String> subDetailIds = msgSendDetailService.batchInsertDetail(msgSendRecord, resultMass.getPendingPhones(), MsgSendDetail.STATE_WAIT);
                    //所有明细的Id
                    detailIds.addAll(subDetailIds);
                }
                list.add(resultMass);
            }
            //批量扣费
            ProductCode product = ProductCode.valueOf(sendType);
            this.batchConsumeMsg(new Date(),sendType,cost,product.getRemark(),appId,tenantId,subaccountId,detailIds);
        }
    }

    public String getMsg(String temp,String arg){
        //判断有几个参数替换符
        int count = countStr(temp,MsgConstant.REPLACE_SYMBOL);
        String[] args = arg.split(MsgConstant.ParamRegexStr);
        if(count != args.length){
            return null;
        }else{
            return repStr(temp,MsgConstant.REPLACE_SYMBOL,args);
        }
    }

    /**
     * 判断str1中包含str2的个数
     * @param str1
     * @param str2
     * @return counter
     */
    public int countStr(String str1, String str2) {
        int count = 0;
        while(str1.indexOf(str2) != -1){
            count ++;
            str1 = str1.substring(str1.indexOf(str2) + str2.length());
        }
        return count;
    }

    public String repStr(String temp, String symbol,String[] args) {
        String msg = "";
        int start = 0;
        int end ;
        int index = 0;
        while(temp.indexOf(symbol) != -1){
            end = temp.indexOf(symbol);
            msg += temp.substring(start,end) + args[index];
            temp = temp.substring(end + symbol.length());
            index ++;
        }
        if(temp.length() > 0){
            msg += temp;
        }
        return msg;
    }

    //检查参数格式
    public boolean checkTempArgs(String tempArgs){
        if(StringUtils.isNotEmpty(tempArgs)){
            String[] args = tempArgs.split(MsgConstant.ParamRegexStr);
            for (int i = 0; i < args.length; i++) {
                if(StringUtils.isEmpty( args[i] )){
                    return false;
                }
            }
        }
        return true;
    }

    //检查号码的合法性
    public boolean checkMobile(String mobile){
        Pattern p = Pattern.compile("^1\\d{10}$");
        Matcher matcher = p.matcher(mobile);
        return matcher.matches();
    }


    //判断号码运营商
    public String getOperator(String mobile){
        if(mobile == null || mobile.length() <7){
            return "";
        }
        String operator = telnumLocationService.getOperator(mobile);
        return operator;
    }

    public boolean isSend(String operator,String sendType){
        boolean flag = false;
        if(MsgConstant.ChinaMobile.equals(operator)){//移动号码
            if(MsgConstant.MSG_USSD.equals(sendType)){//闪印
                flag = true;
            }else if(MsgConstant.MSG_SMS.equals(sendType)){//短信
                flag = true;
            }
        }else if(MsgConstant.ChinaUnicom.equals(operator )){//联通号码
            if(MsgConstant.MSG_USSD.equals(sendType)){//闪印
                return false;
            }else if(MsgConstant.MSG_SMS.equals(sendType)){//短信
                flag = true;//
            }
        }
        return flag;
    }

    //检查多个号码的合法性
    public MassMobile vaildMobiles(String mobiles, String sendType) {
        String[] de = mobiles.split(MsgConstant.NumRegexStr);
        if( de.length > MsgConstant.MaxNum ){
            //TODO 抛异常
            throw new RuntimeException("号码超过最大限制");
        }
        Set<String> allNum = new HashSet<>();
        //联通号码
        Set<String> unicom = new HashSet<>();
        //移动号码
        Set<String> mobile = new HashSet<>();
        //电信号码
        Set<String> telecom = new HashSet<>();
        //无效号码
        Set<String> no = new HashSet<>();
        for (int i = 0; i < de.length; i++) {
            String descMobile = de[i].trim();
            allNum.add(descMobile);
            //检验号码的合法性和是否可发送
            if(checkMobile(descMobile)){
                //判断号码运营商
                String operator = telnumLocationService.getOperator(descMobile);
                //判断是否支持发送
                if(isSend( operator , sendType )){
                    if(MsgConstant.ChinaMobile.equals(operator)){
                        mobile.add(descMobile);
                        continue;
                    }else if(MsgConstant.ChinaUnicom.equals(operator)){
                        unicom.add(descMobile);
                        continue;
                    }else if(MsgConstant.ChinaTelecom.equals(operator)){
                        telecom.add(descMobile);
                        continue;
                    }
                }
            }
            no.add(de[i]);
        }
        return new MassMobile(new ArrayList<>(allNum),new ArrayList<>(unicom),new ArrayList<>(mobile),new ArrayList<>(telecom),new ArrayList<>(no));
    }

    /*
        短信字数＝短信模板内容字数 + 签名字数
        短信字数<=70个字数，按照70个字数一条短信计算
        短信字数>70个字数，即为长短信，按照67个字数记为一条短信计算
    */
    public int getCost(String msg,String sendType){
        int cost = 1;
        if(MsgConstant.MSG_SMS.equals(sendType)){
            if( msg.length() > MsgConstant.MaxMsMLength ){
                cost = msg.length() / MsgConstant.MaxOneMsMLength + msg.length() % MsgConstant.MaxOneMsMLength == 0 ? 0 :1;
            }
        }
        return cost;
    }

    //检查发送内容长度
    public boolean checkMsgLength(String msg,String sendType){
        if(MsgConstant.MSG_SMS.equals(sendType)){
            if( msg.length() <= 0 ){
                return false;
            }else{
                return true;
            }
        }else if(MsgConstant.MSG_SMS.equals(sendType)){
            //检验USSD长度
            if(msg.length() > MsgConstant.MaxUssdLength){
                return false;
            }else {
                return true;
            }
        }
        return false;
    }

}
