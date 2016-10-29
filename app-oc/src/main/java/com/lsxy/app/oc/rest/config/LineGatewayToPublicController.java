package com.lsxy.app.oc.rest.config;

import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.app.oc.rest.config.vo.EditPriorityVo;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.config.model.LineGatewayToPublic;
import com.lsxy.yunhuni.api.config.service.LineGatewayService;
import com.lsxy.yunhuni.api.config.service.LineGatewayToPublicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by zhangxb on 2016/10/25.
 */
@Api(value = "全局线路管理", description = "配置中心相关的接口" )
@RequestMapping("/config/public")
@RestController
public class LineGatewayToPublicController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(LineGatewayToPublicController.class);
    @Autowired
    LineGatewayService lineGatewayService;
    @Autowired
    LineGatewayToPublicService lineGatewayToPublicService;
    @RequestMapping(value = "/plist",method = RequestMethod.GET)
    @ApiOperation(value = "获取分页数据")
    public RestResponse pList(
            @ApiParam(name = "pageNo",value = "第几页")  @RequestParam(defaultValue = "1")Integer pageNo,
            @ApiParam(name = "pageSize",value = "每页记录数")  @RequestParam(defaultValue = "20")Integer pageSize,
            @ApiParam(name = "operator",value = "运营商 中国电信；中国移动；中国联通") @RequestParam(required = false)String operator,
            @ApiParam(name = "isThrough",value = "是否透传 1支持透传 0不支持透传")@RequestParam(required = false) String isThrough,
            @ApiParam(name = "status",value = "状态 1可用 0不可用") @RequestParam(required = false)String status,
//            @ApiParam(name = "isPublicLine",value = "1:全局线路;0:租户专属线路") @RequestParam(required = false)String isPublicLine,
            @ApiParam(name = "order",value = "quality:1按质量降序，quality:0按质量升序,capacity:1按容量降序capacity:0按容量降序") @RequestParam(required = false)String order
    ){
        Page page= lineGatewayToPublicService.getPage(pageNo,pageSize,operator,isThrough,status,null,order);
        return RestResponse.success(page);
    }
    @ApiOperation(value = "将线路加入全局")
    @RequestMapping(value = "/add/{id}",method = RequestMethod.POST)
    public RestResponse addPublic(@ApiParam(name = "id",value = "线路id") @PathVariable String id){
        LineGateway lineGateway = lineGatewayService.findById(id);
        if(lineGateway!=null&& StringUtils.isNotEmpty(lineGateway.getId())){
            int re1 = lineGatewayToPublicService.findByLindId(id);
            if(re1>0){
                return RestResponse.failed("0000","线路已经加入全局序列中");
            }else{
                //获取当前最大编号
                int re2 = lineGatewayToPublicService.getMaxPriority();
                re2++;
                //新建关系
                LineGatewayToPublic lineGatewayToPublic = new LineGatewayToPublic();
                lineGatewayToPublic.setLineGateway(lineGateway);
                lineGatewayToPublic.setPriority(re2);
                lineGatewayToPublicService.save(lineGatewayToPublic);
                //修改线路状态标识为加入全局
                lineGateway.setIsPublicLine("1");
                lineGatewayService.save(lineGateway);
            }
        }else{
            return RestResponse.failed("0000","线路不存在");
        }
        return RestResponse.success("修改成功");
    }

    @ApiOperation(value = "将线路移除全局线路")
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public RestResponse removePublic(@ApiParam(name = "id",value = "全局线路id") @PathVariable String id) throws InvocationTargetException, IllegalAccessException {
        LineGatewayToPublic lineGatewayToPublic = lineGatewayToPublicService.findById(id);
        if(lineGatewayToPublic!=null&& StringUtils.isNotEmpty(lineGatewayToPublic.getId())){
            //删除线路关系
            lineGatewayToPublicService.delete(lineGatewayToPublic);
            //修改对应线路关系
            LineGateway lineGateway = lineGatewayToPublic.getLineGateway();
            if(lineGateway!=null){
                lineGateway.setIsPublicLine("0");
                lineGatewayService.save(lineGateway);
            }
            //修正优先级
            int o3 = lineGatewayToPublicService.getMaxPriority();
            if(o3!=lineGatewayToPublic.getPriority()){
                int re = upPriority(lineGatewayToPublic.getPriority(),o3,null);
                if(re==-1){
                    return RestResponse.failed("0000","删除成功，修正失败，请手动修正");
                }
            }
        }else{
            return RestResponse.failed("0000","线路不存在");
        }
        return RestResponse.success("删除成功");
    }
    @ApiOperation(value = "修改优先级")
    @RequestMapping(value = "/edit/priority/{id}",method = RequestMethod.PUT)
    public RestResponse modify(@ApiParam(name = "id",value = "线路id")
                               @PathVariable String id,@RequestBody EditPriorityVo lineGatewayToPublicEditPriorityVo){
        int o2 = 0;
        try{ o2=Integer.valueOf(lineGatewayToPublicEditPriorityVo.getPriority());}catch (Exception e){
            return RestResponse.failed("0000","目标优先级只能为数字");
        }
        if(o2==0){
            return RestResponse.failed("0000","目标优先级不能为0");
        }
        int o3 = lineGatewayToPublicService.getMaxPriority();
        if(o2>03){
            return RestResponse.failed("0000","目标优先级不能超过当前最大优先级");
        }
        LineGatewayToPublic lineGatewayToPublic = lineGatewayToPublicService.findById(id);
        if(lineGatewayToPublic!=null&&StringUtils.isNotEmpty(lineGatewayToPublic.getId())) {
            int o1 = Integer.valueOf(lineGatewayToPublic.getPriority());
            if(o1==o2){
                return RestResponse.failed("0000","目标优先级和当前优先级一致");
            }
            int re = upPriority(o1,o2,lineGatewayToPublic.getId());
            if(re==-1){
                return RestResponse.failed("0000","修改失败，请重试");
            }
        }else{
            return RestResponse.failed("0000","线路不存在");
        }
        return RestResponse.success("修改成功");
    }
    private int upPriority(int o1,int o2,String line){
        String flag = "+1";
        int begin = -1;
        int end = -1;
        if(o1<o2){
            begin = o1+1;
            end = o2;
            flag = "-1";
        }else{
            begin = o2;
            end = o1-1;
            flag = "+1";
        }
        String[] sql = new String[2];
        sql[0] = " UPDATE db_lsxy_bi_yunhuni.tb_oc_linegateway_to_public SET priority=priority"+flag+" WHERE priority BETWEEN "+begin+" AND "+end+" ";
        if(StringUtils.isNotEmpty(line)) {
            sql[1] = " UPDATE db_lsxy_bi_yunhuni.tb_oc_linegateway_to_public SET priority=" + o2 + " WHERE id ='" + line + "' ";
        }
        int re = lineGatewayService.batchModify(sql);
        return re;
    }
}
