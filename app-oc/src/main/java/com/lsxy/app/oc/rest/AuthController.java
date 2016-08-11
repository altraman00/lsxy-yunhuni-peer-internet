package com.lsxy.app.oc.rest;

import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.framework.api.tenant.model.RealnameCorp;
import com.lsxy.framework.api.tenant.model.RealnamePrivate;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.RealnameCorpService;
import com.lsxy.framework.api.tenant.service.RealnamePrivateService;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实名认证处理类
 * Created by zhangxb on 2016/8/11.
 */
@RequestMapping("/demand")
@RestController
public class AuthController extends AbstractRestController {
    @Autowired
    RealnamePrivateService realnamePrivateService;
    @Autowired
    RealnameCorpService realnameCorpService;
    @Autowired
    TenantService tenantService;
    /**
     * 查找用户下的分页信息
     * @param authStatus await|auditing|unauth
     * @param startTime 开始时间 yyyy-MM-dd
     * @param endTime 结束时间
     * @param type 0个人认证 1企业认证
     * @param search 会员名
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @return
     */
    @RequestMapping(value = "/member/{authStatus}/list",method = RequestMethod.GET)
    public RestResponse pageList(@PathVariable String authStatus, String startTime, String endTime, Integer type, String search, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20")Integer pageSize){
        Integer status = null;
        if("await".equals(authStatus)){
            status = 0;
        }else if("auditing".equals(authStatus)){
            status = 1;
        }else if("unauth".equals(authStatus)){
            status = -1;
        }
        Page page = null;
        if(status!=null) {
            page = realnamePrivateService.pageListAuthInfo(status,startTime,endTime,type,search,pageNo,pageSize);
        }
        return RestResponse.success(page);
    }

    /**
     * 根据记录id和认证类型获取详情页面
     * @param uid 记录id
     * @param type 认证类型 0个人1企业
     * @return
     */
    @RequestMapping(value = "/member/detail/{uid}",method = RequestMethod.GET)
    public RestResponse pageList(@PathVariable String uid,Integer type){
        RestResponse restResponse = null;
        Map map = new HashMap();
        if(Tenant.AUTH_ONESELF==type){//个人
            RealnamePrivate realnamePrivate = null;
            try {
                realnamePrivate = realnamePrivateService.findById(uid);
                if(realnamePrivate.getTenant()!=null){
                    List<RealnamePrivate> list = realnamePrivateService.findByTenantId(realnamePrivate.getTenant().getId());
                    map.put("list",list);
                }
            }catch (Exception e){
            }
            map.put("realname",realnamePrivate);
        }else if(Tenant.AUTH_COMPANY==type){//企业
            RealnameCorp realnameCorp = null;
            try{
                realnameCorp = realnameCorpService.findById(uid);
                if(realnameCorp.getTenant()!=null){
                    List<RealnameCorp> list = realnameCorpService.findByTenantId(realnameCorp.getTenant().getId());
                    map.put("list",list);
                }
            }catch (Exception e){}
            map.put("realname",realnameCorp);
        }else{
            restResponse = RestResponse.failed("0","类型不存");
        }
        if(restResponse==null){
            restResponse = RestResponse.success(map);
        }
        return restResponse;
    }
    /**
     * 根据实名认证记录id，租户id，实名认证类型，修改后的结果来修改实名认证结果
     * @param uid 实名认证记录id
     * @param type 实名认证类型 0个人认证 1实名认证
     * @param status 1个人成功 2企业成功 -1个人失败 -2企业失败
     * @return
     */
    @RequestMapping(value = "/member/edit/{uid}",method = RequestMethod.GET)
    public RestResponse modifyAuthStatus(@PathVariable String uid,Integer type,Integer status){
        Tenant tenant = null;
        RestResponse restResponse = null;
        Integer statusT = null;
        if(Tenant.AUTH_ONESELF==type&&(Tenant.AUTH_ONESELF_FAIL==status||Tenant.AUTH_ONESELF_SUCCESS==status)){//个人
            RealnamePrivate realnamePrivate = null;
            try {
                realnamePrivate = realnamePrivateService.findById(uid);
                tenant = realnamePrivate.getTenant();
                if(tenant!=null){
                    realnamePrivate.setStatus(status);
                    tenant.setIsRealAuth(status);
                    tenant.setTenantName(realnamePrivate.getName());
                    realnamePrivate.setTenant(tenant);
                    realnamePrivateService.save(realnamePrivate);
                    tenant = tenantService.save(tenant);
                }else{
                    restResponse = RestResponse.failed("0","租户不存在");
                }
            }catch (Exception e){
            }
        }else if(Tenant.AUTH_COMPANY==type&&(Tenant.AUTH_COMPANY_SUCCESS==status||Tenant.AUTH_COMPANY_FAIL==status)){//企业
            RealnameCorp realnameCorp = null;
            try{
                realnameCorp = realnameCorpService.findById(uid);
                tenant = realnameCorp.getTenant();
                if(tenant!=null){
                    statusT = tenant.getIsRealAuth();
                    realnameCorp.setStatus(status);
                    if(statusT==Tenant.AUTH_ONESELF_SUCCESS||statusT==Tenant.AUTH_UPGRADE_WAIT||statusT==Tenant.AUTH_UPGRADE_FAIL){
                        if(status==Tenant.AUTH_COMPANY_FAIL){
                            status=Tenant.AUTH_UPGRADE_FAIL;
                        }else if(status==Tenant.AUTH_COMPANY_SUCCESS){
                            status=Tenant.AUTH_UPGRADE_SUCCESS;
                        }
                    }
                    tenant.setTenantName(realnameCorp.getName());
                    tenant.setIsRealAuth(status);
                    realnameCorp.setTenant(tenant);
                    realnameCorpService.save(realnameCorp);
                    tenant = tenantService.save(tenant);
                }else{
                    restResponse = RestResponse.failed("0","租户不存在");
                }
            }catch (Exception e){}
        }else{
            restResponse = RestResponse.failed("0","参数值不对");
        }
        if(restResponse==null){
            restResponse = RestResponse.success(tenant.getIsRealAuth());
        }
        return restResponse;
    }
}
