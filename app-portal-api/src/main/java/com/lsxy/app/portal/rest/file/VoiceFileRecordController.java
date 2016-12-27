package com.lsxy.app.portal.rest.file;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.call.center.api.service.CallCenterConversationMemberService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.portal.VoiceFileRecordSyncEvent;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.file.model.VoiceFileRecord;
import com.lsxy.yunhuni.api.file.service.VoiceFileRecordService;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import com.lsxy.yunhuni.api.session.model.MeetingMember;
import com.lsxy.yunhuni.api.session.model.VoiceCdr;
import com.lsxy.yunhuni.api.session.service.MeetingMemberService;
import com.lsxy.yunhuni.api.session.service.VoiceCdrService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 录音文件
 * Created by zhangxb on 2016/7/21.
 */
@RequestMapping("/rest/voice_file_record")
@RestController
public class VoiceFileRecordController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(VoiceFileRecordController.class);
    private  String path= SystemConfig.getProperty("portal.realauth.resource.upload.file.path");
    @Autowired
    VoiceFileRecordService voiceFileRecordService;
    @Autowired
    VoiceCdrService voiceCdrService;
    @Autowired
    MeetingMemberService meetingMemberService;
    @Reference(timeout=3000,check = false,lazy = true)
    private CallCenterConversationMemberService callCenterConversationMemberService;
    @Autowired
    MQService mqService;

    /**
     * 根据应用id和开始时间，结束时间统计区间内文件数量total和文件总大小size
     * @param appId 应用id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @RequestMapping("/sum")
    public RestResponse sumAndCount(String appId,String type,String startTime,String endTime){
        Tenant tenant = getCurrentAccount().getTenant();
        Date start = null;
        Date end = null;
        try{
            start = DateUtils.parseDate(startTime+" 00:00:00","yyyy-MM-dd HH:mm:ss");
            end = DateUtils.parseDate(endTime+" 23:59:59","yyyy-MM-dd HH:mm:ss");
        }catch (Exception e){
            return RestResponse.failed("0000","日期格式错误");
        }
        Map map = voiceFileRecordService.sumAndCount(appId,tenant.getId(),type,start,end);
        return RestResponse.success(map);
    }
    @RequestMapping("/plist/time")
    public RestResponse pageListByTime(Integer pageNo,Integer pageSize ,String appId,String type,String startTime,String endTime){
        Tenant tenant = getCurrentAccount().getTenant();
        Date start = null;
        Date end = null;
        try{
            start = DateUtils.parseDate(startTime+" 00:00:00","yyyy-MM-dd HH:mm:ss");
            end = DateUtils.parseDate(endTime+" 23:59:59","yyyy-MM-dd HH:mm:ss");
        }catch (Exception e){
            return RestResponse.failed("0000","日期格式错误");
        }
        Page<Map> page = voiceFileRecordService.getPageList(pageNo,pageSize,appId,tenant.getId(),type,start,end);
        return RestResponse.success(page);
    }
    @RequestMapping("/file/download")
    public WebAsyncTask fileDownload(String id){
        Callable<RestResponse> callable = new Callable<RestResponse>() {
            public RestResponse call()  throws Exception{
                Tenant tenant = getCurrentAccount().getTenant();
                VoiceFileRecord voiceFileRecord = voiceFileRecordService.findById(id);
                if(tenant==null||voiceFileRecord==null||!tenant.getId().equals(voiceFileRecord.getTenantId())){
                    return RestResponse.failed("0000","验证失败，无法下载");
                }
                if(voiceFileRecord.getStatus()!=null&&voiceFileRecord.getStatus()==1){
                    String ossUri = getOssTempUri(voiceFileRecord.getOssUrl());
                    if(logger.isDebugEnabled()) {
                        logger.debug("生成ossUri地址：[{}]", ossUri);
                    }
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
                    if(temp.getStatus()==null||1!=temp.getStatus()){
                        temp.setStatus(0);
                        voiceFileRecordService.save(temp);
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
        return new WebAsyncTask(500000,callable);
    }
    @RequestMapping("/cdr/download")
    public WebAsyncTask cdrDownload(String id){
        Callable<RestResponse> callable = new Callable<RestResponse>() {
            public RestResponse call() throws Exception {
                Tenant tenant = getCurrentAccount().getTenant();
                VoiceCdr voiceCdr = voiceCdrService.findById(id);
                if(tenant==null||voiceCdr==null||!tenant.getId().equals(voiceCdr.getTenantId())){
                    return RestResponse.failed("0000","验证失败，无法下载");
                }
                List<VoiceFileRecord> list = getFile(voiceCdr);
                if(list==null||list.size()==0){
                    //TODO 更新CDR
                    voiceCdr.setRecording(0);
                    voiceCdrService.save(voiceCdr);
                    return RestResponse.failed("0401","无对应的录音文件");
                }
                //先判断是否文件已上传，如果是的话，直接生成临时下载链接，否则
                boolean flag = false;
                for(int i=0;i<list.size();i++){
                    VoiceFileRecord voiceFileRecord = list.get(i);
                    if(1!=voiceFileRecord.getStatus()){
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
        return new WebAsyncTask(500000,callable);
    }
    private List<VoiceFileRecord> getFile(VoiceCdr voiceCdr){
        //根据cdr获取业务类型，和业务id，根据业务id和业务类型获取录音文件列表，
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
//                        String te = "";
//                        for (int i = 0; i < temp.size(); i++) {
//                            te += "'" + temp.get(i) + "'";
//                            if (i != temp.size() - 1) {
//                                te += ",";
//                            }
//                        }
                        //使用ivr的id
                        list = voiceFileRecordService.getListBySessionId( temp.toArray(new String[0]));
                    }
                    break;
                }
            }
        }
        return list;
    }

}
