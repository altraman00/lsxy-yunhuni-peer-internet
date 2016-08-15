package com.lsxy.app.oc.rest.message;

import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.framework.api.message.service.MessageService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhangxb on 2016/8/10.
 */
@RequestMapping("/message")
@RestController
public class MessageController extends AbstractRestController {
    @Autowired
    MessageService messageService;
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public RestResponse pageList(@RequestParam(required=false)String type, @RequestParam(required=false)String startTime, @RequestParam(required=false)String endTime, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20")Integer pageSize){
        Page page = messageService.pageList(type, startTime, endTime, pageNo, pageSize);
        RestResponse restResponse = RestResponse.success(page);
        return restResponse;
    }
}
