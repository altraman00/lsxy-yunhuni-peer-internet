package com.lsxy.app.portal.console.test.upload;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.app.model.App;
import org.aspectj.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;

/**
 * Created by zhangxb on 2016/9/17.
 */
@Controller
@RequestMapping("/console/test/upload")
public class TestUploadContrller extends AbstractPortalController {
    private static final Logger logger = LoggerFactory.getLogger(TestUploadContrller.class);
    private static String filePlayPath = SystemConfig.getProperty("portal.file.play");
    private static String filePlayTempPath = SystemConfig.getProperty("portal.file.play.temp");
    /**
     * 应用首页
     * @param request
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView list(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/console/test/upload/index");
        return mav;
    }
    @RequestMapping("/copy")
    public ModelAndView copy(HttpServletRequest request,String file1,String file2){
        File newTempFile = new File(filePlayTempPath +"/"+file1);
        File newFile = new File(filePlayPath +"/"+file2);
        //保存在临时文件夹
        boolean flag = false;
        try {
            FileUtil.copyFile(newTempFile, newFile);
            flag = true;
        }catch (Exception e){
            logger.error("上传放音文件-拷贝文件测试,{}",e);
        }
        ModelAndView mav = new ModelAndView();
        mav.addObject("msg",flag);
        mav.setViewName("/console/test/upload/index");
        return mav;
    }
    /**
     * 上传文件（支持多文件）
     * @param multipartfiles
     * @param request
     * @return
     */
    @RequestMapping("/upload")
    @ResponseBody
    public RestResponse uploadMore(HttpServletRequest request, @RequestParam("file") MultipartFile[] multipartfiles, String appId , String key){
        String tenantId = this.getCurrentUser(request).getTenantId();
        String ymd = DateUtils.formatDate(new Date(),"yyyyMMdd");
        RestResponse restResponse = null;
        try {
            if (null != multipartfiles && multipartfiles.length > 0) {
                //遍历并保存文件
                for (int i=0;i< multipartfiles.length;i++) {
                    MultipartFile file  = multipartfiles[i];
                    String name = file.getOriginalFilename();//文件名
                    long size = file.getSize();//文件大小
                    String type = name.substring(name.lastIndexOf("."),name.length());
                    //如果文件夹不存在，则创建文件夹
                    String folder = getFolder(tenantId,appId,ymd);
                    new File(filePlayPath+"/"+folder).mkdirs();
                    new File(filePlayTempPath+"/"+folder).mkdirs();
                    String fileKey = getFileKey(tenantId,appId,ymd,type);
                    File newTempFile = new File(filePlayTempPath +"/"+fileKey);
                    File newFile = new File(filePlayPath +"/"+fileKey);
                    //保存在临时文件夹
                    boolean flag = false;
                    try {
                        file.transferTo(newTempFile);
                        FileUtil.copyFile(newTempFile, newFile);
                        flag = true;
                    }catch (Exception e){
                        logger.error("上传放音文件-保存文件过程出错,{}",e);
                    }
//                    int re = downFile(file,filePlayPath +"/"+fileKey);
                    if(flag) {
                        //文件保存成功，将对象保存数据库
                        //restResponse = createVoiceFilePlay(request, name, size, fileKey, appId);
                        if (!restResponse.isSuccess()) {
                            logger.info("上传成功，保存失败：{}", name);
                            //将本地文件删除
                            new File(filePlayPath + "/" + fileKey).delete();
                            restResponse = RestResponse.failed("0000", "上传失败");
                        }
                    }else{
                        restResponse = RestResponse.failed("0000", "上传失败");
                    }
                }
            }
        }catch (Exception e){
            logger.info("文件上传异常：{}",e);
            restResponse = RestResponse.failed("0000","上传失败");
        }
        return restResponse;
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
