package com.lsxy.msg.supplier.task;

import com.lsxy.msg.api.model.MsgSendDetail;
import com.lsxy.msg.api.model.MsgSendRecord;
import com.lsxy.msg.api.service.MsgSendDetailService;
import com.lsxy.msg.api.service.MsgSendRecordService;
import com.lsxy.msg.api.service.MsgSendService;
import com.lsxy.msg.supplier.SupplierSelector;
import com.lsxy.msg.supplier.SupplierSendService;
import com.lsxy.msg.supplier.paopaoyu.PaoPaoYuMassNofity;
import com.lsxy.yunhuni.api.consume.service.ConsumeService;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import com.msg.paopaoyu.PaoPaoYuConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 定时检测泡泡鱼任务是否成功
 * Created by liups on 2016/7/27.
 */
@Component
public class PaoPaoYuMassTaskLogTask {
    private static final Logger logger = LoggerFactory.getLogger(PaoPaoYuMassTaskLogTask.class);

    @Autowired
    MsgSendRecordService msgSendRecordService;
    @Autowired
    MsgSendDetailService msgSendDetailService;
    @Autowired
    SupplierSelector supplierSelector;
    @Autowired
    MsgSendService msgSendService;

    /**
     * 每小时的10分钟，检测截止到当前的一个星期内的检测结果
     */
    @Scheduled(cron="0 0/10 * * * ?")
    public void hour(){
        List<MsgSendRecord> records = msgSendRecordService.findWaitedSendMassBySupplier(PaoPaoYuConstant.PaopaoyuCode);
        for(MsgSendRecord record : records){
            selectTask(record);
        }
        logger.info("[泡泡鱼][群发任务][检测结束]");
    }

    //更新单任务的执行情况
    private void  selectTask(MsgSendRecord record){
        try {
            SupplierSendService sendMassService = supplierSelector.getSendMassService(record.getOperator(), record.getSupplierCode());
            PaoPaoYuMassNofity nofity = (PaoPaoYuMassNofity) sendMassService.getTask(record.getTaskId());
            //调用查询结果成功，并且任务状态是完成。
            if( PaoPaoYuMassNofity.sueccess.equals( nofity.getResultCode() ) ){
                PaoPaoYuMassNofity.Task task = nofity.getTask();
                if( PaoPaoYuMassNofity.state_sueccess == task.getState()) {//群发任务结束
                    if(task.getPendingNum() == 0){//当前没有未发送的
                        //校验数据合理性
                        if( record.getPendingNum() == ( task.getSendSuccNum() + task.getSendFailNum() )){
                            //更新结果
                            if(record.getPendingNum() == task.getSendFailNum()){
                                record.setState(MsgSendRecord.STATE_FAIL);
                            }else{
                                record.setState(MsgSendRecord.STATE_SUCCESS);
                            }
                            record.setFailNum(record.getFailNum() + task.getSendFailNum());
                            record.setFailNum(record.getSuccNum() + task.getSendSuccNum());
                            record.setFailNum(task.getPendingNum() + 0L);
                            msgSendRecordService.save(record);
                            List<String> ids = msgSendDetailService.updateStateByRecordIdAndPhones(record.getId(), task.getFailPhoneList(), MsgSendDetail.STATE_FAIL);
                            msgSendDetailService.updateStateFromWaitedToSuccessByRecordId(record.getId());

                            //接口调用成功则不理会，接口调用失败，进行补扣费
                            //处理发送结果
                            if(ids != null && ids.size() > 0){
                                BigDecimal cost = BigDecimal.ZERO.subtract(record.getMsgCost());
                                ProductCode product = ProductCode.valueOf(record.getSendType());
                                msgSendService.batchConsumeMsg(new Date(),record.getSendType(),cost,product.getRemark(),record.getAppId(),record.getTenantId(),record.getSubaccountId(),ids);
                            }
                        }else{
                            logger.error("[校验][泡泡鱼][群发事件结果是否合理][不合理][期待结果值："+ record.getPendingNum() +"][实际结果值：(succ)"+task.getSendSuccNum() +
                                    "(fail)"+task.getSendFailNum()+"(pending)"+task.getPendingNum()+"]");
                        }
                    }else{
                        //TODO 实际结果不符合正常逻辑 应当设置报警！
                        logger.error("[校验][泡泡鱼][群发事件结果是否合理][不合理][任务状态已结束但还有未发送号码(pending)"+task.getPendingNum()+"]");
                    }
                }else{
                    logger.info("检测任务尚未完成");
                }
            }
        } catch (Exception e) {
            logger.error("[泡泡鱼][群发任务]["+record.getTaskId()+"]校验失败,原因:",e);
        }
    }
}
