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
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 放音文件处理
 * Created by zhangxb on 2016/7/21.
 */
@Controller
@RequestMapping("/console/app/file")
public class VoiceFilePlayContrller extends AbstractPortalController {
    private static final Logger logger = LoggerFactory.getLogger(VoiceFilePlayContrller.class);
    private  String repository= SystemConfig.getProperty("global.oss.aliyun.bucket");
    @Autowired
    private OSSService ossService;
    /**
     * 获取分页数据
     * @param request
     * @param pageNo
     * @param pageSize
     * @param appId
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public Map VoiceFilePlay(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20")Integer pageSize, String appId,String name){
        Map map = new HashMap();
        Billing billing = (Billing)getBilling(request).getData();
        map.put("fileRemainSize",new DecimalFormat("#.00").format(billing.getFileRemainSize()/1000/1000));
        map.put("fileTotalSize",new DecimalFormat("#.00").format(billing.getFileTotalSize()/1000/1000));
        map.put("list",pageListVoiceFilePlay(request,pageNo,pageSize,appId,name).getData());
        return map;
    }
    @RequestMapping("/modify")
    @ResponseBody
    public Map modify(HttpServletRequest request, String id,String remark){
        RestResponse restResponse = modifyVoiceFilePlay(request,id,remark);
        Map map = new HashMap();
        map.put("flag",restResponse.isSuccess());
        return map;
    }
    /**
     * 修改放音文件rest
     * @param request
     * @return
     */
    private RestResponse modifyVoiceFilePlay(HttpServletRequest request,String id,String remark){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_play/modify?id={1}&remard={2}";
        return RestRequest.buildSecurityRequest(token).get(uri, Billing.class,id,remark);
    }
    /**
     * 删除放音文件
     * @param request
     * @param id
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
     * 删除放音文件rest
     * @param request
     * @return
     */
    private RestResponse deleteVoiceFilePlay(HttpServletRequest request,String id){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_play/delete?id={1}";
        return RestRequest.buildSecurityRequest(token).get(uri, Billing.class,id);
    }
    /**
     * 查询放音文件
     * @param request
     * @param pageNo
     * @param pageSize
     * @param appId
     * @return
     */
    private RestResponse pageListVoiceFilePlay(HttpServletRequest request, Integer pageNo, Integer pageSize, String appId,String name){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_play/plist?pageNo={1}&pageSize={2}&name={3}&appId={4}";
        return RestRequest.buildSecurityRequest(token).getPage(uri,VoiceFilePlay.class,pageNo,pageSize,name,appId);
    }
    /**
     * 获取账务表
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
        String tenantId = getCurrentAccount(request).getTenant().getId();
        String ymd = DateUtils.formatDate(new Date(),"yyyyMMdd");
        List list = new ArrayList();
        try {
            if (null != multipartfiles && multipartfiles.length > 0) {
                //遍历并保存文件
                for (MultipartFile file : multipartfiles) {
                    String name = file.getOriginalFilename();//文件名
                    long size = file.getSize();
                    String fileKey = getFileKey(tenantId,appId,ymd);
                    boolean flag = ossService.uploadFileStream(file.getInputStream(),size,name,repository,fileKey);
                    Map temp = new HashMap();
                    temp.put("name",name);
                    if(flag){//文件保存成功，将对象保存数据库
                        RestResponse restResponse = createVoiceFilePlay(request,name,size,fileKey,appId);
                        if(restResponse.isSuccess()){
                            logger.info("文件上传成功",name);
                            temp.put("status","上传成功");
                        }else{
                            temp.put("status","上传成功，保存失败");
                            //TODO 删除文件
                        }
                    }else{
                        temp.put("status","上创失败");
                    }
                    list.add(temp);
                }
            }
        }catch (Exception e){
        }
    }
    /**
     * 删除放音文件rest
     * @param request
     * @return
     */
    private RestResponse createVoiceFilePlay(HttpServletRequest request,String name,long size,String fileKey,String appId){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_play/create?name={1}&size={2}&fileKey={3}&appId={4}";
        return RestRequest.buildSecurityRequest(token).get(uri, VoiceFilePlay.class,name,size,fileKey,appId);
    }
    /**
     * 获取在Oss中的标识
     * @param appId  应用id
     * @return
     */
    private String getFileKey(String tenantId,String appId,String ymd){
        //生成规则play_voice/tenant_id/app_id/yyyyMMdd/uuid
        String result = "play_voice/"+tenantId+"/"+appId+"/"+ymd+"/"+ UUIDGenerator.uuid();
        return result;
    }
    /**
     * 查看文件上传进度
     * @param request
     * @param response
     */
    @RequestMapping("/status"  )
    public void  status(HttpServletRequest request, HttpServletResponse response){
        UploadEntity fuploadStatus = (UploadEntity) request.getSession().getAttribute("upload_ps");

        try {
            //计算上传完成的百分比
            long percentComplete = (long) Math.floor(((double) fuploadStatus.getReadTotalSize() / (double) fuploadStatus.getUploadTotalSize()) * 100.0);
            System.out.println("com:" + percentComplete);
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Cache-Control", "no-cache");
            if (((long) fuploadStatus.getReadTotalSize() == (long) fuploadStatus.getUploadTotalSize()) || (fuploadStatus.getCancel() == true)) {
                response.getWriter().write(fuploadStatus.getStatus().toString() + "success");
            } else {
                response.getWriter().write(fuploadStatus.getStatus().toString() + "<div class=\"prog-border\"><div class=\"prog-bar\" style=\"width: "
                        + percentComplete + "%;\"></div></div>");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
