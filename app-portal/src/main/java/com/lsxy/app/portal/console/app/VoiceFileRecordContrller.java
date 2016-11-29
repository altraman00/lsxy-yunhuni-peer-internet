package com.lsxy.app.portal.console.app;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 录音文件
 * Created by zhangxb on 2016/7/23.
 */
@Controller
@RequestMapping("/console/app/file/record")
public class VoiceFileRecordContrller extends AbstractPortalController {
    @RequestMapping("/cdr/download/{id}")
    @ResponseBody
    public RestResponse cdrDownload(HttpServletRequest request, @PathVariable String id){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_record/cdr/download?id={1}";
        return RestRequest.buildSecurityRequest(token).get(uri, String.class,id);
    }
    @RequestMapping("/file/download/{id}")
    @ResponseBody
    public RestResponse fileDownload(HttpServletRequest request, @PathVariable String id){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_record/file/download?id={1}";
        return RestRequest.buildSecurityRequest(token).get(uri, String.class,id);
    }

}
