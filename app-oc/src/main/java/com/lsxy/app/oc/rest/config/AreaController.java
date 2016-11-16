package com.lsxy.app.oc.rest.config;

import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.config.service.AreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by zhangxb on 2016/10/25.
 */
@Api(value = "区域管理", description = "配置中心相关的接口" )
@RequestMapping("/config/area")
@RestController
public class AreaController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(AreaController.class);
    @Autowired
    AreaService areaService;
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ApiOperation(value = "获取全部区域")
    public RestResponse list(){
        List list= (List)areaService.list();
        return RestResponse.success(list);
    }
}
