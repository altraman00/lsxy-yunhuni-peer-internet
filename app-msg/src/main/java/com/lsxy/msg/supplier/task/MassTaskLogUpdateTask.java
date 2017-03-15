package com.lsxy.msg.supplier.task;

/**
 * Created by zhangxb on 2017/2/9.
 */

import com.lsxy.msg.api.model.MsgSendRecord;
import com.lsxy.msg.api.model.MsgUserRequest;
import com.lsxy.msg.api.service.MsgSendDetailService;
import com.lsxy.msg.api.service.MsgSendRecordService;
import com.lsxy.msg.api.service.MsgUserRequestService;
import com.lsxy.msg.supplier.SupplierSelector;
import com.lsxy.yunhuni.api.consume.service.ConsumeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时检测群发任务是否完成
 * Created by liups on 2016/7/27.
 */
@Component
public class MassTaskLogUpdateTask {
    private static final Logger logger = LoggerFactory.getLogger(MassTaskLogUpdateTask.class);
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
     * 每12分钟，检测截止到当前的一个星期内的检测结果
     */
    @Scheduled(cron="0 0/12 * * * ?")
    public void hour(){
        logger.info("[群发任务][检测开始]");
        List<MsgUserRequest> requests = msgUserRequestService.findAwaitedRequets();
        for(MsgUserRequest request : requests){
            updateRequest(request);
        }
        logger.info("[群发任务][检测结束]");
    }

    //查看是否执行完成
    private void updateRequest(MsgUserRequest request){
        long succNum = 0; //成功次数
        long failNum = 0; //失败次数
        long pendingNum = 0; //待发送数
        //泡泡鱼的群发情况
        List<MsgSendRecord> records = msgSendRecordService.findByMsgKey(request.getMsgKey());
        int state = MsgUserRequest.STATE_FAIL;
        boolean flag = true;
        for(MsgSendRecord record : records){
            if(MsgSendRecord.STATE_SUCCESS != record.getState() && MsgSendRecord.STATE_FAIL != record.getState()){
                flag = false;
            }
            if(MsgSendRecord.STATE_SUCCESS == record.getState()){
                state = MsgUserRequest.STATE_SUCCESS; //只要有一条成功，就算成功
            }
            succNum += record.getSuccNum();
            failNum += record.getFailNum();
            pendingNum += record.getPendingNum();
        }
        if(flag){//任务完成
            request.setState(state);
            msgSendDetailService.setEndTimeByMsgKey(request.getMsgKey());
        }
        request.setPendingNum(pendingNum);
        request.setFailNum(failNum);
        request.setSuccNum(succNum);
        msgUserRequestService.save(request);
    }

}
