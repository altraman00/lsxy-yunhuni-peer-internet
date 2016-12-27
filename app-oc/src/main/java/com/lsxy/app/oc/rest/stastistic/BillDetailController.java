package com.lsxy.app.oc.rest.stastistic;


import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.call.center.api.service.CallCenterConversationMemberService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.OssTempUriUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.portal.VoiceFileRecordSyncEvent;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.file.model.VoiceFileRecord;
import com.lsxy.yunhuni.api.file.service.VoiceFileRecordService;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import com.lsxy.yunhuni.api.session.model.CallSession;
import com.lsxy.yunhuni.api.session.model.MeetingMember;
import com.lsxy.yunhuni.api.session.model.VoiceCdr;
import com.lsxy.yunhuni.api.session.service.MeetingMemberService;
import com.lsxy.yunhuni.api.session.service.VoiceCdrService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;

import java.net.URL;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * 详单查询
 * Created by zhangxb on 2016/7/6.
 */
@RequestMapping("/tenant")
@Api(value = "会话详单", description = "租户中心相关的接口" )
@RestController
public class BillDetailController extends AbstractRestController {
    @Autowired
    VoiceCdrService voiceCdrService;
    @Autowired
    VoiceFileRecordService voiceFileRecordService;
    @Autowired
    MeetingMemberService meetingMemberService;
    @Reference(timeout=3000,check = false,lazy = true)
    private CallCenterConversationMemberService callCenterConversationMemberService;
    @Autowired
    MQService mqService;
    @Autowired
    TenantService tenantService;
    @Autowired
    AppService appService;
    /**
     * 会话详单查询
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @param time 时间
     * @param appId 应用id
     * @param type 选择类型
     * @return total 总计数，page分页数
     */
    @RequestMapping(value = "/{uid}/session" ,method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "会话详单查询")
    public RestResponse call(
            @ApiParam(name = "uid",value = "用户id")
            @PathVariable String uid,
            @ApiParam(name = "type",value = "voice_call.语音通知,duo_call.双向回拨,conf_call.会议服务,ivr_call.IVR定制服务,captcha_call.语音验证码,voice_recording.录音服务call_center呼叫中心类型")
            @RequestParam String type,
            @ApiParam(name = "time",value = "yyyy-MM-dd")
            @RequestParam(required=false) String time,
            @ApiParam(name = "appId",value = "应用id")
            @RequestParam(required=false)String appId,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize
    ){
        Map re = new HashMap();
        RestResponse restResponse = null;
        if(StringUtil.isNotEmpty(appId)&&StringUtil.isNotEmpty(type)){
            //获取分页数据
            Page page = voiceCdrService.pageList(pageNo, pageSize, type, uid, time, appId);
            re.put("page", page);
            if (CallSession.TYPE_VOICE_VOICECODE.equals(type)) {//语音验证码
                re.put("total", page.getTotalCount());
            } else {
                Map map = voiceCdrService.sumCost(type, uid, time, appId);
                re.put("total", map.get("cost"));
            }
            restResponse = RestResponse.success(re);
        }else{
            restResponse = RestResponse.failed("0","上传参数错误");
        }
        return restResponse;
    }
    @RequestMapping(value = "/{uid}/session/voice_recording" ,method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "会话详单之录音查询")
    public RestResponse voice_recording(
            @ApiParam(name = "uid",value = "用户id")
            @PathVariable String uid,
            @ApiParam(name = "type",value = "为空或者是返回的types里面的值")
            @RequestParam(required = false) String type,
            @ApiParam(name = "time",value = "yyyy-MM-dd")
            @RequestParam(required=false) String time,
            @ApiParam(name = "appId",value = "应用id")
            @RequestParam(required=false)String appId,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize
    ){
        Date start = null;
        Date end = null;
        try{
            start = DateUtils.parseDate(time+" 00:00:00","yyyy-MM-dd HH:mm:ss");
            end = DateUtils.parseDate(time+" 23:59:59","yyyy-MM-dd HH:mm:ss");
        }catch (Exception e){
            return RestResponse.failed("0000","日期格式错误");
        }
        Page<Map> page = voiceFileRecordService.getPageList(pageNo,pageSize,appId,uid,type,start,end);
        Map map = voiceFileRecordService.sumAndCount(appId,uid,type,start,end);
        Map re = new HashMap();
        re.put("page",page);
        re.put("total",map);
        String serviceType = "";
        if(StringUtils.isNotEmpty(appId)) {
            App app = appService.findById(appId);
            serviceType = app.getServiceType();
        }
        re.put("types",VoiceFileRecord.getRecordType(serviceType));
        return RestResponse.success(re);
    }
    @RequestMapping(value = "/{uid}/file/download/{id}" ,method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "会话详单之录音文件下载")
    public WebAsyncTask fileDownload( @ApiParam(name = "uid",value = "用户id")@PathVariable String uid, @ApiParam(name = "id",value = "记录id") @PathVariable String id){
        Callable<RestResponse> callable = new Callable<RestResponse>() {
            public RestResponse call() throws Exception {
                Tenant tenant = tenantService.findById(uid);
                VoiceFileRecord voiceFileRecord = voiceFileRecordService.findById(id);
                if(tenant==null||voiceFileRecord==null||!tenant.getId().equals(voiceFileRecord.getTenantId())){
                    return RestResponse.failed("0000","验证失败，无法下载");
                }
                if(voiceFileRecord.getStatus()!=null&&voiceFileRecord.getStatus()==1){
                    String ossUri = getOssTempUri(voiceFileRecord.getOssUrl());
                    return RestResponse.success(ossUri);
                }
                List<VoiceFileRecord> list = voiceFileRecordService.getListBySessionId(voiceFileRecord.getSessionId());
                if(list==null||list.size()==0){
                    return RestResponse.failed("0000","无对应的录音文件");
                }
                //先判断是否文件已上传，如果是的话，直接生成临时下载链接，否则
                boolean flag = false;
                for(int i=0;i<list.size();i++){
                    VoiceFileRecord temp = list.get(i);
                    if(voiceFileRecord.getStatus()==null||1!=temp.getStatus()){
                        flag=true;
                        break;
                    }
                }
                //发起文件上传
                if(flag) {
                    mqService.publish(new VoiceFileRecordSyncEvent(tenant.getId(), voiceFileRecord.getAppId(), voiceFileRecord.getId(), VoiceFileRecordSyncEvent.TYPE_FILE));
                    for (int j = 1; j <= 30; j++) {
                        Thread.sleep(j * 1000);
                        VoiceFileRecord v1 = voiceFileRecordService.findById(id);
                        if(v1.getStatus()!=null){
                            if(v1.getStatus()==1) {
                                String ossUri = getOssTempUri(v1.getOssUrl());
                                return RestResponse.success(ossUri);
                            }else if(v1.getStatus()==-1){
                                return RestResponse.failed("0000","下载失败，请稍后重试");
                            }
                        }
                    }
                    return RestResponse.failed("0000","下载超时失败");
                }else {
                    String ossUri = getOssTempUri(list.get(0).getOssUrl());
                    return RestResponse.success(ossUri);
                }
            }
        };
        return new WebAsyncTask(callable);
    }
    @RequestMapping(value = "/{uid}/cdr/download/{id}" ,method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "会话详单之CDR文件（非录音文件）下载")
    public WebAsyncTask cdrDownload(@ApiParam(name = "uid",value = "用户id")@PathVariable String uid, @ApiParam(name = "id",value = "记录id") @PathVariable String id){
        Callable<RestResponse> callable = new Callable<RestResponse>() {
            public RestResponse call() throws Exception {
                Tenant tenant = tenantService.findById(uid);
                VoiceCdr voiceCdr = voiceCdrService.findById(id);
                if(tenant==null||voiceCdr==null||!tenant.getId().equals(voiceCdr.getTenantId())){
                    return RestResponse.failed("0000","验证失败，无法下载");
                }
                List<VoiceFileRecord> list = getFile(id);
                if(list==null||list.size()==0){
                    return RestResponse.failed("0000","无对应的录音文件");
                }
                //先判断是否文件已上传，如果是的话，直接生成临时下载链接，否则
                boolean flag = false;
                for(int i=0;i<list.size();i++){
                    VoiceFileRecord voiceFileRecord = list.get(i);
                    if(voiceFileRecord.getStatus()!=null&&1!=voiceFileRecord.getStatus()){
                        flag=true;
                        break;
                    }
                }
                //发起文件上传
                if(flag) {
                    VoiceFileRecord temp = voiceFileRecordService.findById(list.get(0).getId());
                    temp.setStatus(0);
                    voiceFileRecordService.save(temp);
                    mqService.publish(new VoiceFileRecordSyncEvent(tenant.getId(), voiceCdr.getAppId(), voiceCdr.getId(), VoiceFileRecordSyncEvent.TYPE_CDR));
                    for (int j = 1; j <= 30; j++) {
                        Thread.sleep(j * 1000);
                        VoiceFileRecord v1 = voiceFileRecordService.findById(list.get(0).getId());
                        if(v1.getStatus()!=null){
                            if(v1.getStatus()==1) {
                                String ossUri = getOssTempUri(v1.getOssUrl());
                                return RestResponse.success(ossUri);
                            }else if(v1.getStatus()==-1){
                                return RestResponse.failed("0000","下载失败，请稍后重试");
                            }
                        }
                    }
                    return RestResponse.failed("0000","下载超时失败");
                }else {
                    String ossUri = getOssTempUri(list.get(0).getOssUrl());
                    return RestResponse.success(ossUri);
                }
            }
        };
        return new WebAsyncTask(callable);
    }
    private List<VoiceFileRecord> getFile(String id){
        //根据cdr获取业务类型，和业务id，根据业务id和业务类型获取录音文件列表，
        VoiceCdr voiceCdr = voiceCdrService.findById(id);
        List list = null;
        if(voiceCdr!=null&& StringUtils.isNotEmpty(voiceCdr.getId())) {
            ProductCode p1 = ProductCode.valueOf(voiceCdr.getType());
            switch(p1){
                case sys_conf:{
                    //获取会议操作者
                    MeetingMember meetingMember = meetingMemberService.findById(voiceCdr.getSessionId());
                    if (meetingMember!=null) {
                        //使用会议id
                        list = voiceFileRecordService.getListBySessionId(meetingMember.getMeeting().getId());
                    }
                    break;
                }
                case ivr_call:{
                    //使用ivr的id
                    list = voiceFileRecordService.getListBySessionId(voiceCdr.getSessionId());
                    break;
                }
                case duo_call:{
                    //使用双向回拨的id
                    list = voiceFileRecordService.getListBySessionId(voiceCdr.getSessionId());
                    break;
                }
                case call_center:{
                    //根据sessionid获取呼叫中心交互成员，在获取呼叫中心交谈，在获取文件
                    List<String> temp = callCenterConversationMemberService.getListBySessionId(voiceCdr.getSessionId());
                    if (temp!=null&&temp.size() > 0) {
//
//                        String te = "";
//                        for (int i = 0; i < temp.size(); i++) {
//                            te += "'" + temp.get(i) + "'";
//                            if (i != temp.size() - 1) {
//                                te += ",";
//                            }
//                        }
                        //使用ivr的id
                        list = voiceFileRecordService.getListBySessionId(temp.toArray(new String[0]));
                    }
                    break;
                }
            }
        }
        return list;
    }
    protected String getOssTempUri(String resource){
        String host = SystemConfig.getProperty("global.oss.aliyun.endpoint.internet","http://oss-cn-beijing.aliyuncs.com");
        String accessId = SystemConfig.getProperty("global.aliyun.key","nfgEUCKyOdVMVbqQ");
        String accessKey = SystemConfig.getProperty("global.aliyun.secret","HhmxAMZ2jCrE0fTa2kh9CLXF9JPcOW");
        String resource1 = SystemConfig.getProperty("global.oss.aliyun.bucket");
        try {
            URL url = new URL(host);
            host = url.getHost();
        }catch (Exception e){}
        resource = "/"+resource1+"/"+resource;
        String result = OssTempUriUtils.getOssTempUri( accessId, accessKey, host, "GET",resource ,60);
        return result;
    }


}
