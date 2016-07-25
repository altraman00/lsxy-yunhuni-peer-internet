package com.lsxy.app.portal.console.app;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.file.model.VoiceFileRecord;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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
    public Map pageist(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20")Integer pageSize, String appId ){
        Map map = new HashMap();
        map.put("list",pageListVoiceRecordPlay(request,pageNo,pageSize,appId).getData());
        return map;
    }

    /**
     * 根据当前页，每页记录数，应用id获取录音文件的分页信息
     * @param request
     * @param pageNo 当前页
     * @param pageSize 每页记录数
     * @param appId 应用id
     * @return
     */
    private RestResponse pageListVoiceRecordPlay(HttpServletRequest request, Integer pageNo, Integer pageSize, String appId ){
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
    public Map sumAndCount(HttpServletRequest request,String appId,String startTime,String endTime){
        Map map =  (Map)sumAndCountRest(request,appId,startTime,endTime).getData();
        return map;
    }

    /**
     * 根据应用id和开始时间，结束时间统计区间内文件数量total和文件总大小size
     * @param request
     * @param appId 应用id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    private RestResponse sumAndCountRest(HttpServletRequest request,String appId,String startTime,String endTime){
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
    public Map delete(HttpServletRequest request, String id){
        RestResponse restResponse = deleteVoiceFileRecord(request,id);
        Map map = new HashMap();
        map.put("flag",restResponse.isSuccess());
        return map;
    }
    /**
     * 根据录音文件id删除录音文件
     * @param request
     * @param id 录音文件id
     * @return
     */
    private RestResponse deleteVoiceFileRecord(HttpServletRequest request,String id){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_record/delete?id={1}";
        return RestRequest.buildSecurityRequest(token).get(uri, VoiceFileRecord.class,id);
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
    public Map batchDelete(HttpServletRequest request,String appId,String startTime,String endTime){
        RestResponse restResponse = batchDeleteVoiceFileRecord(request,appId,startTime,endTime);
        Map map = new HashMap();
        map.put("flag",restResponse.isSuccess());
        return map;
    }

    /**
     * 根据应用id，删除在开始时间和结束时间区间内的录音文件
     * @param request
     * @param appId 应用id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    private RestResponse batchDeleteVoiceFileRecord(HttpServletRequest request,String appId,String startTime,String endTime){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_record/batch/delete?appId={1}&startTime={2}&endTime={3}";
        return RestRequest.buildSecurityRequest(token).get(uri, VoiceFileRecord.class,appId,startTime,endTime);
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
    public Map zip(HttpServletRequest request,String appId,String startTime,String endTime){
        Map map = new HashMap();
        RestResponse restResponse = zipVoiceFileRecord(request,appId,startTime,endTime);
        map.put("flag",restResponse.isSuccess());
        map.put("fileName",restResponse.getData());
        return map;
    }

    /**
     * 根据应用id，在开始时间，结束时间的区间内的录音文件进行压缩 ，返回压缩后的文件地址
     * @param request
     * @param appId 应用id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    private RestResponse zipVoiceFileRecord(HttpServletRequest request,String appId,String startTime,String endTime){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_record/batch/download?appId={1}&startTime={2}&endTime={3}";
        return RestRequest.buildSecurityRequest(token).get(uri, String.class,appId,startTime,endTime);
    }
}
