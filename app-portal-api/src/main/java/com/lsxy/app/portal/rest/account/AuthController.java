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
        if(Tenant.AUTH_COMPANY_SUCCESS==tenant.getIsRealAuth()){
            RealnameCorp realnameCorp = realnameCorpService.findByTenantIdAndStatus(tenant.getId(),Tenant.AUTH_COMPANY_SUCCESS);
            if(realnameCorp!=null) {
                map.put("realnameCorp",realnameCorp);
                map.put("createTime2", DateUtils.getTime(realnameCorp.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            }else{
                map.put("status",Tenant.AUTH_NO);
            }
        }else if(Tenant.AUTH_ONESELF_SUCCESS==tenant.getIsRealAuth()||Tenant.AUTH_UPGRADE_WAIT==tenant.getIsRealAuth()||Tenant.AUTH_UPGRADE_FAIL==tenant.getIsRealAuth()){
            RealnamePrivate realnamePrivate =  realnaePrivateService.findByTenantIdAndStatus(tenant.getId(),Tenant.AUTH_ONESELF_SUCCESS);
            if(realnamePrivate!=null) {
                map.put("realnamePrivate",realnamePrivate);
                map.put("createTime1", DateUtils.getTime(realnamePrivate.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            }else{
                map.put("status",Tenant.AUTH_NO);
            }
        }else if(Tenant.AUTH_UPGRADE_SUCCESS==tenant.getIsRealAuth()){
            RealnameCorp realnameCorp = realnameCorpService.findByTenantIdAndStatus(tenant.getId(),Tenant.AUTH_COMPANY_SUCCESS);
            if(realnameCorp!=null) {
                map.put("realnameCorp",realnameCorp);
                map.put("createTime2", DateUtils.getTime(realnameCorp.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            }else{
                map.put("status",Tenant.AUTH_NO);
            }
            RealnamePrivate realnamePrivate =  realnaePrivateService.findByTenantIdAndStatus(tenant.getId(),Tenant.AUTH_ONESELF_SUCCESS);
            if(realnamePrivate!=null) {
                map.put("realnamePrivate",realnamePrivate);
                map.put("createTime1", DateUtils.getTime(realnamePrivate.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
            }else{
                map.put("status",Tenant.AUTH_UPGRADE_FAIL);
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

    /**
     * 根据实名认证记录id，租户id，实名认证类型，修改后的结果来修改实名认证结果
     * @param id 实名认证记录id
     * @param tenantTd 租户id
     * @param type 实名认证类型 0个人认证 1实名认证
     * @param status 1个人成功 2企业成功 -1个人失败 -2企业失败
     * @return
     */
    @RequestMapping("/modify_auth_status")
    public RestResponse modifyAuthStatus(String id,String tenantTd,String type,Integer status){
        Tenant tenant = tenantService.findById(tenantTd);
        Integer statusT = tenant.getIsRealAuth();
        if(Integer.valueOf(type)==Tenant.AUTH_ONESELF){
            RealnamePrivate realnamePrivate = realnaePrivateService.findById(id);
            realnamePrivate.setStatus(status);
            tenant.setIsRealAuth(status);
            realnamePrivate.setTenant(tenant);
            realnaePrivateService.save(realnamePrivate);
        }else if(Integer.valueOf(type)==Tenant.AUTH_COMPANY){
            RealnameCorp realnameCorp = realnameCorpService.findById(id);
            realnameCorp.setStatus(status);
            if(statusT==Tenant.AUTH_ONESELF_SUCCESS||statusT==Tenant.AUTH_UPGRADE_WAIT||statusT==Tenant.AUTH_UPGRADE_FAIL){
                if(status==Tenant.AUTH_COMPANY_FAIL){
                    status=Tenant.AUTH_UPGRADE_FAIL;
                }else if(status==Tenant.AUTH_COMPANY_SUCCESS){
                    status=Tenant.AUTH_UPGRADE_SUCCESS;
                }
            }
            tenant.setIsRealAuth(status);
            realnameCorp.setTenant(tenant);
            realnameCorpService.save(realnameCorp);
        }
        tenant = tenantService.save(tenant);
        return RestResponse.success(tenant.getIsRealAuth());
    }
}
