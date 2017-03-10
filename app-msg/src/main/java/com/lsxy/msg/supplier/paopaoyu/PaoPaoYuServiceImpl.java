package com.lsxy.msg.supplier.paopaoyu;

import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.msg.supplier.AbstractSupplierSendServiceImpl;
import com.lsxy.msg.supplier.common.MsgConstant;
import com.lsxy.msg.supplier.common.PaoPaoYuMassNofity;
import com.lsxy.msg.supplier.common.ResultMass;
import com.lsxy.msg.supplier.common.ResultOne;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by zhangxb on 2017/1/10.
 */
@Service("paoPaoYuService")
public class PaoPaoYuServiceImpl extends AbstractSupplierSendServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(PaoPaoYuServiceImpl.class);
    private PaoPaoYuClient paoPaoYuClient;

    public PaoPaoYuClient getPaoPaoYuClient(){
        if(paoPaoYuClient == null){
            synchronized(PaoPaoYuServiceImpl.class){
                if(paoPaoYuClient == null){
                    paoPaoYuClient = new PaoPaoYuClient();
                }
            }
        }
        return paoPaoYuClient;
    }

    @Override
    public PaoPaoYuMassNofity getTask(String taskId) {
        String result = getPaoPaoYuClient().getTask(taskId);
        logger.info("调用[泡泡鱼][查询群发]结果:"+ result);
        return new PaoPaoYuMassNofity(result);
    }

    @Override
    public int getMaxSendNum() {
        return PaoPaoYuConstant.PaoPaoYuMaxNum;
    }

    @Override
    public ResultOne sendOne(String tempId, List<String> tempArgs, String msg, String mobile,String sendType) {
        if(MsgConstant.MSG_USSD.equals(sendType)){
            String supplierTempId = getSupplierTempId(tempId, PaoPaoYuConstant.PaopaoyuCode);
            String tempArgsStr = StringUtils.join(tempArgs, PaoPaoYuConstant.PaoPaoYuParamRegexStr);
            String result =  getPaoPaoYuClient().send( mobile , supplierTempId , tempArgsStr,"0");
            logger.info("调用[泡泡鱼][单发][闪印]结果:"+ result);
            return new PaoPaoYuResultOne(result,supplierTempId);
        }else if(MsgConstant.MSG_SMS.equals(sendType)){
            String supplierTempId = getSupplierTempId(tempId, PaoPaoYuConstant.PaopaoyuCode);
            String tempArgsStr = StringUtils.join(tempArgs, PaoPaoYuConstant.PaoPaoYuParamRegexStr);
            String result =  getPaoPaoYuClient().send( mobile , supplierTempId , tempArgsStr,"1");
            logger.info("调用[泡泡鱼][单发][模板短信]结果:"+ result);
            return new PaoPaoYuResultOne(result,supplierTempId);
        }else{
            return null;
        }
    }

    @Override
    public ResultMass sendMass(String tenantId,String appId,String subaccountId,String msgKey ,String taskName, String tempId, List<String> tempArgs,String msg,  List<String> mobiles, Date sendTime,String sendType,String cost) {
        if(MsgConstant.MSG_USSD.equals(sendType)){
            if(mobiles == null || mobiles.size() == 0){
                //TODO 抛异常
            }
            String supplierTempId = getSupplierTempId(tempId, PaoPaoYuConstant.PaopaoyuCode);
            String mobilesStr = StringUtils.join(mobiles, PaoPaoYuConstant.PaoPaoYuNumRegexStr);
            String sendTimeStr = DateUtils.getDate(sendTime,PaoPaoYuConstant.PaoaoyuTimePartten);
            String tempArgsStr = StringUtils.join(tempArgs, PaoPaoYuConstant.PaoPaoYuParamRegexStr);
            String result =  getPaoPaoYuClient().addTask( taskName, supplierTempId, tempArgsStr, mobilesStr, sendTimeStr, "0");
            logger.info("调用[泡泡鱼][群发][闪印]结果:"+ result);
            return new PaoPaoYuResultMass(result,mobilesStr,supplierTempId);
        }else if(MsgConstant.MSG_SMS.equals(sendType)){
            if(mobiles == null || mobiles.size() == 0){
                //TODO 抛异常
            }
            String supplierTempId = getSupplierTempId(tempId, PaoPaoYuConstant.PaopaoyuCode);
            String mobilesStr = StringUtils.join(mobiles, PaoPaoYuConstant.PaoPaoYuNumRegexStr);
            String sendTimeStr = DateUtils.getDate(sendTime,PaoPaoYuConstant.PaoaoyuTimePartten);
            String tempArgsStr = StringUtils.join(tempArgs, PaoPaoYuConstant.PaoPaoYuParamRegexStr);
            String result =  getPaoPaoYuClient().addTask( taskName, supplierTempId, tempArgsStr, mobilesStr, sendTimeStr, "1");
            logger.info("调用[泡泡鱼][群发][模板短信]结果:"+ result);
            return new PaoPaoYuResultMass(result,mobilesStr,supplierTempId);
        }else{
            return null;
        }

    }

}
