package com.lsxy.app.portal.console.app;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.app.portal.console.test.upload.UploadEntity;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.oss.OSSService;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.billing.model.Billing;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 放音文件处理
 * Created by zhangxb on 2016/7/21.
 */
@Controller
@RequestMapping("/console/app/file/play")
public class VoiceFilePlayContrller extends AbstractPortalController {
    private static final Logger logger = LoggerFactory.getLogger(VoiceFilePlayContrller.class);
    private  String repository= SystemConfig.getProperty("global.oss.aliyun.bucket");
    private String maxSize = SystemConfig.getProperty("portal.voiceflieplay.maxsize");
    @Autowired
    private OSSService ossService;

    /**
     * 查询文件容量
     * @param request
     * @return
     */
    @RequestMapping("/total")
    @ResponseBody
    public Map fileTotalSize(HttpServletRequest request){
        Map map = new HashMap();
        Long fileTotalSize = Long.parseLong(maxSize)*1024*1024;
        Billing billing = (Billing)getBilling(request).getData();
        map.put("fileRemainSize",fileTotalSize-billing.getFileRemainSize());
        map.put("fileTotalSize",fileTotalSize);
        return map;
    }
    /**
     * 根据当前页，每页记录数，应用ｉｄ，放音文件的名字（模糊查询）进行查询放音文件的分页信息
     * @param request
     * @param pageNo　当前页
     * @param pageSize　每页记录数
     * @param appId　应用的ｉｄ
     * @param name　放音文件的名字
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map VoiceFilePlay(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20")Integer pageSize, String appId,String name){
        Map map = new HashMap();
        map.put("list",pageListVoiceFilePlay(request,pageNo,pageSize,appId,name).getData());
        return map;
    }
    /**
     * 根据放音文件的ｉｄ修改放音文件的备注
     * @param request
     * @param id　放音文件ｉｄ
     * @param remark 备注
     * @return
     */
    @RequestMapping("/modify")
    @ResponseBody
    public Map modify(HttpServletRequest request, String id,String remark){
        RestResponse restResponse = modifyVoiceFilePlay(request,id,remark);
        Map map = new HashMap();
        map.put("flag",restResponse.isSuccess());
        return map;
    }
    /**
     * 根据放音文件的ｉｄ修改放音文件的备注
     * @param request
     * @param id　放音文件ｉｄ
     * @param remark 备注
     * @return
     */
    private RestResponse modifyVoiceFilePlay(HttpServletRequest request,String id,String remark){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_play/modify?id={1}&remark={2}";
        return RestRequest.buildSecurityRequest(token).get(uri, VoiceFilePlay.class,id,remark);
    }
    /**
     * 根据放音文件的ｉｄ删除放音文件
     * @param request
     * @param id　放音文件ｉｄ
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Map delete(HttpServletRequest request, String id){
        RestResponse restResponse = deleteVoiceFilePlay(request,id);
        Map map = new HashMap();
        map.put("flag",restResponse.isSuccess());
        return map;
    }

    /**
     * 根据放音文件的ｉｄ删除放音文件
     * @param request
     * @param id　放音文件ｉｄ
     * @return
     */
    private RestResponse deleteVoiceFilePlay(HttpServletRequest request,String id){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_play/delete?id={1}";
        return RestRequest.buildSecurityRequest(token).get(uri, VoiceFilePlay.class,id);
    }

    /**
     * 根据当前页，每页记录数，应用ｉｄ，放音文件的名字（模糊查询）进行查询放音文件的分页信息
     * @param request
     * @param pageNo　当前页
     * @param pageSize　每页记录数
     * @param appId　应用的ｉｄ
     * @param name　放音文件的名字
     * @return
     */
    private RestResponse pageListVoiceFilePlay(HttpServletRequest request, Integer pageNo, Integer pageSize, String appId,String name){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_play/plist?pageNo={1}&pageSize={2}&name={3}&appId={4}";
        return RestRequest.buildSecurityRequest(token).getPage(uri,VoiceFilePlay.class,pageNo,pageSize,name,appId);
    }
    /**
     * 获取用户的账务表信息
     * @param request
     * @return
     */
    private RestResponse getBilling(HttpServletRequest request){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/billing/get";
        return RestRequest.buildSecurityRequest(token).get(uri, Billing.class);
    }
    /**
     * 上传文件（支持多文件）
     * @param multipartfiles
     * @param request
     * @return
     */
    @RequestMapping("/upload")
    public void uploadMore(HttpServletRequest request,@RequestParam("file") MultipartFile[] multipartfiles,String appId ){
        String tenantId = this.getCurrentUser(request).getTenantId();
        String ymd = DateUtils.formatDate(new Date(),"yyyyMMdd");
        UploadEntity oss = new UploadEntity();
        try {
            if (null != multipartfiles && multipartfiles.length > 0) {
                //遍历并保存文件
                oss.setUploadTotalSize(multipartfiles.length);
                for (int i=0;i< multipartfiles.length;i++) {
                    MultipartFile file  = multipartfiles[i];
                    String name = file.getOriginalFilename();//文件名
                    String type = name.substring(name.lastIndexOf("."),name.length());
                    long size = file.getSize();
                    String fileKey = getFileKey(tenantId,appId,ymd,type);
                    boolean flag = ossService.uploadFileStream(file.getInputStream(),size,name,repository,fileKey);
                    if(flag){//文件保存成功，将对象保存数据库
                        RestResponse restResponse = createVoiceFilePlay(request,name,size,fileKey,appId);
                        if(restResponse.isSuccess()){
                            logger.info("文件上传成功",name);
                            oss.setStatus(oss.getStatus()+name+"文件上传");
                        }else{
                            logger.info("上传成功，保存失败",name);
                            ossService.deleteObject(repository, fileKey);
                            oss.setStatus(oss.getStatus()+name+"文件上传失败");
                        }
                    }else{
                        logger.info("上创失败",name);
                        oss.setStatus(oss.getStatus()+name+"文件上传失败");
                    }
                    oss.setReadTotalSize(i+1);
                    request.getSession().setAttribute("ossUpload",oss);
                }
            }
        }catch (Exception e){
        }
        oss.setFlag(true);
    }

    /**
     * 创建上传文件记录
     * @param request
     * @param name 文件名字
     * @param size 文件大小
     * @param fileKey 文件ＯＳＳ地址
     * @param appId　应用ｉｄ
     * @return
     */
    private RestResponse createVoiceFilePlay(HttpServletRequest request,String name,long size,String fileKey,String appId){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_play/create?name={1}&size={2}&fileKey={3}&appId={4}";
        return RestRequest.buildSecurityRequest(token).get(uri, VoiceFilePlay.class,name,size,fileKey,appId);
    }

    /**
     * 获取在Oss中的标识 生成规则play_voice/tenant_id/app_id/yyyyMMdd/uuid
     * @param tenantId 租户id
     * @param appId 应用id
     * @param ymd 时间yyyyMMdd类型
     * @param type 文件类型
     * @return
     */
    private String getFileKey(String tenantId,String appId,String ymd,String type){
        String result = "tenant_res/"+tenantId+"/play_voice/"+appId+"/"+ymd+"/"+ UUIDGenerator.uuid()+type;
        return result;
    }
    /**
     * 查看文件上传进度
     * @param request
     */
    @RequestMapping("/status"  )
    @ResponseBody
    public Map  status(HttpServletRequest request ){
        UploadEntity fuploadStatus = (UploadEntity) request.getSession().getAttribute("ossUpload");
        Map map = new HashMap();
        if(fuploadStatus==null){
            map.put("flag",false);
            map.put("percentComplete",0);
        }else{
            //计算上传完成的百分比
            long percentComplete = (long) Math.floor(((double) fuploadStatus.getReadTotalSize() / (double) fuploadStatus.getUploadTotalSize()) * 100.0);
             if (((long) fuploadStatus.getReadTotalSize() == (long) fuploadStatus.getUploadTotalSize())) {
                 map.put("flag",true);
                 map.put("percentComplete",percentComplete);
             }else{
                 map.put("flag",false);
                 map.put("percentComplete",percentComplete);
             }
        }
        return map;
    }
}
