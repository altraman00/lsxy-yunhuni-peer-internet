package com.lsxy.msg.service;

import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.msg.MsgRequestCompletedEvent;
import com.lsxy.msg.api.model.MsgConstant;
import com.lsxy.msg.api.model.MsgSendDetail;
import com.lsxy.msg.api.model.MsgSendRecord;
import com.lsxy.msg.api.model.MsgUserRequest;
import com.lsxy.msg.api.service.*;
import com.lsxy.msg.supplier.SupplierSelector;
import com.lsxy.msg.supplier.SupplierSendService;
import com.lsxy.msg.supplier.common.ResultOne;
import com.lsxy.msg.supplier.paopaoyu.PaoPaoYuMassNofity;
import com.lsxy.yunhuni.api.consume.service.ConsumeService;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import com.msg.paopaoyu.PaoPaoYuConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by liups on 2017/3/16.
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class MsgTaskServiceImpl implements MsgTaskService{
    private static final Logger logger = LoggerFactory.getLogger(MsgTaskServiceImpl.class);
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
    @Autowired
    MsgSendService msgSendService;
    @Autowired
    MQService mqService;


    /**
     * 每12分钟，检测截止到当前的一个星期内的检测结果
     */

    @Override
    public void massTaskRequestUpdate(){
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
        //群发情况
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
            mqService.publish(new MsgRequestCompletedEvent(request.getMsgKey()));
        }
        request.setPendingNum(pendingNum);
        request.setFailNum(failNum);
        request.setSuccNum(succNum);
        msgUserRequestService.save(request);
    }


    @Override
    public void massTaskRequestOverdueUpdate(){
        logger.info("[群发任务过期][检测开始]");
        List<MsgUserRequest> requests = msgUserRequestService.findAwaitedButOverdueRequets();
        for(MsgUserRequest request : requests){
            updateOverdueRequest(request);
        }
        logger.info("[群发任务过期][检测结束]");
    }

    //查看是否执行完成
    private void updateOverdueRequest(MsgUserRequest request){
        long succNum = 0; //成功次数
        long failNum = 0; //失败次数
        long pendingNum = 0; //待发送数
        Date endTime = new Date();
        //群发情况
        List<MsgSendRecord> records = msgSendRecordService.findByMsgKey(request.getMsgKey());
        for(MsgSendRecord record : records){
            Long recordFailNum = msgSendDetailService.finishOverdueRecordId(record.getId(), endTime);
            record.setFailNum(recordFailNum);
            record.setPendingNum(0L);
            record.setSuccNum(record.getSumNum() - recordFailNum);
            record.setState(MsgSendRecord.STATE_SUCCESS);
            msgSendRecordService.save(record);

            succNum += record.getSuccNum();
            failNum += record.getFailNum();
        }
        request.setState(MsgUserRequest.STATE_SUCCESS);
        request.setPendingNum(pendingNum);
        request.setFailNum(failNum);
        request.setSuccNum(succNum);
        msgUserRequestService.save(request);

        mqService.publish(new MsgRequestCompletedEvent(request.getMsgKey()));
    }

    /**
     * 每小时的10分钟，检测截止到当前的一个星期内的检测结果
     */

    @Override
    public void paoPaoYuMassTaskUpdate(){
        List<MsgSendRecord> records = msgSendRecordService.findWaitedSendMassBySupplier(PaoPaoYuConstant.PaopaoyuCode);
        for(MsgSendRecord record : records){
            paoPaoYuMassTaskRecordUpdate(record);
        }
        logger.info("[泡泡鱼][群发任务][检测结束]");
    }

    //更新单任务的执行情况
    private void paoPaoYuMassTaskRecordUpdate(MsgSendRecord record){
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
                            Date endTime = new Date();
                            List<String> ids = msgSendDetailService.updateStateAndSetEndTimeByRecordIdAndPhones(record.getId(), task.getFailPhoneList(), MsgSendDetail.STATE_FAIL,endTime);
                            msgSendDetailService.updateStateAndSetEndTimeFromWaitedToSuccessByRecordId(record.getId(),endTime);

                            //接口调用成功则不理会，接口调用失败，进行补扣费
                            //处理发送结果
                            if(ids != null && ids.size() > 0){
                                BigDecimal cost = BigDecimal.ZERO.subtract(record.getMsgCost());
                                ProductCode product = ProductCode.valueOf(record.getSendType());
                                msgSendService.batchConsumeMsg(endTime,record.getSendType(),cost,product.getRemark(),record.getAppId(),record.getTenantId(),record.getSubaccountId(),ids);
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


    /**
     * 每分钟，检测发送失败3次以内的闪印信息，并进行重发
     */

    @Override
    public void paoPaoYuUssdReSendTask(){
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
                record.setSendFailNum(record.getSendFailNum() + 1);//失败次数+1
                if(MsgConstant.SEND_FIAL_MAX_NUM <= record.getSendFailNum()){//需要重发
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


    /**
     * 每10分钟，检测截止到当前的一个星期内的检测结果
     */

    @Override
    public void qiXunTongMassTaskUpdate(){
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
