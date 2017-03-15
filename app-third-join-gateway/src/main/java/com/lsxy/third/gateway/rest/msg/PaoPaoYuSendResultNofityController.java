package com.lsxy.third.gateway.rest.msg;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.framework.web.utils.WebUtils;
import com.lsxy.msg.api.model.MsgConstant;
import com.lsxy.msg.api.model.MsgSendRecord;
import com.lsxy.msg.api.service.MsgSendDetailService;
import com.lsxy.msg.api.service.MsgSendRecordService;
import com.lsxy.msg.api.service.MsgUserRequestService;
import com.lsxy.third.gateway.base.AbstractAPIController;
import com.lsxy.yunhuni.api.consume.model.Consume;
import com.lsxy.yunhuni.api.consume.service.ConsumeService;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import com.msg.paopaoyu.PaoPaoYuClient;
import com.msg.paopaoyu.PaoPaoYuConstant;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxb on 2016/12/8.
 */
@RestController
@RequestMapping("/sendResultNofity")
public class PaoPaoYuSendResultNofityController extends AbstractAPIController {
    private static final Logger logger = LoggerFactory.getLogger(PaoPaoYuSendResultNofityController.class);
    private static final String SUCCESS = "{\"result_code\":\"0\",\"result_desc\":\"Successfully received\"}";
    private static final String FAILED =  "{\"result_code\":\"1\",\"result_desc\":\"Processing failed\"}";

    @Reference(timeout=3000,check = false,lazy = true)
    MsgSendRecordService msgSendRecordService;
    @Reference(timeout=3000,check = false,lazy = true)
    MsgUserRequestService msgUserRequestService;
    @Reference(timeout=3000,check = false,lazy = true)
    MsgSendDetailService msgSendDetailService;
    @Autowired
    ConsumeService consumeService;

    @RequestMapping(value = "",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String handle(HttpServletRequest request){
        String ip = WebUtils.getRemoteAddress(request);
        String serialId ;
        String taskId ;
        String sign ;
        String phone ;
        String status ;
        String msg ;
        try {
            //{"serialId":"20161216135926376","taskId":"4a9f25750b03489fa92ba0ebb758d450","sign":"6f931b3d19fa0e7233555cafab7c5d30","phone":"13570238835","status":"0","msg":"The USSD_delivery succeed"}
            String content = getRequestContent(request);
            Map map = JSONUtil.parseObject(content);
            serialId = (String)map.get("serialId");
            taskId = (String)map.get("taskId");
            sign = (String)map.get("sign");
            phone = (String)map.get("phone");
            status = (String)map.get("status");
            msg = (String)map.get("msg");
        } catch (IOException e) {
            logger.error("send request message failed.", e);
            return FAILED;
        }
        logger.info("[泡泡鱼][消息发送情况回调接口][请求]["+ip+"][参数：serialId="+serialId+", taskId="+taskId+", sign="+sign+", phone="+phone+", status="+status+", msg="+msg+"]");
       //[参数：serialId=20161219171522308, taskId=88a7d07b1f9048e895aa092c15a92322, sign=d7b455e3d76a048e6020e663ce9c31c4, phone=18826474526, status=0, msg=The USSD_delivery succeed]
        if(StringUtils.isEmpty(serialId)||StringUtils.isEmpty(taskId)||
                StringUtils.isEmpty(sign)||StringUtils.isEmpty(phone)||StringUtils.isEmpty(status)
                ){
            logger.info("[泡泡鱼][消息发送情况回调接口][请求]["+ip+"][参数不符合要求]");
            return FAILED;
        }
        MsgSendRecord msgSendRecord = msgSendRecordService.findByTaskId(taskId);
        if(msgSendRecord == null){//没有存在的记录
            logger.info("[泡泡鱼][消息发送情况回调接口][请求]["+ip+"][没有存在的记录]");
            return FAILED;
        }
        boolean flag = isSign( serialId, taskId, sign);
        if(flag) {
            if (PaoPaoYuConstant.PapPaoyuStateSuccess.equals(status) || PaoPaoYuConstant.PapPaoyuStateFail.equals(status)) {
                boolean isUpdateMain = true;
                if(PaoPaoYuConstant.PapPaoyuStateFail.equals(status)){//发送失败检查是否需要重发
                    //检查是否需要重发，更新泡泡鱼记录
                    if(MsgConstant.MSG_USSD.equals(msgSendRecord.getSendType()) && !msgSendRecord.getIsMass() && MsgConstant.SEND_FIAL_MAX_NUM > msgSendRecord.getSendFailTime() ){//需要重发
                        int failTime = msgSendRecord.getSendFailTime() == null ? 0 : msgSendRecord.getSendFailTime();
                        msgSendRecord.setSendFailTime( failTime + 1 );//失败次数+1
                        msgSendRecordService.save(msgSendRecord);//更新泡泡鱼记录
                        logger.info("[泡泡鱼][消息发送情况回调接口][等待重发]任务："+msgSendRecord.getTaskId());
                        isUpdateMain = false;
                    }else{//不需要重发
                        isUpdateMain = true;
                    }
                }else{//发送成功更新
                    isUpdateMain = true;
                }
                if(isUpdateMain) {
                    int state;
                    if(PaoPaoYuConstant.PapPaoyuStateSuccess.equals(status)){
                        state = MsgSendRecord.STATE_SUCCESS;
                    }else{
                        state = MsgSendRecord.STATE_FAIL;
                    }
                    //更新泡泡鱼记录
                    msgSendRecord.setState(state);
                    msgSendRecordService.save(msgSendRecord);
                    msgUserRequestService.updateStateByMsgKey(msgSendRecord.getMsgKey(),state);
                    String mobiles = msgSendRecord.getMobiles();
                    List<String> mobileList = Arrays.asList(mobiles.split(MsgConstant.NumRegexStr));
                    List<String> detailIds = msgSendDetailService.updateStateAndTaskIdByRecordIdAndPhones(msgSendRecord.getId(), mobileList, state, msgSendRecord.getTaskId());
                    //查找主记录
                    if (state == MsgSendRecord.STATE_FAIL) {//发送失败
                        // 返还扣费
                        //插入消费
                        for(String detailId : detailIds){
                            ProductCode productCode = ProductCode.valueOf(msgSendRecord.getSendType());
                            BigDecimal cost = BigDecimal.ZERO.subtract(msgSendRecord.getMsgCost());
                            Consume consume = new Consume(new Date(),productCode.name(),cost,productCode.getRemark(),msgSendRecord.getAppId(),msgSendRecord.getTenantId(),detailId,msgSendRecord.getSubaccountId());
                            consumeService.consume(consume);
                        }
                    }
                }
                logger.info("[泡泡鱼][消息发送情况回调接口][请求]["+ip+"][处理成功]");
                return SUCCESS;
            } else {//状态类型失败
                logger.info("[泡泡鱼][消息发送情况回调接口][请求]["+ip+"][状态类型失败]");
                return FAILED;
            }
        }else{//签名检验失败
            logger.info("[泡泡鱼][消息发送情况回调接口][请求]["+ip+"][签名失败]");
            return FAILED;
        }
    }
    /**解析参数*/
    private String getRequestContent(HttpServletRequest req) throws IOException {
        StringBuilder content = new StringBuilder(req.getContentLength());
        BufferedReader read = req.getReader();
        try {
            String line = null;
            while ((line = read.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            logger.error("[泡泡鱼]send request message failed.", e);
            throw e;
        }

        return content.toString();
    }
    /**
     * 签名认证
     */
    private boolean isSign(String serialId,String taskId,String sign){
        String sign1 = DigestUtils.md5Hex(serialId + "|" + PaoPaoYuClient.APP_SECRET + "|" + taskId);
        if(sign1.equals(sign)){
            return true;
        }else{
            return false;
        }
    }
}
