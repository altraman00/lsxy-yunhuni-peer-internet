package com.lsxy.app.oc.rest.config;

import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.config.service.LineGatewayService;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourcesRent;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourceTelenumService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by zhangxb on 2016/10/29.
 */
@Api(value = "租户号码管理", description = "租户中心相关的接口" )
@RequestMapping("/config/rent")
@RestController
public class ResourcesRentController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(ResourcesRentController.class);
    @Autowired
    ResourcesRentService resourcesRentService;
    @Autowired
    ResourceTelenumService resourceTelenumService;
    @Autowired
    TenantService tenantService;
    @Autowired
    LineGatewayService lineGatewayService;
    /**
     * 获取租户的呼入号码分页数据
     * @param pageNo
     * @param pageSize
     * @return
     * @throws MatchMutiEntitiesException
     */
    @ApiOperation(value = "号码列表")
    @RequestMapping(value = "/list/{id}",method = RequestMethod.GET)
    public RestResponse pageList(@ApiParam(name = "id",value = "租户id") @PathVariable String id,
                                 @ApiParam(name = "pageNo",value = "第几页")@RequestParam(defaultValue = "1") Integer pageNo,
                                 @ApiParam(name = "pageSize",value = "每页记录数")@RequestParam(defaultValue = "20") Integer pageSize){
        Tenant tenant = tenantService.findById(id);
        if(tenant==null|| StringUtils.isEmpty(tenant.getId())){
            return RestResponse.failed("0000","租户不存在");
        }
        //获取该租户下的所有号码信息
        Page<ResourcesRent> page = resourcesRentService.pageListByTenantId(tenant.getId(),pageNo,pageSize);
        List<ResourcesRent> list1 = page.getResult();
        List<ResourcesRent> list2 = page.getResult();
        for(int i = 0; i < list1.size();i ++){
            ResourcesRent rent = list1.get(i);
            ResourceTelenum telenum = rent.getResourceTelenum();
            if(telenum !=null && StringUtils.isNotEmpty(telenum.getLineId())){
                telenum.setLine(lineGatewayService.findById(telenum.getLineId()));
                rent.setResourceTelenum(telenum);
            }
            list2.add(rent);
        }//long start, long totalSize, int pageSize, List<T> data
        return RestResponse.success(new Page(page.getStartIndex(),page.getTotalCount(),page.getPageSize(),list2));
    }
//    @ApiOperation(value = "号码列表")
//    @RequestMapping(value = "/list/{id}",method = RequestMethod.GET)
//    public RestResponse pageList(@ApiParam(name = "线路id",value = "号码id") @PathVariable String id,
//                                 @ApiParam(name = "pageNo",value = "第几页")@RequestParam(defaultValue = "1") Integer pageNo,
//                                 @ApiParam(name = "pageSize",value = "每页记录数")@RequestParam(defaultValue = "20") Integer pageSize){
//        Tenant tenant = tenantService.findById(id);
//        if(tenant==null|| StringUtils.isEmpty(tenant.getId())){
//            return RestResponse.failed("0000","租户不存在");
//        }
//        //获取该租户下的所有号码信息
//        Page<ResourcesRent> page = resourcesRentService.pageListByTenantId(tenant.getId(),pageNo,pageSize);
//        return RestResponse.success(page);
//    }
}
