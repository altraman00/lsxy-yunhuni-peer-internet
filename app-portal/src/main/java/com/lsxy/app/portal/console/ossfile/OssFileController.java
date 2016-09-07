package com.lsxy.app.portal.console.ossfile;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.oss.OSSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by liups on 2016/7/28.
 */
@Controller
@RequestMapping("/console/oss")
public class OssFileController extends AbstractPortalController {
    @Autowired
    private OSSService ossService;

    @RequestMapping("/img")
    public void getImg(HttpServletResponse response, String uri){
        String type = uri.substring(uri.lastIndexOf(".")+1, uri.length()).toLowerCase();
        if("jpg".equals(type)){
            type = "jpeg";
        }
        response.setContentType("image/"+ type); //必须设置ContentType为image/图片类型
        InputStream in = null;
        ServletOutputStream out = null;
        try {
            in = ossService.getFileStream(SystemConfig.getProperty("global.oss.aliyun.bucket"), uri);
            out = response.getOutputStream();
            byte[] buffer = new byte[4 * 1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        } catch (Exception e) {
            System.out.println("文件流输出异常");
            e.printStackTrace();
        }finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                System.out.println("文件流关闭异常");
                e.printStackTrace();
            }
        }
    }
}
