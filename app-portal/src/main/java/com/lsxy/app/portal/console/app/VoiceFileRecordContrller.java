package com.lsxy.app.portal.console.app;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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
                String token = getSecurityToken(request);
                String uri = PortalConstants.REST_PREFIX_URL + "/rest/voice_file_record/cdr/download?id={1}";
                RestResponse restResponse1 =  RestRequest.buildSecurityRequest(token).get(uri, String.class, id);
                String temp1 = (String)restResponse1.getData();
                if(restResponse1.isSuccess() && temp1.indexOf("http")!=-1 ){
                    return restResponse1;
                }else if("0401".equals(restResponse1.getErrorCode())){
                    return restResponse1;
                }else{
                    String pId = restResponse1.getErrorMsg();
//                    long init = new Date().getTime();
                    for (int j = 1; j <= 10; j++) {
                        Thread.sleep( 6 * 1000);
//                        logger.info("正在执行第["+j+"]次查询已耗费时间："+ (new Date().getTime()-init) );
                        RestResponse restResponse2 = getPolling(request, pId);
                        String temp2 = (String)restResponse2.getData();
                        if (restResponse2.isSuccess() && temp2.indexOf("http")!=-1) {
                            return restResponse2;
                        } else {
                            if ("0001".equals(restResponse2.getErrorCode())) {
                                return restResponse2;
                            }
                        }
                    }
                    return RestResponse.failed("0000","下载超时失败");
                }
            }
        };
        return new WebAsyncTask(60000,callable);
    }
    @RequestMapping("/file/download/{id}")
    @ResponseBody
    public WebAsyncTask fileDownload(HttpServletRequest request, @PathVariable String id){
        Callable<RestResponse> callable = new Callable<RestResponse>() {
            public RestResponse call() throws Exception {
                String token = getSecurityToken(request);
                String uri = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_record/file/download?id={1}";
                RestResponse restResponse1 =   RestRequest.buildSecurityRequest(token).get(uri, String.class,id);
                String temp1 = (String)restResponse1.getData();
                if(restResponse1.isSuccess() && temp1.indexOf("http")!=-1 ){
                    return restResponse1;
                }else{
                    String pId = restResponse1.getErrorMsg();
                    for (int j = 1; j <= 10; j++) {
                        Thread.sleep( 6 * 1000);
                        RestResponse restResponse2 = getPolling(request, pId);
                        String temp2 = (String)restResponse2.getData();
                        if (restResponse2.isSuccess() && temp2.indexOf("http")!=-1) {
                            return restResponse2;
                        } else {
                            if ("0001".equals(restResponse2.getErrorCode())) {
                                return restResponse2;
                            }
                        }
                    }
                    return RestResponse.failed("0000","下载超时失败");
                }
            }
        };
        return new WebAsyncTask(600000,callable);
    }
    public RestResponse getPolling(HttpServletRequest request,String id){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_record/polling/{1}";
        return RestRequest.buildSecurityRequest(token).get(uri, String.class,id);
    }
}
