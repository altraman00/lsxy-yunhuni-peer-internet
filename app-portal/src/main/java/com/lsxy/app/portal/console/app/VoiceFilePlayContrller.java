package com.lsxy.app.portal.console.app;

import com.alibaba.fastjson.JSON;
import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.oss.OSSService;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.framework.api.billing.model.Billing;
import com.lsxy.yunhuni.api.app.model.App;
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
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
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
    private static String repository = SystemConfig.getProperty("global.oss.aliyun.bucket");
    private static String filePlayPath = SystemConfig.getProperty("portal.file.play");
    @Autowired
    private OSSService ossService;

    /**
     * 查询文件容量
     * @param request
     * @return
     */
    @RequestMapping("/total")
    @ResponseBody
    public RestResponse fileTotalSize(HttpServletRequest request){
        Map map = new HashMap();
        RestResponse response = getBilling(request);
        if(response.isSuccess()){
            Billing billing = (Billing)getBilling(request).getData();
            Long fileTotalSize = billing.getFileTotalSize();
            //取出剩余空间
            map.put("fileRemainSize",fileTotalSize-billing.getFileRemainSize());
            map.put("fileTotalSize",fileTotalSize);
            return RestResponse.success(map);
        }else{
            return response;
        }
    }

    /**
     *
     * @param request
     * @param appId
     * @param name
     * @return
     */
    @RequestMapping("/verify/name")
    @ResponseBody
    public RestResponse verify(HttpServletRequest request,String appId,String name){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_play/count/name?appId={1}&name={2}";
        RestResponse countResponse =  RestRequest.buildSecurityRequest(token).get(uri,Long.class,appId,name);
        return countResponse;
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
    public RestResponse VoiceFilePlay(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20")Integer pageSize, String appId,String name,String subId){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_play/plist?pageNo={1}&pageSize={2}&name={3}&appId={4}&subId={5}";
        return RestRequest.buildSecurityRequest(token).getPage(uri,VoiceFilePlay.class,pageNo,pageSize,name,appId,subId);
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
    public RestResponse modify(HttpServletRequest request, String id,String remark){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_play/modify?id={1}&remark={2}";
        RestResponse<VoiceFilePlay> response = RestRequest.buildSecurityRequest(token).get(uri, VoiceFilePlay.class, id, remark);
        if(response.isSuccess()){
            return RestResponse.success();
        }else{
            return response;
        }
    }

    /**
     * 根据放音文件的ｉｄ删除放音文件
     * @param request
     * @param id　放音文件ｉｄ
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public RestResponse delete(HttpServletRequest request, String id){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_play/delete?id={1}";
        RestResponse<VoiceFilePlay> response = RestRequest.buildSecurityRequest(token).get(uri, VoiceFilePlay.class, id);
        if(response.isSuccess()){
            return RestResponse.success();
        }else{
            return response;
        }
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
    public void uploadMore(HttpServletRequest request, HttpServletResponse response, @RequestParam("file") MultipartFile[] multipartfiles, String appId , String key,String subId){
        response.setHeader("content-type", "application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            RestResponse<App> appResp = this.getAppById(request, appId);
            if(appResp.isSuccess() && appResp.getData() != null){
//                App app = appResp.getData();
//                if(app.getStatus() == App.STATUS_OFFLINE){
//                    throw new RuntimeException("请先上线应用");
//                }
            }else{
                throw new RuntimeException("获取app失败");
            }
            RestResponse restResponse = getRestResponse( request,  response, multipartfiles,  appId ,  key,subId);
            if(!restResponse.isSuccess()) {
                response.setStatus(489,"错误啦");
            }
            Writer writer = response.getWriter();
            writer.write(JSON.toJSONString(restResponse));
            writer.close();
        } catch (IOException e1) {
            logger.error("上传文件IO异常",e1);
        }
    }
    private RestResponse getRestResponse(HttpServletRequest request, HttpServletResponse response, @RequestParam("file") MultipartFile[] multipartfiles, String appId , String key,String subId){
        String tenantId = this.getCurrentUser(request).getTenantId();
        String ymd = DateUtils.formatDate(new Date(),"yyyyMMdd");
        RestResponse restResponse = null;
        try {
            if (null != multipartfiles && multipartfiles.length > 0) {
                //遍历并保存文件
                for (int i=0;i< multipartfiles.length;i++) {
                    MultipartFile file  = multipartfiles[i];
                    String name = file.getOriginalFilename();//文件名
                    boolean flagName = name.matches("^.+(.wav)$");
                    if(!flagName){
                        return RestResponse.failed("0000", "上传失败,上传格式不正确");
                    }
                    long size = file.getSize();//文件大小
                    if(size > 5*1024*1024) {
                        return RestResponse.failed("0000", "上传失败,文件超过5M");
                    }
//                    RestResponse response1 = getBilling(request);
//                    if(response1.isSuccess()) {
//                        Billing billing = (Billing) response1.getData();
//                        if(billing.getFileRemainSize()<size){
//                            return RestResponse.failed("0000", "存储空间不足，无法上传");
//                        }
//                    }else{
//                        return response1;
//                    }
                    String type = name.substring(name.lastIndexOf("."), name.length());
                    //如果文件夹不存在，则创建文件夹
                    String folder = getFolder(tenantId, appId, ymd);
                    new File(filePlayPath + "/" + folder).mkdirs();
                    String fileKey = getFileKey(tenantId, appId, ymd, type);
                    File newFile = new File(filePlayPath + "/" + fileKey);
                    //保存在临时文件夹
                    boolean flag = false;
                    try {
                        file.transferTo(newFile);
                        flag = true;
                    } catch (Exception e) {
                        logger.error("上传放音文件-保存文件过程出错,{}", e);
                    }
//                    int re = downFile(file,filePlayPath +"/"+fileKey);
                    if (flag) {
                        //文件保存成功，将对象保存数据库
                        restResponse = createVoiceFilePlay(request, name, size, fileKey, appId,subId);
                        if (!restResponse.isSuccess()) {
                            logger.info("上传成功，保存失败：{}", name);
                            //将本地文件删除
                            new File(filePlayPath + "/" + fileKey).delete();
                            return RestResponse.failed("0000", "上传失败");
                        }else{
                            return RestResponse.success("上传成功");
                        }
                    } else {
                        return RestResponse.failed("0000", "上传失败");
                    }
                }
            }
        }catch (Exception e){
            logger.info("文件上传异常：{}",e);
            return RestResponse.failed("0000","上传失败");
        }
        return null;
    }
    /**
     * 上传文件（支持多文件）--直接上传OSS版本
     * @param multipartfiles
     * @param request
     * @return
     */
//    @RequestMapping("/upload")
//    @ResponseBody
//    public RestResponse uploadMore(HttpServletRequest request,@RequestParam("file") MultipartFile[] multipartfiles,String appId ,String key){
//        String tenantId = this.getCurrentUser(request).getTenantId();
//        String ymd = DateUtils.formatDate(new Date(),"yyyyMMdd");
//        String msg = "";
//        try {
//            if (null != multipartfiles && multipartfiles.length > 0) {
//                //遍历并保存文件
//                for (int i=0;i< multipartfiles.length;i++) {
//                    MultipartFile file  = multipartfiles[i];
//                    String name = file.getOriginalFilename();//文件名
//                    String type = name.substring(name.lastIndexOf("."),name.length());
//                    long size = file.getSize();
//                    String fileKey = getFileKey(tenantId,appId,ymd,type);
//                    boolean flag = ossService.uploadFileStream(file.getInputStream(),size,name,repository,fileKey);
//                    if(flag){//文件保存成功，将对象保存数据库
//                        RestResponse restResponse = createVoiceFilePlay(request,name,size,fileKey,appId);
//                        if(restResponse.isSuccess()){
//                            logger.info("文件上传成功：{}",name);
//                            // oss.setStatus(oss.getStatus()+name+"文件上传成功");
//                        }else{
//                            logger.info("上传成功，保存失败：{}",name);
//                            ossService.deleteObject(repository, fileKey);
//                            msg+=name+"文件上传失败；";
//                        }
//                    }else{
//                        logger.info("上创失败",name);
//                        msg+=name+"文件上传失败；";
//                    }
//                }
//            }
//        }catch (Exception e){
//            logger.info("文件上传异常：{}",e);
//        }
//        return RestResponse.success();
//    }
    /**
     * 创建上传文件记录
     * @param request
     * @param name 文件名字
     * @param size 文件大小
     * @param fileKey 文件ＯＳＳ地址
     * @param appId　应用ｉｄ
     * @return
     */
    private RestResponse createVoiceFilePlay(HttpServletRequest request,String name,long size,String fileKey,String appId,String subId){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_play/create?name={1}&size={2}&fileKey={3}&appId={4}&subId={5}";
        return RestRequest.buildSecurityRequest(token).get(uri, VoiceFilePlay.class,name,size,fileKey,appId,subId);
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
     * 获得本地文件夹
     * @param tenantId
     * @param appId
     * @param ymd
     * @return
     */
    private String getFolder(String tenantId,String appId,String ymd){
        String result = "tenant_res/"+tenantId+"/play_voice/"+appId+"/"+ymd;
        return result;
    }
}
