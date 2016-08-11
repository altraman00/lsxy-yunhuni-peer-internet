package com.lsxy.app.oc.rest.custservice;

import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.framework.api.customer.model.Feedback;
import com.lsxy.framework.api.customer.service.FeedbackService;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 反馈意见
 * Created by zhangxb on 2016/7/4.
 */
@RequestMapping("/service")
@RestController
public class FeedbackController extends AbstractRestController {
    @Autowired
    private FeedbackService feedbackService;

    /**
     * 保存反馈信息
     * @param pageNo 第几页
     * @param pageSize 每页几条记录
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param status 状态
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public RestResponse save(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize, String startTime, String endTime, Integer status)   {
        Page page = feedbackService.pageList(pageNo,pageSize,startTime,endTime,status);
        return RestResponse.success(page);
    }
}
