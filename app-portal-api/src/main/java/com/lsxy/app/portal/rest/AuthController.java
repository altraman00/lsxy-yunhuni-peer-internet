package com.lsxy.app.portal.rest;

import com.lsxy.framework.api.tenant.model.RealnameCorp;
import com.lsxy.framework.api.tenant.model.RealnamePrivate;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.api.tenant.service.RealnameCorpService;
import com.lsxy.framework.api.tenant.service.RealnamePrivateService;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static sun.java2d.cmm.ColorTransform.In;

/**
 * Created by zhangxb on 2016/6/28.
 */
@RequestMapping("/rest/account/auth")
@RestController
public class AuthController {
    @Autowired
    private RealnamePrivateService realnaePrivateService;
    @Autowired
    private RealnameCorpService realnameCorpService;
    @Autowired
    private TenantService tenantService;
    @Autowired
    private AccountService accountService;

    /**
     * 个人实名认证方法
     * @param userName 用户名
     * @param status 认证状态
     * @param name 姓名
     * @param idNumber 认证号码
     * @param idPhoto 认证照片
     * @param idType 认证类型
     * @return
     */
    @RequestMapping("/privateAuth")
    public RestResponse privateAuth(String userName,String status,String name,String idNumber,String idPhoto,String idType){
        //获取租户对象
        Tenant tenant = accountService.findByUserName(userName).getTenant();
        //查看是否已进行过实名认证
        RealnamePrivate realnamePrivate =  realnaePrivateService.findByTenantId(tenant.getId());
        if(realnamePrivate==null) {
            realnamePrivate = new RealnamePrivate(name, idNumber, idPhoto, tenant.getId(), idType);
        }else{
            realnamePrivate.setName(name);
            realnamePrivate.setIdNumber(idNumber);
            realnamePrivate.setIdPhoto(idPhoto);
            realnamePrivate.setIdType(idType);
        }
        //保存到数据库
        realnamePrivate = realnaePrivateService.save(realnamePrivate);
        //修改租户中实名认证状态
        tenant.setIsRealAuth(Integer.valueOf(status));
        tenantService.save(tenant);
        return RestResponse.success(realnamePrivate);
    }

    /**
     * 企业实名认证
     * @param userName 用户名
     * @param status 认证状态
     * @param corpName 企业名字
     * @param addr 企业地址
     * @param fieldCode 企业行业
     * @param authType 认证类型
     * @param type01Prop01 统一社会信用代码
     * @param type01Prop02 营业执照（照片地址）
     * @param type02Prop01 注册号
     * @param type02Prop02 税务登记号
     * @param type03Prop02 税务登记（照片地址）
     * @return
     */
    @RequestMapping("/corpAuth")
    public RestResponse corpAuth( String userName,String status,String corpName,String addr,String fieldCode,String authType,String type01Prop01,String type01Prop02,String type02Prop01,String type02Prop02,String type03Prop02){
        //获取租户对象
        Tenant tenant = accountService.findByUserName(userName).getTenant();
        //查看是否已进行过实名认证
        RealnameCorp realnameCorp = realnameCorpService.findByTenantId(tenant.getId());
        if(realnameCorp==null){
            realnameCorp = new RealnameCorp(tenant.getId(), corpName, addr, fieldCode, authType, type01Prop01, type01Prop02, type02Prop01, type02Prop02, type03Prop02);
        }else{
            realnameCorp.setName(corpName);
            realnameCorp.setAddr(addr);
            realnameCorp.setFieldCode(fieldCode);
            realnameCorp.setAuthType(authType);
            realnameCorp.setType01Prop01(type01Prop01);
            realnameCorp.setType01Prop02(type01Prop02);
            realnameCorp.setType02Prop01(type02Prop01);
            realnameCorp.setType02Prop02(type02Prop02);
            realnameCorp.setType03Prop02(type03Prop02);
        }
        //保存到数据库
        realnameCorp = realnameCorpService.save(realnameCorp);
        //修改租户中实名认证状态
        tenant.setIsRealAuth(Integer.valueOf(status));
        tenantService.save(tenant);
        return RestResponse.success(realnameCorp);
    }
}
