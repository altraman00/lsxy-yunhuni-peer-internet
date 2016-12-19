package com.lsxy.app.oc.rest.config;

import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.app.oc.rest.config.vo.ApiGwRedBlankNumVo;
import com.lsxy.app.oc.rest.message.MessageVo;
import com.lsxy.framework.api.message.model.Message;
import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.config.model.ApiGwRedBlankNum;
import com.lsxy.yunhuni.api.config.service.ApiGwRedBlankNumService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * Created by zhangxb on 2016/10/27.
 */
@Api(value = "红黑名单", description = "配置中心相关的接口" )
@RequestMapping("/config/redblank")
@RestController
public class ApiGwRedBlankNumController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(ApiGwRedBlankNumController.class);
    @Autowired
    ApiGwRedBlankNumService apiGwRedBlankNumService;
    @RequestMapping(value = "/plist",method = RequestMethod.GET)
    @ApiOperation(value = "获取列表信息")
    public RestResponse pageList(
            @ApiParam(name = "type",value = "1红名单2黑名单")
            @RequestParam(required=false)Integer type,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "20")Integer pageSize){
        Page page = apiGwRedBlankNumService.getPage( pageNo, pageSize,type);
        RestResponse restResponse = RestResponse.success(page);
        return restResponse;
    }
    @ApiOperation(value = "新建红黑名单")
    @RequestMapping(value = "/new",method = RequestMethod.POST)
    public RestResponse create(
            @RequestBody ApiGwRedBlankNumVo apiGwRedBlankNumVo){
        long r = apiGwRedBlankNumService.findByNumber(apiGwRedBlankNumVo.getNumber());
        if(r>0){
            return RestResponse.failed("0000","该号码已绑定");
        }
        ApiGwRedBlankNum apiGwRedBlankNum = new ApiGwRedBlankNum(apiGwRedBlankNumVo.getNumber(),apiGwRedBlankNumVo.getType());
        apiGwRedBlankNumService.save(apiGwRedBlankNum);
        return RestResponse.success("新增成功");
    }
    @ApiOperation(value = "删除红黑名单")
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public RestResponse delete(
            @ApiParam(name="id",value = "红黑名单id")
            @PathVariable  String id
    ) throws InvocationTargetException, IllegalAccessException {
        ApiGwRedBlankNum apiGwRedBlankNum = apiGwRedBlankNumService.findById(id);
        if(apiGwRedBlankNum==null){
            return RestResponse.failed("0000","记录不存在");
        }
        apiGwRedBlankNumService.delete(apiGwRedBlankNum);
        return RestResponse.success("删除成功");
    }
    @ApiOperation(value = "启用红黑名单")
    @RequestMapping(value = "/enabled/{id}",method = RequestMethod.PUT)
    public RestResponse enabled(
            @ApiParam(name = "id",value = "消息id")
            @PathVariable String id){
        ApiGwRedBlankNum apiGwRedBlankNum = apiGwRedBlankNumService.findById(id);
        if(apiGwRedBlankNum==null){
            return RestResponse.failed("0000","记录不存在");
        }
        apiGwRedBlankNum.setStatus(ApiGwRedBlankNum.STATUS_ENABLED);
        apiGwRedBlankNumService.save(apiGwRedBlankNum);
        return RestResponse.success("启用红黑名单成功");
    }
    @ApiOperation(value = "禁用红黑名单")
    @RequestMapping(value = "/disabled/{id}",method = RequestMethod.PUT)
    public RestResponse disabled(
            @ApiParam(name = "id",value = "消息id")
            @PathVariable String id){
        ApiGwRedBlankNum apiGwRedBlankNum = apiGwRedBlankNumService.findById(id);
        if(apiGwRedBlankNum==null){
            return RestResponse.failed("0000","记录不存在");
        }
        apiGwRedBlankNum.setStatus(ApiGwRedBlankNum.STATUS_DISABLED);
        apiGwRedBlankNumService.save(apiGwRedBlankNum);
        return RestResponse.success("禁用红黑名单成功");
    }
}
