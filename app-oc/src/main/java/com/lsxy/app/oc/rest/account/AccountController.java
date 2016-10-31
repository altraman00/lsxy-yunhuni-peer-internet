package com.lsxy.app.oc.rest.account;

import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.portal.SendActivePasswordSuccessEvent;
import com.lsxy.framework.mq.events.portal.RegisterSuccessEvent;
import com.lsxy.framework.web.rest.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 账号管理
 * Created by zhangxb on 2016/10/19.
 */
@Api(value = "账号管理", description = "" )
@RequestMapping("/acount")
@RestController
public class AccountController {
    public static final String ACCOUNT_MACTIVE_PREFIX = "account_mactive_";
    @Autowired
    private MQService mqService;
    @Autowired
    private RedisCacheService cacheManager;
    @Autowired
    AccountService accountService;
    @ApiOperation(value = "获取注册用户分页信息")
    @RequestMapping(value = "/{type}/list",method = RequestMethod.GET)
    public RestResponse pageList(
            @ApiParam(name = "type",value = " not_active 未激活| expire 过期")
            @PathVariable String type,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize){
        Integer status = null;
        if("not_active".equals(type)){//未激活
            status = Account.STATUS_NOT_ACTIVE;
        }else if("expire".equals(type)){//过期
            status = Account.STATUS_EXPIRE;
        }else{
            return RestResponse.failed("0000","请求类型错误");
        }
        Page page2 = null;
        List<AccountVo> list2 = new ArrayList();
        if(status != null){
            Page page= accountService.pList(status,pageNo,pageSize);
            List<Account> list  = page.getResult();
            if(list != null && list.size() > 0){
                for(int i=0;i < list.size(); i++ ){
                    AccountVo accountVo = new AccountVo();
                    try {
                        BeanUtils.copyProperties(accountVo,list.get(i));
                    } catch (Exception e) {
                        return RestResponse.failed("0000","转换出错");
                    }
                    list2.add(accountVo);
                }
            }
            page2 = new Page<>(page.getStartIndex(),page.getTotalCount(),page.getPageSize(),list2);
        }
        return RestResponse.success(page2);
    }
    @ApiOperation(value = "发送激活邮件")
    @RequestMapping(value = "/send/active/{id}",method = RequestMethod.PUT)
    public RestResponse sendActive(@ApiParam(name = "id",value = "用户标志")
                                     @PathVariable String id){
        //获取用户信息
        Account account = accountService.findById(id);
        if(account!=null) {
            //判断当前用户状态
            if(account.getStatus()==Account.STATUS_NOT_ACTIVE) {
                //发送激活邮件
                RegisterSuccessEvent event = new RegisterSuccessEvent(id);
                mqService.publish(event);
                return  RestResponse.success("发送激活邮件成功");
            }else{
                return RestResponse.failed("0000","用户对应状态不是未激活，无法发送激活邮件");
            }
        }else{
            return RestResponse.failed("0000","用户不存在，无法发送激活邮件");
        }
    }
    @ApiOperation(value = "发送随机密码邮件")
    @RequestMapping(value = "/send/password/{id}",method = RequestMethod.PUT)
    public RestResponse sendPassword(@ApiParam(name = "id",value = "用户标志")
                                 @PathVariable String id){
        //获取用户信息
        Account account = accountService.findById(id);
        if(account!=null) {
            //判断当前用户状态
            if(account.getStatus()==Account.STATUS_NOT_ACTIVE) {
                //生成随机密码
                String password =  UUIDGenerator.uuid().toString().substring(0,6);
                //激活账号
                accountService.activeAccount(id, password);
                //删除redis数据
                cacheManager.del(ACCOUNT_MACTIVE_PREFIX + id);
                //发送邮件
                mqService.publish(new SendActivePasswordSuccessEvent(id,password));
                return  RestResponse.success("激活成功，发送随机密码");
            }else{
                return RestResponse.failed("0000","用户对应状态不是未激活，无法激活");
            }
        }else{
            return RestResponse.failed("0000","用户不存在，无法激活");
        }
    }
    @ApiOperation(value = "释放账号")
    @RequestMapping(value = "/opt/expire/{id}",method = RequestMethod.DELETE)
    public RestResponse expire(@ApiParam(name = "id",value = "用户标志")
                                     @PathVariable String id){
        //获取用户信息
        Account account = accountService.findById(id);
        if(account!=null) {
            //判断当前用户状态
            if(account.getStatus()==Account.STATUS_NOT_ACTIVE) {
                //修改状态为过期
                account.setStatus(Account.STATUS_EXPIRE);
                accountService.save(account);
                //删除redis数据
                cacheManager.del(ACCOUNT_MACTIVE_PREFIX + id);
                return  RestResponse.success("释放成功");
            }else{
                return RestResponse.failed("0000","用户对应状态不是未激活，无法释放");
            }
        }else{
            return RestResponse.failed("0000","用户不存在，无法释放");
        }
    }
}
