package com.lsxy.app.oc.rest.message;

import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.framework.api.message.model.Message;
import com.lsxy.framework.api.message.service.AccountMessageService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by zhangxb on 2016/8/10.
 */
@Api(value = "消息中心")
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
     * 修改消息 type：1表示活动消息，不需要通知用户 0表示用户消息，status为1是发送消息给用户
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
        if(messageVo.getStatus()!=null&&message1.getStatus()!=messageVo.getStatus()){
            if(messageVo.getType()==Message.MESSAGE_ACCOUNT&&messageVo.getType()==Message.ONLINE) {
                isSendMsg = true;
            }
        }
        RestResponse restResponse = null;
        try {
            BeanUtils.copyProperties(message1,messageVo);
            if(StringUtil.isNotEmpty(messageVo.getLine())){
                message1.setLineTime(DateUtils.parseDate(messageVo.getLine(),"yyyy-MM-dd HH:mm"));
            }
            message1 = messageService.save(message1);
            if(isSendMsg){
                sendMessage(message1);
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
            BeanUtils.copyProperties(message,messageVo);
            if(StringUtil.isNotEmpty(messageVo.getLine())) {
                message.setLineTime(DateUtils.parseDate(messageVo.getLine(), "yyyy-MM-dd HH:mm"));
            }
            message = messageService.save(message);
            if(message.getStatus()!=null&&message.getType()==Message.MESSAGE_ACCOUNT&&message.getStatus()==Message.ONLINE) {
                sendMessage(message);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
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
