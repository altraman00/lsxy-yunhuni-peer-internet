package com.lsxy.app.portal.rest.number;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.resourceTelenum.model.TestNumBind;
import com.lsxy.yunhuni.api.resourceTelenum.service.TestNumBindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 测试号码绑定
 * Created by zhangxb on 2016/7/2.
 */
@RequestMapping("/rest/test_num_bind/")
@RestController
public class TestNumBindController extends AbstractRestController {
    @Autowired
    TestNumBindService testMobileBindService;
    @Autowired
    TenantService tenantService;
    /**
     * 获取用户下的所有测试号码
     * @return
     * @throws MatchMutiEntitiesException
     */
    @RequestMapping("/list")
    public RestResponse list() throws MatchMutiEntitiesException {
        String userName = getCurrentAccountUserName();
        List<TestNumBind> list = testMobileBindService.findAll(userName);
        return RestResponse.success(list);
    }

    /**
     * 解除绑定号码
     * @param number
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @RequestMapping("/disbind")
    public RestResponse disbind(String number) throws InvocationTargetException, IllegalAccessException, MatchMutiEntitiesException {
        String userName = getCurrentAccountUserName();
        List<TestNumBind> testMobileBindList = testMobileBindService.findByNumber(userName,number);
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
        List<TestNumBind> testMobileBindList = testMobileBindService.findByNumber(userName,number);
        if(testMobileBindList.size()>0){
            return RestResponse.failed("0020","号码已被绑定");
        }
        List<TestNumBind> list = testMobileBindService.findAll(userName);
        if(list.size()>5){
            return RestResponse.failed("0030","绑定号码已超过5个");
        }
        Tenant tenant = tenantService.findTenantByUserName(userName);
        TestNumBind testNumBind = new TestNumBind();
        testNumBind.setTenant(tenant);
        testNumBind.setNumber(number);
        testNumBind = testMobileBindService.save(testNumBind);
        return RestResponse.success(testNumBind);
    }

}
