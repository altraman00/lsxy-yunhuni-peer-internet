package com.lsxy.app.portal.console.app;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.framework.web.rest.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Callable;

/**
 * 录音文件
 * Created by zhangxb on 2016/7/23.
 */
@Controller
@RequestMapping("/console/app/file/record")
public class VoiceFileRecordContrller extends AbstractPortalController {
    private static final Logger logger = LoggerFactory.getLogger(VoiceFileRecordContrller.class);
    @RequestMapping("/cdr/download/{id}")
    @ResponseBody
    public WebAsyncTask cdrDownload(HttpServletRequest request, @PathVariable String id){
        Callable<RestResponse> callable = new Callable<RestResponse>() {
            public RestResponse call() throws Exception {
//                String token = getSecurityToken(request);
//                String uri = PortalConstants.REST_PREFIX_URL + "/rest/voice_file_record/cdr/download?id={1}";
//                return RestRequest.buildSecurityRequest(token).get(uri, String.class, id);

                if(logger.isDebugEnabled()){
                    logger.debug("开始等。。。。。。");
                }

                Thread.sleep(2*60*1000);
                if(logger.isDebugEnabled()){
                    logger.debug("等待结束");
                }

                return RestResponse.success();
            }
        };
        return new WebAsyncTask(60000,callable);
    }
    @RequestMapping("/file/download/{id}")
    @ResponseBody
    public WebAsyncTask fileDownload(HttpServletRequest request, @PathVariable String id){
        Callable<RestResponse> callable = new Callable<RestResponse>() {
            public RestResponse call() throws Exception {
//                String token = getSecurityToken(request);
//                String uri = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_record/file/download?id={1}";
//                return RestRequest.buildSecurityRequest(token).get(uri, String.class,id);
                if(logger.isDebugEnabled()){
                    logger.debug("开始等。。。。。。");
                }

                Thread.sleep(2*60*1000);
                if(logger.isDebugEnabled()){
                    logger.debug("等待结束");
                }
                return RestResponse.success();
            }
        };
        return new WebAsyncTask(600000,callable);
    }

}
