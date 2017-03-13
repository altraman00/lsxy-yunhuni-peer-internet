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
import com.lsxy.yunhuni.api.consume.model.Consume;
import com.lsxy.yunhuni.api.consume.service.ConsumeService;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
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
        List<MsgUserRequest> sendOneFails = msgUserRequestService.findSendOneFailAndSendNotOver();
        for(MsgUserRequest request : sendOneFails){
            SupplierSendService sendOneService = supplierSelector.getSendOneService(MsgConstant.ChinaMobile, MsgConstant.MSG_USSD);
            String tempArgs = request.getTempArgs();
            String[] split = tempArgs.split(MsgConstant.ParamRegexStr);
            List<String> tempArgsList = Arrays.asList(split);
            ResultOne resultOne = sendOneService.sendOne(request.getTempId(), tempArgsList, request.getMsg(), request.getMobile(), request.getSendType());
            if(MsgConstant.SUCCESS.equals( resultOne.getResultCode() )) {//重发成功，更新记录
                request.setState(MsgUserRequest.STATE_WAIT);
                msgUserRequestService.save(request);
                MsgSendRecord msgSendRecord = new MsgSendRecord(request.getMsgKey(),request.getTenantId(),request.getAppId(),request.getSubaccountId(),resultOne.getTaskId(),MsgConstant.MSG_USSD,resultOne.getHandlers(),
                        resultOne.getHandlers(),request.getMsg(),request.getTempId(),resultOne.getSupplierTempId(),tempArgs,new Date(),request.getMsgCost());
                msgSendRecordService.save(msgSendRecord);
                MsgSendDetail msgSendDetail = new MsgSendDetail(request.getMsgKey(),request.getTenantId(),request.getAppId(),request.getSubaccountId(),resultOne.getTaskId(),msgSendRecord.getId(),request.getMobile(),request.getMsg(),
                        request.getTempId(),resultOne.getSupplierTempId(),tempArgs,new Date(),request.getMsgCost(),MsgConstant.MSG_USSD,resultOne.getHandlers(),MsgConstant.ChinaMobile);
                msgSendDetailService.save(msgSendDetail);
                //插入消费记录
                if(request.getMsgCost().compareTo(BigDecimal.ZERO) == 1){
                    //插入消费
                    ProductCode productCode = ProductCode.valueOf(request.getSendType());
                    Consume consume = new Consume(request.getCreateTime(),productCode.name(),request.getMsgCost(),productCode.getRemark(),request.getAppId(),request.getTenantId(),msgSendDetail.getId(),request.getSubaccountId());
                    consumeService.consume(consume);
                }
                logger.info("重发成功：requestId:" + request.getId());
            }else{//重发失败，更新失败记录
                logger.info("重发失败：requestId:"+ request.getId()+"结果："+resultOne.toString());
                request.setSendFailTime(request.getSendFailTime() + 1);//失败次数+1
                if(MsgConstant.SEND_FIAL_MAX_NUM <= request.getSendFailTime()){//需要重发
                    //任务标记为失败，进行补扣费
                    //更新泡泡鱼记录
                    request.setState(MsgUserRequest.STATE_FAIL);
                    //查找主记录
                    logger.info("已失败3次，标记任务为失败:requestId:"+ request.getId());
                }
                msgUserRequestService.save(request);
            }
        }
        logger.info("启动失败重发机制执行结束");
    }
}
