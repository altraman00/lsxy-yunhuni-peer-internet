package com.lsxy.msg.supplier.task;

import com.lsxy.msg.api.model.MsgSendDetail;
import com.lsxy.msg.api.model.MsgSendRecord;
import com.lsxy.msg.api.service.MsgSendDetailService;
import com.lsxy.msg.api.service.MsgSendRecordService;
import com.lsxy.msg.api.service.MsgUserRequestService;
import com.lsxy.msg.supplier.SupplierSelector;
import com.lsxy.yunhuni.api.consume.service.ConsumeService;
import com.msg.paopaoyu.PaoPaoYuConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 定时判断企讯通的群发任务是否完成
 * Created by zhangxb on 2017/2/9.
 */
@Component
public class QiXunTongMassTaskLogTask {
    private static final Logger logger = LoggerFactory.getLogger(QiXunTongMassTaskLogTask.class);
    @Autowired
    MsgSendRecordService msgSendRecordService;
    @Autowired
    MsgSendDetailService msgSendDetailService;
    /**
     * 每10分钟，检测截止到当前的一个星期内的检测结果
     */
    @Scheduled(cron="0 0/10 * * * ?")
    public void hour(){
        logger.info("[群发任务][检测开始]");
        List<MsgSendRecord> records = msgSendRecordService.findWaitedSendMassBySupplier(PaoPaoYuConstant.PaopaoyuCode);
        for(MsgSendRecord record : records){
            checkQiXunTongMassTask(record);
        }
        logger.info("[群发任务][检测结束]");
    }

    //检查群发任务下的全部子任务是否成功
    private void checkQiXunTongMassTask(MsgSendRecord record){
        logger.info("[群发任务][检测开启][taskId：" + record.getTaskId() + "]");
        Map result = msgSendDetailService.getStateCountByRecordId(record.getId());
        long succNum = 0;//成功次数
        long failNum = 0;//失败次数
        long pendingNum = 0;//待发送数
        if(result != null){
            succNum = result.get(MsgSendDetail.STATE_SUCCESS) == null? 0L : (Long)result.get(MsgSendDetail.STATE_SUCCESS);
            failNum = result.get(MsgSendDetail.STATE_FAIL) == null? 0L : (Long)result.get(MsgSendDetail.STATE_FAIL);
            pendingNum = result.get(MsgSendDetail.STATE_WAIT) == null? 0L : (Long)result.get(MsgSendDetail.STATE_WAIT);
        }
        if(pendingNum == 0){//没有等待的号码，即任务完成
            //检验号码数是否正确
            if( (record.getSuccNum() +record.getFailNum() +record.getPendingNum()) == (succNum + pendingNum + failNum)) {
                record.setPendingNum(pendingNum);
                record.setFailNum(failNum);
                record.setSuccNum(succNum);
                record.setState(MsgSendRecord.STATE_SUCCESS);
                msgSendRecordService.save(record);
            }else{
                logger.error("[企讯通群发任务][任务id]["+record.getTaskId()+"]参数检验失败：[新数据][succNum="+succNum+" ;pendingNum="+pendingNum+" ;failNum="+failNum+"][旧数据][" +
                        "succNum="+record.getSuccNum() +" ;pendingNum="+record.getPendingNum()+" ;failNum="+record.getFailNum()+"]");
            }
        }else{
            if( (record.getSuccNum() +record.getFailNum() +record.getPendingNum()) == (succNum + pendingNum + failNum)) {
                if(succNum > 0){//有号码成功即是开始处理
                    record.setPendingNum(pendingNum);
                    record.setFailNum(failNum);
                    record.setSuccNum(succNum);
                    msgSendRecordService.save(record);
                }
            }else{
                logger.error("[企讯通群发任务][任务id]["+record.getTaskId()+"]参数检验失败：[新数据][succNum="+succNum+" ;pendingNum="+pendingNum+" ;failNum="+failNum+"][旧数据][" +
                        "succNum="+record.getSuccNum() +" ;pendingNum="+record.getPendingNum()+" ;failNum="+record.getFailNum()+"]");
            }
        }
    }
}
