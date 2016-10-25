package com.lsxy.app.oc.rest.config;

import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.app.oc.rest.message.MessageVo;
import com.lsxy.framework.core.utils.EntityUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.config.model.LineGatewayToPublic;
import com.lsxy.yunhuni.api.config.service.LineGatewayService;
import com.lsxy.yunhuni.api.config.service.LineGatewayToPublicService;
import com.lsxy.yunhuni.api.resourceTelenum.service.TelnumToLineGatewayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.aspectj.apache.bcel.generic.LineNumberGen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by zhangxb on 2016/10/24.
 */
@Api(value = "线路管理", description = "配置中心相关的接口" )
@RequestMapping("/config/line")
@RestController
public class LineGatewayController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(LineGatewayController.class);
    @Autowired
    LineGatewayService lineGatewayService;
    @Autowired
    TelnumToLineGatewayService telnumToLineGatewayService;
    @Autowired
    LineGatewayToPublicService lineGatewayToPublicService;
    @RequestMapping(value = "/plist",method = RequestMethod.GET)
    @ApiOperation(value = "获取分页数据")
    public RestResponse pList(
            @ApiParam(name = "pageNo",value = "第几页")  @RequestParam(defaultValue = "1")Integer pageNo,
            @ApiParam(name = "pageSize",value = "每页记录数")  @RequestParam(defaultValue = "20")Integer pageSize,
            @ApiParam(name = "operator",value = "运营商") @RequestParam(required = false)String operator,
            @ApiParam(name = "isThrough",value = "是否透传 1支持透传 0不支持透传")@RequestParam(required = false) String isThrough,
            @ApiParam(name = "status",value = "状态 1可用 0不可用") @RequestParam(required = false)String status,
            @ApiParam(name = "isPublicLine",value = "1:全局线路;0:租户专属线路") @RequestParam(required = false)String isPublicLine,
            @ApiParam(name = "order",value = "1:全局线路;0:租户专属线路") @RequestParam(required = false)String order
    ){
        Page page= lineGatewayService.getPage(pageNo,pageSize,operator,isThrough,status,isPublicLine,order);
        return RestResponse.success(page);
    }
    @ApiOperation(value = "新建线路")
    @RequestMapping(value = "/new",method = RequestMethod.POST)
    public RestResponse create(
            @RequestBody LineGatewayVo lineGatewayVo) {
        LineGateway lineGateway = new LineGateway();
        try {
            EntityUtils.copyProperties(lineGatewayVo, lineGateway);
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
        return RestResponse.success(lineGateway);
    }
    @ApiOperation(value = "修改状态")
    @RequestMapping(value = "/edit/status/{id}",method = RequestMethod.PUT)
    public RestResponse modify(@ApiParam(name = "id",value = "线路id")
                                   @PathVariable String id,@RequestBody LineGatewayEditStatusVo lineGatewayEditStatusVo){
        LineGateway lineGateway = lineGatewayService.findById(id);
        if("0".equals(lineGatewayEditStatusVo.getStatus())||"1".equals(lineGatewayEditStatusVo.getStatus())) {
            lineGateway.setStatus(lineGatewayEditStatusVo.getStatus());
            lineGatewayService.save(lineGateway);
        }else{
            return RestResponse.failed("0000","状态错误");
        }
        return RestResponse.success("修改成功");
    }
    /**
     * 删除消息
     * @return
     */
    @ApiOperation(value = "删除线路")
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public RestResponse delete(@ApiParam(name="id",value = "线路id") @PathVariable  String id) throws InvocationTargetException, IllegalAccessException {
        LineGateway lineGateway = lineGatewayService.findById(id);
        //删除线路
        lineGatewayService.delete(lineGateway);
        //删除线路号码关联关系表
        telnumToLineGatewayService.deleteByLineId(lineGateway.getId());
        return RestResponse.success("删除成功");
    }

}
