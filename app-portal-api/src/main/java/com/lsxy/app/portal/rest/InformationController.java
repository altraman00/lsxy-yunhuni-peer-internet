package com.lsxy.app.portal.rest;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhangxb on 2016/6/28.
 * 基础资料
 */
@RequestMapping("/rest/account/information")
@PreAuthorize("hasAuthority('ROLE_TENANT_USER')")
@RestController
public class InformationController extends AbstractRestController {

    @Autowired
    private AccountService accountService;

    /**
     * 根据用户名获取用户对象
     * @param userName 用户标识
     * @param phone 手机号码
     * @param industry 行业
     * @param business 业务
     * @param url 网站
     * @param province 省份
     * @param city 城市
     * @param address 地址
     * @return
     */
    @RequestMapping("/updateInformation")
    public RestResponse update(String userName,String phone,String industry,String business,String url,String province,String city,String address ){
        Account account = accountService.findByUserName(userName);
        account.setPhone(phone);
        account.setIndustry(industry);
        account.setBusiness(business);
        account.setUrl(url);
        account.setProvince(province);
        account.setCity(city);
        account.setAddress(address);
        account  = accountService.save(account);
        return RestResponse.success(account);
    }

}
