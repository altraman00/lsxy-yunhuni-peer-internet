package com.lsxy.app.oc.rest.config;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.app.oc.rest.config.vo.GlobalEditVo;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.config.model.GlobalConfig;
import com.lsxy.yunhuni.api.config.service.GlobalConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by zhangxb on 2016/11/28.
 */
@Api(value = "全局配置", description = "配置中心相关的接口" )
@RequestMapping("/config/global")
@RestController
public class GlobalConfigControlle extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(GlobalConfigControlle.class);
    @Autowired
    private GlobalConfigService globalConfigService;
    @RequestMapping(value = "/plist",method = RequestMethod.GET)
    @ApiOperation(value = "获取分页数据")
    public RestResponse pList(
            @ApiParam(name = "pageNo",value = "第几页")  @RequestParam(defaultValue = "1")Integer pageNo,
            @ApiParam(name = "pageSize",value = "每页记录数")  @RequestParam(defaultValue = "20")Integer pageSize
    ){
        Page page= globalConfigService.pageList(pageNo,pageSize);
        return RestResponse.success(page);
    }
    @RequestMapping(value = "/edit/{id}",method = RequestMethod.PUT)
    @ApiOperation(value = "修改配置项的值")
    public RestResponse discountEdit(
            @ApiParam(name = "id",value = "配置id")  @PathVariable String  id, @RequestBody GlobalEditVo globalEditVo
    ){
        GlobalConfig globalConfig = globalConfigService.findById(id);
        if(globalConfig==null){
            return RestResponse.failed("0000","id无对应记录");
        }
        if(StringUtils.isEmpty(globalEditVo.getValue())) {
            return RestResponse.failed("0000","修改内容为空");
        }
        globalConfig.setValue(globalEditVo.getValue());
        globalConfigService.save(globalConfig);
        return RestResponse.success("修改成功");
    }
}
