package com.lsxy.app.oc.rest.file;

import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;
import com.lsxy.yunhuni.api.file.service.VoiceFilePlayService;
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
@RequestMapping("/demand/member/voice")
@RestController
public class VoiceFilePlayController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(VoiceFilePlayController.class);
    @Autowired
    VoiceFilePlayService voiceFilePlayService;
    @Autowired
    TenantService tenantService;



    /**
     * 根据id修改备注
     * @param id
     * @param reason 新备注
     * @return
     */
    @RequestMapping(value = "/edit/{id}" ,method = RequestMethod.GET)
    public RestResponse modifyRemark(@PathVariable String id,@RequestParam(defaultValue = "") String reason,@ApiParam(name = "status",value = "通过1 不通过-1 必填")@RequestParam Integer status){
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
    @RequestMapping(value = "/{type}/list",method = RequestMethod.GET)
    public RestResponse pageList(@ApiParam(name = "type",value = "await|auditing|unauth")@PathVariable String type, @RequestParam(required=false)String name, @RequestParam(required=false)String startTime, @RequestParam(required=false)String endTime, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20")Integer pageSize){
        RestResponse restResponse = null;
        Page page = null;
        if("await".equals(type)||"auditing".equals(type)) {
            Integer status = null;
            if("await".equals(type)){
                status=VoiceFilePlay.STATUS_WAIT;
            }else if("auditing".equals(type)){
                status=VoiceFilePlay.STATUS_SUCCESS;
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
                    page = voiceFilePlayService.pageList(pageNo, pageSize, null, null, tenantId,status, startTime, endTime);
                }
            }else{
                page = voiceFilePlayService.pageList(pageNo, pageSize, null, null, new String[]{},status, startTime, endTime);
            }
            restResponse = RestResponse.success(page);
        }else{
            restResponse = RestResponse.failed("0","访问路劲不存在");
        }

        return restResponse;
    }
}
