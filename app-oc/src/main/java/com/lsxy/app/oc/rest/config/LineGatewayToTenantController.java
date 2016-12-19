package com.lsxy.app.oc.rest.config;

import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.app.oc.rest.config.vo.EditPriorityVo;
import com.lsxy.app.oc.rest.config.vo.IdsVo;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.config.model.LineGatewayToTenant;
import com.lsxy.yunhuni.api.config.service.LineGatewayService;
import com.lsxy.yunhuni.api.config.service.LineGatewayToTenantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by zhangxb on 2016/10/29.
 */
@Api(value = "私有线路", description = "用户中心相关的接口" )
@RequestMapping("/config/tenant")
@RestController
public class LineGatewayToTenantController  extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(LineGatewayToTenantController.class);
    @Autowired
    LineGatewayToTenantService lineGatewayToTenantService;
    @Autowired
    LineGatewayService lineGatewayService;
    @Autowired
    TenantService tenantService;

    @RequestMapping(value = "/plist/{id}",method = RequestMethod.GET)
    @ApiOperation(value = "获取分页数据")
    public RestResponse pList(
            @ApiParam(name = "id",value = "租户id")  @PathVariable String id,
            @ApiParam(name = "pageNo",value = "第几页")  @RequestParam(defaultValue = "1")Integer pageNo,
            @ApiParam(name = "pageSize",value = "每页记录数")  @RequestParam(defaultValue = "20")Integer pageSize
//            @ApiParam(name = "operator",value = "运营商 中国电信；中国移动；中国联通") @RequestParam(required = false)String operator,
//            @ApiParam(name = "isThrough",value = "是否透传 1支持透传 0不支持透传")@RequestParam(required = false) String isThrough,
//            @ApiParam(name = "status",value = "状态 1可用 0不可用") @RequestParam(required = false)String status,
////            @ApiParam(name = "isPublicLine",value = "1:全局线路;0:租户专属线路") @RequestParam(required = false)String isPublicLine,
//            @ApiParam(name = "order",value = "quality:1按质量降序，quality:0按质量升序,capacity:1按容量降序capacity:0按容量降序") @RequestParam(required = false)String order
    ){
        Page page= lineGatewayToTenantService.getPage(id,pageNo,pageSize);
        return RestResponse.success(page);
    }
    @RequestMapping(value = "/line/plist/{id}",method = RequestMethod.GET)
    @ApiOperation(value = "获取加入Tenant的线路")
    public RestResponse pListByLine(
            @ApiParam(name = "id",value = "租户id")  @PathVariable String id,
            @ApiParam(name = "pageNo",value = "第几页")  @RequestParam(defaultValue = "1")Integer pageNo,
            @ApiParam(name = "pageSize",value = "每页记录数")  @RequestParam(defaultValue = "20")Integer pageSize,
            @ApiParam(name = "operator",value = "运营商") @RequestParam(required = false)String operator,
            @ApiParam(name = "lineNumber",value = "线路标识")@RequestParam(required = false) String lineNumber
    ){
        Page page= lineGatewayService.getNotTenantPage(pageNo,pageSize,id,operator,lineNumber);
        return RestResponse.success(page);
    }
    @ApiOperation(value = "将线路加入私有线路")
    @RequestMapping(value = "/add/{id}",method = RequestMethod.POST)
    public RestResponse addTenantLine(
            @ApiParam(name = "id",value = "租户id") @PathVariable String id,
            @ApiParam(name = "idsVo",value = "线路集合") @RequestBody IdsVo idsVo){
        if(idsVo.getIds().length==0){
            return RestResponse.failed("0000", "无绑定线路id");
        }
        Tenant tenant = tenantService.findById(id);
        if(tenant!=null&& StringUtils.isNotEmpty(tenant.getId())){
            for(int i=0;i<idsVo.getIds().length;i++) {
                LineGateway lineGateway = lineGatewayService.findById(idsVo.getIds()[i]);
                if(lineGateway==null||StringUtils.isEmpty(lineGateway.getId())){
                    return RestResponse.failed("0000", "成功执行["+(i-1)+"]条，第["+i+"]条错误：线路不存在");
                }
                long re1 = lineGatewayToTenantService.findByLineIdAndTenantId(lineGateway.getId(),id);
                if (re1 > 0) {
                    return RestResponse.failed("0000", "成功执行["+(i-1)+"]条，第["+i+"]条错误：已存在");
                } else {
                    //获取当前最大编号
                    int re2 = lineGatewayToTenantService.getMaxPriority(tenant.getId());
                    re2++;
                    //新建关系
                    LineGatewayToTenant lineGatewayToTenant = new LineGatewayToTenant();
                    lineGatewayToTenant.setLineGateway(lineGateway);
                    lineGatewayToTenant.setTenantId(tenant.getId());
                    lineGatewayToTenant.setPriority(re2);
                    lineGatewayToTenantService.save(lineGatewayToTenant);
                }
            }
        }else{
            return RestResponse.failed("0000","租户不存在");
        }
        return RestResponse.success("添加成功");
    }

    @ApiOperation(value = "将线路移除私有线路")
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public RestResponse removeTenantLine(@ApiParam(name = "id",value = "全局线路id") @PathVariable String id) throws InvocationTargetException, IllegalAccessException {
        LineGatewayToTenant lineGatewayToTenant = lineGatewayToTenantService.findById(id);
        if(lineGatewayToTenant!=null&& StringUtils.isNotEmpty(lineGatewayToTenant.getId())){
            //删除线路关系
            lineGatewayToTenantService.removeTenantLine(id,lineGatewayToTenant.getTenantId());
        }else{
            return RestResponse.failed("0000","线路不存在");
        }
        return RestResponse.success("删除成功");
    }
    @ApiOperation(value = "修改优先级")
    @RequestMapping(value = "/edit/priority/{id}",method = RequestMethod.PUT)
    public RestResponse modify(@ApiParam(name = "id",value = "线路id")
                               @PathVariable String id,@RequestBody EditPriorityVo editPriorityVo){
        int o2 = 0;
        try{ o2=Integer.valueOf(editPriorityVo.getPriority());}catch (Exception e){
            return RestResponse.failed("0000","目标优先级只能为数字");
        }
        if(o2==0||o2<0){
            return RestResponse.failed("0000","目标优先级必须大于0");
        }
        LineGatewayToTenant lineGatewayToTenant = lineGatewayToTenantService.findById(id);
        int o3 = lineGatewayToTenantService.getMaxPriority(lineGatewayToTenant.getTenantId());
        if(o2>o3){
            return RestResponse.failed("0000","目标优先级不能超过当前最大优先级");
        }
        if(lineGatewayToTenant!=null&&StringUtils.isNotEmpty(lineGatewayToTenant.getId())) {
            int o1 = Integer.valueOf(lineGatewayToTenant.getPriority());
            if(o1==o2){
                return RestResponse.failed("0000","目标优先级和当前优先级一致");
            }
            int re = lineGatewayToTenantService.upPriority(o1,o2,lineGatewayToTenant.getId());
            if(re==-1){
                return RestResponse.failed("0000","修改失败，请重试");
            }
        }else{
            return RestResponse.failed("0000","线路不存在");
        }
        return RestResponse.success("修改成功");
    }

}