package com.lsxy.app.oc.rest.config;

import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.app.oc.rest.config.vo.TelnumTEditVo;
import com.lsxy.app.oc.rest.config.vo.TelnumTVo;
import com.lsxy.app.oc.rest.config.vo.TelnumToLineGatewayBatchEditVo;
import com.lsxy.app.oc.rest.message.MessageVo;
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
        resourcesRent.setRentStatus(ResourcesRent.RENT_STATUS_RELEASE);
        resourcesRentService.save(resourcesRent);
        resourceTelenum.setTenant(null);
        resourceTelenum.setStatus(0);
        resourceTelenumService.save(resourceTelenum);
        return RestResponse.success("释放号码成功");
    }
    @ApiOperation(value = "修改号码")
    @RequestMapping(value = "/edit/{id}",method = RequestMethod.PUT)
    public RestResponse edit(@ApiParam(name = "id",value = "号码id") @PathVariable String id, @RequestBody TelnumTEditVo telnumTVo) {
        ResourceTelenum resourceTelenum = resourceTelenumService.findById(id);
        if(resourceTelenum==null||StringUtils.isEmpty(resourceTelenum.getId())){
            return RestResponse.failed("0000","号码不存在");
        }
        String re = vailVo(telnumTVo,true);
        if(StringUtils.isNotEmpty(re)){
            return RestResponse.failed("0000","新增线路失败:"+re);
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
        if(tenantType!=0){
            resourceTelenum.setTenant(tenant);
            resourceTelenum.setStatus(1);
        }
        resourceTelenum = resourceTelenumService.save(resourceTelenum);

        //只更改租户
        if(tenantType != 0&&!isEditNum){
            if(tenantType==2){//如果修改租户，需要删除号码和租户的关系
                ResourcesRent resourcesRent = resourcesRentService.findByResourceTelenumId(resourceTelenum.getId());
                if(resourcesRent!=null&&StringUtils.isNotEmpty(resourcesRent.getId())){//释放存在旧的关系
                    resourcesRent.setRentStatus(ResourcesRent.RENT_STATUS_RELEASE);
                    resourcesRentService.save(resourcesRent);
                }
            }
            createResurcesRent(tenant, resourceTelenum);
        }else if(tenantType==0&& isEditNum){//只更改手机号码
            //修改号码和租户关系，更新手机号码
            ResourcesRent resourcesRent = resourcesRentService.findByResourceTelenumId(resourceTelenum.getId());
            if(resourcesRent!=null&&StringUtils.isNotEmpty(resourcesRent.getId())){//存在旧的关系不用释放
                resourcesRent.setResData(resourceTelenum.getTelNumber());
                resourcesRentService.save(resourcesRent);
            }
            //修正线路原来的记录号码线路关系
            telnumToLineGatewayService.updateTelnum(telnum1,telnumTVo.getTelNumber());
        }else if(tenantType!=0&&isEditNum){//同时修改租户和号码
            if(tenantType==2){//如果修改租户，需要删除号码和租户的关系
                ResourcesRent resourcesRent = resourcesRentService.findByResourceTelenumId(resourceTelenum.getId());
                if(resourcesRent!=null&&StringUtils.isNotEmpty(resourcesRent.getId())){//释放存在旧的关系
                    resourcesRent.setRentStatus(ResourcesRent.RENT_STATUS_RELEASE);
                    resourcesRentService.save(resourcesRent);
                }
            }
            createResurcesRent(tenant, resourceTelenum);
            //修正线路原来的记录号码线路关系
            telnumToLineGatewayService.updateTelnum(telnum1,telnumTVo.getTelNumber());
        }
        return RestResponse.success("释放号码成功");
    }
    @ApiOperation(value = "新建号码")
    @RequestMapping(value = "/new",method = RequestMethod.POST)
    public RestResponse create(
            @RequestBody TelnumTVo telnumTVo){
        String re = vailVo(telnumTVo,false);
        if(StringUtils.isNotEmpty(re)){
            return RestResponse.failed("0000","新增线路失败:"+re);
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
            //验证运营商
            if((","+lineGateway.getOperator()+",").indexOf(","+resourceTelenum.getOperator()+",")==-1){
                return RestResponse.failed("0000","号码运营商和线路运营不一致");
            }
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
            createResurcesRent(tenant, resourceTelenum);
        }
        return RestResponse.success("创建成功");
    }
    @ApiOperation(value = "根据id查询号码")
    @RequestMapping(value = "/detail/{id}",method = RequestMethod.GET)
    public RestResponse detail(
            @ApiParam(name = "id",value = "号码id")
            @PathVariable String id){
        ResourceTelenum resourceTelenum = resourceTelenumService.findById(id);
        if(resourceTelenum==null||StringUtils.isEmpty(resourceTelenum.getId())){
            return RestResponse.failed("0000","线路不存在");
        }
        return RestResponse.success(resourceTelenum);
    }
    @ApiOperation(value = "删除号码")
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public RestResponse delete(@ApiParam(name="id",value = "号码id") @PathVariable  String id) throws InvocationTargetException, IllegalAccessException {
        ResourceTelenum resourceTelenum = resourceTelenumService.findById(id);
        if(resourceTelenum==null||StringUtils.isEmpty(resourceTelenum.getId())){
            return RestResponse.failed("0000","线路不存在");
        }
        //删除号码
        resourceTelenumService.delete(resourceTelenum);
        //释放号码存在的关系
        ResourcesRent resourcesRent = resourcesRentService.findByResourceTelenumId(resourceTelenum.getId());
        if(resourcesRent!=null&&StringUtils.isNotEmpty(resourcesRent.getId())){
            resourcesRent.setRentStatus(ResourcesRent.RENT_STATUS_RELEASE);
            resourcesRentService.save(resourcesRent);
        }
        //删除该号码的号码线路关系
        telnumToLineGatewayService.deleteByTelnum(resourceTelenum.getTelNumber());
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
        resourceTelenum.setUsable("1");
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
        resourceTelenum.setUsable("0");
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
        TelnumToLineGateway telnumToLineGateway = telnumToLineGatewayService.findById(resourceTelenum.getLineId());
        return RestResponse.success(telnumToLineGateway);
    }
    @ApiOperation(value = "关联线路-列表")
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
        Page page = telnumToLineGatewayService.getPage(pageNo,pageSize,null,resourceTelenum.getTelNumber(),null,null,null);
        return RestResponse.success(page);
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

    /**
     * 新增号码租户关系
     * @param tenant
     * @param resourceTelenum
     */
    private void createResurcesRent(Tenant tenant, ResourceTelenum resourceTelenum) {
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

    private String vailVo(TelnumTEditVo telnumTVo,boolean isNull){
        if(isNull&&StringUtils.isEmpty(telnumTVo.getOperator())){
        }else {
            if (!Arrays.asList(ResourceTelenum.OPERATORS).contains(telnumTVo.getOperator())) {
                return "运营商错误";
            }
        }
        if(isNull&&StringUtils.isEmpty(telnumTVo.getAreaCode())){
        }else {
            String areaName = telnumLocationService.getAreaNameByAreaCode(telnumTVo.getAreaCode());
            if (StringUtils.isEmpty(areaName)) {
                return "归属地区号不存在";
            }
        }
        String[] is = {"0","1"};
        if(isNull&&StringUtils.isEmpty(telnumTVo.getIsThrough()+"")){
        }else {
            if (!Arrays.asList(is).contains(telnumTVo.getIsThrough()+"")) {
                return "可透传错误";
            }
        }
        if(isNull&&StringUtils.isEmpty(telnumTVo.getIsCalled()+"")){
        }else {
            if (!Arrays.asList(is).contains(telnumTVo.getIsCalled()+"")) {
                return "可被叫错误";
            }
        }
        if(isNull&&StringUtils.isEmpty(telnumTVo.getIsDialing()+"")){
        }else {
            if (!Arrays.asList(is).contains(telnumTVo.getIsDialing()+"")) {
                return "可主叫错误";
            }
        }
        if(isNull&&StringUtils.isEmpty(telnumTVo.getTelNumber())){
        }else {
            Pattern p = Pattern.compile("^[0-9]{1,32}$");
            Matcher matcher = p.matcher(telnumTVo.getTelNumber());
            if (!matcher.matches()) {
                return "号码格式错误";
            }
        }
        return "";
    }
}
