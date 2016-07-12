package com.lsxy.app.portal.rest.account;

import com.lsxy.framework.api.tenant.model.Tenant;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Tandy on 2016/6/14.
 */
@RestController
@RequestMapping("/tenant")
public class TenantController {

    @RequestMapping("/")
    public Tenant getTenantInfo(){
        return new Tenant();
    }
}
