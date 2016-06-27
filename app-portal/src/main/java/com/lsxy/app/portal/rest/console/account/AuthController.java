package com.lsxy.app.portal.rest.console.account;

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
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/24.
 */
@Controller
@RequestMapping("/console/account/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @RequestMapping("/index" )
    public ModelAndView index(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        //todo 获取实名认证的状态
        String status = (String)request.getSession().getAttribute("authStatus");
        status="";
        if(status==null||status.length()==0){
            //TOdo 未实名认证
            mav.setViewName("/console/account/auth/index");
        }else {
            int authStatus = Integer.valueOf(status);
            if (authStatus == 0) {
                //todo 审核中
                mav.setViewName("/console/account/auth/wait");
            } else if (authStatus == 1) {
                //todo 个人实名认证
                mav.setViewName("/console/account/auth/sucess");
            } else if (authStatus == 2) {
                //todo 企业实名认证
                mav.setViewName("/console/account/auth/sucess");
            } else if (authStatus == -1) {
                //todo 个人实名认证失败
                mav.addObject("msg","身份证与名称不符合，请重新提交资料认证");
                mav.setViewName("/console/account/auth/fail");
            } else if (authStatus == -2) {
                //todo 企业实名认证失败
                mav.addObject("msg","上传资料不符合要求，请重新提交资料认证");
                mav.setViewName("/console/account/auth/fail");
            }
        }

        return mav;
    }

    @RequestMapping(value="/edit" ,method = RequestMethod.POST)
    public ModelAndView edit(HttpServletRequest request,AuthVo authVo,String type, @RequestParam("file") MultipartFile[] multipartfiles){
            //保存文件的目录
            try {
                //实际上传文件地址，可根据需要从配置获取
                if (null != multipartfiles && multipartfiles.length > 0) {
                    //遍历并保存文件
                    if(Integer.valueOf(type)==0){
                        MultipartFile file = multipartfiles[0];
                        String savePath = "F:/" + file.getOriginalFilename();
                        file.transferTo(new File(savePath));
                        authVo.setId_photo(savePath);
                    }else if(Integer.valueOf(type)==1){
                        MultipartFile file = multipartfiles[1];
                        String savePath = "F:/" + file.getOriginalFilename();
                        file.transferTo(new File(savePath));
                        authVo.setType01_prop01(savePath);
                        file = multipartfiles[2];
                        savePath = "F:/" + file.getOriginalFilename();
                        file.transferTo(new File(savePath));
                        authVo.setType03_prop02(savePath);
                    }


                }
            }catch (Exception e){
                e.printStackTrace();
            }
        ModelAndView mav = new ModelAndView();
        if(Integer.valueOf(type)==0){

            //todo 提交个人实名认证操作
        }else{
            //todo 提交企业实名认证操作
            //todo 企业认证类型
        }
        mav.addObject("type",type);
        mav.addObject("msg",authVo.toString());
        request.getSession().setAttribute("authStatus","0");
        request.getSession().setAttribute("authEditVo",authVo.toString());
        mav.setViewName("/console/account/auth/index");
        return mav;
    }
}
