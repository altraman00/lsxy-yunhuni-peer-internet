package com.lsxy.app.portal.rest.account;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.yunhuni.api.message.model.AccountMessage;
import com.lsxy.yunhuni.api.message.service.AccountMessageService;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 用户消息
 * Created by zhangxb on 2016/7/5.
 */
@RequestMapping("/rest/message/account_message")
@PreAuthorize("hasAuthority('ROLE_TENANT_USER')")
@RestController
public class AccountMessageController extends AbstractRestController {
    @Autowired
    AccountMessageService accountMessageService;

    /**
     * 查询用户消息
     * @param pageNo 第几页
     * @param pageSize 每页多少条记录
     * @return
     */
    @RequestMapping("/plist")
    public RestResponse plist(Integer pageNo,Integer pageSize){
//        Account account = getCurrentAccount();
//        Date date = new Date();
//        String endTime = DateUtils.formatDate(date,"yyyy-MM-dd");
//        Calendar cale = Calendar.getInstance();
//        cale.setTime(date);
//        cale.set(Calendar.DATE, cale.get(Calendar.DATE) -3  );
//        String time = DateUtils.formatDate(cale.getTime(),"yyyy-MM-dd");
//        Page<AccountMessage> list = accountMessageService.pageAll(account.getId(),DateUtils.parseDate(time,"yyyy-MM-dd"),DateUtils.parseDate(endTime+" 23:59:59","yyyy-MM-dd HH:mm:ss"),pageNo,pageSize);
//        return RestResponse.success(list);
        String userName = getCurrentAccountUserName();
        Page<AccountMessage> list = accountMessageService.pageListByAccountId(userName,pageNo,pageSize);
        return RestResponse.success(list);
    }
    /**
     * 查询用户消息
     * @return
     */
    @RequestMapping("/list")
    public RestResponse list(){
        Account account = getCurrentAccount();
        Date date = new Date();
        String endTime = DateUtils.formatDate(date,"yyyy-MM-dd");
        Calendar cale = Calendar.getInstance();
        cale.setTime(date);
        cale.set(Calendar.DATE, cale.get(Calendar.DATE) -3  );
        String time = DateUtils.formatDate(cale.getTime(),"yyyy-MM-dd");
        List<AccountMessage> list = accountMessageService.listAll(account.getId(),DateUtils.parseDate(time,"yyyy-MM-dd"),DateUtils.parseDate(endTime+" 23:59:59","yyyy-MM-dd HH:mm:ss"));
        return RestResponse.success(list);
    }
    /**
     * 修改状态为已读
     * @return
     */
    @RequestMapping("/edit")
    public RestResponse modifyStauts(){
        Date date = new Date();
        Calendar cale = Calendar.getInstance();
        cale.setTime(date);
        cale.set(Calendar.DATE, cale.get(Calendar.DATE) -3  );
        String time = DateUtils.formatDate(cale.getTime(),"yyyy-MM-dd");
        Date endTime = DateUtils.parseDate(time,"yyyy-MM-dd");
        Account account = getCurrentAccount();
        accountMessageService.modifyMessageStatus(account.getId(),AccountMessage.READ,endTime);
        return RestResponse.success();
    }
    /**
     * 根据状态查询数量
     * @return
     */
    @RequestMapping("/count")
    public RestResponse count(){
        Account account = getCurrentAccount();
        Date date = new Date();
        String endTime = DateUtils.formatDate(date,"yyyy-MM-dd");
        Calendar cale = Calendar.getInstance();
        cale.setTime(date);
        cale.set(Calendar.DATE, cale.get(Calendar.DATE) -3  );
        String time = DateUtils.formatDate(cale.getTime(),"yyyy-MM-dd");
        Long num = accountMessageService.countAll(account.getId(),DateUtils.parseDate(time,"yyyy-MM-dd"),DateUtils.parseDate(endTime+" 23:59:59","yyyy-MM-dd HH:mm:ss"));
        return RestResponse.success(num);
    }
    /**
     * 根据用户消息id删除用户消息记录
     * @param id 用户消息id
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @RequestMapping("/delete")
    public RestResponse delete(String id) throws InvocationTargetException, IllegalAccessException {
        AccountMessage accountMessage = accountMessageService.findById(id);
        accountMessageService.delete(accountMessage);
        return RestResponse.success(accountMessage);
    }
    @RequestMapping("/detail")
    public RestResponse detail(String id)  {
        AccountMessage accountMessage = accountMessageService.findById(id);
        return RestResponse.success(accountMessage);
    }

}
