package com.lsxy.app.oc.rest.message;

import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.framework.api.message.model.Message;
import com.lsxy.framework.api.message.service.MessageService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by zhangxb on 2016/8/10.
 */
@RequestMapping("/message")
@RestController
public class MessageController extends AbstractRestController {
    @Autowired
    MessageService messageService;

    /**
     *  根据日期和类型查询消息列表信息
     * @param type 类型 0未上线 1上线 -1下线
     * @param startTime 开始时间 yyyy-MM-dd
     * @param endTime 结束时间
     * @param pageNo 第几页
     * @param pageSize 每页几条数据
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public RestResponse pageList(@RequestParam(required=false)String type, @RequestParam(required=false)String startTime, @RequestParam(required=false)String endTime, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20")Integer pageSize){
        Page page = messageService.pageList(type, startTime, endTime, pageNo, pageSize);
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
}
