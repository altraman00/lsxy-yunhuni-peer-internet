package com.lsxy.app.oc.rest.ossfile;

import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.oss.OSSService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * oss文件操作
 * Created by zhangxb on 2016/8/11.
 */
@Api(value = "OSS文件操作", description = "租户中心相关的接口" )
@RequestMapping("/ossfile")
@RestController
public class OssFileController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(OssFileController.class);
    @Autowired
    private OSSService ossService;
    @ApiOperation(value = "文件下载")
    @RequestMapping(value = "/download" ,method = RequestMethod.GET)
    public void download(HttpServletResponse response, @RequestParam String uri, @RequestParam String addr) throws IOException {
        String type = uri.substring(uri.lastIndexOf(".")+1, uri.length());
        response.setContentType("image/"+ type); //必须设置ContentType为image/图片类型
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename="+addr);
        response.setContentType("application/octet-stream; charset=utf-8");
        handle(response,SystemConfig.getProperty("global.oss.aliyun.bucket"), uri);
    }
    @ApiOperation(value = "获取资源流")
    @RequestMapping(value = "/img" ,method = RequestMethod.GET)
    public void getImg(HttpServletResponse response, @RequestParam String uri){
//        String type = uri.substring(uri.lastIndexOf(".")+1, uri.length());
//        response.setContentType("image/"+ type); //必须设置ContentType为image/图片类型
        handle(response,SystemConfig.getProperty("global.oss.aliyun.bucket"), uri);
    }

    /**
     *
     * @param response
     * @param uri
     */
    private void handle(HttpServletResponse response,String type, String uri) {
        InputStream in = null;
        ServletOutputStream out = null;
        try {
            in = ossService.getFileStream(type, uri);
            out = response.getOutputStream();
            byte[] buffer = new byte[4 * 1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        } catch (Exception e) {
            logger.error("文件流输出异常",e);
        }finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                logger.error("文件流关闭异常",e);
            }
        }
    }
    @ApiOperation(value = "获取上传号码的excel模板")
    @RequestMapping(value = "/telnum" ,method = RequestMethod.GET)
    public void getNumber(HttpServletResponse response) {
        handle(response,SystemConfig.getProperty("global.oss.aliyun.bucket")+"-open", "oc/files/telnum.xlsx");
    }
}
