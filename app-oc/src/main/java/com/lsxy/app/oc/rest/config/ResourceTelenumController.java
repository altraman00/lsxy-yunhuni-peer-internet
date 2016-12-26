package com.lsxy.app.oc.rest.config;

import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.app.oc.rest.config.vo.LineVo;
import com.lsxy.app.oc.rest.config.vo.TelnumTEditVo;
import com.lsxy.app.oc.rest.config.vo.TelnumTVo;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.core.utils.BeanUtils;
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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
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
            @ApiParam(name = "number",value = "手机号码")@RequestParam(required = false) String number,
            @ApiParam(name = "operator",value = "运营商 中国电信 中国移动 中国联通 ") @RequestParam(required = false)String operator,
            @ApiParam(name = "isThrough",value = "是否支持透传，1是，0否")@RequestParam(required = false) String isThrough,
            @ApiParam(name = "status",value = "状态 1启用0禁用")@RequestParam(required = false) String status
    ){
        Page page= resourceTelenumService.getPage(pageNo,pageSize,number,operator,isThrough,status);
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
    @ApiOperation(value = "释放号码")
    @RequestMapping(value = "/release/{id}",method = RequestMethod.PUT)
    public RestResponse release(@ApiParam(name = "id",value = "号码id") @PathVariable String id) {
        ResourceTelenum resourceTelenum = resourceTelenumService.findById(id);
        if(resourceTelenum==null||StringUtils.isEmpty(resourceTelenum.getId())){
            return RestResponse.failed("0000","号码不存在");
        }
        ResourcesRent resourcesRent = resourcesRentService.findByResourceTelenumId(resourceTelenum.getId());
        if(resourcesRent==null||StringUtils.isEmpty(resourcesRent.getId())){
            return RestResponse.failed("0000","号码和租户不存在对于关系");
        }
        resourceTelenumService.release(id);
        return RestResponse.success("释放号码成功");
    }
    @ApiOperation(value = "修改号码")
    @RequestMapping(value = "/edit/{id}",method = RequestMethod.PUT)
    public RestResponse edit(@ApiParam(name = "id",value = "号码id") @PathVariable String id, @RequestBody TelnumTEditVo telnumTVo) {
        ResourceTelenum resourceTelenum = resourceTelenumService.findById(id);
        if(resourceTelenum==null||StringUtils.isEmpty(resourceTelenum.getId())){
            return RestResponse.failed("0000","号码不存在");
        }
        if(StringUtils.isEmpty(telnumTVo.getOperator())){
            return RestResponse.failed("0000","运营商错误");
        }
        if (!Arrays.asList(ResourceTelenum.OPERATORS).contains(telnumTVo.getOperator())) {
            return RestResponse.failed("0000","运营商错误");
        }
        if(StringUtils.isEmpty(telnumTVo.getAreaCode())){
            return RestResponse.failed("0000","归属地区号不存在");
        }
        String areaName = telnumLocationService.getAreaNameByAreaCode(telnumTVo.getAreaCode());
        if (StringUtils.isEmpty(areaName)) {
            return RestResponse.failed("0000","归属地区号不存在");
        }
        boolean isEditNum = false;
        String telnum1 = resourceTelenum.getTelNumber();
        if(StringUtils.isNotEmpty(telnumTVo.getTelNumber())&&!resourceTelenum.getTelNumber().equals(telnumTVo.getTelNumber())){
            //验证号码和呼叫URI
            ResourceTelenum temp = resourceTelenumService.findByTelNumber(telnumTVo.getTelNumber());
            if(temp!=null){
                return RestResponse.failed("0000", "该号码已存在号码池中");
            }
            isEditNum = true;
        }
        if(StringUtils.isNotEmpty(telnumTVo.getCallUri())&&!resourceTelenum.getCallUri().equals(telnumTVo.getCallUri())) {
            String temp2 = resourceTelenumService.findNumByCallUri(telnumTVo.getCallUri());
            if (StringUtils.isNotEmpty(temp2)) {
                return RestResponse.failed("0000", "该呼出URI已存在号码池中");
            }
        }
        Tenant tenant = null;
        int tenantType = 0;
        if(StringUtils.isNotEmpty(telnumTVo.getTenantId())&&(resourceTelenum.getTenant()==null)){
            tenant = tenantService.findById(telnumTVo.getTenantId());
            if(tenant==null || StringUtils.isEmpty(tenant.getId())){
                return RestResponse.failed("0000","所选租户不存在");
            }
            tenantType = 1;//原来没有租户，新加租户
        }else if(StringUtils.isNotEmpty(telnumTVo.getTenantId())&&!resourceTelenum.getTenant().getId().equals(telnumTVo.getTenantId())){
            tenant = tenantService.findById(telnumTVo.getTenantId());
            if(tenant==null || StringUtils.isEmpty(tenant.getId())){
                return RestResponse.failed("0000","所选租户不存在");
            }
            tenantType = 2;//原来有租户，修改原来租户资源关系，新增租户资源关系
        }
        //拷贝非空参数
        try {
            BeanUtils.copyProperties2(resourceTelenum,telnumTVo,false);
        }catch (Exception e){
            return RestResponse.failed("0000","修改线路失败");
        }
        resourceTelenumService.editNum(resourceTelenum,tenantType,isEditNum,tenant,telnum1,telnumTVo.getTelNumber());
        return RestResponse.success("修改号码成功");
    }
    @ApiOperation(value = "新建号码")
    @RequestMapping(value = "/new",method = RequestMethod.POST)
    public RestResponse create(
            @RequestBody TelnumTVo telnumTVo){
        if(StringUtils.isEmpty(telnumTVo.getOperator())){
            return RestResponse.failed("0000","运营商错误");
        }
        if (!Arrays.asList(ResourceTelenum.OPERATORS).contains(telnumTVo.getOperator())) {
            return RestResponse.failed("0000","运营商错误");
        }
        if(StringUtils.isEmpty(telnumTVo.getAreaCode())){
            return RestResponse.failed("0000","归属地区号不存在");
        }
        String areaName = telnumLocationService.getAreaNameByAreaCode(telnumTVo.getAreaCode());
        if (StringUtils.isEmpty(areaName)) {
            return RestResponse.failed("0000","归属地区号不存在");
        }
        String[] is = {"0","1"};
        if(StringUtils.isNotEmpty(telnumTVo.getIsThrough()+"")&&!Arrays.asList(is).contains(telnumTVo.getIsThrough()+"")){
            return RestResponse.failed("0000","可透传错误");
        }
        if(StringUtils.isNotEmpty(telnumTVo.getIsCalled()+"")&&!Arrays.asList(is).contains(telnumTVo.getIsCalled()+"")){
            return RestResponse.failed("0000","可被叫错误");
        }
        if(StringUtils.isNotEmpty(telnumTVo.getIsDialing()+"")&&!Arrays.asList(is).contains(telnumTVo.getIsDialing()+"")){
            return RestResponse.failed("0000","可主叫错误");
        }
        if(StringUtils.isEmpty(telnumTVo.getTelNumber())){
            return RestResponse.failed("0000","号码格式错误");
        }
        Pattern p = Pattern.compile("^[0-9]{1,32}$");
        Matcher matcher = p.matcher(telnumTVo.getTelNumber());
        if (!matcher.matches()) {
            return RestResponse.failed("0000","号码格式错误");
        }
        if(StringUtils.isEmpty(telnumTVo.getCallUri())){
            return RestResponse.failed("0000","呼叫URI错误");
        }
        //验证号码和呼叫URI
        ResourceTelenum temp = resourceTelenumService.findByTelNumber(telnumTVo.getTelNumber());
        if(temp!=null){
            return RestResponse.failed("0000", "该号码已存在号码池中");
        }
        String temp2 = resourceTelenumService.findNumByCallUri(telnumTVo.getCallUri());
        if(StringUtils.isNotEmpty(temp2)){
            return RestResponse.failed("0000", "该呼出URI已存在号码池中");
        }
        //如果有选择线路，则检验线路是否存在
        LineGateway lineGateway = null;
        if(StringUtils.isNotEmpty(telnumTVo.getLineId())){
            lineGateway = lineGatewayService.findById(telnumTVo.getLineId());
            if(lineGateway==null||StringUtils.isEmpty(lineGateway.getId())){
                return RestResponse.failed("0000","所选线路不存在");
            }
        }else{
            //无线路时
            telnumTVo.setIsCalled(0);
            telnumTVo.setIsDialing(0);
            telnumTVo.setIsThrough(0);
        }
        //如果有选择租户，则检验租户是否存在
        Tenant tenant = null;
        //线路是租户自带线路时，必选租户
//        if("0".equals(telnumTVo.getType())){
//            if (StringUtils.isEmpty(telnumTVo.getTenantId())) {
//                return RestResponse.failed("0000", "所选租户不能为空");
//            }
//            tenant = tenantService.findById(telnumTVo.getTenantId());
//            if (tenant == null || StringUtils.isEmpty(tenant.getId())) {
//                return RestResponse.failed("0000", "所选租户不存在");
//            }
//        }else {
            if (StringUtils.isNotEmpty(telnumTVo.getTenantId())) {
                tenant = tenantService.findById(telnumTVo.getTenantId());
                if (tenant == null || StringUtils.isEmpty(tenant.getId())) {
                    return RestResponse.failed("0000", "所选租户不存在");
                }
            }
//        }
        //拷贝对象
        ResourceTelenum resourceTelenum = new ResourceTelenum();
        try {
            EntityUtils.copyProperties(resourceTelenum,telnumTVo);
        }catch (Exception e){
            return RestResponse.failed("0000","新增线路失败");
        }
        resourceTelenum.setUsable(ResourceTelenum.USABLE_FALSE);//设置不可用
        //如果绑定线路的话，需要为号码设置区号
        if(lineGateway!=null&&StringUtils.isNotEmpty(lineGateway.getId())) {
            //验证运营商
            if((","+lineGateway.getOperator()+",").indexOf(","+resourceTelenum.getOperator()+",")==-1){
                return RestResponse.failed("0000","号码运营商和线路运营不一致");
            }
            //设置区号与线路一致
            resourceTelenum.setAreaId(lineGateway.getAreaId());
        }
        resourceTelenumService.createNum(resourceTelenum,lineGateway,tenant);
        return RestResponse.success("创建成功");
    }
    @ApiOperation(value = "根据id查询号码")
    @RequestMapping(value = "/detail/{id}",method = RequestMethod.GET)
    public RestResponse detail(
            @ApiParam(name = "id",value = "号码id")
            @PathVariable String id){
        ResourceTelenum resourceTelenum = resourceTelenumService.findById(id);
        if(resourceTelenum==null||StringUtils.isEmpty(resourceTelenum.getId())){
            return RestResponse.failed("0000","号码不存在");
        }
        resourceTelenum.setLine(lineGatewayService.findById(resourceTelenum.getLineId()));
        return RestResponse.success(resourceTelenum);
    }
    @ApiOperation(value = "删除号码")
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public RestResponse delete(@ApiParam(name="id",value = "号码id") @PathVariable  String id) throws InvocationTargetException, IllegalAccessException {
        ResourceTelenum resourceTelenum = resourceTelenumService.findById(id);
        if(resourceTelenum==null||StringUtils.isEmpty(resourceTelenum.getId())){
            return RestResponse.failed("0000","线路不存在");
        }
        resourceTelenumService.delete(id);
        return RestResponse.success("删除成功");
    }
    @ApiOperation(value = "启用号码")
    @RequestMapping(value = "/enabled/{id}",method = RequestMethod.PUT)
    public RestResponse enabled(
            @ApiParam(name = "id",value = "号码id")
            @PathVariable String id){
        ResourceTelenum resourceTelenum = resourceTelenumService.findById(id);
        if(resourceTelenum==null){
            return RestResponse.failed("0000","记录不存在");
        }
        resourceTelenum.setUsable(ResourceTelenum.USABLE_TRUE);
        resourceTelenumService.save(resourceTelenum);
        return RestResponse.success("启用号码成功");
    }
    @ApiOperation(value = "禁用号码")
    @RequestMapping(value = "/disabled/{id}",method = RequestMethod.PUT)
    public RestResponse disabled(
            @ApiParam(name = "id",value = "号码id")
            @PathVariable String id){
        ResourceTelenum resourceTelenum = resourceTelenumService.findById(id);
        if(resourceTelenum==null){
            return RestResponse.failed("0000","记录不存在");
        }
        resourceTelenum.setUsable(ResourceTelenum.USABLE_FALSE);
        resourceTelenumService.save(resourceTelenum);
        return RestResponse.success("禁用号码成功");
    }
    @ApiOperation(value = "关联线路-归属线路")
    @RequestMapping(value = "/line/one/{id}",method = RequestMethod.GET)
    public RestResponse telnumLinePlistOne(
            @ApiParam(name = "id",value = "号码id") @PathVariable String id
    ){
        ResourceTelenum resourceTelenum = resourceTelenumService.findById(id);
        if(resourceTelenum==null){
            return RestResponse.failed("0000","记录不存在");
        }
        TelnumToLineGateway telnumToLineGateway = telnumToLineGatewayService.findByTelNumberAndLineId(resourceTelenum.getTelNumber(),resourceTelenum.getLineId());
        LineVo lineVo = new LineVo();
        try {
            EntityUtils.copyProperties(lineVo,telnumToLineGateway);
        } catch (Exception e) {
        }
        if(telnumToLineGateway!=null) {
            lineVo.setLineGateway(lineGatewayService.findById(telnumToLineGateway.getLineId()));
        }
        return RestResponse.success(lineVo);
    }
    @ApiOperation(value = "关联线路-透传列表")
    @RequestMapping(value = "/line/plist/{id}",method = RequestMethod.GET)
    public RestResponse telnumLinePlist(
            @ApiParam(name = "id",value = "号码id") @PathVariable String id,
            @ApiParam(name = "pageNo",value = "第几页")@RequestParam(defaultValue = "1") Integer pageNo,
            @ApiParam(name = "pageSize",value = "每页记录数")@RequestParam(defaultValue = "20") Integer pageSize
    ){
        ResourceTelenum resourceTelenum = resourceTelenumService.findById(id);
        if(resourceTelenum==null){
            return RestResponse.failed("0000","记录不存在");
        }
        String lineId = resourceTelenum.getLineId()!=null?resourceTelenum.getLineId():"";
        Page page = telnumToLineGatewayService.getIsNotNullPage(pageNo,pageSize,lineId,resourceTelenum.getTelNumber());
        List<LineVo> list = new ArrayList<>();
        List<TelnumToLineGateway> list2 = page.getResult();
        for(int i=0;i<list2.size();i++){
            LineVo lineVo = new LineVo();
            try {
                EntityUtils.copyProperties(lineVo,list2.get(i));
            } catch (Exception e) {
            }
            if(list2.get(i)!=null) {
                lineVo.setLineGateway(lineGatewayService.findById(list2.get(i).getLineId()));
            }
            list.add(lineVo);
        }
        Page page2 = new Page(page.getStartIndex(),page.getTotalCount(),page.getPageSize(), list);
        return RestResponse.success(page2);
    }
    @ApiOperation(value = "关联线路-列表")
    @RequestMapping(value = "/tenant/plist/{id}",method = RequestMethod.GET)
    public RestResponse tenantPlist(
            @ApiParam(name = "id",value = "号码id") @PathVariable String id,
            @ApiParam(name = "pageNo",value = "第几页")@RequestParam(defaultValue = "1") Integer pageNo,
            @ApiParam(name = "pageSize",value = "每页记录数")@RequestParam(defaultValue = "20") Integer pageSize
    ){
        ResourceTelenum resourceTelenum = resourceTelenumService.findById(id);
        if(resourceTelenum==null){
            return RestResponse.failed("0000","记录不存在");
        }
        Page page = telnumToLineGatewayService.getPage(pageNo,pageSize,null,resourceTelenum.getTelNumber(),null,null,null);
        return RestResponse.success(page);
    }
}
