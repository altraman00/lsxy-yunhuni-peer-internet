package com.lsxy.app.oc.rest.message;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.msg.api.service.MsgTemplateService;
import com.lsxy.yunhuni.api.message.model.AccountMessage;
import com.lsxy.framework.api.message.model.Message;
import com.lsxy.yunhuni.api.message.service.AccountMessageService;
import com.lsxy.framework.api.message.service.MessageService;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.web.rest.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxb on 2016/8/10.
 */
@Api(value = "消息中心")
@RequestMapping("/message")
@RestController
public class MessageController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    MessageService messageService;
    @Autowired
    AccountService accountService;
    @Autowired
    AccountMessageService accountMessageService;
    @Reference(timeout=3000,check = false,lazy = true)
    private MsgTemplateService msgTemplateService;
    /**
     *  根据日期和类型查询消息列表信息
     * @param type 0用户消息 1活动消息
     * @param status 类型 0未上线 1上线 -1下线
     * @param startTime 开始时间 yyyy-MM-dd
     * @param endTime 结束时间
     * @param pageNo 第几页
     * @param pageSize 每页几条数据
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ApiOperation(value = "根据日期和类型查询消息列表信息")
    public RestResponse pageList(
            @ApiParam(name = "type",value = "0用户消息 1活动消息,默认1")
            @RequestParam(defaultValue = "1",required=false)Integer type,
            @ApiParam(name = "status",value = "类型 0未上线 1上线 -1下线")
            @RequestParam(required = false)Integer status,
            @ApiParam(name = "startTime",value = "yyyy-MM-dd")
            @RequestParam(required=false)String startTime,
            @ApiParam(name = "endTime",value = "yyyy-MM-dd")
            @RequestParam(required=false)String endTime,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "20")Integer pageSize){
        Page page = messageService.pageList(type,status, startTime, endTime, pageNo, pageSize);
        RestResponse restResponse = RestResponse.success(page);
        return restResponse;
    }

    /**
     * 根据消息id查询消息
     * @param id
     * @return
     */
    @ApiOperation(value = "根据消息id查询消息")
    @RequestMapping(value = "/detail/{id}",method = RequestMethod.GET)
    public RestResponse detail(
            @ApiParam(name = "id",value = "消息id")
            @PathVariable String id){
        Message message = messageService.findById(id);
        return RestResponse.success(message);
    }
    /**
     * 修改消息 type：1表示活动消息，需要通知用户 0表示用户消息，status为1是发送消息给用户
     * @return
     */
    @ApiOperation(value = "修改消息")
    @RequestMapping(value = "/edit/{id}",method = RequestMethod.PUT)
    public RestResponse modify(
            @ApiParam(name = "id",value = "消息id")
            @PathVariable String id,
            @RequestBody MessageVo messageVo){
        Message message1 = messageService.findById(id);
        boolean isSendMsg = false;
        Integer old = message1.getStatus();
        RestResponse restResponse = null;
        try {
            BeanUtils.copyProperties2(message1,messageVo,false);
            if(StringUtil.isNotEmpty(messageVo.getLine())){
                message1.setLineTime(DateUtils.parseDate(messageVo.getLine(),"yyyy-MM-dd HH:mm"));
            }
            if(messageVo.getStatus()!=Message.OFFLINE) {
                if (message1.getLineTime().getTime() <= new Date().getTime()) {
                    message1.setStatus(Message.ONLINE);
                }
            }
            message1 = messageService.save(message1);
            logger.info("是否需要群发消息:{}",isSendMsg);
            if(old!=message1.getStatus()&&message1.getStatus()==Message.ONLINE){
                sendMessage(message1);
            }
            if(message1.getStatus()==Message.OFFLINE){
                accountMessageService.modifyMessageStatus(message1.getId(), AccountMessage.DELETE);
            }
        }catch (Exception e){
            restResponse = RestResponse.failed("0","上传内容不符合要求");
        }
        restResponse = RestResponse.success(message1);
        return restResponse;
    }

    /**
     * 新建消息 type：1表示活动消息，不需要通知用户 0表示用户消息，status为1是发送消息给用户
     * @param messageVo
     * @return
     */
    @ApiOperation(value = "新建消息")
    @RequestMapping(value = "/new",method = RequestMethod.POST)
    public RestResponse create(
            @RequestBody MessageVo messageVo){
        Message message = new Message();
        try {
            BeanUtils.copyProperties2(message,messageVo,false);
            if(StringUtil.isNotEmpty(messageVo.getLine())) {
                message.setLineTime(DateUtils.parseDate(messageVo.getLine(), "yyyy-MM-dd HH:mm"));
            }
            if(message.getType()==Message.MESSAGE_ACTIVITY){
                if(message.getLineTime().getTime()<=new Date().getTime()){
                    message.setStatus(Message.ONLINE);
                }
            }
            message = messageService.save(message);
            logger.info("是否需要群发消息:{}",message.getStatus()!=null&&message.getStatus()==Message.ONLINE);
            if(message.getStatus()!=null&&message.getStatus()==Message.ONLINE) {
                sendMessage(message);
            }
        } catch (IllegalAccessException e) {
            logger.error("参数异常",e);
        } catch (InvocationTargetException e) {
            logger.error("调用异常",e);
        }
        return RestResponse.success(message);
    }
    /**
     * 删除消息
     * @return
     */
    @ApiOperation(value = "删除消息")
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public RestResponse delete(
            @ApiParam(name="id",value = "消息id")
            @PathVariable  String id
    ) throws InvocationTargetException, IllegalAccessException {
        Message message = messageService.findById(id);
        messageService.delete(message);
        accountMessageService.modifyMessageStatus(message.getId(), AccountMessage.DELETE);
        return RestResponse.success(message);
    }
    /**
     * 发送消息给状态正常的用户
     */
    private  void sendMessage( Message message){
        logger.info("群发消息:消息体{}",message);
        List<Account> list = accountService.findByStatus(Account.STATUS_NORMAL);
        accountMessageService.insertMultiple(list,message);
    }
    /**
     * 等待处理数量
     * @return
     */
    @ApiOperation(value = "等待处理数量,客服中心awaitService财务中心awaitInvoice审核中心awaitDemand")
    @RequestMapping(value = "/await/num",method = RequestMethod.GET)
    public RestResponse getAwaitNum(){
        Map map = new HashMap();
        map.putAll(accountMessageService.getAwaitNum());
        Long awaitDemand = (Long)map.get("awaitDemand");
        Long msgTemplat = msgTemplateService.findByWait();
        map.put("awaitDemand",awaitDemand+msgTemplat);
        Map map1 = (HashMap)map.get("son");
        map1.put("msgTemplat",msgTemplat);
        map.put("son",map1);
        return RestResponse.success(map);
    }

}
