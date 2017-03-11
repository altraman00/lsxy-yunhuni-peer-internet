package com.lsxy.third.gateway.rest.msg;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.framework.web.utils.WebUtils;
import com.lsxy.msg.api.model.MsgSendDetail;
import com.lsxy.msg.api.service.MsgSendDetailService;
import com.lsxy.msg.api.service.MsgSendRecordService;
import com.lsxy.msg.api.service.MsgUserRequestService;
import com.lsxy.third.gateway.base.AbstractAPIController;
import com.msg.qixuntong.QiXunTongConstant;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxb on 2017/1/12.
 */
@RestController
@RequestMapping("/qixuntong/result/nofity")
public class QiXunTongSendResultNofityController extends AbstractAPIController {
    private static final Logger logger = LoggerFactory.getLogger(QiXunTongSendResultNofityController.class);
    private static final String SUCCESS = "{\"result_code\":\"0\",\"result_desc\":\"Successfully received\"}";
    private static final String FAILED =  "{\"result_code\":\"1\",\"result_desc\":\"Processing failed\"}";

    @Reference(timeout=3000,check = false,lazy = true)
    MsgSendRecordService msgSendRecordService;
    @Reference(timeout=3000,check = false,lazy = true)
    MsgUserRequestService msgUserRequestService;
    @Reference(timeout=3000,check = false,lazy = true)
    MsgSendDetailService msgSendDetailService;

    @RequestMapping(value = "",method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public String handle(HttpServletRequest request){
        String ip = WebUtils.getRemoteAddress(request);
        logger.info("[企讯通][消息发送情况回调接口][请求]["+ip+"][开始]");
        try {
            //{"taskid":"C945316932025847","state":"0","mobile":"17620010850","time":"20170106155935"}
            //[{"mobile":"18607714703","state":"1","taskid":"C2051117068821760","time":"20170119140121"},{"mobile":"18648807841","state":"0","taskid":"C2051117068821760","time":"20170119140120"}]
            String content = getRequestContent(request);
            if(StringUtils.isEmpty(content)){
                logger.error("[企讯通][消息发送情况回调接口][失败][接收参数为空]");
                return FAILED;
            }
            logger.info("[企讯通][消息发送情况回调接口][请求]["+ip+"][内容]:"+content);
            List<Result> list = setResults(content);
            for (int i = 0; i < list.size(); i++) {
                checkOneLog(list.get(i));
            }
        } catch (Exception e) {
            logger.error("[企讯通][消息发送情况回调接口][失败]send request message failed.", e);
            return FAILED;
        }
        logger.info("[企讯通][消息发送情况回调接口][请求]["+ip+"][结束]");
        return SUCCESS;
    }
    //根据回调处理记录
    private void checkOneLog(Result result){
        MsgSendDetail msgSendDetail = msgSendDetailService.findByTaskIdAndMobile(result.getTaskId() , result.getMobile());
        if(msgSendDetail == null){//没有存在的记录
            logger.info("[企讯通][消息发送情况回调接口][请求][没有存在的记录]"+result);
        }else {
            if (isSign()) {
                int state;
                if (QiXunTongConstant.QixuntongStateSuccess == result.getState()) {//发送成功
                    state = MsgSendDetail.STATE_SUCCESS;
                } else {//发送失败
                    state = MsgSendDetail.STATE_FAIL;
                }
                msgSendDetail.setState(state);
                msgSendDetailService.save(msgSendDetail);
                if(MsgSendDetail.STATE_FAIL == state){
                    //TODO 扣费返还
                }
                if(!msgSendDetail.getIsMass()) {
                    try{
                        msgUserRequestService.updateStateByMsgKey(msgSendDetail.getMsgKey(),state);
                        msgSendRecordService.updateStateByMsgKey(msgSendDetail.getMsgKey(),state);
                        logger.info("[企讯通][消息发送情况回调接口][请求][处理成功]" + result + "[更新主记录],msgKey:" + msgSendDetail.getMsgKey());
                    }catch (Exception e){
                        logger.info("[企讯通][消息发送情况回调接口][请求][处理成功]" + result + "[更新主记录异常],msgKey:" + msgSendDetail.getMsgKey());

                    }

                }else {
                    logger.info("[企讯通][消息发送情况回调接口][请求][处理成功]" + result);
                }
            } else {
                logger.error("[企讯通][消息发送情况回调接口][请求][签名失败]"+result);
            }
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
            logger.error("[企讯通][回调]send request message failed.", e);
            throw e;
        }
        return content.toString();
    }
    /**
     * 签名认证
     */
    private boolean isSign(){
        return true;
    }
    //将json数组转换成对象
    private List<Result> setResults(String result){
        List<Result> list = new ArrayList<>();
        try {
            JSONArray jsonArray = JSONArray.fromObject(result);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                list.add( new Result( obj.getString("taskid"),obj.getString("mobile"),obj.getInt("state"),obj.getString("time") ) );
            }
            return list;
        }catch (Exception e){
            logger.error("[企讯通][回调]转换成json数组出错",e);
            throw new RuntimeException("转换成json数组出错");
        }
    }
    class Result{
        //{"taskid":"C945316932025847","state":"0","mobile":"17620010850","time":"20170106155935"}
        private String taskId;
        private String mobile;
        private int state;
        private String time;

        public Result(String taskId, String mobile, int state, String time) {
            this.taskId = taskId;
            this.mobile = mobile;
            this.state = state;
            this.time = time;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }


        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        @Override
        public String toString() {
            return "企讯通回调参数内容{" +
                    "taskId='" + taskId + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", state=" + state +
                    ", time='" + time + '\'' +
                    '}';
        }
    }
}
