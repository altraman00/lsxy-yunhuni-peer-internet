package com.lsxy.msg.supplier.paopaoyu;

import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.msg.supplier.AbstractSupplierSendServiceImpl;
import com.lsxy.msg.supplier.model.PaoPaoYuMassNofity;
import com.lsxy.msg.supplier.model.ResultMass;
import com.lsxy.msg.supplier.model.ResultOne;
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
    public ResultOne ussdSendOne(String tempId, List<String> tempArgs, String msg, String mobile) {
        String tempArgsStr = StringUtils.join(tempArgs, PaoPaoYuConstant.PaoPaoYuParamRegexStr);
        String result =  getPaoPaoYuClient().send( mobile , tempId , tempArgsStr,"0");
        logger.info("调用[泡泡鱼][单发][闪印]结果:"+ result);
        return new PaoPaoYuResultOne(result);
    }

    @Override
    public ResultMass ussdSendMass(String taskName, String tempId, List<String> tempArgs,String msg,  List<String> mobiles, Date sendTime) {
        if(mobiles == null || mobiles.size() == 0){
            //TODO 抛异常
        }
        //TODO 组成字符串
        String mobilesStr = StringUtils.join(mobiles, ",");
        String sendTimeStr = DateUtils.getDate(sendTime,PaoPaoYuConstant.PaoaoyuTimePartten);
        String tempArgsStr = StringUtils.join(tempArgs, PaoPaoYuConstant.PaoPaoYuParamRegexStr);
        String result =  getPaoPaoYuClient().addTask( taskName, tempId, tempArgsStr, mobilesStr, sendTimeStr, "0");
        logger.info("调用[泡泡鱼][群发][闪印]结果:"+ result);
        return new PaoPaoYuResultMass(result,mobilesStr);
    }

    @Override
    public ResultOne smsSendOne(String tempId, List<String> tempArgs, String msg, String mobile) {
        String tempArgsStr = StringUtils.join(tempArgs, PaoPaoYuConstant.PaoPaoYuParamRegexStr);
        String result =  getPaoPaoYuClient().send( mobile , tempId , tempArgsStr,"1");
        logger.info("调用[泡泡鱼][单发][模板短信]结果:"+ result);
        return new PaoPaoYuResultOne(result);
    }

    @Override
    public ResultMass smsSendMass(String taskName, String tempId, List<String> tempArgs,String msg,  List<String> mobiles, Date sendTime) {
        if(mobiles == null || mobiles.size() == 0){
            //TODO 抛异常
        }
        //TODO 组成字符串
        String mobilesStr = StringUtils.join(mobiles, ",");
        String sendTimeStr = DateUtils.getDate(sendTime,PaoPaoYuConstant.PaoaoyuTimePartten);
        String tempArgsStr = StringUtils.join(tempArgs, PaoPaoYuConstant.PaoPaoYuParamRegexStr);
        String result =  getPaoPaoYuClient().addTask( taskName, tempId, tempArgsStr, mobilesStr, sendTimeStr, "1");
        logger.info("调用[泡泡鱼][群发][模板短信]结果:"+ result);
        return new PaoPaoYuResultMass(result,mobilesStr);
    }
}
