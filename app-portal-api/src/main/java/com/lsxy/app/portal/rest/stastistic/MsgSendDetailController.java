package com.lsxy.app.portal.rest.stastistic;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.msg.api.service.MsgSendDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhangxb on 2017/3/9.
 */
@RequestMapping("/rest/msg_user_detail")
@RestController
public class MsgSendDetailController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(MsgUserRequestController.class);
    @Reference(timeout=3000,check = false,lazy = true)
    private MsgSendDetailService msgSendDetailService;
    @RequestMapping("plist")
    public RestResponse pList(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize, @RequestParam String msgKey, String mobile, String state ){
        Page page = msgSendDetailService.getPageByContiton( pageNo,  pageSize,msgKey,  mobile,state );
        return RestResponse.success(page);
    }
}
