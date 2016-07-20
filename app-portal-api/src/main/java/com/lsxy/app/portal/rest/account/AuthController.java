package com.lsxy.app.portal.rest.account;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.model.RealnameCorp;
import com.lsxy.framework.api.tenant.model.RealnamePrivate;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.api.tenant.service.RealnameCorpService;
import com.lsxy.framework.api.tenant.service.RealnamePrivateService;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * Created by zhangxb on 2016/6/28.
 */
@RequestMapping("/rest/account/auth")
@RestController
public class AuthController extends AbstractRestController {
    @Autowired
    private RealnamePrivateService realnaePrivateService;
    @Autowired
    private RealnameCorpService realnameCorpService;
    @Autowired
    private TenantService tenantService;
    @Autowired
    private AccountService accountService;


    /**
     * 获取实名认证状态
     * @return
     */
    @RequestMapping("/find_auth_status")
    public RestResponse findAuthStatus() throws MatchMutiEntitiesException {
        String userName = getCurrentAccountUserName();
        HashMap<String,Object> map = new HashMap<>();
        //获取租户对象
        Tenant tenant = accountService.findAccountByUserName(userName).getTenant();
        map.put("userName",userName);
        map.put("status",tenant.getIsRealAuth());
        if(Tenant.AUTH_COMPANY_SUCESS==tenant.getIsRealAuth()){
            RealnameCorp realnameCorp = realnameCorpService.findByTenantIdAndStatus(tenant.getId(),Tenant.AUTH_COMPANY_SUCESS);
            if(realnameCorp!=null) {
                map.put("name", realnameCorp.getName());
                map.put("time", DateUtils.getTime(realnameCorp.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            }else{
                map.put("status",Tenant.AUTH_NO);
            }
        }else if(Tenant.AUTH_ONESELF_SUCESS==tenant.getIsRealAuth()){
            RealnamePrivate realnamePrivate =  realnaePrivateService.findByTenantIdAndStatus(tenant.getId(),Tenant.AUTH_ONESELF_SUCESS);
            if(realnamePrivate!=null) {
                map.put("name", realnamePrivate.getName());
                map.put("time", DateUtils.getTime(realnamePrivate.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            }else{
                map.put("status",Tenant.AUTH_NO);
            }
        }

        return RestResponse.success(map);
    }
    /**
     * 个人实名认证方法
     * @param status 认证状态
     * @param realnamePrivate 个人实名认证
     * @return
     */
    @RequestMapping("/save_private_auth")
    private RestResponse svePrivateAuth( String status, RealnamePrivate realnamePrivate) throws MatchMutiEntitiesException {
        String userName = getCurrentAccountUserName();
        //获取租户对象
        Account acount  = accountService.findAccountByUserName(userName);
        Tenant tenant = acount.getTenant();
        tenant.setIsRealAuth(Integer.valueOf(status));
        realnamePrivate.setTenant(tenant);
        //保存到数据库
        realnamePrivate = realnaePrivateService.save(realnamePrivate);
        tenantService.save(tenant);
        acount.setTenant(tenant);
        accountService.save(acount);
        return RestResponse.success(realnamePrivate);
    }

    /**
     企业实名认证
     * @param status 认证状态
     * @param realnameCorp 企业认证对象
     * @return
     */
    @RequestMapping("/save_corp_auth")
    public RestResponse saveCorpAuth( String status,RealnameCorp realnameCorp) throws MatchMutiEntitiesException {
        String userName = getCurrentAccountUserName();
        //获取租户对象
        Account acount  = accountService.findAccountByUserName(userName);
        Tenant tenant = acount.getTenant();
        tenant.setIsRealAuth(Integer.valueOf(status));
        realnameCorp.setTenant(tenant);
        //保存到数据库
        realnameCorp = realnameCorpService.save(realnameCorp);
        //修改租户中实名认证状态
        tenantService.save(tenant);
        acount.setTenant(tenant);
        accountService.save(acount);
        return RestResponse.success(realnameCorp);
    }
}
