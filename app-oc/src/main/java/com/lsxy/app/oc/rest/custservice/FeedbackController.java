package com.lsxy.app.oc.rest.custservice;

import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.framework.api.customer.model.Feedback;
import com.lsxy.framework.api.customer.service.FeedbackService;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 反馈意见
 * Created by zhangxb on 2016/7/4.
 */
@Api(value = "意见反馈", description = "客服中心相关接口" )
@RequestMapping("/service")
@RestController
public class FeedbackController extends AbstractRestController {
    @Autowired
    private FeedbackService feedbackService;

    /**
     * 反馈信息分页信息
     * @param pageNo 第几页
     * @param pageSize 每页几条记录
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param status 状态
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ApiOperation(value = "反馈信息分页信息")
    public RestResponse list(
            @ApiParam(name = "status",value = "状态 '0未读;1已读',")
            @RequestParam(required=false)Integer status,
            @ApiParam(name = "startTime",value = "格式:yyyy-MM")
            @RequestParam(required=false)String startTime,
            @ApiParam(name = "endTime",value = "格式:yyyy-MM")
            @RequestParam(required=false)String endTime,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize
            )   {
        Page page = feedbackService.pageList(pageNo,pageSize,startTime,endTime,status);
        return RestResponse.success(page);
    }
    /**
     * 修改状态为已读
     * @return
     */
    @RequestMapping(value = "/edit/{id}",method = RequestMethod.PATCH)
    @ApiOperation(value = "修改状态为已读")
    public RestResponse modify(
            @ApiParam(name = "id",value = "消息id")
            @PathVariable String id
    )   {
        Feedback feedback = feedbackService.findById(id);
        feedback.setStatus(Feedback.READ);
        feedback = feedbackService.save(feedback);
        return RestResponse.success(feedback);
    }
    /**
     * 修改状态为已读
     * @return
     */
    @RequestMapping(value = "/edit",method = RequestMethod.PUT)
    @ApiOperation(value = "批量修改状态为已读")
    public RestResponse modify(
            @RequestBody FeedbackVo feedbackVo
    )   {
        feedbackService.batchModifyStatus(feedbackVo.getIds());
        return RestResponse.success(feedbackVo);
    }
}
