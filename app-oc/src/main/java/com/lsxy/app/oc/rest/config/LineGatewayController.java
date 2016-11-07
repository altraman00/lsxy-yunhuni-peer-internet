package com.lsxy.app.oc.rest.config;

import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.app.oc.rest.config.vo.*;
import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.framework.core.utils.EntityUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.config.model.Area;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.config.model.LineGatewayToTenant;
import com.lsxy.yunhuni.api.config.service.*;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;
import com.lsxy.yunhuni.api.resourceTelenum.model.TelnumToLineGateway;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourceTelenumService;
import com.lsxy.yunhuni.api.resourceTelenum.service.TelnumToLineGatewayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
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
        //默认启用线路
        lineGateway.setStatus("1");
        //默认没有加入全局线路中
        lineGateway.setIsPublicLine("0");
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
        String isThrough = lineGateway.getIsThrough();
        try {
            BeanUtils.copyProperties2(lineGateway,lineGatewayVo,false);
        }catch (Exception e){
            return RestResponse.failed("0000","修改线路失败");
        }
        lineGatewayService.save(lineGateway);
        //如果线路可透传状况发生变化，则需要维护线路中号码的可透传情况
        if("0".equals(lineGatewayVo.getIsThrough())&&"1".equals(isThrough)){
            //对透传进行处理
            telnumToLineGatewayService.updateIsThrough(lineGateway.getId(),"0");
            //更新号码的状态
            batchUpCall(lineGateway.getId());
        }
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
        lineGateway.setStatus("1");
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
        lineGateway.setStatus("0");
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
        lineGatewayService.delete(lineGateway);
        //删除线路号码关联关系表
        telnumToLineGatewayService.deleteByLineId(lineGateway.getId());
        //删除全局线路和归属线路
        lineGatewayToTenantService.deleteLine(lineGateway.getId());
        lineGatewayToPublicService.deleteLine(lineGateway.getId());
        //更新号码的状态
        batchUpCall(lineGateway.getId());
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
        Page page = telnumToLineGatewayService.getPage(pageNo,pageSize,id,number,isDialing,isCalled,isThrough);
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
        String[] idss = new String[ids.getIds().length];
        for(int i=0;i<ids.getIds().length;i++){
            TelnumToLineGateway telnumToLineGateway= telnumToLineGatewayService.findById(ids.getIds()[i]);
            if(telnumToLineGateway==null||StringUtils.isEmpty(telnumToLineGateway.getId())){
                return RestResponse.failed("",ids.getIds()[i]+"无对应号码，删除失败");
            }
            idss[i] = telnumToLineGateway.getTelNumber();
        }
        telnumToLineGatewayService.batchDelete(id,ids.getIds());
        //更新号码的状态
        batchUpCall(lineGateway.getId(),idss);
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
        String[] sql = new String[telnums.getTelnums().length];
        int in = 0;
        for(int i=0;i<telnums.getTelnums().length;i++){
            TelnumToLineGatewayEditVo telnum = telnums.getTelnums()[i];
            if(StringUtils.isEmpty(telnum.getId())) {
                return RestResponse.failed("0000","不允许存在记录id为空");
            }
            if(StringUtils.isEmpty(telnum.getIsCalled())&&StringUtils.isEmpty(telnum.getIsDialing())&&StringUtils.isEmpty(telnum.getIsThrough())){
                return RestResponse.failed("0000","不允许存在无修改项的记录");
            }
            String sq = "  UPDATE db_lsxy_bi_yunhuni.tb_oc_telnum_to_linegateway SET ";
            if(StringUtils.isNotEmpty(telnum.getIsCalled())){
                sq += " is_called = '"+telnum.getIsCalled()+"' ,";
            }
            if(StringUtils.isNotEmpty(telnum.getIsDialing())){
                sq += " is_dialing = '"+telnum.getIsDialing()+"' ,";
            }
            if(StringUtils.isNotEmpty(telnum.getIsThrough())){
                sq += " is_through = '"+telnum.getIsThrough()+"' ,";
            }
            int r = sq.lastIndexOf(",");
            if( r !=-1){
                sq = sq.substring(0,r);
                sq += " WHERE line_id='"+lineGateway.getId()+"' AND id = '"+telnum.getId()+"' ";
                sql[in] = sq;
                in++;
            }
        }
        if(in==0){
            return RestResponse.failed("0000","无修改项目");
        }else{
            int result = lineGatewayService.batchModify(sql);
            if(result==-1){
                return RestResponse.failed("0000","后台修改失败，请重试");
            }
        }
        //更新号码的状态
        batchUpCall(lineGateway.getId());
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
        ResourceTelenum temp = resourceTelenumService.findByTelNumber(telnumVo.getTelNumber());
        if(temp!=null){
            return RestResponse.failed("0000","该号码已存在号码池中");
        }
        String temp2 = resourceTelenumService.findNumByCallUri(telnumVo.getCallUri());
        if(StringUtils.isNotEmpty(temp2)){
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
            ResourceTelenum resourceTelenum = new ResourceTelenum(telnumVo.getTelNumber(),telnumVo.getCallUri(),telnumVo.getOperator(),lineGateway.getAreaCode(),lineGateway.getId(),telnumVo.getAmount());
            resourceTelenum = upCall(resourceTelenum,telnumVo.getTelNumber(),telnumVo.getIsCalled(),telnumVo.getIsDialing(),0);
            //创建号码线路对象
            TelnumToLineGateway telnumToLineGateway = new TelnumToLineGateway(resourceTelenum.getTelNumber(),lineGateway.getId(),telnumVo.getIsCalled()+"",telnumVo.getIsCalled()+"",0+"","1");
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
                    String telnum = row.getCell(0).getStringCellValue().trim();
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
                        String temp2 = resourceTelenumService.findNumByCallUri(callUri);
                        if(StringUtils.isNotEmpty(temp2)){
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
                            //创建号码对象
                            ResourceTelenum resourceTelenum = new ResourceTelenum(telnum,callUri,operator,areaCode,lineGateway.getId(),amount);
                            resourceTelenum = upCall(resourceTelenum,telnum,Integer.valueOf(isCalled),Integer.valueOf(isDialing),0);
                            //创建号码线路对象
                            TelnumToLineGateway telnumToLineGateway = new TelnumToLineGateway(resourceTelenum.getTelNumber(),lineGateway.getId(),isCalled,isCalled,"0","1");
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
    private ResourceTelenum upCall(ResourceTelenum resourceTelenum,String telnum,int isCalled0,int isDialing0,int isThrough0){
//        Map<String,Long> map = telnumToLineGatewayService.getTelnumCall(telnum,null);
        long isCalled = 0;//map.get("isCalled");
        long isDialing = 0;//map.get("isDialing");
        long isThrough = 0;
        isCalled += isCalled0;
        isDialing += isDialing0;
        isThrough += isThrough0;
        isCalled = isCalled>0?1:0;
        isDialing = isDialing>0?1:0;
        isThrough = isThrough>0?1:0;
        resourceTelenum.setIsDialing(isDialing+"");
        resourceTelenum.setIsCalled(isCalled+"");
        resourceTelenum.setIsThrough(isThrough+"");
        return resourceTelenumService.save(resourceTelenum);
    }
    private void batchUpCall(String line,String... nums){
        if(nums.length==0) {
            //获取该线路的全部号码
            List<String> list = telnumToLineGatewayService.getTelnumByLineId(line);
            //修改号码中的状态
            for (int i = 0; i < list.size(); i++) {
                upCalls(line,list.get(i));
            }
        }else if(nums.length>0){
            for(int i=0;i<nums.length;i++){
                upCalls(line,nums[i]);
            }
        }
    }
    private void upCalls(String line,String telnum){
        Map<String, Long> map = telnumToLineGatewayService.getTelnumCall(telnum, null);
        ResourceTelenum resourceTelenum = resourceTelenumService.findByTelNumber(telnum);
        long isCalled = map.get("isCalled");
        long isDialing = map.get("isDialing");
        long isThrough = map.get("isThrough");
        isCalled = isCalled>0?1:0;
        isDialing = isDialing>0?1:0;
        isThrough = isThrough>0?1:0;
        if(line.equals(resourceTelenum.getLineId())){
            resourceTelenum.setLineId("无");
        }
        resourceTelenum.setIsCalled(isCalled+ "");
        resourceTelenum.setIsDialing(isDialing + "");
        resourceTelenum.setIsThrough(isThrough + "");
        resourceTelenumService.save(resourceTelenum);
    }
    private String vailLineGatewayVo(LineGatewayVo lineGatewayVo){
        if(StringUtils.isEmpty(lineGatewayVo.getOperator())){
            return "运营商错误";
        }
        String[] op = lineGatewayVo.getOperator().split(",");
        for(int i=0;i<op.length;i++){
            if(!Arrays.asList(ResourceTelenum.OPERATORS).contains(op[i])){
                return "运营商错误";
            }
        }
        if(StringUtils.isEmpty(lineGatewayVo.getAreaId())){
            return "区域编号错误";
        }
        Area area = areaService.findById(lineGatewayVo.getAreaId());
        if(area==null||StringUtils.isEmpty(area.getId())){
            return "区域编号错误";
        }
        if(StringUtils.isEmpty(lineGatewayVo.getAreaCode())){
            return "归属地区号不存在";
        }
        String areaName = telnumLocationService.getAreaNameByAreaCode(lineGatewayVo.getAreaCode());
        if(StringUtils.isEmpty(areaName)){
            return "归属地区号不存在";
        }
        String[] rule = {"0","1","2"};
        if(!Arrays.asList(rule).contains(lineGatewayVo.getMobileAreaRule())){
            return "手机区号规则错误";
        }
        String[] rule1 = {"0","2"};
        if(!Arrays.asList(rule1).contains(lineGatewayVo.getTelAreaRule())){
            return "固话区号规则错误";
        }
        String[] is = {"0","1"};
        if(!Arrays.asList(is).contains(lineGatewayVo.getIsThrough())){
            return "是否可透传规则错误";
        }
        if(lineGatewayVo.getQuality()>10||lineGatewayVo.getQuality()<1){
            return "质量范围错误";
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
