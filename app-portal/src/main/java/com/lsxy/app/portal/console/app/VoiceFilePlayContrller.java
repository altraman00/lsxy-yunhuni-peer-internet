package com.lsxy.app.portal.console.app;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.billing.model.Billing;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 放音文件处理
 * Created by zhangxb on 2016/7/21.
 */
@Controller
@RequestMapping("/console/app/file")
public class VoiceFilePlayContrller extends AbstractPortalController {
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
    public Map VoiceFilePlay(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20")Integer pageSize, String appId,String name){
        Map map = new HashMap();
        Billing billing = (Billing)getBilling(request).getData();
        map.put("fileRemainSize",new DecimalFormat("#.00").format(billing.getFileRemainSize()/1000/1000));
        map.put("fileTotalSize",new DecimalFormat("#.00").format(billing.getFileTotalSize()/1000/1000));
        map.put("list",pageListVoiceFilePlay(request,pageNo,pageSize,appId,name).getData());
        return map;
    }
    @RequestMapping("/modify")
    @ResponseBody
    public Map modify(HttpServletRequest request, String id,String remark){
        RestResponse restResponse = modifyVoiceFilePlay(request,id,remark);
        Map map = new HashMap();
        map.put("flag",restResponse.isSuccess());
        return map;
    }
    /**
     * 修改放音文件rest
     * @param request
     * @return
     */
    private RestResponse modifyVoiceFilePlay(HttpServletRequest request,String id,String remark){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_play/modify?id={1}&remard={2}";
        return RestRequest.buildSecurityRequest(token).get(uri, Billing.class,id,remark);
    }
    /**
     * 删除放音文件
     * @param request
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Map delete(HttpServletRequest request, String id){
        RestResponse restResponse = deleteVoiceFilePlay(request,id);
        Map map = new HashMap();
        map.put("flag",restResponse.isSuccess());
        return map;
    }
    /**
     * 删除放音文件rest
     * @param request
     * @return
     */
    private RestResponse deleteVoiceFilePlay(HttpServletRequest request,String id){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_play/delete?id={1}";
        return RestRequest.buildSecurityRequest(token).get(uri, Billing.class,id);
    }
    /**
     * 查询放音文件
     * @param request
     * @param pageNo
     * @param pageSize
     * @param appId
     * @return
     */
    private RestResponse pageListVoiceFilePlay(HttpServletRequest request, Integer pageNo, Integer pageSize, String appId,String name){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_play/plist?pageNo={1}&pageSize={2}&name={3}&appId={4}";
        return RestRequest.buildSecurityRequest(token).getPage(uri,VoiceFilePlay.class,pageNo,pageSize,name,appId);
    }
    /**
     * 获取账务表
     * @param request
     * @return
     */
    private RestResponse getBilling(HttpServletRequest request){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/billing/get";
        return RestRequest.buildSecurityRequest(token).get(uri, Billing.class);
    }
}
