package com.lsxy.app.portal.rest.console.upload;

import com.lsxy.app.portal.rest.console.upload.UploadEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Iterator;

/**
 * Created by Administrator on 2016/6/18.
 */
@Controller
@RequestMapping("/file")
public class UploadController {
    static final long serialVersionUID = 1L;

    @RequestMapping("/index"  )
    public ModelAndView  index(HttpServletRequest request, HttpServletResponse response){
        ModelAndView mav = new ModelAndView();
        mav.addObject("a","b");
        mav.setViewName("/console/upload/upload");
        return mav;
    }

    @RequestMapping("/cancel"  )
    public void  cancel(HttpServletRequest request, HttpServletResponse response){
        UploadEntity status = (UploadEntity) request.getSession().getAttribute("upload_ps");
        status.setCancel(true);
        request.getSession().setAttribute("upload_ps",status);
        status(request,response);
    }

    @RequestMapping("/status"  )
    public void  status(HttpServletRequest request, HttpServletResponse response){
        UploadEntity status = (UploadEntity) request.getSession().getAttribute("upload_ps");

        try {
            //计算上传完成的百分比
            long percentComplete = (long) Math.floor(((double) status.getReadTotalSize() / (double) status.getUploadTotalSize()) * 100.0);
            System.out.println("com:" + percentComplete);
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Cache-Control", "no-cache");
            if (((long) status.getReadTotalSize() == (long) status.getUploadTotalSize()) || (status.getCancel() == true)) {
                response.getWriter().write(status.getStatus().toString() + "success");
            } else {
                response.getWriter().write(status.getStatus().toString() + "<div class=\"prog-border\"><div class=\"prog-bar\" style=\"width: "
                        + percentComplete + "%;\"></div></div>");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping("/upload"  )
    public ModelAndView uploadMore(@RequestParam("file") MultipartFile[] multipartfiles,HttpServletRequest request){
        //保存文件的目录
        try {
            String savePath = request.getSession().getServletContext().getRealPath("/") + "/uploadTempDirectory/";
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
        mav.setViewName("/console/upload/upload");
        return mav;
    }

}
