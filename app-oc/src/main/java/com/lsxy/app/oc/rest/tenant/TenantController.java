package com.lsxy.app.oc.rest.tenant;

import com.lsxy.framework.api.tenant.model.TenantVO;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Created by Administrator on 2016/8/10.
 */
@Api(value = "租户中心", description = "租户中心相关的接口" )
@RestController
@RequestMapping("/tenant")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @ApiOperation(value = "租户列表")
    @RequestMapping(value = "/tenants",method = RequestMethod.GET)
    public RestResponse tenants(
    @RequestParam(required = false) String name,
    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") Date begin,
    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM") Date end,
    @ApiParam(name = "authStatus",value = "认证状态，1已认证，0未认证")
    @RequestParam(required = false) Integer authStatus,
    @ApiParam(name = "accStatus",value = "账号状态，2正常/启用，1被锁定/禁用")
    @RequestParam(required = false) Integer accStatus,
    @RequestParam(required = false,defaultValue = "1") Integer pageNo,
    @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        if(begin!=null){
            begin = DateUtils.getFirstTimeOfMonth(begin);
        }
        if(end!=null){
            end = DateUtils.getLastTimeOfMonth(end);
        }
        Page<TenantVO> list = tenantService.pageListBySearch(name,begin,end,authStatus,accStatus,pageNo,pageSize);
        return RestResponse.success(list);
    }

    @ApiOperation(value = "租户状态禁用/启用")
    @RequestMapping(value = "/tenants/{id}",method = RequestMethod.PATCH)
    public RestResponse tenants(@PathVariable String id,@RequestParam Integer status){
        return RestResponse.success(true);
    }
}
