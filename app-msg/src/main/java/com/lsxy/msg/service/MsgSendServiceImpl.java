package com.lsxy.msg.service;

import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.msg.api.model.MsgTemplate;
import com.lsxy.msg.api.service.MsgSendService;
import com.lsxy.msg.api.service.MsgTemplateService;
import com.lsxy.msg.supplier.SupplierSelector;
import com.lsxy.msg.supplier.SupplierSendService;
import com.lsxy.msg.supplier.model.MsgConstant;
import com.lsxy.msg.supplier.model.ResultCode;
import com.lsxy.msg.supplier.model.ResultOne;
import com.lsxy.yunhuni.api.config.service.TelnumLocationService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
    @Override
    public String sendUssd(String appId,String accountId,String mobile, String tempId, String tempArgw) {
        return null;
    }

    @Override
    public String sendMassUssd(String appId,String accountId,String taskName, String tempId, String tempIdArgs, String mobiles, Date sendTime) {
        return null;
    }

    @Override
    public String sendSms(String appId,String accountId,String mobile, String tempId, String tempArgs) {
        tempId = tempId.trim();
        tempArgs = tempArgs.trim();
        MsgTemplate temp = msgTemplateService.findByTempId(appId, accountId, tempId, true);
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
        //检验号码的合法性
        mobile = mobile.trim();
        if(!checkMobile(mobile)){
            //TODO 抛异常
            return ResultCode.ERROR_20001.toString();
        }
        //判断号码运营商
        String operator = getOperator(mobile);
        //判断是否支持发送
        if(!isSend( operator , MsgConstant.MSG_SMS )){
            //TODO 抛异常
            return ResultCode.ERROR_20009.toString();
        }
        //TODO 判断余额是否可以发送
        int cost = 1;

        //生成任务标识，发送
        String key = UUIDGenerator.uuid();
        //开始发送
        ResultOne resultOne = null;
        SupplierSendService smsSendOneService = supplierSelector.getSmsSendOneService(operator);

        String[] split = tempArgs.split(MsgConstant.ParamRegexStr);
        List<String> tempArgsList = Arrays.asList(split);
        resultOne = smsSendOneService.smsSendOne(tempId, tempArgsList, msg, mobile);

        if(resultOne == null){
            //TODO 抛异常
            resultOne = new ResultOne(ResultCode.ERROR_20008);
        }

        //处理发送结果
        if(MsgConstant.SUCCESS.equals( resultOne.getResultCode() )) {
            //TODO 获取余额，扣费
            //TODO 插入数据库
        }
        logger.info("发送器："+resultOne.getHandlers()+"|发送类型：单发短信|手机号码："+mobile+"|模板id："+tempId+"|模板参数："+tempArgs+"|短信内容："+msg+"|发送结果："+resultOne.toString2());
        return resultOne.toString();
    }

    @Override
    public String sendMassSms(String appId,String accountId,String taskName, String tempId, String tempIdArgs, String mobiles, Date sendTime) {
        return null;
    }

    public static String getMsg(String temp,String arg){
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
    public static int countStr(String str1, String str2) {
        int count = 0;
        while(str1.indexOf(str2) != -1){
            count ++;
            str1 = str1.substring(str1.indexOf(str2) + str2.length());
        }
        return count;
    }

    public static String repStr(String temp, String symbol,String[] args) {
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

}
