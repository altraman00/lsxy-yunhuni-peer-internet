package com.lsxy.app.portal.rest;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yuhuni.api.resourceTelenum.model.TestMobileBind;
import com.lsxy.yuhuni.api.resourceTelenum.service.TestMobileBindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static javafx.scene.input.KeyCode.Y;

/**
 * 测试号码绑定
 * Created by zhangxb on 2016/7/2.
 */
@RequestMapping("/rest/test_mobile_bind/")
@RestController
public class TestMobileBindController extends AbstractRestController {
    @Autowired
    TestMobileBindService testMobileBindService;
    @Autowired
    TenantService tenantService;
    /**
     * 获取用户下的所有测试号码
     * @return
     * @throws MatchMutiEntitiesException
     */
    @RequestMapping("/find_all")
    public RestResponse findAll() throws MatchMutiEntitiesException {
        String userName = getCurrentAccountUserName();
        List<TestMobileBind> list = testMobileBindService.findAll(userName);
        return RestResponse.success(list);
    }

    /**
     * 解除绑定号码
     * @param number
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @RequestMapping("/delete")
    public RestResponse delete(String number) throws InvocationTargetException, IllegalAccessException, MatchMutiEntitiesException {
        String userName = getCurrentAccountUserName();
        List<TestMobileBind> testMobileBindList = testMobileBindService.findByNumber(userName,number);
        for(int i=0;i<testMobileBindList.size();i++) {
            testMobileBindService.delete(testMobileBindList.get(i));
        }
        return RestResponse.success(testMobileBindList.size());
    }
    /**
     * 测试号码绑定
     * @param number
     * @return
     */
    @RequestMapping("/save")
    public RestResponse save(String number) throws MatchMutiEntitiesException {
        String userName = getCurrentAccountUserName();
        List<TestMobileBind> testMobileBindList = testMobileBindService.findByNumber(userName,number);
        if(testMobileBindList.size()>0){
            return RestResponse.failed("0020","号码已被绑定");
        }
        List<TestMobileBind> list = testMobileBindService.findAll(userName);
        if(list.size()>5){
            return RestResponse.failed("0030","绑定号码已超过5个");
        }
        Tenant tenant = tenantService.findTenantByUserName(userName);
        TestMobileBind testMobileBind = new TestMobileBind();
        testMobileBind.setTenant(tenant);
        testMobileBind.setNumber(number);
        testMobileBind = testMobileBindService.save(testMobileBind);
        return RestResponse.success(testMobileBind);
    }

}
