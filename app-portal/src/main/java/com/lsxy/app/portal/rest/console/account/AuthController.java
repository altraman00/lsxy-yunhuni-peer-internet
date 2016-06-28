package com.lsxy.app.portal.rest.console.account;

import com.lsxy.framework.config.SystemConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * Created by zhangxb on 2016/6/24.
 * 实名认证
 */
@Controller
@RequestMapping("/console/account/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private  static final Integer AUTH_WAIT = 0;
    private  static final Integer AUTH_COMPANY_FAIL = -2;
    private  static final Integer AUTH_COMPANY_SUCESS = 2;
    private  static final Integer AUTH_ONESELF_FAIL = -1;
    private  static final Integer AUTH_ONESELF_SUCESS = 1;
    private static final String UPLOAD_TYPE_FILE = "file";
    private static final String UPLOAD_TYPE_OSS = "oss";
    private static final Integer AUTH_COMPANY=1;
    private static final Integer AUTH_ONESELF=0;


    @RequestMapping("/index" )
    public ModelAndView index(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        //TODO 获取实名认证的状态
        String status = (String)request.getSession().getAttribute("authStatus");
        status="";
        if(status==null||status.length()==0){
            // 未实名认证
            mav.setViewName("/console/account/auth/index");
        }else {
            int authStatus = Integer.valueOf(status);
            if (authStatus == AUTH_WAIT) {
                //TODO 审核中
                mav.setViewName("/console/account/auth/wait");
            } else if (AUTH_ONESELF_SUCESS == 1) {
                //TODO 个人实名认证
                mav.setViewName("/console/account/auth/sucess");
            } else if (AUTH_COMPANY_SUCESS == 2) {
                //TODO 企业实名认证
                mav.setViewName("/console/account/auth/sucess");
            } else if (AUTH_ONESELF_FAIL == -1) {
                //TODO 个人实名认证失败
                mav.addObject("msg","身份证与名称不符合，请重新提交资料认证");
                mav.setViewName("/console/account/auth/fail");
            } else if (AUTH_COMPANY_FAIL == -2) {
                //TODO 企业实名认证失败
                mav.addObject("msg","上传资料不符合要求，请重新提交资料认证");
                mav.setViewName("/console/account/auth/fail");
            }
        }
        return mav;
    }
    private String UploadFile(MultipartFile file){
        String tempPath ="";
        try{
            //实际上传文件地址，可根据需要从配置获取
            String svaeType = SystemConfig.getProperty("portal.realauth.resource.upload.mode");
            String savePath ="";
            if(UPLOAD_TYPE_FILE.equals(svaeType)){
                //上传到文件到指定位置
                savePath = SystemConfig.getProperty("portal.realauth.resource.upload."+UPLOAD_TYPE_FILE+".path");
                tempPath = savePath + file.getOriginalFilename();
                file.transferTo(new File(tempPath));

            }else if(UPLOAD_TYPE_OSS.equals(svaeType)) {
            }
        }catch(Exception e){

        }
        return tempPath;
    }
    @RequestMapping(value="/edit" ,method = RequestMethod.POST)
    public ModelAndView edit(HttpServletRequest request,AuthVo authVo,String type, @RequestParam("file") MultipartFile[] multipartfiles){
            //保存文件的目录
        if (null != multipartfiles && multipartfiles.length > 0) {
            //遍历并保存文件
            if(Integer.valueOf(type)==0){
                String tempPath = UploadFile(multipartfiles[0]);
                authVo.setIdPhoto(tempPath);
            }else if(Integer.valueOf(type)==1){
                MultipartFile file = multipartfiles[1];
                String tempPath = UploadFile(multipartfiles[1]);
                authVo.setType01Prop01(tempPath);
                tempPath = UploadFile(multipartfiles[2]);
                authVo.setType03Prop02(tempPath);
            }
        }
        ModelAndView mav = new ModelAndView();
        if(Integer.valueOf(type)==AUTH_ONESELF){
            //TODO 提交个人实名认证操作
        }else if(Integer.valueOf(type)==AUTH_COMPANY){
            //TODO 提交企业实名认证操作
            //TODO 企业认证类型
        }
        mav.addObject("type",type);
        mav.addObject("msg",authVo.toString());
        request.getSession().setAttribute("authStatus","0");
        request.getSession().setAttribute("authEditVo",authVo.toString());
        mav.setViewName("/console/account/auth/index");
        return mav;
    }
}
