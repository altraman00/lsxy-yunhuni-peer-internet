package com.lsxy.app.portal.rest.stastistic;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.msg.api.model.MsgUserRequest;
import com.lsxy.msg.api.service.MsgUserRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import java.util.Date;

/**
 * Created by zhangxb on 2017/3/8.
 */
@RequestMapping("/rest/msg_user_request")
@RestController
public class MsgUserRequestController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(MsgUserRequestController.class);
    @Reference(timeout=3000,check = false,lazy = true)
    private MsgUserRequestService msgUserRequestService;

    @RequestMapping("/plist")
    public RestResponse pList(@RequestParam(defaultValue = "1") Integer pageNo,@RequestParam(defaultValue = "20") Integer pageSize,String sendType,String appId, String  start, String  end, int isMass, String taskName, String mobile ){
        Date startTime = null;
        Date endTime = null;
        try {
            startTime = DateUtils.parseDate(start, "yyyy-MM-dd");
            endTime = DateUtils.parseDate(end + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
        }catch (Exception e){}

        Page page = msgUserRequestService.getPageByCondition( pageNo,  pageSize,sendType, appId, startTime,  endTime,  isMass,  taskName,  mobile ,getCurrentAccount().getTenant().getId());
        return RestResponse.success(page);
    }
    @RequestMapping("/get/{id}")
    public RestResponse pList(@PathVariable String id){
        MsgUserRequest msgUserRequest = msgUserRequestService.findById(id);
        return RestResponse.success(msgUserRequest);
    }
}
