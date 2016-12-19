package com.lsxy.app.oc.rest.config;

import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.config.service.TelnumLocationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by zhangxb on 2016/10/25.
 */
@Api(value = "归属地信息", description = "配置中心相关的接口" )
@RequestMapping("/config/attribution")
@RestController
public class TelnumLocationController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(TelnumLocationController.class);
    @Autowired
    TelnumLocationService telnumLocationService;
    @RequestMapping(value = "/province/list",method = RequestMethod.GET)
    @ApiOperation(value = "获取全部省份数据")
    public RestResponse getProvinceList(){
        List list= telnumLocationService.getProvinceList();
        return RestResponse.success(list);
    }
    @RequestMapping(value = "/city/list/{province}",method = RequestMethod.GET)
    @ApiOperation(value = "获取省份对于的城市数据")
    public RestResponse getCityList(
            @ApiParam(name = "province",value = "省份名字")  @PathVariable String province){
        List list= telnumLocationService.getCityAndAreaCode(province);
        return RestResponse.success(list);
    }
}
