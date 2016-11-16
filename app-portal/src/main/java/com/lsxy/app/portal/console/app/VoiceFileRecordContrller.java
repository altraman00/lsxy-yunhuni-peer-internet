package com.lsxy.app.portal.console.app;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.file.model.VoiceFileRecord;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 录音文件
 * Created by zhangxb on 2016/7/23.
 */
@Controller
@RequestMapping("/console/app/file/record")
public class VoiceFileRecordContrller extends AbstractPortalController {

    /**
     * 根据当前页，每页记录数，应用id获取录音文件的分页信息
     * @param request
     * @param pageNo 当前页
     * @param pageSize 每页记录数
     * @param appId 应用id
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public RestResponse pageist(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20")Integer pageSize, String appId ){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_record/plist?pageNo={1}&pageSize={2}&appId={3}";
        return RestRequest.buildSecurityRequest(token).getPage(uri,VoiceFileRecord.class,pageNo,pageSize,appId);
    }

    /**
     * 根据应用id和开始时间，结束时间统计区间内文件数量total和文件总大小size
     * @param request
     * @param appId 应用id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @RequestMapping("/sum")
    @ResponseBody
    public RestResponse sumAndCount(HttpServletRequest request,String appId,String startTime,String endTime){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_record/sum?appId={1}&startTime={2}&endTime={3}";
        return RestRequest.buildSecurityRequest(token).get(uri, Map.class,appId,startTime,endTime);
    }

    /**
     * 根据录音文件id删除录音文件
     * @param request
     * @param id 录音文件id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public RestResponse delete(HttpServletRequest request, String id){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_record/delete?id={1}";
        RestResponse<VoiceFileRecord> response = RestRequest.buildSecurityRequest(token).get(uri, VoiceFileRecord.class, id);
        if(response.isSuccess()){
            return RestResponse.success();
        }else{
            return response;
        }
    }


    /**
     * 根据应用id，删除在开始时间和结束时间区间内的录音文件
     * @param request
     * @param appId 应用id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @RequestMapping("/batch_delete")
    @ResponseBody
    public RestResponse batchDelete(HttpServletRequest request,String appId,String startTime,String endTime){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_record/batch/delete?appId={1}&startTime={2}&endTime={3}";
        return RestRequest.buildSecurityRequest(token).get(uri, Integer.class,appId,startTime,endTime);
    }

    /**
     * 根据应用id，在开始时间，结束时间的区间内的录音文件进行压缩 ，返回压缩后的文件地址
     * @param request
     * @param appId 应用id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @RequestMapping("/zip")
    @ResponseBody
    public RestResponse zip(HttpServletRequest request,String appId,String startTime,String endTime){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_record/batch/download?appId={1}&startTime={2}&endTime={3}";
        return RestRequest.buildSecurityRequest(token).get(uri, String.class,appId,startTime,endTime);
    }
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
