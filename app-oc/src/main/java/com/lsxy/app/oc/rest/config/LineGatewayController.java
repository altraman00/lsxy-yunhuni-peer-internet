package com.lsxy.app.oc.rest.config;

import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.app.oc.rest.config.vo.*;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.framework.core.utils.EntityUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.config.model.Area;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.config.service.*;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;
import com.lsxy.yunhuni.api.resourceTelenum.model.TelnumToLineGateway;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourceTelenumService;
import com.lsxy.yunhuni.api.resourceTelenum.service.TelnumToLineGatewayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhangxb on 2016/10/24.
 */
@Api(value = "线路管理", description = "配置中心相关的接口" )
@RequestMapping("/config/line")
@RestController
public class LineGatewayController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(LineGatewayController.class);
    @Autowired
    TelnumLocationService telnumLocationService;
    @Autowired
    LineGatewayService lineGatewayService;
    @Autowired
    TelnumToLineGatewayService telnumToLineGatewayService;
    @Autowired
    LineGatewayToPublicService lineGatewayToPublicService;
    @Autowired
    ResourceTelenumService resourceTelenumService;
    @Autowired
    AreaService areaService;
    @Autowired
    LineGatewayToTenantService lineGatewayToTenantService;
    @Autowired
    TenantService tenantService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ApiOperation(value = "获取全部数据")
    public RestResponse pList(){
        List list= (List)lineGatewayService.list();
        return RestResponse.success(list);
    }
    @RequestMapping(value = "/plist",method = RequestMethod.GET)
    @ApiOperation(value = "获取分页数据")
    public RestResponse pList(
            @ApiParam(name = "pageNo",value = "第几页")  @RequestParam(defaultValue = "1")Integer pageNo,
            @ApiParam(name = "pageSize",value = "每页记录数")  @RequestParam(defaultValue = "20")Integer pageSize,
            @ApiParam(name = "operator",value = "运营商") @RequestParam(required = false)String operator,
            @ApiParam(name = "isThrough",value = "是否透传 1支持透传 0不支持透传")@RequestParam(required = false) String isThrough,
            @ApiParam(name = "status",value = "状态 1可用 0不可用") @RequestParam(required = false)String status,
            @ApiParam(name = "order",value = "quality:1按质量降序，quality:0按质量升序") @RequestParam(required = false)String order
    ){
        if(StringUtils.isNotEmpty(operator)&&!Arrays.asList(ResourceTelenum.OPERATORS).contains(operator)){
            RestResponse.failed("0000","运营商错误");
        }
        Page page= lineGatewayService.getPage(pageNo,pageSize,operator,isThrough,status,null,order);
        return RestResponse.success(page);
    }
    @ApiOperation(value = "新建线路")
    @RequestMapping(value = "/new",method = RequestMethod.POST)
    public RestResponse create(
            @RequestBody LineGatewayVo lineGatewayVo) {
        String re = vailLineGatewayVo(lineGatewayVo);
        if(StringUtils.isNotEmpty(re)){
            return RestResponse.failed("0000","新增线路失败:"+re);
        }
        LineGateway lineGateway = new LineGateway();
        try {
            EntityUtils.copyProperties(lineGateway,lineGatewayVo );
        }catch (Exception e){
            return RestResponse.failed("0000","新增线路失败");
        }
        lineGateway.setCapacity(Integer.valueOf(lineGatewayVo.getCapacity()));
        //默认禁用线路
        lineGateway.setStatus(LineGateway.STATUS_UNUSABLE);
        //默认没有加入全局线路中
        lineGateway.setIsPublicLine(LineGateway.ISPUBLICLINE_FALSE);
        lineGatewayService.save(lineGateway);
        return RestResponse.success("创建成功");
    }
    /**
     * 根据消息id查询消息
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id查询线路")
    @RequestMapping(value = "/detail/{id}",method = RequestMethod.GET)
    public RestResponse detail(
            @ApiParam(name = "id",value = "线路id")
            @PathVariable String id){
        LineGateway lineGateway = lineGatewayService.findById(id);
        if(lineGateway==null||StringUtils.isEmpty(lineGateway.getId())){
            return RestResponse.failed("0000","线路不存在");
        }
        return RestResponse.success(lineGateway);
    }
    @ApiOperation(value = "修改线路")
    @RequestMapping(value = "/edit/{id}",method = RequestMethod.PUT)
    public RestResponse modify(@ApiParam(name = "id",value = "线路id")
                               @PathVariable String id,@RequestBody LineGatewayVo lineGatewayVo){
        LineGateway lineGateway = lineGatewayService.findById(id);
        if(lineGateway==null||StringUtils.isEmpty(lineGateway.getId())){
            return RestResponse.failed("0000","线路不存在");
        }
        String re = vailLineGatewayVo(lineGatewayVo);
        if(StringUtils.isNotEmpty(re)){
            return RestResponse.failed("0000","新增线路失败:"+re);
        }
        String isThrough = lineGateway.getIsThrough();
        try {
            BeanUtils.copyProperties2(lineGateway,lineGatewayVo,false);
        }catch (Exception e){
            return RestResponse.failed("0000","修改线路失败");
        }
        if(StringUtils.isNotEmpty(lineGatewayVo.getCapacity())) {
            lineGateway.setCapacity(Integer.valueOf(lineGatewayVo.getCapacity()));
        }
        telnumToLineGatewayService.modify(lineGateway,lineGatewayVo.getIsThrough(),isThrough);
        return RestResponse.success("修改成功");
    }
    @ApiOperation(value = "启用线路")
    @RequestMapping(value = "/enabled/{id}",method = RequestMethod.PUT)
    public RestResponse enabled(
            @ApiParam(name = "id",value = "线路id")
            @PathVariable String id){
        LineGateway lineGateway = lineGatewayService.findById(id);
        if(lineGateway==null||StringUtils.isEmpty(lineGateway.getId())){
            return RestResponse.failed("0000","线路不存在");
        }
        lineGateway.setStatus(LineGateway.STATUS_USABLE);
        lineGatewayService.save(lineGateway);
        return RestResponse.success("启用线路成功");
    }
    @ApiOperation(value = "禁用线路")
    @RequestMapping(value = "/disabled/{id}",method = RequestMethod.PUT)
    public RestResponse disabled(
            @ApiParam(name = "id",value = "线路id")
            @PathVariable String id){
        LineGateway lineGateway = lineGatewayService.findById(id);
        if(lineGateway==null||StringUtils.isEmpty(lineGateway.getId())){
            return RestResponse.failed("0000","线路不存在");
        }
//        //本来是启用现在改成禁用
//        if("1".equals(lineGateway.getStatus())){
//            //更新号码的状态
//            batchUpCall(lineGateway.getId());
//        }
        lineGateway.setStatus(LineGateway.STATUS_UNUSABLE);
        lineGatewayService.save(lineGateway);
        return RestResponse.success("禁用线路成功");
    }
//    @ApiOperation(value = "修改状态")
//    @RequestMapping(value = "/edit/status/{id}",method = RequestMethod.PUT)
//    public RestResponse modifyStatus(@ApiParam(name = "id",value = "线路id")
//                                   @PathVariable String id,@RequestBody LineGatewayEditStatusVo lineGatewayEditStatusVo){
//        LineGateway lineGateway = lineGatewayService.findById(id);
//        if(lineGateway==null||StringUtils.isEmpty(lineGateway.getId())){
//            return RestResponse.failed("0000","线路不存在");
//        }
//        String status = lineGateway.getStatus();
//        if("0".equals(lineGatewayEditStatusVo.getStatus())||"1".equals(lineGatewayEditStatusVo.getStatus())) {
//            lineGateway.setStatus(lineGatewayEditStatusVo.getStatus());
//            lineGatewayService.save(lineGateway);
//            if("1".equals(status)&&"0".equals(lineGatewayEditStatusVo.getStatus())){
//                //更新号码的状态
//                batchUpCall(lineGateway.getId());
//            }
//        }else{
//            return RestResponse.failed("0000","状态错误");
//        }
//        return RestResponse.success("修改成功");
//    }
    @ApiOperation(value = "删除线路")
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public RestResponse delete(@ApiParam(name="id",value = "线路id") @PathVariable  String id) throws InvocationTargetException, IllegalAccessException {
        LineGateway lineGateway = lineGatewayService.findById(id);
        if(lineGateway==null||StringUtils.isEmpty(lineGateway.getId())){
            return RestResponse.failed("0000","线路不存在");
        }
        //删除线路
        telnumToLineGatewayService.deleteLine(lineGateway.getId());
        //删除线路号码关联关系表并更新号码状况
        List<TelnumToLineGateway> list = telnumToLineGatewayService.getListByLine(lineGateway.getId());
        for(int i=0;i<list.size();i++){
            TelnumToLineGateway telnumToLineGateway = list.get(i);
            telnumToLineGatewayService.delete(telnumToLineGateway);
            //是归属线路
            if(TelnumToLineGateway.ISDIALING_TRUE.equals(telnumToLineGateway.getIsDialing())||TelnumToLineGateway.ISCALLED_TRUE.equals(telnumToLineGateway.getIsCalled())){
                ResourceTelenum resourceTelenum1 = resourceTelenumService.findByTelNumber(telnumToLineGateway.getTelNumber());
                //判断号码线路关系中是否是归属线路，是的话，删除归属线路关系
                if(resourceTelenum1!=null&&StringUtils.isNotEmpty(resourceTelenum1.getId())){
                    //删除归属线路关系
                    resourceTelenum1.setLineId(null);
                    //设置号码不可主叫不可被叫
                    resourceTelenum1.setIsCalled(ResourceTelenum.ISCALLED_FALSE);
                    resourceTelenum1.setIsDialing(ResourceTelenum.ISDIALING_FALSE);
                    resourceTelenumService.save(resourceTelenum1);
                }
            }
            //是透传线路
            if(TelnumToLineGateway.ISTHROUGH_TRUE.equals(telnumToLineGateway.getIsThrough())){
                //获取是否还拥有可透传的号码
                ResourceTelenum resourceTelenum1 = resourceTelenumService.findByTelNumber(telnumToLineGateway.getTelNumber());
                int isThrough = telnumToLineGatewayService.getIsThrough(telnumToLineGateway.getTelNumber());
                //判断号码线路关系中是否还是可透传线路
                if(resourceTelenum1!=null&&isThrough == 0){
                    resourceTelenum1.setIsThrough(ResourceTelenum.ISTHROUGH_FALSE);
                    resourceTelenumService.save(resourceTelenum1);
                }
            }
        }
        return RestResponse.success("删除成功");
    }
    @ApiOperation(value = "线路配套号码-列表")
    @RequestMapping(value = "/telnum/plist/{id}",method = RequestMethod.GET)
    public RestResponse telnum(
            @ApiParam(name = "id",value = "线路id") @PathVariable String id,
            @ApiParam(name = "pageNo",value = "第几页")@RequestParam(defaultValue = "1") Integer pageNo,
            @ApiParam(name = "pageSize",value = "每页记录数")@RequestParam(defaultValue = "20") Integer pageSize,
            @ApiParam(name = "number",value = "号码")@RequestParam(required = false)String number,
            @ApiParam(name = "type",value = "号码属性：1=可主叫；2=可被叫；3=可透传")@RequestParam(required = false)Integer type
    ){
        String isDialing = null;
        String isCalled = null;
        String isThrough = null;
        if(type!=null) {
            if (1 == type) {
                isDialing = "1";
            } else if (2 == type) {
                isCalled = "1";
            } else if (3 == type) {
                isThrough = "1";
            } else {
                return RestResponse.failed("0000", "号码属性类型错误");
            }
        }
        Page<TelnumToLineGateway> page = telnumToLineGatewayService.getPage(pageNo,pageSize,id,number,isDialing,isCalled,isThrough);
        Set<String> telNumbers = new HashSet<>();
        for(TelnumToLineGateway ttg:page.getResult()){
            if(StringUtil.isNotBlank(ttg.getTelNumber())){
                telNumbers.add(ttg.getTelNumber());
            }
        }
        List<ResourceTelenum> resourceTelenums = resourceTelenumService.findByTelNumbers(telNumbers);
        Set<String> tenantIds = new HashSet<>();
        for(ResourceTelenum resourceTelenum:resourceTelenums){
            if(StringUtil.isNotBlank(resourceTelenum.getTenantId())){
                tenantIds.add(resourceTelenum.getTenantId());
            }
        }
        //获取绑定租户
        List<Tenant> tenants = tenantService.findByIds(tenantIds);
        for(ResourceTelenum telenum:resourceTelenums){
            for(Tenant tenant:tenants){
                if(tenant.getId().equals(telenum.getTenantId())){
                    telenum.setTenant(tenant);
                    break;
                }
            }
        }
        for(TelnumToLineGateway ttg:page.getResult()){
            for(ResourceTelenum num:resourceTelenums){
                if(num.getTelNumber().equals(ttg.getTelNumber())){
                    ttg.setResourceTelenum(num);
                }
            }
        }
        return RestResponse.success(page);
    }


    @ApiOperation(value = "线路配套号码-批量删除")
    @RequestMapping(value = "/telnum/{id}",method = RequestMethod.DELETE)
    public RestResponse telnumDelete(
            @ApiParam(name = "id",value = "线路id") @PathVariable String id,
            @ApiParam(name = "ids",value = "线路配套号码ids集合") @RequestBody IdsVo ids){
        LineGateway lineGateway = lineGatewayService.findById(id);
        if(lineGateway==null||StringUtils.isEmpty(lineGateway.getId())){
            return RestResponse.failed("0000","线路不存在");
        }
        for(int i=0;i<ids.getIds().length;i++){
            //获取号码线路关系
            TelnumToLineGateway telnumToLineGateway = telnumToLineGatewayService.findById(ids.getIds()[i]);
            if(telnumToLineGateway!=null&&StringUtils.isNotEmpty(telnumToLineGateway.getId())){//如果号码存在
                //删除号码线路关系
                try {
                    telnumToLineGatewayService.delete(telnumToLineGateway);
                } catch (Exception e) {
                    //删除失败
                    return RestResponse.failed("0000","第["+(i+1)+"]个删除失败");
                }
                //是归属线路
                if(TelnumToLineGateway.ISDIALING_TRUE.equals(telnumToLineGateway.getIsDialing())||TelnumToLineGateway.ISCALLED_TRUE.equals(telnumToLineGateway.getIsCalled())){
                    ResourceTelenum resourceTelenum1 = resourceTelenumService.findByTelNumber(telnumToLineGateway.getTelNumber());
                    //判断号码线路关系中是否是归属线路，是的话，删除归属线路关系
                    if(resourceTelenum1!=null&&StringUtils.isNotEmpty(resourceTelenum1.getId())){
                        //本身有归属线路
                        if(resourceTelenum1.getLineId()!=null&&!lineGateway.getId().equals(resourceTelenum1.getLineId())){
                            TelnumToLineGateway telnumToLineGateway12 = telnumToLineGatewayService.findByTelNumberAndLineId(resourceTelenum1.getTelNumber(),resourceTelenum1.getLineId());
                            telnumToLineGateway12.setIsCalled(TelnumToLineGateway.ISCALLED_FALSE);
                            telnumToLineGateway12.setIsDialing(TelnumToLineGateway.ISDIALING_FALSE);
                            telnumToLineGatewayService.save(telnumToLineGateway12);
                        }
                        //删除归属线路关系
                        resourceTelenum1.setLineId(null);
                        //设置号码不可主叫不可被叫
                        resourceTelenum1.setIsCalled(TelnumToLineGateway.ISCALLED_FALSE);
                        resourceTelenum1.setIsDialing(TelnumToLineGateway.ISDIALING_FALSE);
                        resourceTelenumService.save(resourceTelenum1);
                    }
                }
                //是透传线路
                if(TelnumToLineGateway.ISTHROUGH_TRUE.equals(telnumToLineGateway.getIsThrough())){
                    //获取是否还拥有可透传的号码
                    ResourceTelenum resourceTelenum1 = resourceTelenumService.findByTelNumber(telnumToLineGateway.getTelNumber());
                    int isThrough = telnumToLineGatewayService.getIsThrough(telnumToLineGateway.getTelNumber());
                    //判断号码线路关系中是否还是可透传线路
                    if(resourceTelenum1!=null&&isThrough == 0){
                        resourceTelenum1.setIsThrough(ResourceTelenum.ISTHROUGH_FALSE);
                        resourceTelenumService.save(resourceTelenum1);
                    }
                }
            }
        }
        return RestResponse.success("删除成功");
    }


    @ApiOperation(value = "线路配套号码-批量修改")
    @RequestMapping(value = "/telnum/edit/{id}",method = RequestMethod.PUT)
    public RestResponse modify(
            @ApiParam(name = "id",value = "线路id") @PathVariable String id,
            @ApiParam(name = "telnums",value = "线路配套号码telnums集合")@RequestBody TelnumToLineGatewayBatchEditVo telnums){
        LineGateway lineGateway = lineGatewayService.findById(id);
        if(lineGateway==null||StringUtils.isEmpty(lineGateway.getId())){
            return RestResponse.failed("0000","线路不存在");
        }
        for(int i=0;i<telnums.getTelnums().length;i++){
            TelnumToLineGatewayEditVo telnum = telnums.getTelnums()[i];
            if(StringUtils.isEmpty(telnum.getId())) {
               continue;
                // return RestResponse.failed("0000","不允许存在记录id为空");
            }
            if(StringUtils.isEmpty(telnum.getIsCalled())&&StringUtils.isEmpty(telnum.getIsDialing())&&StringUtils.isEmpty(telnum.getIsThrough())){
                continue;
                //return RestResponse.failed("0000","不允许存在无修改项的记录");
            }
            //获取号码线路关系
            TelnumToLineGateway telnumToLineGateway = telnumToLineGatewayService.findById(telnum.getId());
            boolean flag1 = false;
            boolean flag2 = false;
            //修改被叫
            if((TelnumToLineGateway.ISCALLED_FALSE.equals(telnum.getIsCalled())||TelnumToLineGateway.ISCALLED_TRUE.equals(telnum.getIsCalled()))&&!telnumToLineGateway.getIsCalled().equals(telnum.getIsCalled())){
                flag1=true;
                telnumToLineGateway.setIsCalled(telnum.getIsCalled());
            }
            //修改主叫
            if((TelnumToLineGateway.ISDIALING_FALSE.equals(telnum.getIsDialing())||TelnumToLineGateway.ISDIALING_TRUE.equals(telnum.getIsDialing()))&&!telnumToLineGateway.getIsDialing().equals(telnum.getIsDialing())){
                flag1=true;
                telnumToLineGateway.setIsDialing(telnum.getIsDialing());
            }
            //修改透传
            if((TelnumToLineGateway.ISTHROUGH_FALSE.equals(telnum.getIsThrough())||TelnumToLineGateway.ISTHROUGH_TRUE.equals(telnum.getIsThrough()))&&!telnumToLineGateway.getIsThrough().equals(telnum.getIsThrough())){
                flag2=true;
                telnumToLineGateway.setIsThrough(telnum.getIsThrough());
            }
            telnumToLineGateway = telnumToLineGatewayService.save(telnumToLineGateway);
            //如果是修改主被叫，需要修改归属线路
            if(flag1){
                if(TelnumToLineGateway.ISDIALING_TRUE.equals(telnumToLineGateway.getIsDialing())||TelnumToLineGateway.ISCALLED_TRUE.equals(telnumToLineGateway.getIsCalled())){
                    ResourceTelenum resourceTelenum1 = resourceTelenumService.findByTelNumber(telnumToLineGateway.getTelNumber());
                    //判断号码线路关系中是否是归属线路，是的话，删除归属线路关系
                    if(resourceTelenum1!=null&&StringUtils.isNotEmpty(resourceTelenum1.getId())){
                        //本身有归属线路
                        if(resourceTelenum1.getLineId()!=null&&!lineGateway.getId().equals(resourceTelenum1.getLineId())){
                            TelnumToLineGateway telnumToLineGateway12 = telnumToLineGatewayService.findByTelNumberAndLineId(resourceTelenum1.getTelNumber(),resourceTelenum1.getLineId());
                            telnumToLineGateway12.setIsCalled(TelnumToLineGateway.ISCALLED_FALSE);
                            telnumToLineGateway12.setIsDialing(TelnumToLineGateway.ISDIALING_FALSE);
                            telnumToLineGatewayService.save(telnumToLineGateway12);
                        }
                        //修正归属线路关系
                        resourceTelenum1.setLineId(lineGateway.getId());
                        resourceTelenum1.setIsCalled(telnumToLineGateway.getIsCalled());
                        resourceTelenum1.setIsDialing(telnumToLineGateway.getIsDialing());
                        resourceTelenumService.save(resourceTelenum1);
                    }
                }else if(TelnumToLineGateway.ISDIALING_FALSE.equals(telnumToLineGateway.getIsDialing())||TelnumToLineGateway.ISCALLED_FALSE.equals(telnumToLineGateway.getIsCalled())){
                    ResourceTelenum resourceTelenum1 = resourceTelenumService.findByTelNumber(telnumToLineGateway.getTelNumber());
                    //判断号码线路关系中是否是归属线路，是的话，删除归属线路关系
                    if(resourceTelenum1!=null&&StringUtils.isNotEmpty(resourceTelenum1.getId())){
                        //当前号码的归属线路是当前线路，删除归属线路关系
                        if(resourceTelenum1.getLineId()!=null&&lineGateway.getId().equals(resourceTelenum1.getLineId())){
                            //删除归属线路关系
                            resourceTelenum1.setLineId(lineGateway.getId());
                            //设置号码不可主叫不可被叫
                            resourceTelenum1.setIsCalled(ResourceTelenum.ISCALLED_FALSE);
                            resourceTelenum1.setIsDialing(ResourceTelenum.ISDIALING_FALSE);
                            resourceTelenumService.save(resourceTelenum1);
                        }
                    }
                }
            }
            //如果修改线路号码关系属性
            if(flag2){
                //获取是否还拥有可透传的号码
                ResourceTelenum resourceTelenum1 = resourceTelenumService.findByTelNumber(telnumToLineGateway.getTelNumber());
                //判断号码线路关系中是否是归属线路，是的话，删除归属线路关系
                if(resourceTelenum1!=null&&StringUtils.isNotEmpty(resourceTelenum1.getId())){
                    resourceTelenum1.setIsThrough(telnumToLineGatewayService.getIsThrough(telnumToLineGateway.getTelNumber())+"");
                    resourceTelenumService.save(resourceTelenum1);
                }
            }
        }
        return RestResponse.success("修改成功");
    }
    @ApiOperation(value = "新建透传号码")
    @RequestMapping(value = "/telnum/through/new/{id}",method = RequestMethod.POST)
    public RestResponse telnumCreateThrough(
            @ApiParam( name="id",value = "线路id")@PathVariable String id,
            @ApiParam(name = "ids",value = "新增号码集合")@RequestBody IdsVo ids) {
        if(ids.getIds().length==0){
            return RestResponse.failed("0000","号码集合不存在");
        }
        LineGateway lineGateway = lineGatewayService.findById(id);
        if(lineGateway!=null&&StringUtils.isNotEmpty(lineGateway.getId())){
            telnumToLineGatewayService.batchInsert(lineGateway.getId(),"",ids.getIds());
        }else{
            return RestResponse.failed("0000","线路不存在");
        }
        return RestResponse.success("创建成功");
    }
    @ApiOperation(value = "新建号码")
    @RequestMapping(value = "/telnum/new/{id}",method = RequestMethod.POST)
    public RestResponse telnumCreate(
            @ApiParam( name="id",value = "线路id")@PathVariable String id,
            @ApiParam(name = "telnumVo",value = "新增号码集合")@RequestBody TelnumVo telnumVo) {
        telnumVo.setTelNumber(telnumVo.getTelNumber().trim());//对号码去空
        if(StringUtils.isEmpty(telnumVo.getTelNumber())){
            return RestResponse.failed("0000","号码不能为空");
        }
        Pattern p = Pattern.compile("^[0-9]{1,32}$");
        Matcher matcher = p.matcher(telnumVo.getTelNumber());
        if (!matcher.matches()) {
            return RestResponse.failed("0000","号码格式错误");
        }
        ResourceTelenum temp = resourceTelenumService.findByTelNumber(telnumVo.getTelNumber());
        if(temp!=null){
            return RestResponse.failed("0000","该号码已存在号码池中");
        }
        if(StringUtils.isEmpty(telnumVo.getCallUri())){
            return RestResponse.failed("0000","呼出URI不能为空");
        }
        ResourceTelenum temp2 = resourceTelenumService.findNumByCallUri(telnumVo.getCallUri());
        if(temp2 != null){
            return RestResponse.failed("0000","该呼出URI已存在号码池中");
        }
        //先获取线路对象
        LineGateway lineGateway = lineGatewayService.findById(id);
        //验证运营商
        if((","+lineGateway.getOperator()+",").indexOf(","+telnumVo.getOperator()+",")==-1){
            return RestResponse.failed("0000","号码运营商和线路运营不一致");
        }
        if(lineGateway!=null&&StringUtils.isNotEmpty(lineGateway.getId())){
            //创建号码对象
            //是否是归属线路
            ResourceTelenum resourceTelenum = null;
            if(telnumVo.getIsCalled()==1||telnumVo.getIsDialing()==1) {//是归属线路
                resourceTelenum = new ResourceTelenum(
                        telnumVo.getTelNumber(), telnumVo.getCallUri(), telnumVo.getOperator(), lineGateway.getAreaCode(), lineGateway.getId(), telnumVo.getAmount()
                        , telnumVo.getIsCalled() + "", telnumVo.getIsDialing() + "", "0", lineGateway.getAreaId());
            }else{//不是归属线路
                resourceTelenum = new ResourceTelenum(
                        telnumVo.getTelNumber(), telnumVo.getCallUri(), telnumVo.getOperator(), lineGateway.getAreaCode(), null, telnumVo.getAmount()
                        , telnumVo.getIsCalled() + "", telnumVo.getIsDialing() + "", "0", lineGateway.getAreaId());
            }
            resourceTelenum = resourceTelenumService.save(resourceTelenum);
            //创建线路和号码关系
            TelnumToLineGateway telnumToLineGateway = new TelnumToLineGateway(resourceTelenum.getTelNumber(),lineGateway.getId(),telnumVo.getIsDialing()+"",telnumVo.getIsCalled()+"",0+"","1");
            telnumToLineGatewayService.save(telnumToLineGateway);
        }else{
            return RestResponse.failed("0000","线路不存在");
        }
        return RestResponse.success("创建成功");
    }
    @ApiOperation(value = "批量导入号码")
    @RequestMapping(value="/telnum/upload/{id}", method=RequestMethod.POST)
    public RestResponse handleFileUpload(
            @ApiParam( name="id",value = "线路id")@PathVariable String id,
            MultipartFile file
    ){
        if (file==null||!file.isEmpty()) {
           String fileName =  file.getOriginalFilename();
            boolean isE2007 = false;    //判断是否是excel2007格式
            if(fileName.endsWith("xlsx")) {
                isE2007 = true;
            }else if(fileName.endsWith("xls")){

            }else{
                return RestResponse.failed("0000","请上传excel文件");
            }
            int i=1;
            String reason = "";
            try {
                InputStream input = file.getInputStream();  //建立输入流
                Workbook wb  = null;
                //根据文件格式(2003或者2007)来初始化
                if(isE2007) {
                    wb = new XSSFWorkbook(input);
                }else {
                    wb = new HSSFWorkbook(input);
                }
                Sheet sheet = wb.getSheetAt(0);     //获得第一个表单
                int rowleng = 1000;//一次只支持1000个号码导入
                for( ;i<=rowleng;i++){
                    Row row = sheet.getRow(i);//获取一行数据
                    if(row==null){
                        break;
                    }
                    //号码
                    String telnum = "";
                    try {
                        telnum = row.getCell(0).getStringCellValue().trim();
                    }catch (Exception e){break;}
                    Pattern p = Pattern.compile("^[0-9]{1,32}$");
                    Matcher matcher = p.matcher(telnum);
                    if (!matcher.matches()) {
                        reason = "号码格式错误";
                        break;
                    }
                    if(StringUtils.isNotEmpty(telnum)){
                        String callUri = row.getCell(1).getStringCellValue().trim();//呼出URI
                        String isDialing = row.getCell(2).getStringCellValue().trim();//是否呼出
                        if("是".equals(isDialing)){
                            isDialing = "1";
                        }else if("否".equals(isDialing)){
                            isDialing = "0";
                        }else{
                            reason = "是否呼出格式错误";
                            break;
                        }
                        String isCalled = row.getCell(3).getStringCellValue().trim();//是否呼入
                        if("是".equals(isCalled)){
                            isCalled = "1";
                        }else if("否".equals(isCalled)){
                            isCalled = "0";
                        }else{
                            reason = "是否呼入格式错误";
                            break;
                        }
                        String operator = row.getCell(4).getStringCellValue().trim();//运营商
                        if(!Arrays.asList(ResourceTelenum.OPERATORS).contains(operator)){
                            reason = "运营商格式错误";
                            break;
                        }
                        String areaCode = row.getCell(5).getStringCellValue().trim();//归属地区号
                        String areaName = telnumLocationService.getAreaNameByAreaCode(areaCode);
                        if(StringUtils.isEmpty(areaName)){
                            reason = "归属地区号不存在";
                            break;
                        }
                        String amount = row.getCell(6).getNumericCellValue()+"";//号码占用费
                        //验证号码和呼叫URI
                        ResourceTelenum temp = resourceTelenumService.findByTelNumber(telnum);
                        if(temp!=null){
                            reason = "该号码已存在号码池中";
                            break;
                        }
                        ResourceTelenum temp2 = resourceTelenumService.findNumByCallUri(callUri);
                        if(temp2 != null){
                            reason = "该呼出URI已存在号码池中";
                            break;
                        }
                        //先获取线路对象
                        LineGateway lineGateway = lineGatewayService.findById(id);
                        //验证运营商
                        if((","+lineGateway.getOperator()+",").indexOf(","+operator+",")==-1){
                            return RestResponse.failed("0000","号码运营商和线路运营不一致");
                        }
                        if(lineGateway!=null&&StringUtils.isNotEmpty(lineGateway.getId())){
                            //是否是归属线路
                            ResourceTelenum resourceTelenum = null;
                            if("1".equals(isCalled)||"1".equals(isDialing)) {//是归属线路
                                //创建号码对象
                                resourceTelenum = new ResourceTelenum(telnum, callUri, operator, areaCode, lineGateway.getId(), amount, isCalled, isDialing, "0", lineGateway.getAreaId());
                            }else{//不是归属线路
                                resourceTelenum = new ResourceTelenum(telnum, callUri, operator, areaCode, null, amount, isCalled, isDialing, "0", lineGateway.getAreaId());
                            }
                            resourceTelenum = resourceTelenumService.save(resourceTelenum);
                            //创建号码线路对象
                            TelnumToLineGateway telnumToLineGateway = new TelnumToLineGateway(resourceTelenum.getTelNumber(),lineGateway.getId(),isDialing,isCalled,"0","1");
                            telnumToLineGatewayService.save(telnumToLineGateway);
                        }else{
                            reason = "线路不存在";
                            break;
                        }
                    }else{
                        reason = "号码不能不空";
                        break;
                    }
                }
                if(reason.length()>0){
                    return RestResponse.failed("0000","第["+i+"]行开始处理处理失败;"+reason);
                }
                return RestResponse.success("成功处理到第["+i+"]行;");
            } catch (Exception e) {
                return RestResponse.failed("0000","第["+i+"]行开始处理处理失败;"+reason);
            }
        } else {
            return RestResponse.failed("0000","文件不存");
        }
    }
    private String vailLineGatewayVo(LineGatewayVo lineGatewayVo){
        if(StringUtils.isEmpty(lineGatewayVo.getLineNumber())){
            return "线路标识为空";
        }
        if(StringUtils.isEmpty(lineGatewayVo.getOperator())){
            return "运营商为空";
        }
        String[] op = lineGatewayVo.getOperator().split(",");
        for(int i=0;i<op.length;i++){
            if(!Arrays.asList(ResourceTelenum.OPERATORS).contains(op[i])){
                return "运营商错误";
            }
        }
        if(StringUtils.isEmpty(lineGatewayVo.getAreaId())){
            return "区域编号为空";
        }
        Area area = areaService.findById(lineGatewayVo.getAreaId());
        if(area==null||StringUtils.isEmpty(area.getId())){
            return "区域编号错误";
        }
        if(StringUtils.isEmpty(lineGatewayVo.getAreaCode())){
            return "归属地区号为空";
        }
        String areaName = telnumLocationService.getAreaNameByAreaCode(lineGatewayVo.getAreaCode());
        if(StringUtils.isEmpty(areaName)){
            return "归属地区号不存在";
        }
        if(StringUtils.isEmpty(lineGatewayVo.getLineType())){
            return "线路类型为空";
        }
        if(StringUtils.isEmpty(lineGatewayVo.getSipProviderIp())){
            return "IP+端口为空";
        }
        if(StringUtils.isEmpty(lineGatewayVo.getSipProviderDomain())){
            return "域名+端口为空";
        }
        if("1".equals(lineGatewayVo.getSipAuthType())){
            if(StringUtils.isEmpty(lineGatewayVo.getSipAuthAccount())){
                return "账号为空";
            }
            if(StringUtils.isEmpty(lineGatewayVo.getSipAuthPassword())){
                return "密码为空";
            }
        }else if("2".equals(lineGatewayVo.getSipAuthType())){
            if(StringUtils.isEmpty(lineGatewayVo.getSipAuthIp())){
                return "sip接入点的外网IP地址为空";
            }
        }else{
            return "鉴权方式错误";
        }
        String[] rule = {"0","1","2"};
        if(!Arrays.asList(rule).contains(lineGatewayVo.getMobileAreaRule())){
            return "手机区号规则错误";
        }
        String[] rule1 = {"0","2"};
        if(!Arrays.asList(rule1).contains(lineGatewayVo.getTelAreaRule())){
            return "固话区号规则错误";
        }
        if(lineGatewayVo.getLinePrice()==null){
            return "成本价为空";
        }
        String[] is = {"0","1"};
        if(!Arrays.asList(is).contains(lineGatewayVo.getIsThrough())){
            return "是否可透传规则错误";
        }
        if(lineGatewayVo.getQuality()==null){
            return "质量范围为空";
        }else{
            if(lineGatewayVo.getQuality()>10||lineGatewayVo.getQuality()<1){
                return "质量范围只能在[1,10]";
            }
        }
        if(StringUtils.isEmpty(lineGatewayVo.getCapacity())){
            return "并发容量为空";
        }else{
            Pattern p1 = Pattern.compile("^[0-9]{1,4}$");
            Matcher matcher = p1.matcher(lineGatewayVo.getCapacity());
            if (!matcher.matches()) {
                return "并发容量格式错误";
            }
            try{
               int i =  Integer.valueOf(lineGatewayVo.getCapacity());
                if(i<1||i>1000){
                    return "并发容量只能在[1,1000]";
                }
            }catch (Exception e){
                return "并发容量格式错误";
            }
        }
        return "";
    }
    @RequestMapping(value = "/tenant/plist/{id}",method = RequestMethod.GET)
    @ApiOperation(value = "线路绑定租户信息-获取分页数据")
    public RestResponse tenantPlist(
            @ApiParam( name="id",value = "线路id")@PathVariable String id,
            @ApiParam(name = "pageNo",value = "第几页")  @RequestParam(defaultValue = "1")Integer pageNo,
            @ApiParam(name = "pageSize",value = "每页记录数")  @RequestParam(defaultValue = "20")Integer pageSize,
            @ApiParam(name = "tenantName",value = "用户名") @RequestParam (required = false)String tenantName
    ){
        LineGateway lineGateway = lineGatewayService.findById(id);
        if(lineGateway==null||StringUtils.isEmpty(lineGateway.getId())){
            return RestResponse.failed("0000","线路不存");
        }
        Page page= resourceTelenumService.getTenatPageByLine(pageNo,pageSize,id,tenantName);
        return RestResponse.success(page);
    }
}
