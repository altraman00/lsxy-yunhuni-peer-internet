package com.lsxy.msg.supplier.qixuntong;

import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.msg.mq.DelaySendMassEvent;
import com.lsxy.msg.supplier.AbstractSupplierSendServiceImpl;
import com.lsxy.msg.api.model.MsgConstant;
import com.lsxy.msg.supplier.common.ResultMass;
import com.lsxy.msg.supplier.common.ResultOne;
import com.msg.qixuntong.QiXinTongClient;
import com.msg.qixuntong.QiXunTongConstant;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by zhangxb on 2017/1/10.
 */
@Service("qiXunTongService")
public class QiXunTongServiceImpl extends AbstractSupplierSendServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(QiXunTongServiceImpl.class);

    private QiXinTongClient qiXinTongClient;

    @Autowired
    private MQService mqService;

    public QiXinTongClient getQiXinTongClient(){
        if(qiXinTongClient == null){
            synchronized(QiXunTongServiceImpl.class){
                if(qiXinTongClient == null){
                    qiXinTongClient = new QiXinTongClient();
                }
            }

        }
        return qiXinTongClient;
    }

    @Override
    public ResultOne sendOne(String tempId, List<String> tempArgs, String msg, String mobile,String sendType,String msgKey) {
        if(MsgConstant.MSG_SMS.equals(sendType)){
            String result = getQiXinTongClient().sendMSM("", msg ,  mobile);
            logger.info("调用[企信通][单发][模板短信]结果:"+ result);
            return new QiXunTongResultOne(result);
        }else{
            return null;
        }
    }

    @Override
    public ResultMass sendMass(String recordId,String tenantId,String appId,String subaccountId,String msgKey ,String taskName, String tempId, List<String> tempArgs,String msg,  List<String> mobiles, Date sendTime,String sendType,String cost) {
        if(MsgConstant.MSG_SMS.equals(sendType)){
            if(mobiles == null || mobiles.size() == 0){
                //TODO 抛异常
            }
            long delay = sendTime.getTime() - new Date().getTime();
            if(delay <= 0){
                String mobilesStr = StringUtils.join(mobiles, QiXunTongConstant.QiXunTongNumRegexStr);
                String result = getQiXinTongClient().sendMSM(taskName, msg ,  mobilesStr);
                logger.info("调用[企信通][群发][模板短信]结果:"+ result);
                return new QiXunTongResultMass(result,mobilesStr);
            }else{
                String mobilesStr = StringUtils.join(mobiles, MsgConstant.NumRegexStr);
                String  sendTimeStr = DateUtils.getDate(sendTime,MsgConstant.TimePartten);
                String tempArgsStr = StringUtils.join(tempArgs, MsgConstant.ParamRegexStr);
                //发布定时任务
                mqService.publish( new DelaySendMassEvent( delay, tenantId,appId,subaccountId,recordId, msgKey,taskName,tempId,  tempArgsStr, mobilesStr, sendTimeStr, msg,  MsgConstant.MSG_SMS, MsgConstant.ChinaUnicom,cost));
                //先按发送成功的计算
                ResultMass resultMass = new QiXunTongResultMass("{\"resultcode\":0,\"resultmsg\":\"成功\",\"taskid\":\"" + MsgConstant.AwaitingTaskId + "\"}",mobilesStr);
                return resultMass;
            }
        }else{
            return null;
        }

    }

    @Override
    public int getMaxSendNum() {
        return QiXunTongConstant.QiXunTongMaxNum;
    }

}
