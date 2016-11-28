package com.lsxy.app.oc.rest.config;

import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.app.oc.rest.config.vo.ProductEditVo;
import com.lsxy.app.oc.rest.config.vo.ProductVo;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.product.enums.PriceType;
import com.lsxy.yunhuni.api.product.model.Product;
import com.lsxy.yunhuni.api.product.model.ProductPrice;
import com.lsxy.yunhuni.api.product.model.ProductItem;
import com.lsxy.yunhuni.api.product.service.ProductPriceService;
import com.lsxy.yunhuni.api.product.service.ProductService;
import com.lsxy.yunhuni.api.product.service.ProductItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    ProductItemService productItemService;
    @Autowired
    ProductPriceService productPriceService;
    @RequestMapping(value = "/price/type/list/{id}",method = RequestMethod.GET)
    @ApiOperation(value = "获取计价单位")
    public RestResponse priceTypeList(@ApiParam(name = "id",value = "产品计费项id")@PathVariable String id){
        ProductPrice price= productPriceService.findById(id);
        if(price==null){
            return RestResponse.failed("0000","id无对应记录");
        }
        List list= PriceType.getPriceTypeAllByCalType(price.getCalType());
        return RestResponse.success(list);
    }
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ApiOperation(value = "获取产品数据")
    public RestResponse list(){
        List list= (List)productService.list();
        return RestResponse.success(list);
    }
    @RequestMapping(value = "/plist",method = RequestMethod.GET)
    @ApiOperation(value = "获取产品分页数据")
    public RestResponse pList(
            @ApiParam(name = "pageNo",value = "第几页")  @RequestParam(defaultValue = "1")Integer pageNo,
            @ApiParam(name = "pageSize",value = "每页记录数")  @RequestParam(defaultValue = "20")Integer pageSize
    ){
        Page page= productService.pageList(pageNo,pageSize);
        return RestResponse.success(page);
    }
    @ApiOperation(value = "启用产品")
    @RequestMapping(value = "/enabled/{id}",method = RequestMethod.PUT)
    public RestResponse enabled(
            @ApiParam(name = "id",value = "产品id")
            @PathVariable String id){
        Product product = productService.findById(id);
        if(product==null|| StringUtils.isEmpty(product.getId())){
            return RestResponse.failed("0000","产品不存在");
        }
        product.setStatus(1);
        productService.save(product);
        return RestResponse.success("启用产品成功");
    }
    @ApiOperation(value = "禁用产品")
    @RequestMapping(value = "/disabled/{id}",method = RequestMethod.PUT)
    public RestResponse disabled(
            @ApiParam(name = "id",value = "产品id")
            @PathVariable String id) {
        Product product = productService.findById(id);
        if (product == null || StringUtils.isEmpty(product.getId())) {
            return RestResponse.failed("0000", "产品不存在");
        }
        product.setStatus(0);
        productService.save(product);
        return RestResponse.success("禁用产品成功");
    }

    @RequestMapping(value = "price/plist",method = RequestMethod.GET)
    @ApiOperation(value = "获取产品费用分页数据")
    public RestResponse pricePList(
            @ApiParam(name = "pageNo",value = "第几页")  @RequestParam(defaultValue = "1")Integer pageNo,
            @ApiParam(name = "pageSize",value = "每页记录数")  @RequestParam(defaultValue = "20")Integer pageSize
    ){
        Page<ProductPrice> page= productPriceService.getPageOrderCreate(pageNo,pageSize);
        List<ProductVo> tempList = new ArrayList<>();
        for(int i=0;i<page.getResult().size();i++){
            tempList.add(new ProductVo(page.getResult().get(i)));
        }
        Page page1 = new Page(page.getStartIndex(),page.getTotalCount(),page.getPageSize(),tempList);
        return RestResponse.success(page1);
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
        ProductVo productVo = new ProductVo(price);
        return RestResponse.success(productVo);
    }
    @RequestMapping(value = "price/edit/{id}",method = RequestMethod.PUT)
    @ApiOperation(value = "修改产品费用")
    public RestResponse priceEdit(
            @ApiParam(name = "id",value = "费用记录id")  @PathVariable String  id,@RequestBody ProductEditVo productEditVo
    ){
        ProductPrice price= productPriceService.findById(id);
        if(price==null){
            return RestResponse.failed("0000","id无对应记录");
        }
        ProductItem productItem = price.getProductItem();
        if(StringUtils.isNotEmpty(productEditVo.getProductId())&&!productItem.getProduct().getId().equals(productEditVo.getProductId())){
            Product product = productService.findById(productEditVo.getProductId());
            if(product != null){
                productItem.setProduct(product);
            }else{
                return RestResponse.failed("0000","无对应产品");
            }
        }
        if(StringUtils.isNotEmpty(productEditVo.getPriceItem())){
            productItem.setName(productEditVo.getPriceItem());

        }
        PriceType priceType = PriceType.getPriceTypeById(productEditVo.getUnit());
        if(priceType==null){
            return RestResponse.failed("0000","计价单位错误");
        }
        productItem =  productItemService.save(productItem);
        price.setProductItem(productItem);
        price.setPrice(productEditVo.getPrice());
        price.setTimeUnit(priceType.getTimeUnit());
        price.setUnit(priceType.getUnit());
        price.setCalType(priceType.getCalType());
        productPriceService.save(price);
        return RestResponse.success("修改成功");
    }

}
