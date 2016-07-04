package com.lsxy.app.portal.rest;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.api.customer.model.Feedback;
import com.lsxy.framework.api.customer.service.FeedbackService;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 反馈意见
 * Created by zhangxb on 2016/7/4.
 */
@RequestMapping("/rest/customer/feedback")
@RestController
public class FeedbackController extends AbstractRestController {
    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private AccountService accountService;

    /**
     * 保存反馈信息
     * @param content 反馈内容
     * @param status 状态
     * @return
     */
    @RequestMapping("/save")
    public RestResponse save(String content,String status) throws MatchMutiEntitiesException {
        String userName = getCurrentAccountUserName();
        Account account = accountService.findAccountByUserName(userName);
        Feedback feedback = new Feedback();
        feedback.setAccount(account);
        feedback.setTenant(account.getTenant());
        feedback.setContent(content);
        feedback.setStatus(Integer.valueOf(status));
        feedback = feedbackService.save(feedback);
        return RestResponse.success(feedback);
    }
}
