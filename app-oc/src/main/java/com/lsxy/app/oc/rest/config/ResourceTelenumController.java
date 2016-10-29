package com.lsxy.app.oc.rest.config;

import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.app.oc.rest.config.vo.TelnumTVo;
import com.lsxy.app.oc.rest.message.MessageVo;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.core.utils.EntityUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.config.service.LineGatewayService;
import com.lsxy.yunhuni.api.config.service.TelnumLocationService;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourcesRent;
import com.lsxy.yunhuni.api.resourceTelenum.model.TelnumToLineGateway;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourceTelenumService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import com.lsxy.yunhuni.api.resourceTelenum.service.TelnumToLineGatewayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhangxb on 2016/10/26.
 */
@Api(value = "号码管理", description = "配置中心相关的接口" )
@RequestMapping("/config/telnum")
@RestController
public class ResourceTelenumController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(ResourceTelenumController.class);
    @Autowired
    ResourceTelenumService resourceTelenumService;
    @Autowired
    LineGatewayService lineGatewayService;
    @Autowired
    TelnumLocationService telnumLocationService;
    @Autowired
    TelnumToLineGatewayService telnumToLineGatewayService;
    @Autowired
    TenantService tenantService;
    @Autowired
    ResourcesRentService resourcesRentService;

    @RequestMapping(value = "/plist",method = RequestMethod.GET)
    @ApiOperation(value = "获取分页数据")
    public RestResponse pList(
            @ApiParam(name = "pageNo",value = "第几页")  @RequestParam(defaultValue = "1")Integer pageNo,
            @ApiParam(name = "pageSize",value = "每页记录数")  @RequestParam(defaultValue = "20")Integer pageSize,
            @ApiParam(name = "operator",value = "运营商") @RequestParam(required = false)String operator,
            @ApiParam(name = "number",value = "手机号码")@RequestParam(required = false) String number
    ){
        Page page= resourceTelenumService.getPage(pageNo,pageSize,operator,number);
        return RestResponse.success(page);
    }
    @RequestMapping(value = "/notline/plist/{id}",method = RequestMethod.GET)
    @ApiOperation(value = "获取可透传号码")
    public RestResponse pListByLine(
            @ApiParam(name = "id",value = "线路id")  @PathVariable String id,
            @ApiParam(name = "pageNo",value = "第几页")  @RequestParam(defaultValue = "1")Integer pageNo,
            @ApiParam(name = "pageSize",value = "每页记录数")  @RequestParam(defaultValue = "20")Integer pageSize,
            @ApiParam(name = "operator",value = "运营商") @RequestParam(required = false)String operator,
            @ApiParam(name = "number",value = "手机号码")@RequestParam(required = false) String number
    ){
        LineGateway lineGateway = lineGatewayService.findById(id);
        if(lineGateway==null|| StringUtils.isEmpty(lineGateway.getId())){
            return RestResponse.failed("0000","线路不存在");
        }
        Page page= resourceTelenumService.getPageByNotLine(id,lineGateway.getAreaCode(),pageNo,pageSize,operator,number);
        return RestResponse.success(page);
    }

    @ApiOperation(value = "新建号码")
    @RequestMapping(value = "/new",method = RequestMethod.POST)
    public RestResponse create(
            @RequestBody TelnumTVo telnumTVo){
        String re = vaiVo(telnumTVo);
        if(StringUtils.isNotEmpty(re)){
            return RestResponse.failed("0000","新增线路失败:"+re);
        }
        //如果有选择线路，则检验线路是否存在
        LineGateway lineGateway = null;
        if(StringUtils.isNotEmpty(telnumTVo.getLineId())){
            lineGateway = lineGatewayService.findById(telnumTVo.getLineId());
            if(lineGateway==null||StringUtils.isEmpty(lineGateway.getId())){
                return RestResponse.failed("0000","所选线路不存在");
            }
        }
        //如果有选择租户，则检验租户是否存在
        Tenant tenant = null;
        if(StringUtils.isNotEmpty(telnumTVo.getTenantId())){
            tenant = tenantService.findById(telnumTVo.getTenantId());
            if(tenant==null || StringUtils.isEmpty(tenant.getId())){
                return RestResponse.failed("0000","所选租户不存在");
            }
        }
        //拷贝对象
        ResourceTelenum resourceTelenum = new ResourceTelenum();
        try {
            EntityUtils.copyProperties(resourceTelenum,telnumTVo);
        }catch (Exception e){
            return RestResponse.failed("0000","新增线路失败");
        }
        resourceTelenum.setTenant(tenant);//绑定租户
        resourceTelenum.setStatus(0);//设置状态可用
        resourceTelenum.setUsable("1");//设置可用
        //如果绑定线路的话，需要为号码设置区号
        if(lineGateway!=null&&StringUtils.isNotEmpty(lineGateway.getId())) {
            resourceTelenum.setAreaId(lineGateway.getAreaId());
        }
        //创建号码
        resourceTelenum = resourceTelenumService.save(resourceTelenum);
        //创建线路号码关联
        if(lineGateway!=null&&StringUtils.isNotEmpty(lineGateway.getId())){
            //判断线路号码是否已关联，提示用户自己去更新；未关联，直接产生关联
            TelnumToLineGateway telnumToLineGateway = telnumToLineGatewayService.findByTelNumberAndLineId(resourceTelenum.getTelNumber(),lineGateway.getId());
            if(telnumToLineGateway!=null && StringUtils.isNotEmpty(telnumToLineGateway.getId())){
                RestResponse.success("创建成功,号码和线路关系已存在");
            }else{
                telnumToLineGateway = new TelnumToLineGateway(resourceTelenum.getTelNumber(), lineGateway.getId(), resourceTelenum.getIsDialing(), resourceTelenum.getIsCalled(),resourceTelenum.getIsThrough(), resourceTelenum.getType());
                telnumToLineGatewayService.save(telnumToLineGateway);
            }
        }
        //判断是否需要添加号码租户的关系
        if(tenant!=null &&StringUtils.isNotEmpty(tenant.getId())){
            ResourcesRent resourcesRent = new ResourcesRent();
            resourcesRent.setTenant(tenant);
            resourcesRent.setResourceTelenum(resourceTelenum);
            resourcesRent.setResName("号码资源");
            resourcesRent.setResData(resourceTelenum.getTelNumber());
            resourcesRent.setResType("1");//号码资源
            resourcesRent.setRentDt(new Date());
            resourcesRent.setRentStatus(ResourcesRent.RENT_STATUS_UNUSED);//没有在使用，但号码属于租户
            resourcesRentService.save(resourcesRent);
        }
        return RestResponse.success("创建成功");
    }
    private String vaiVo(TelnumTVo telnumTVo){
        if(!Arrays.asList(ResourceTelenum.OPERATORS).contains(telnumTVo.getOperator())){
            return "运营商错误";
        }
        String areaName = telnumLocationService.getAreaNameByAreaCode(telnumTVo.getAreaCode());
        if(StringUtils.isEmpty(areaName)){
            return "归属地区号不存在";
        }
        String[] is = {"0","1"};
        if(!Arrays.asList(is).contains(telnumTVo.getIsThrough())){
            return "可透传错误";
        }
        if(!Arrays.asList(is).contains(telnumTVo.getIsCalled())){
            return "可被叫错误";
        }
        if(!Arrays.asList(is).contains(telnumTVo.getIsDialing())){
            return "可主叫错误";
        }
        Pattern p = Pattern.compile("^[0-9]{1,32}$");
        Matcher matcher = p.matcher(telnumTVo.getTelNumber());
        if (!matcher.matches()) {
            return "号码格式错误";
        }
        //验证号码和呼叫URI
        ResourceTelenum temp = resourceTelenumService.findByTelNumber(telnumTVo.getTelNumber());
        if(temp!=null){
            return "该号码已存在号码池中";
        }
        String temp2 = resourceTelenumService.findNumByCallUri(telnumTVo.getCallUri());
        if(StringUtils.isNotEmpty(temp2)){
            return "该呼出URI已存在号码池中";
        }
        return "";
    }
}
