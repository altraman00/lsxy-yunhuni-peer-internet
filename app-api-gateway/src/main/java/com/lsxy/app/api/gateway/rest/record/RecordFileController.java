package com.lsxy.app.api.gateway.rest.record;

import com.lsxy.app.api.gateway.response.ApiGatewayResponse;
import com.lsxy.app.api.gateway.rest.AbstractAPIController;
import com.lsxy.app.api.gateway.rest.record.vo.DownloadUrlVo;
import com.lsxy.app.api.gateway.rest.record.vo.RecordFileVo;
import com.lsxy.framework.core.exceptions.api.RecordFileNotExistException;
import com.lsxy.framework.core.exceptions.api.RequestIllegalArgumentException;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.OssTempUriUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.apigw.RecordFileDownloadNotificationEvent;
import com.lsxy.framework.mq.events.portal.VoiceFileRecordSyncEvent;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.file.model.VoiceFileRecord;
import com.lsxy.yunhuni.api.file.service.VoiceFileRecordService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liups on 2017/3/17.
 */
@RestController
public class RecordFileController extends AbstractAPIController {
    private static final Logger logger = LoggerFactory.getLogger(RecordFileController.class);

    @Autowired
    VoiceFileRecordService voiceFileRecordService;
    @Autowired
    MQService mqService;
    @Autowired
    AppService appService;

    @RequestMapping(value = "/{accountId}/recording/plist",method = RequestMethod.GET)
    public ApiGatewayResponse getRecordFilePage(HttpServletRequest request,
                                                @PathVariable String accountId,
                                                @RequestHeader(value = "AppID") String appId,
                                                @RequestParam(defaultValue = "1") Integer pageNo,
                                                @RequestParam(defaultValue = "10") Integer pageSize,
                                                @RequestParam String start,
                                                @RequestParam String end
                                                )throws YunhuniApiException {
        Page<VoiceFileRecord> page = voiceFileRecordService.getPageListForGW(appId,getSubaccountId(request),getDate(start),getDate(end),pageNo,pageSize);
        List<VoiceFileRecord> result = page.getResult();
        List<RecordFileVo> vos = new ArrayList<>();
        result.stream().forEach(record -> vos.add(new RecordFileVo(record)));
        page.setResult(vos);
        return ApiGatewayResponse.success(page);
    }

    public Date getDate(String dateStr) throws YunhuniApiException{
        Date date;
        if(StringUtils.isNotBlank(dateStr)){
            try{
                date = DateUtils.parseDate(dateStr, "yyyy-MM-dd HH:mm:ss");
            }catch (Exception e){
                throw new RequestIllegalArgumentException();
            }
        }else{
            throw new RequestIllegalArgumentException();
        }
        return date;
    }

    @RequestMapping(value = "/{accountId}/recording/url",method = RequestMethod.GET)
    public ApiGatewayResponse getRecordFileDownloadUrl(HttpServletRequest request,
                                                @PathVariable String accountId,
                                                @RequestHeader(value = "AppID") String appId,
                                                @RequestParam String id
                                                )throws YunhuniApiException {
        App app = appService.findById(appId);
        String subaccountId = getSubaccountId(request);
        VoiceFileRecord voiceFileRecord = voiceFileRecordService.findById(id);
        if(voiceFileRecord==null||!appId.equals(voiceFileRecord.getAppId())){
            throw new RequestIllegalArgumentException();
        }
        if(StringUtils.isNotBlank(subaccountId) && !subaccountId.equals(voiceFileRecord.getSubaccountId())){
            throw new RequestIllegalArgumentException();
        }
        if(StringUtils.isBlank(subaccountId) && StringUtils.isNotBlank(voiceFileRecord.getSubaccountId())){
            throw new RequestIllegalArgumentException();
        }

        if(voiceFileRecord.getStatus()!=null&&voiceFileRecord.getStatus()==1){
            String ossUri = OssTempUriUtils.getOssTempUri(voiceFileRecord.getOssUrl());
            if(logger.isDebugEnabled()) {
                logger.debug("生成ossUri地址：[{}]", ossUri);
            }
            return ApiGatewayResponse.success(new DownloadUrlVo(DownloadUrlVo.STATE_DONE,ossUri));
        }

        List<VoiceFileRecord> list = voiceFileRecordService.getListBySessionId(voiceFileRecord.getSessionId());
        if(list==null||list.size()==0){
            throw new RecordFileNotExistException();
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
            mqService.publish(new VoiceFileRecordSyncEvent(app.getTenant().getId(), voiceFileRecord.getAppId(), voiceFileRecord.getId(), VoiceFileRecordSyncEvent.TYPE_FILE));
            //TODO 延时处理录音同步查询时间
            mqService.publish(new RecordFileDownloadNotificationEvent(appId,subaccountId,voiceFileRecord.getId(),1));
            return ApiGatewayResponse.success(new DownloadUrlVo(DownloadUrlVo.STATE_WAIT,null));
        }else {
            String ossUri = OssTempUriUtils.getOssTempUri(list.get(0).getOssUrl());
            return ApiGatewayResponse.success(new DownloadUrlVo(DownloadUrlVo.STATE_DONE,ossUri));
        }
    }

}
