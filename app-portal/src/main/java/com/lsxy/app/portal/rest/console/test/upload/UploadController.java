package com.lsxy.app.portal.rest.console.test.upload;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * file upload demo
 * Created by zhangxb on 2016/6/18.
 */
@Controller
@RequestMapping("/test/upload")
public class UploadController {
    static final long serialVersionUID = 1L;

    /**
     * 上传首页
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/index"  )
    public ModelAndView  index(HttpServletRequest request, HttpServletResponse response){
        ModelAndView mav = new ModelAndView();
        mav.addObject("a","b");
        mav.setViewName("/console/test/upload/index");
        return mav;
    }

    /**
     * 取消文件上传
     * @param request
     * @param response
     */
    @RequestMapping("/cancel"  )
    public void  cancel(HttpServletRequest request, HttpServletResponse response){
        UploadEntity status = (UploadEntity) request.getSession().getAttribute("upload_ps");
        status.setCancel(true);
        request.getSession().setAttribute("upload_ps",status);
        status(request,response);
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

    /**
     * 上传文件（支持多文件）
     * @param multipartfiles
     * @param request
     * @return
     */
    @RequestMapping("/upload"  )
    public ModelAndView uploadMore(@RequestParam("file") MultipartFile[] multipartfiles,HttpServletRequest request){
        //保存文件的目录
        try {
            //实际上传文件地址，可根据需要从配置获取
            //String savePath = request.getCallSession().getServletContext().getRealPath("/") + "/uploadTempDirectory/";
            if (null != multipartfiles && multipartfiles.length > 0) {
                //遍历并保存文件
                for (MultipartFile file : multipartfiles) {
                    file.transferTo(new File("F:/" + file.getOriginalFilename()));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        ModelAndView mav = new ModelAndView();
        mav.addObject("a","b");
        mav.setViewName("/console/test/upload/index");
        return mav;
    }

}
