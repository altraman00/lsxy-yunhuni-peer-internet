package com.lsxy.app.oc.rest.cost;

import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.framework.api.invoice.model.InvoiceApply;
import com.lsxy.framework.api.invoice.service.InvoiceApplyService;
import com.lsxy.framework.api.invoice.service.InvoiceInfoService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.consume.enums.ConsumeCode;
import com.lsxy.yunhuni.api.consume.model.Consume;
import com.lsxy.yunhuni.api.consume.service.ConsumeService;
import com.lsxy.yunhuni.api.message.model.AccountMessage;
import com.lsxy.yunhuni.api.message.service.AccountMessageService;
import com.lsxy.yunhuni.api.statistics.model.ConsumeDay;
import com.lsxy.yunhuni.api.statistics.service.ConsumeDayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发票申请
 * Created by liups on 2016/7/21.
 */
@Api(value = "发票审核", description = "财务中心相关的接口" )
@RequestMapping("/finance/invoice")
@RestController
public class InvoiceApplyController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(InvoiceApplyController.class);
    @Autowired
    InvoiceApplyService invoiceApplyService;
    @Autowired
    TenantService tenantService;
    @Autowired
    InvoiceInfoService invoiceInfoService;
    @Autowired
    ConsumeService consumeService;
    @Autowired
    ConsumeDayService consumeDayService;
    @Autowired
    AccountMessageService accountMessageService;
    /**
     * 发票申请分页获取
     * @param status await|auditing|unauth
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @param name 名字
     * @return
     * @return
     */
    @RequestMapping(value = "/{status}/send/list",method = RequestMethod.GET)
    @ApiOperation(value = "发票邮寄分页获取")
    public RestResponse page(
            @ApiParam(name = "status",value = "状态await待寄出auditing已寄出")
            @PathVariable String status,
            @ApiParam(name = "type",value = "类型：1个人增值税普通发票2：企业增值税普通发票3:企业增值税专用发票")
            @RequestParam(required=false)Integer type,
            @ApiParam(name = "name",value = "会员名")
            @RequestParam(required=false)String name,
            @RequestParam(defaultValue = "1")Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize){
        RestResponse restResponse = null;
        Page page = null;
        if("await".equals(status)||"auditing".equals(status)) {
            Integer statusT = InvoiceApply.STATUS_DONE;
            Boolean flag = false;
            if("await".equals(status)){
                flag=false;
            }else if("auditing".equals(status)){
                flag=true;
            }
            if(StringUtil.isNotEmpty(name)) {
                List<Tenant> tList = tenantService.pageListByUserName(name);
                if (tList.size() == 0) {
                    page = null;
                } else {
                    String[] tenantId = new String[tList.size()];
                    for (int i = 0; i < tList.size(); i++) {
                        tenantId[i] = tList.get(i).getId();
                    }
                    page = invoiceApplyService.pageList(pageNo,pageSize,tenantId,statusT,type,flag);
                }
            }else{
                page = invoiceApplyService.pageList(pageNo, pageSize, new String[]{},statusT,type,flag);
            }
            restResponse = RestResponse.success(page);
        }else{
            restResponse = RestResponse.failed("0","访问路劲不存在");
        }
        return RestResponse.success(page);
    }
    /**
     * 发票申请分页获取
     * @param type await|auditing|unauth
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @param name 名字
     * @param startTime 开始时间 yyyy-MM-dd
     * @param endTime 结束时间 yyyy-MM-dd
     * @return
     * @return
     */
    @ApiOperation(value = "发票申请分页获取")
    @RequestMapping(value = "/{status}/list",method = RequestMethod.GET)
    public RestResponse page(
            @ApiParam(name = "status",value = "状态await待处理auditing审核通过unauth异常")
            @PathVariable String status,
            @ApiParam(name = "type",value = "类型：1个人增值税普通发票2：企业增值税普通发票3:企业增值税专用发票")
            @RequestParam(required=false)Integer type,
            @ApiParam(name = "name",value = "会员名")
            @RequestParam(required=false)String name,
            @ApiParam(name = "startTime",value = "开始时间 yyyy-MM-dd")
            @RequestParam(required=false)String startTime,
            @ApiParam(name = "endTime",value = "结束时间 yyyy-MM-dd")
            @RequestParam(required=false) String endTime,
            @RequestParam(defaultValue = "1")Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize){
        RestResponse restResponse = null;
        Page page = null;
        if("await".equals(status)||"auditing".equals(status)||"unauth".equals(status)) {
            Integer statusT = null;
            if("await".equals(status)){
                statusT = InvoiceApply.STATUS_SUBMIT;
            }else if("auditing".equals(status)){
                statusT = InvoiceApply.STATUS_DONE;
            }else if("unauth".equals(status)){
                statusT = InvoiceApply.STATUS_EXCEPTION;
            }
            if (StringUtil.isNotEmpty(name)) {
                List<Tenant> tList = tenantService.pageListByUserName(name);
                if (tList.size() == 0) {
                    page = null;
                } else {
                    String[] tenantId = new String[tList.size()];
                    for (int i = 0; i < tList.size(); i++) {
                        tenantId[i] = tList.get(i).getId();
                    }
                    page = invoiceApplyService.pageList(pageNo,pageSize,type,tenantId,statusT,startTime,endTime);
                }
            }else{
                page = invoiceApplyService.pageList(pageNo, pageSize, type, new String[]{},statusT, startTime, endTime);
            }
            restResponse = RestResponse.success(page);
        }else{
            restResponse = RestResponse.failed("0","访问路劲不存在");
        }
        return RestResponse.success(page);
    }

    /**
     * 保存发票申请信息
     * @param invoiceApplyVo 接收参数对象
     * @return
     */
    @ApiOperation(value = "保存发票审核信息")
    @RequestMapping(value = "/edit/{id}",method = RequestMethod.PUT)
    public RestResponse modify(
            @PathVariable String id,
            @RequestBody  InvoiceApplyVo invoiceApplyVo){
        Integer status = invoiceApplyVo.getStatus();
        InvoiceApply invoiceApply = invoiceApplyService.findById(id);
        RestResponse restResponse =null;
        if(status==InvoiceApply.STATUS_DONE||status==InvoiceApply.STATUS_EXCEPTION){
            invoiceApply.setStatus(status);
            try {
                BeanUtils.copyProperties(invoiceApply,invoiceApplyVo);
                invoiceApply = invoiceApplyService.save(invoiceApply);
                if(invoiceApply.getStatus()==InvoiceApply.STATUS_EXCEPTION){
                    accountMessageService.sendTenantTempletMessage(null,invoiceApply.getTenant().getId(), AccountMessage.MESSAGE_TYPE_INVOCE_APPLY_FAIL);
                }
            } catch (IllegalAccessException e) {
                logger.error("参数错误",e);
                restResponse = RestResponse.failed("0","参数错误");
            } catch (InvocationTargetException e) {
                logger.error("参数错误",e);
                restResponse = RestResponse.failed("0","参数错误");
            }
        }else{
            restResponse = RestResponse.failed("0","参数错误");
        }
        if(restResponse==null){
            restResponse = RestResponse.success(invoiceApply);
        }
        return restResponse;
    }
    /**
     * 保存发票申请信息
     * @param invoiceApplyVo 接收参数对象
     * @return
     */
    @ApiOperation(value = "保存发票邮寄信息")
    @RequestMapping(value = "/edit/send/{id}",method = RequestMethod.PUT)
    public RestResponse modifySend(
            @PathVariable String id,
            @RequestBody InvoiceApplyVo invoiceApplyVo){
        InvoiceApply invoiceApply = invoiceApplyService.findById(id);
        RestResponse restResponse =null;
        if(invoiceApply.getStatus()==InvoiceApply.STATUS_DONE){
            invoiceApply.setExpressNo(invoiceApplyVo.getExpressNo());
            invoiceApply.setExpressCom(invoiceApplyVo.getExpressCom());
            invoiceApply = invoiceApplyService.save(invoiceApply);
            accountMessageService.sendTenantTempletMessage(null,invoiceApply.getTenant().getId(), AccountMessage.MESSAGE_TYPE_INVOCE_APPLY_SUCCESS);
        }else{
            restResponse = RestResponse.failed("0","该发票审核未通过，不能发送");
        }
        if(restResponse==null){
            restResponse = RestResponse.success(invoiceApply);
        }
        return restResponse;
    }

    /**
     * 根据ID获取申请信息
     * @param id 发票记录
     * @return
     */
    @ApiOperation(value = "查看发票申请信息")
    @RequestMapping(value = "/detail/{id}",method = RequestMethod.GET)
    public RestResponse get(@PathVariable String id){
        InvoiceApply invoiceApply = invoiceApplyService.findById(id);
        return RestResponse.success(invoiceApply);
    }

    /**
     * 获取分页数据
     * @return
     */
    @ApiOperation(value = "查看发票申请信息的消费列表信息")
    @RequestMapping(value = "/detail/list/{id}",method = RequestMethod.GET)
    public RestResponse consumePageList(
            @ApiParam(name = "id",value = "发票记录id")
            @PathVariable String id,
            @RequestParam(defaultValue = "1") Integer pageNo ,
            @RequestParam(defaultValue = "20")Integer pageSize){
        InvoiceApply invoiceApply = invoiceApplyService.findById(id);
        Map map = new HashMap();
        map.put("sum",invoiceApply.getAmount());
    //    pageList(String tenantId, String appId,String type,Date startTime, Date endTime,Integer pageNo,Integer pageSize);
//        Page<Consume> page =  consumeService.pageList(invoiceApply.getTenant().getId(),pageNo,pageSize,invoiceApply.getStart(),DateUtils.getLastTimeOfMonth(invoiceApply.getEnd()) );
        Page<ConsumeDay> page = consumeDayService.pageList(invoiceApply.getTenant().getId(),null,null,invoiceApply.getStart(),DateUtils.getLastTimeOfMonth(invoiceApply.getEnd()),pageNo,pageSize);
        map.put("list",page);
        return RestResponse.success(map);
    }
    @ApiOperation(value = "查看发票申请信息的消费列表中某天的信息")
    @RequestMapping(value = "/detail/list/{id}/detail",method = RequestMethod.GET)
    public RestResponse consumeDetailPageList(
            @ApiParam(name = "id",value = "发票记录id")
            @PathVariable String id,
            @ApiParam(name = "time",value = "yyyy-MM-dd")
            @RequestParam String time){
        InvoiceApply invoiceApply = invoiceApplyService.findById(id);
        Date startTime = DateUtils.parseDate(time,"yyyy-MM-dd");
        Date endTime = DateUtils.parseDate(time+" 23:59:59","yyyy-MM-dd HH:mm:ss");
        List<ConsumeDay> list =  consumeDayService.list(invoiceApply.getTenant().getId(),null,false,startTime,endTime);
        changeTypeToChinese(list);
        return RestResponse.success(list);
    }
    /**
     * 将消费类型转换为中文，运用枚举
     * @param consumes
     */
    private void changeTypeToChinese(List<ConsumeDay> consumes){
        for(int i = 0;i < consumes.size();i++){
            ConsumeDay consume = new ConsumeDay();
            try {
                BeanUtils.copyProperties(consume,consumes.get(i));
            } catch (Exception e) {
                logger.error("转换类属性出错",e);
            }
            String type = consume.getType();
            try{
                ConsumeCode consumeCode = ConsumeCode.valueOf(type);
                consume.setType(consumeCode.getName());
            }catch(Exception e){
//                e.printStackTrace();
                consume.setType("未知项目");
            }
            consumes.set(i,consume);
        }
    }
    /**
     * 等待处理数量
     * @return
     */
    @ApiOperation(value = "等待处理数量,await表示未处理，awaitSend未寄送")
    @RequestMapping(value = "/await/num",method = RequestMethod.GET)
    public RestResponse getAwaitNum(){
        Map map = new HashMap();
        map.putAll(invoiceApplyService.getAwaitNum());
        return RestResponse.success(map);
    }

}
