package com.lsxy.app.oc.rest.message;

import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.framework.api.message.model.Message;
import com.lsxy.framework.api.message.service.AccountMessageService;
import com.lsxy.framework.api.message.service.MessageService;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.core.utils.EntityUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by zhangxb on 2016/8/10.
 */
@RequestMapping("/message")
@RestController
public class MessageController extends AbstractRestController {
    @Autowired
    MessageService messageService;
    @Autowired
    AccountService accountService;
    @Autowired
    AccountMessageService accountMessageService;

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
    public RestResponse pageList(@RequestParam(defaultValue = "1",required=false)Integer type,@RequestParam(required = false)Integer status, @RequestParam(required=false)String startTime, @RequestParam(required=false)String endTime, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20")Integer pageSize){
        Page page = messageService.pageList(type,status, startTime, endTime, pageNo, pageSize);
        RestResponse restResponse = RestResponse.success(page);
        return restResponse;
    }

    /**
     * 根据消息查新消息
     * @param id
     * @return
     */
    @RequestMapping(value = "/detail/{id}",method = RequestMethod.GET)
    public RestResponse detail(@PathVariable String id){
        Message message = messageService.findById(id);
        return RestResponse.success(message);
    }
    /**
     * 修改消息 type：1表示活动消息，不需要通知用户 0表示用户消息，status为1是发送消息给用户
     * @param message
     * @return
     */
    @RequestMapping(value = "/edit/{id}",method = RequestMethod.POST)
    public RestResponse modify(@PathVariable String id, @RequestBody Message message){
        Message message1 = messageService.findById(id);
        boolean isSendMsg = false;
        if(message.getStatus()!=null&&message1.getStatus()!=message.getStatus()){
            if(message.getType()==Message.MESSAGE_ACCOUNT&&message.getStatus()==Message.ONLINE) {
                isSendMsg = true;
            }
        }
        RestResponse restResponse = null;
        try {
            EntityUtils.copyProperties(message1, message);
            messageService.save(message1);
            if(isSendMsg){
                sendMessage(message1);
            }
        }catch (Exception e){
            restResponse = RestResponse.failed("0","转换对象失败");
        }
        restResponse = RestResponse.success(message);
        return restResponse;
    }

    /**
     * 新建消息 type：1表示活动消息，不需要通知用户 0表示用户消息，status为1是发送消息给用户
     * @param message
     * @return
     */
    @RequestMapping(value = "/new",method = RequestMethod.GET)
    public RestResponse create(@RequestBody Message message){
        message = messageService.save(message);
        if(message.getStatus()!=null&&message.getType()==Message.MESSAGE_ACCOUNT&&message.getStatus()==Message.ONLINE) {
            sendMessage(message);
        }
        return RestResponse.success(message);
    }
    /**
     * 发送消息给状态正常的用户
     * @param message
     */
    private  void sendMessage(@RequestBody Message message){
        List<Account> list = accountService.list();
        accountMessageService.insertMultiple(list,message);
    }

}
