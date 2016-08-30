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
import java.util.List;
import java.util.Map;

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
     * 获取实名认证状态(只要一种认证成功即成功)
     * 个人认证升级企业认证成功，返回企业认证成功 2
     * 个人认证升级等待或者失败，返回个人认证成功 1
     * @return
     */
    @RequestMapping("/find_auth_status")
    public RestResponse findAuthStatus(){
       Integer isRealAuth = getCurrentAccount().getTenant().getIsRealAuth();
       Map map = new HashMap();
        boolean flag = false;
        if(isRealAuth==Tenant.AUTH_UPGRADE_WAIT|| isRealAuth==Tenant.AUTH_UPGRADE_SUCCESS || isRealAuth==Tenant.AUTH_UPGRADE_FAIL|| isRealAuth==Tenant.AUTH_COMPANY_SUCCESS|| isRealAuth==Tenant.AUTH_ONESELF_SUCCESS){
            flag = true;
        }
        Integer stauts = isRealAuth;
        if (isRealAuth == Tenant.AUTH_UPGRADE_SUCCESS  ) {
            stauts = Tenant.AUTH_COMPANY_SUCCESS;
        }else if(isRealAuth == Tenant.AUTH_UPGRADE_WAIT || isRealAuth == Tenant.AUTH_UPGRADE_FAIL ){
            stauts = Tenant.AUTH_ONESELF_SUCCESS;
        }
       map.put("flag",flag);
       map.put("status",stauts);
       return RestResponse.success(map);
    }
    /**
     * 获取实名认证状态
     * @return
     */
    @RequestMapping("/find_auth_info")
    public RestResponse findAuthInfo() {
        String userName = getCurrentAccountUserName();
        HashMap<String,Object> map = new HashMap<>();
        //获取租户对象
        Tenant tenant = accountService.findAccountByUserName(userName).getTenant();
        map.put("userName",userName);
        map.put("status",tenant.getIsRealAuth());
        if(Tenant.AUTH_COMPANY_SUCCESS==tenant.getIsRealAuth()||Tenant.AUTH_COMPANY_FAIL==tenant.getIsRealAuth()){
            List<RealnameCorp> realnameCorpList = realnameCorpService.list(tenant.getId(),tenant.getIsRealAuth());
            if(realnameCorpList.size()>0&&realnameCorpList.get(0)!=null) {
                map.put("realnameCorp",realnameCorpList.get(0));
                map.put("createTime2", DateUtils.getTime(realnameCorpList.get(0).getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            }else{
                map.put("status",Tenant.AUTH_NO);
            }
        }else if(Tenant.AUTH_ONESELF_SUCCESS==tenant.getIsRealAuth()||Tenant.AUTH_ONESELF_FAIL==tenant.getIsRealAuth()){
            List<RealnamePrivate> realnamePrivateList =  realnaePrivateService.list(tenant.getId(),tenant.getIsRealAuth());
            if(realnamePrivateList.size()>0&&realnamePrivateList.get(0)!=null) {
                map.put("realnamePrivate",realnamePrivateList.get(0));
                map.put("createTime1", DateUtils.getTime(realnamePrivateList.get(0).getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            }else{
                map.put("status",Tenant.AUTH_NO);
            }
        }else if(Tenant.AUTH_UPGRADE_SUCCESS==tenant.getIsRealAuth()||Tenant.AUTH_UPGRADE_WAIT==tenant.getIsRealAuth()||Tenant.AUTH_UPGRADE_FAIL==tenant.getIsRealAuth()){
            List<RealnamePrivate> realnamePrivateList =  realnaePrivateService.list(tenant.getId(),Tenant.AUTH_ONESELF_SUCCESS);
            if(realnamePrivateList.size()>0&&realnamePrivateList.get(0)!=null) {
                map.put("realnamePrivate",realnamePrivateList.get(0));
                map.put("createTime1", DateUtils.getTime(realnamePrivateList.get(0).getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            }else{
                map.put("status",Tenant.AUTH_UPGRADE_FAIL);
            }
            int statusT = tenant.getIsRealAuth();
            if(Tenant.AUTH_UPGRADE_WAIT==tenant.getIsRealAuth()){
                statusT = Tenant.AUTH_WAIT;
            }else if(Tenant.AUTH_UPGRADE_FAIL==tenant.getIsRealAuth()){
                statusT = Tenant.AUTH_COMPANY_FAIL;
            }else if(Tenant.AUTH_UPGRADE_SUCCESS==tenant.getIsRealAuth()){
                statusT = Tenant.AUTH_COMPANY_SUCCESS;
            }
            List<RealnameCorp> realnameCorpList = realnameCorpService.list(tenant.getId(),statusT);
            if(realnameCorpList.size()>0&&realnameCorpList.get(0)!=null) {
                map.put("realnameCorp",realnameCorpList.get(0));
                map.put("createTime2", DateUtils.getTime(realnameCorpList.get(0).getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
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
    public RestResponse svePrivateAuth(String status, RealnamePrivate realnamePrivate) throws MatchMutiEntitiesException {
        String userName = getCurrentAccountUserName();
        //获取租户对象
        Account acount  = accountService.findAccountByUserName(userName);
        Tenant tenant = acount.getTenant();
        Integer statusT = Integer.valueOf(status);
        tenant.setIsRealAuth(statusT);
        if(statusT==Tenant.AUTH_ONESELF_WAIT){
            statusT=Tenant.AUTH_WAIT;
        }
        realnamePrivate.setStatus(statusT);
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
        Integer statusT = Integer.valueOf(status);
        //获取租户对象
        Account acount  = accountService.findAccountByUserName(userName);
        Tenant tenant = acount.getTenant();
        tenant.setIsRealAuth(statusT);
        if(statusT==Tenant.AUTH_UPGRADE_WAIT){
            statusT=Tenant.AUTH_WAIT;
        }else if(statusT==Tenant.AUTH_UPGRADE_SUCCESS){
            statusT=Tenant.AUTH_COMPANY_SUCCESS;
        }else if(statusT==Tenant.AUTH_UPGRADE_FAIL){
            statusT=Tenant.AUTH_COMPANY_FAIL;
        }
        realnameCorp.setStatus(statusT);
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
