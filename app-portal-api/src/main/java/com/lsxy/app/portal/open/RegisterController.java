package com.lsxy.app.portal.open;

import com.lsxy.framework.api.events.RegisterSuccessEvent;
import com.lsxy.framework.api.exceptions.RegisterException;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.web.rest.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.lsxy.framework.web.rest.RestResponse.failed;

/**
 * 注册入口
 * Created by liups on 2016/7/6.
 */
@RestController
@RequestMapping("/reg")
public class RegisterController {
    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private MQService mqService;
    /**
     * 用户注册信息检查
     * @param userName 用户名
     * @param mobile 手机
     * @param email 邮箱
     * @return
     */
    @RequestMapping("/reg_info_check")
    public RestResponse regInfoCheck(String userName,String mobile,String email){
        RestResponse response;
        int result = accountService.checkRegInfo(userName,mobile,email);
        switch (result){
            case AccountService.REG_CHECK_PASS:
                response = RestResponse.success(AccountService.REG_CHECK_PASS);
                break;
            case AccountService.REG_CHECK_USERNAME_EXIST:
                response = failed("0000","用户名已被注册");
                break;
            case AccountService.REG_CHECK_MOBILE_EXIST:
                response = failed("0000","手机号已被注册");
                break;
            case AccountService.REG_CHECK_EMAIL_EXIST:
                response = failed("0000","邮箱已被注册");
                break;
            default:
                response = failed("0000","信息检验失败");
        }
        return response;
    }

    /**
     * 用户激活
     * @param accountId 账号ID
     * @return 激活成功则返回true
     */
    @RequestMapping("/active")
    public RestResponse activeInfoCheck(String accountId,String password){
        RestResponse response;
        try {
            Account account = accountService.activeAccount(accountId, password);
            response = RestResponse.success(account);
        } catch (RegisterException e) {
            response = RestResponse.failed("0000",e.getMessage());
        }
        return response;
    }

    /**
     * 保存用户注册信息
     * @param userName 用户名
     * @param mobile 手机
     * @param email 邮箱
     * @return
     */
    @RequestMapping("/create_account")
    public RestResponse createAccount(String userName,String mobile,String email) throws Exception {
        RestResponse response;
        //创建用户前再进行一次校验
        int result = accountService.checkRegInfo(userName,mobile,email);
        if(logger.isDebugEnabled()){
            logger.debug("checkRegInfo：{} "+result);
        }
        if(result == AccountService.REG_CHECK_PASS){
            Account account = accountService.createAccount(userName,mobile,email);
            if(logger.isDebugEnabled()){
                logger.debug("createAccount：{}"+account);
            }
            if(account != null){
                if(logger.isDebugEnabled()){
                    logger.debug("RegisterSuccessEvent-start;{}"+account);
                }
                RegisterSuccessEvent event = new RegisterSuccessEvent(account.getId());
                mqService.publish(event);
                if(logger.isDebugEnabled()){
                    logger.debug("RegisterSuccessEvent-end;{},{}"+account,event);
                }
                response = RestResponse.success(account);
            }else{
                response = failed("0000","注册用户失败，系统出错！");
            }
        }else{
            response = failed("0000","注册用户失败,注册信息已存在");
        }
        return response;
    }

    /**
     * 获取账号
     * @param accountId
     * @return
     */
    @RequestMapping("/account/{accountId}")
    public RestResponse getAccount(@PathVariable String accountId){
        RestResponse response;
        Account account = accountService.findById(accountId);
        if(account != null){
            response = RestResponse.success(account);
        }else{
            response = failed("0000","账号不存在");
        }
        return  response;
    }

}
