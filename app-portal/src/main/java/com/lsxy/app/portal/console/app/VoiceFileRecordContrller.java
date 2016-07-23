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
 * Created by zhangxb on 2016/7/23.
 */
@Controller
@RequestMapping("/console/app/file/record")
public class VoiceFileRecordContrller extends AbstractPortalController {
    /**
     * 获取分页数据
     * @param request
     * @param pageNo
     * @param pageSize
     * @param appId
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
     * 获取录音文件的分页数据
     * @param request
     * @param pageNo
     * @param pageSize
     * @param appId
     * @return
     */
    private RestResponse pageListVoiceRecordPlay(HttpServletRequest request, Integer pageNo, Integer pageSize, String appId ){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_record/plist?pageNo={1}&pageSize={2}&appId={3}";
        return RestRequest.buildSecurityRequest(token).getPage(uri,VoiceFileRecord.class,pageNo,pageSize,appId);
    }
    /**
     * 查询文件容量
     * @param request
     * @return
     */
    @RequestMapping("/sum")
    @ResponseBody
    public Map sum(HttpServletRequest request,String appId){
        Map map =  (Map)sumAndCount(request,appId).getData();
        return map;
    }

    /**
     * 根据appId查询文件容量
     * @param request
     * @param appId
     * @return
     */
    private RestResponse sumAndCount(HttpServletRequest request,String appId){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_record/sum?appId={1}";
        return RestRequest.buildSecurityRequest(token).get(uri, Map.class,appId);
    }
    /**
     * 删除放音文件
     * @param request
     * @param id
     * @return
     */
    @RequestMapping("/deletes")
    @ResponseBody
    public Map delete(HttpServletRequest request, String id){
        RestResponse restResponse = deleteVoiceFileRecord(request,id);
        Map map = new HashMap();
        map.put("flag",restResponse.isSuccess());
        return map;
    }
    /**
     * 删除放音文件rest
     * @param request
     * @return
     */
    private RestResponse deleteVoiceFileRecord(HttpServletRequest request,String id){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_play/deletes?id={1}";
        return RestRequest.buildSecurityRequest(token).get(uri, VoiceFileRecord.class,id);
    }
}
