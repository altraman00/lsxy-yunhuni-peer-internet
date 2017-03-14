package com.lsxy.msg.supplier.task;

import com.lsxy.msg.api.model.MsgConstant;
import com.lsxy.msg.api.model.MsgSendDetail;
import com.lsxy.msg.api.model.MsgSendRecord;
import com.lsxy.msg.api.model.MsgUserRequest;
import com.lsxy.msg.api.service.MsgSendDetailService;
import com.lsxy.msg.api.service.MsgSendRecordService;
import com.lsxy.msg.api.service.MsgUserRequestService;
import com.lsxy.msg.supplier.SupplierSelector;
import com.lsxy.msg.supplier.SupplierSendService;
import com.lsxy.msg.supplier.common.ResultOne;
import com.lsxy.yunhuni.api.consume.service.ConsumeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
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
    @Autowired
    SupplierSelector supplierSelector;
    @Autowired
    ConsumeService consumeService;

    /**
     * 每分钟，检测发送失败3次以内的闪印信息，并进行重发
     */
    @Scheduled(cron="0 0/1 * * * ?")
    public void minute(){
        logger.info("启动闪印失败重发机制");
        List<MsgSendRecord> sendOneFails = msgSendRecordService.findUssdSendOneFailAndSendNotOver();
        for(MsgSendRecord record : sendOneFails){
            SupplierSendService sendOneService = supplierSelector.getSendOneService(MsgConstant.ChinaMobile, MsgConstant.MSG_USSD);
            String tempArgs = record.getTempArgs();
            String[] split = tempArgs.split(MsgConstant.ParamRegexStr);
            List<String> tempArgsList = Arrays.asList(split);
            ResultOne resultOne = sendOneService.sendOne(record.getTempId(), tempArgsList, record.getMsg(), record.getMobiles(), record.getSendType());
            if(MsgConstant.SUCCESS.equals( resultOne.getResultCode() )) {//重发成功，更新记录
                MsgSendDetail detail = msgSendDetailService.findByTaskIdAndMobile(record.getTaskId(), record.getMobiles());
                detail.setTaskId(resultOne.getTaskId());
                msgSendDetailService.save(detail);

                record.setTaskId(resultOne.getTaskId());
                record.setState(MsgUserRequest.STATE_WAIT);
                msgSendRecordService.save(record);
                logger.info("重发成功：requestId:" + record.getId());
            }else{//重发失败，更新失败记录
                logger.info("重发失败：requestId:"+ record.getId()+"结果："+resultOne.toString());
                record.setSendFailTime(record.getSendFailTime() + 1);//失败次数+1
                if(MsgConstant.SEND_FIAL_MAX_NUM <= record.getSendFailTime()){//需要重发
                    //任务标记为失败，进行补扣费
                    //更新泡泡鱼记录
                    record.setState(MsgUserRequest.STATE_FAIL);
                    //查找主记录
                    logger.info("已失败3次，标记任务为失败:requestId:"+ record.getId());
                }
                msgSendRecordService.save(record);
            }
        }
        logger.info("启动失败重发机制执行结束");
    }
}
