package com.lsxy.app.oc.rest.account;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.app.oc.rest.file.VoiceFilePlayVo;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.oc.VoiceFilePlayAuditCompletedEvent;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.msg.api.model.MsgTemplate;
import com.lsxy.msg.api.service.MsgTemplateService;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;
import com.lsxy.yunhuni.api.file.service.VoiceFilePlayService;
import com.lsxy.yunhuni.api.message.model.AccountMessage;
import com.lsxy.yunhuni.api.message.service.AccountMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 模板审核
 * Created by zhangxb on 2016/7/21.
 */
@Api(value = "消息模板审核", description = "审核管理相关的接口" )
@RequestMapping("/demand/member/msgtemplate")
@RestController
public class MsgTemptaleController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(MsgTemptaleController.class);
    @Reference(timeout=3000,check = false,lazy = true)
    private MsgTemplateService msgTemplateService;
    /**
     * 根据名字和应用id查询用户名下的放音文件
     * @param type await|auditing|unauth
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @param name 名字
     * @param startTime 开始时间 yyyy-MM-dd
     * @param endTime 结束时间 yyyy-MM-dd
     * @return
     */
    @ApiOperation(value = "根据名字和应用id查询用户名下的放音文件分页信息")
    @RequestMapping(value = "/{type}/list",method = RequestMethod.GET)
    public RestResponse pageList(
            @ApiParam(name = "type",value = "状态await待处理auditing审核通过unauth不通过")
            @PathVariable String type,
            @ApiParam(name = "name",value = "模板名称")
            @RequestParam(required=false)String name,
            @ApiParam(name = "startTime",value = "开始时间 yyyy-MM-dd")
            @RequestParam(required=false)String startTime,
            @ApiParam(name = "endTime",value = "结束时间 yyyy-MM-dd")
            @RequestParam(required=false)String endTime,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "20")Integer pageSize
    ){
        int state = 0;
        if("await".equals(type)){
            state = 0;
        }else if("auditing".equals(type)){
            state = 1;
        }else if("unauth".equals(type)){
            state = -1;
        }else{
            return RestResponse.failed("","类型错误");
        }
        Date date1 = null;
        Date date2 = null;
        try{
            if(StringUtils.isNotEmpty(startTime)) {
                date1 = DateUtils.parseDate(startTime, "yyyy-MM-dd");
            }
            if(StringUtils.isNotEmpty(endTime)) {
                date2 = DateUtils.parseDate(endTime, "yyyy-MM-dd");
            }
        }catch (Exception e){
            return RestResponse.failed("","日期格式错误");
        }
        Page page = msgTemplateService.getPageByCondition(pageNo,pageSize,state,date1,date2,name);
        return RestResponse.success(page);
    }
    @ApiOperation(value = "根据模板ID修改模板状态为审核通过")
    @RequestMapping(value = "/pass/{id}",method = RequestMethod.PUT)
    public RestResponse pass(
            @ApiParam(name = "id",value = "模板ID")
            @PathVariable String id
    ){
        MsgTemplate msgTemplate = msgTemplateService.findById(id);
        if(msgTemplate!=null){
            msgTemplate.setStatus(MsgTemplate.STATUS_PASS);
            msgTemplateService.save(msgTemplate);
            return RestResponse.success();
        }else{
            return RestResponse.failed("","模板不存在");
        }
    }
    @ApiOperation(value = "根据模板ID修改模板状态为审核不通过")
    @RequestMapping(value = "/nopass/{id}",method = RequestMethod.PUT)
    public RestResponse pageList(
            @ApiParam(name = "id",value = "模板ID")
            @PathVariable String id,
            @ApiParam(name="reason",value = "原因")
            @RequestBody Map map
            ){
        MsgTemplate msgTemplate = msgTemplateService.findById(id);
        if(msgTemplate!=null){
            msgTemplate.setStatus(MsgTemplate.STATUS_FAIL);
            msgTemplate.setReason((String)map.get("reason"));
            msgTemplateService.save(msgTemplate);
            return RestResponse.success();
        }else{
            return RestResponse.failed("","模板不存在");
        }
    }
}
