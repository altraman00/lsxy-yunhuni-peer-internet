package com.lsxy.app.oc.rest.file;

import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.yunhuni.api.message.model.AccountMessage;
import com.lsxy.yunhuni.api.message.service.AccountMessageService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.oc.VoiceFilePlayAuditCompletedEvent;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;
import com.lsxy.yunhuni.api.file.service.VoiceFilePlayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


/**
 * 放音文件
 * Created by zhangxb on 2016/7/21.
 */
@Api(value = "放音文件", description = "审核管理相关的接口" )
@RequestMapping("/demand/member/voice")
@RestController
public class VoiceFilePlayController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(VoiceFilePlayController.class);
    @Autowired
    VoiceFilePlayService voiceFilePlayService;
    @Autowired
    TenantService tenantService;
    @Autowired
    AccountMessageService accountMessageService;
    @Autowired
    private MQService mqService;

    /**
     * 根据id修改备注
     * @param voiceFilePlayVo 接收参数对象
     * @return
     */
    @ApiOperation(value = "根据id修改备注")
    @RequestMapping(value = "/edit/{id}" ,method = RequestMethod.PUT)
    public RestResponse modifyRemark(
            @PathVariable String id,
            @RequestBody VoiceFilePlayVo voiceFilePlayVo){
        Integer status = voiceFilePlayVo.getStatus();
        String reason = voiceFilePlayVo.getReason();
        RestResponse restResponse = null;
        VoiceFilePlay voiceFilePlay = null;
        if(status==VoiceFilePlay.STATUS_SUCCESS||status==VoiceFilePlay.STATUS_FAIL){
            try {
                voiceFilePlay = voiceFilePlayService.findById(id);
                if (voiceFilePlay != null) {
                    voiceFilePlay.setStatus(status);
                    voiceFilePlay.setReason(reason);
                    voiceFilePlay.setCheckTime(new Date());
                    voiceFilePlay = voiceFilePlayService.save(voiceFilePlay);
                    if(voiceFilePlay.getStatus()==VoiceFilePlay.STATUS_FAIL){
                        accountMessageService.sendTenantTempletMessage(null,voiceFilePlay.getTenant().getId(),AccountMessage.MESSAGE_TYPE_VOICE_PLAY_FAIL );
                    }else if(voiceFilePlay.getStatus()==VoiceFilePlay.STATUS_SUCCESS){
                        accountMessageService.sendTenantTempletMessage(null,voiceFilePlay.getTenant().getId(),AccountMessage.MESSAGE_TYPE_VOICE_PLAY_SUCCESS);
                        VoiceFilePlayAuditCompletedEvent vfpace = new VoiceFilePlayAuditCompletedEvent(voiceFilePlay.getId());
                        mqService.publish(vfpace);
                    }
                } else {
                    restResponse = RestResponse.failed("0", "参数id无效");
                }
            }catch (Exception e){
                restResponse = RestResponse.failed("0", "对象不存在");
            }
        }else{
            restResponse = RestResponse.failed("0","参数status无效");
        }
        if(restResponse==null){
            restResponse = RestResponse.success(voiceFilePlay);
        }
        return restResponse;
    }

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
            @ApiParam(name = "name",value = "会员名")
            @RequestParam(required=false)String name,
            @ApiParam(name = "startTime",value = "开始时间 yyyy-MM-dd")
            @RequestParam(required=false)String startTime,
            @ApiParam(name = "endTime",value = "结束时间 yyyy-MM-dd")
            @RequestParam(required=false)String endTime,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "20")Integer pageSize
    ){
        RestResponse restResponse = null;
        Page page = null;
        if("await".equals(type)||"auditing".equals(type)||"unauth".equals(type) ){
            Integer status = null;
            if("await".equals(type)){
                status=VoiceFilePlay.STATUS_WAIT;
            }else if("auditing".equals(type)){
                status=VoiceFilePlay.STATUS_SUCCESS;
            }else if("unauth".equals(type) ){
                status=VoiceFilePlay.STATUS_FAIL;
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
                    page = voiceFilePlayService.pageList(pageNo, pageSize, null, null, tenantId,status, startTime, endTime,null);
                }
            }else{
                page = voiceFilePlayService.pageList(pageNo, pageSize, null, null, new String[]{},status, startTime, endTime,null);
            }
            restResponse = RestResponse.success(page);
        }else{
            restResponse = RestResponse.failed("0","访问路劲不存在");
        }

        return restResponse;
    }
}
