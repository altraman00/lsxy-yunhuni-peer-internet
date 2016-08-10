package com.lsxy.app.oc.rest.tenant;

import com.lsxy.framework.api.message.model.AccountMessage;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2016/8/10.
 */
@RestController
@RequestMapping("/tenant")
public class IndexController {

    @Autowired
    private TenantService tenantService;

    @ApiOperation(value = "租户列表")
    @RequestMapping(value = "/tenants",method = RequestMethod.GET)
    public RestResponse tenants(){
        Page<AccountMessage> list = tenantService.pageListByAccountId(userName,pageNo,pageSize);
        return RestResponse.success(list);
    }
}
