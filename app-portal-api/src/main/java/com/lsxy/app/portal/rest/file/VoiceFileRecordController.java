package com.lsxy.app.portal.rest.file;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.call.center.api.service.CallCenterConversationMemberService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.*;
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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    /**
     * 根据放音文件id删除放音文件
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public RestResponse delete(String id) throws InvocationTargetException, IllegalAccessException {
        VoiceFileRecord voiceFileRecord = voiceFileRecordService.findById(id);
        voiceFileRecordService.delete(voiceFileRecord);
        //删除文件
        boolean flag = false;
        try{
            flag = FileUtil.delFile(path+voiceFileRecord.getUrl());
        }catch(Exception e){}
        logger.info(path+voiceFileRecord.getUrl()+"删除结果:"+flag);
        return RestResponse.success(voiceFileRecord);
    }

    /**
     * 根据应用id，删除在开始时间和结束时间区间内的录音文件
     * @param appId 应用id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @RequestMapping("/batch/delete")
    public RestResponse batchDelete(String appId,Date startTime,Date endTime){
        Tenant tenant = getCurrentAccount().getTenant();
        int result = voiceFileRecordService.batchDelete(appId,tenant.getId(),startTime,endTime);
        if(result>0){
            List<VoiceFileRecord> list = voiceFileRecordService.list(appId,tenant.getId(),startTime,endTime);
            //开始删除文件
            for(int i=0;i<list.size();i++){
                boolean flag = false;
                try{
                    flag = FileUtil.delFile(path+list.get(i).getUrl());
                }catch(Exception e){}
                logger.info(path+list.get(0).getUrl()+"删除结果:"+flag);
            }
        }else{
           return RestResponse.failed("0000","删除失败");
        }
        return RestResponse.success(result);
    }
    /**
     * 批量下载
     * @param appId
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping("/batch/download")
    public RestResponse batchDownload(String appId,Date startTime,Date endTime){
        Tenant tenant = getCurrentAccount().getTenant();
        List<VoiceFileRecord> list = voiceFileRecordService.list(appId,tenant.getId(),startTime,endTime);
        //生成文件名 开始时间yyyyMMdd+结束时间yyyyMMdd+随机数(3位)+文件类型后缀
        String fileName = path+DateUtils.formatDate(startTime,"yyyyMMdd")+"-"+DateUtils.formatDate(endTime,"yyyyMMdd")+"-"+RandomNumberUtil.randomLong(3)+".zip";
        List<String> orgins = new ArrayList();
        for(int i=0;i<list.size();i++){
            orgins.add(path+list.get(i).getName());
        }
        //进行压缩
        ZipUtil.zip(orgins,fileName);
        //返回zip文件的完整路径
        return RestResponse.success(fileName);
    }

    /**
     * 根据应用id和开始时间，结束时间统计区间内文件数量total和文件总大小size
     * @param appId 应用id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @RequestMapping("/sum")
    public RestResponse sumAndCount(String appId,Date startTime,Date endTime){
        Tenant tenant = getCurrentAccount().getTenant();
        Map map = voiceFileRecordService.sumAndCount(appId,tenant.getId(),startTime,endTime);
        return RestResponse.success(map);
    }
    /**
     * 根据名字和应用id查询用户名下的录音文件
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @param appId 应用id
     * @return
     */
    @RequestMapping("/plist")
    public RestResponse pageList(Integer pageNo,Integer pageSize ,String appId){
        Tenant tenant = getCurrentAccount().getTenant();
        Page<VoiceFileRecord> page = voiceFileRecordService.pageList(pageNo,pageSize,appId,tenant.getId());
        return RestResponse.success(page);
    }

    @RequestMapping("/cdr/download")
    public RestResponse cdrDownload(String id){
        Tenant tenant = getCurrentAccount().getTenant();
        VoiceCdr voiceCdr = voiceCdrService.findById(id);
        if(tenant==null||voiceCdr==null||!tenant.getId().equals(voiceCdr.getTenantId())){
            return RestResponse.failed("0000","验证失败，无法下载");
        }
        List<VoiceFileRecord> list = getFile(id);
        if(list==null){
            return RestResponse.failed("0000","不存在录音文件");
        }
        return RestResponse.success();
    }


    private List<VoiceFileRecord> getFile(String id){
        //根据cdr获取业务类型，和业务id，根据业务id和业务类型获取录音文件列表，
        VoiceCdr voiceCdr = voiceCdrService.findById(id);
        if(voiceCdr!=null&& StringUtils.isNotEmpty(voiceCdr.getId())) {
            ProductCode p1 = ProductCode.changeApiCmdToProductCode(voiceCdr.getType());
            //语音会议
            if (ProductCode.sys_conf.getRemark().equals(p1.getRemark())) {
                //获取会议操作者
                MeetingMember meetingMember = meetingMemberService.findById(voiceCdr.getSessionId());
                String hql = "  FROM VoiceFileRecord obj WHERE obj.sessionId=?1 ";
                //使用会议id
                List list = (List) voiceFileRecordService.list(hql, meetingMember.getMeeting().getId());
                return list;
            }
            //自定义IVR
            else if (ProductCode.ivr_call.getRemark().equals(p1.getRemark())) {
                String hql = "  FROM VoiceFileRecord obj WHERE obj.sessionId=?1 ";
                //使用ivr的id
                List list = (List) voiceFileRecordService.list(hql, voiceCdr.getSessionId());
                return list;
            }
            //语音回拔
            else if (ProductCode.duo_call.getRemark().equals(p1.getRemark())) {
                String hql = "  FROM VoiceFileRecord obj WHERE obj.sessionId=?1 ";
                //使用双向回拨的id
                List list = (List) voiceFileRecordService.list(hql, voiceCdr.getSessionId());
                return list;
            }
            //呼叫中心
            else if (ProductCode.call_center.getRemark().equals(p1.getRemark())) {
                //根据sessionid获取呼叫中心交互成员，在获取呼叫中心交谈，在获取文件
                List<String> temp = callCenterConversationMemberService.getListBySessionId(voiceCdr.getSessionId());
                if (temp.size() == 0) {
                    return null;
                }
                String te = "";
                for (int i = 0; i < temp.size(); i++) {
                    te += "'" + temp.get(i) + "'";
                    if (i != temp.size() - 1) {
                        te += ",";
                    }
                }
                String hql = "  FROM VoiceFileRecord obj WHERE obj.sessionId in ( ?)";
                //使用ivr的id
                List list = (List) voiceFileRecordService.list(hql, te);
                return list;
            }
        }
        return null;
    }

}
