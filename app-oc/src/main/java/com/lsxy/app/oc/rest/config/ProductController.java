package com.lsxy.app.oc.rest.config;

import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.product.model.Product;
import com.lsxy.yunhuni.api.product.model.ProductPrice;
import com.lsxy.yunhuni.api.product.model.ProductType;
import com.lsxy.yunhuni.api.product.service.ProductPriceService;
import com.lsxy.yunhuni.api.product.service.ProductService;
import com.lsxy.yunhuni.api.product.service.ProductTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by zhangxb on 2016/11/19.
 */
@Api(value = "产品管理", description = "配置中心相关的接口" )
@RequestMapping("/config/product")
@RestController
public class ProductController  extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    ProductService productService;
    @Autowired
    ProductTypeService productTypeService;
    @Autowired
    ProductPriceService productPriceService;

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ApiOperation(value = "获取产品数据")
    public RestResponse list(){
        List list= (List)productTypeService.list();
        return RestResponse.success(list);
    }
    @RequestMapping(value = "/plist",method = RequestMethod.GET)
    @ApiOperation(value = "获取产品分页数据")
    public RestResponse pList(
            @ApiParam(name = "pageNo",value = "第几页")  @RequestParam(defaultValue = "1")Integer pageNo,
            @ApiParam(name = "pageSize",value = "每页记录数")  @RequestParam(defaultValue = "20")Integer pageSize
    ){
        Page page= productTypeService.pageList(pageNo,pageSize);
        return RestResponse.success(page);
    }
    @ApiOperation(value = "启用产品")
    @RequestMapping(value = "/enabled/{id}",method = RequestMethod.PUT)
    public RestResponse enabled(
            @ApiParam(name = "id",value = "产品id")
            @PathVariable String id){
        ProductType product = productTypeService.findById(id);
        if(product==null|| StringUtils.isEmpty(product.getId())){
            return RestResponse.failed("0000","产品不存在");
        }
        product.setStatus(1);
        productTypeService.save(product);
        return RestResponse.success("启用产品成功");
    }
    @ApiOperation(value = "禁用产品")
    @RequestMapping(value = "/disabled/{id}",method = RequestMethod.PUT)
    public RestResponse disabled(
            @ApiParam(name = "id",value = "产品id")
            @PathVariable String id) {
        ProductType product = productTypeService.findById(id);
        if (product == null || StringUtils.isEmpty(product.getId())) {
            return RestResponse.failed("0000", "产品不存在");
        }
        product.setStatus(0);
        productTypeService.save(product);
        return RestResponse.success("禁用产品成功");
    }

    @RequestMapping(value = "price/plist",method = RequestMethod.GET)
    @ApiOperation(value = "获取产品费用分页数据")
    public RestResponse pricePList(
            @ApiParam(name = "pageNo",value = "第几页")  @RequestParam(defaultValue = "1")Integer pageNo,
            @ApiParam(name = "pageSize",value = "每页记录数")  @RequestParam(defaultValue = "20")Integer pageSize
    ){
        Page page= productPriceService.getPageOrderCreate(pageNo,pageSize);
        return RestResponse.success(page);
    }
    @RequestMapping(value = "price/{id}",method = RequestMethod.GET)
    @ApiOperation(value = "获取产品费用单调记录")
    public RestResponse priceOne(
            @ApiParam(name = "id",value = "费用记录id")  @PathVariable String  id
    ){
        ProductPrice price= productPriceService.findById(id);
        if(price==null){
            return RestResponse.failed("0000","id无对应记录");
        }
        return RestResponse.success(price);
    }
    @RequestMapping(value = "price/edit/{id}",method = RequestMethod.PUT)
    @ApiOperation(value = "修改产品费用")
    public RestResponse priceEdit(
            @ApiParam(name = "id",value = "费用记录id")  @PathVariable String  id
    ){
        ProductPrice price= productPriceService.findById(id);
        if(price==null){
            return RestResponse.failed("0000","id无对应记录");
        }
        return RestResponse.success(price);
    }

}
