package com.lsxy.msg.supplier.task;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.uusd.message.model.base.BaseResult;
import com.lsxy.app.uusd.message.model.paopaoyu.PaoPaoYuResultOne;
import com.lsxy.msg.api.model.MsgSendRecord;
import com.lsxy.msg.api.service.MsgSendDetailService;
import com.lsxy.msg.api.service.MsgSendRecordService;
import com.lsxy.msg.api.service.MsgUserRequestService;
import com.lsxy.yunhuni.api.ussd.model.OneLog;
import com.lsxy.yunhuni.api.ussd.model.PaoPaoYuOneLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zhangxb on 2017/2/23.
 */
@Component
public class PaoPaoYuUssdReSendTask {
    private static final Logger logger = LoggerFactory.getLogger(PaoPaoYuUssdReSendTask.class);

    @Autowired
    MsgSendRecordService msgSendRecordService;
    @Autowired
    MsgUserRequestService msgUserRequestService;
    @Autowired
    MsgSendDetailService msgSendDetailService;

    /**
     * 每分钟，检测发送失败3次以内的闪印信息，并进行重发
     */
    @Scheduled(cron="0 0/1 * * * ?")
    public void minute(){
        logger.info("启动闪印失败重发机制");
        List<MsgSendRecord> records = msgSendRecordService.findSendOneWait();
        List<PaoPaoYuOneLog> list = paoPaoYuOneLogService.getListBySendFail();
        for (int i = 0; i < list.size(); i++) {
            PaoPaoYuOneLog paoPaoYuOneLog = list.get(i);
            PaoPaoYuResultOne resultOne = paoPaoYuService.sendTempUssd(paoPaoYuOneLog.getMobile(),paoPaoYuOneLog.getTempId(),paoPaoYuOneLog.getTempArgs());
            if(BaseResult.SUCCESS.equals( resultOne.getResultCode() )) {//重发成功，更新记录
                paoPaoYuOneLog.setTaskId( resultOne.getTaskId() );
                paoPaoYuOneLog = paoPaoYuOneLogService.save(paoPaoYuOneLog);//更新泡泡鱼记录
                logger.info("重发成功："+paoPaoYuOneLog.toString());
            }else{//重发失败，更新失败记录
                logger.info("重发失败："+paoPaoYuOneLog.toString()+"结果："+resultOne.toString());
                paoPaoYuOneLog.setSendFailNum(paoPaoYuOneLog.getSendFailNum() + 1);//失败次数+1
                paoPaoYuOneLog = paoPaoYuOneLogService.save(paoPaoYuOneLog);//更新泡泡鱼记录
                OneLog oneLog = oneLogService.findByKey(paoPaoYuOneLog.getMsgKey());
                if (oneLog != null) {
                    oneLog.setSendFailNum(paoPaoYuOneLog.getSendFailNum());
                    oneLogService.save(oneLog);
                }
                if(BaseResult.SEND_FIAL_MAX_NUM > paoPaoYuOneLog.getSendFailNum() ) {//需要重发
                    //等待下次定时任务扫描
                    logger.info("等待下次重发:"+paoPaoYuOneLog.toString());
                }else{//不需要重发
                    //任务标记为失败，进行补扣费
                    //更新泡泡鱼记录
                    paoPaoYuOneLog.setState(OneLog.fail);
                    paoPaoYuOneLogService.save(paoPaoYuOneLog);
                    //查找主记录
                    if (oneLog != null) {
                        oneLog.setState(OneLog.fail);
                        oneLogService.save(oneLog);
                    }
                    //扣费
                    int realCost = 1 * oneLog.getMsgCost();
                    costBillingService.costFail(paoPaoYuOneLog.getUserId(), paoPaoYuOneLog.getUserName(), paoPaoYuOneLog.getMsgKey(), paoPaoYuOneLog.getSendType(), realCost);
                    logger.info("已失败3次，标记任务为失败:"+paoPaoYuOneLog.toString());
                }
            }
        }
        logger.info("启动失败重发机制执行结束");
    }
}
