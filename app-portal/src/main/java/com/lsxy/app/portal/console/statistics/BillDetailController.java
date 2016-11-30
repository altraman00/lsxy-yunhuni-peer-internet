package com.lsxy.app.portal.console.statistics;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.call.center.api.model.CallCenter;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.file.model.VoiceFileRecord;
import com.lsxy.yunhuni.api.session.model.CallSession;
import com.lsxy.yunhuni.api.session.model.VoiceCdr;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 详单查询
 * Created by zhangxb on 2016/7/18.
 */
@Controller
@RequestMapping("/console/statistics/billdetail")
public class BillDetailController extends AbstractPortalController {
    /**
     * 语音通知
     * @param request
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @param time 时间
     * @param appId 应用id
     * @return
     */
    @RequestMapping("/notify")
    public ModelAndView notify(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize, String time, String appId){
        ModelAndView mav = new ModelAndView();
        Map<String,String> map = init(request,time,appId,App.PRODUCT_VOICE);
        mav.addAllObjects(map);
        Page pageObj = (Page)getPageList(request,pageNo,pageSize, CallSession.TYPE_VOICE_NOTIFY,map.get("time"),map.get("appId")).getData();
        mav.addObject("sum",sum(request,CallSession.TYPE_VOICE_NOTIFY,map.get("time"),map.get("appId")).getData());
        mav.addObject("pageObj",pageObj);
        mav.setViewName("/console/statistics/billdetail/notify");
        return mav;
    }
    /**
     * 语音验证码
     * @param request
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @param time 时间
     * @param appId 应用id
     * @return
     */
    @RequestMapping("/code")
    public ModelAndView code(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize, String time, String appId){
        ModelAndView mav = new ModelAndView();
        Map<String,String> map = init(request,time,appId,App.PRODUCT_VOICE);
        mav.addAllObjects(map);
        mav.addObject("sum",sum(request,CallSession.TYPE_VOICE_VOICECODE,map.get("time"),map.get("appId")).getData());
        mav.addObject("pageObj",getPageList(request,pageNo,pageSize, CallSession.TYPE_VOICE_VOICECODE,map.get("time"),map.get("appId")).getData());
        mav.setViewName("/console/statistics/billdetail/code");
        return mav;
    }
    /**
     * 录音
     * @param request
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @param time 时间
     * @param appId 应用id
     * @return
     */
    @RequestMapping("/recording")
    public ModelAndView recording(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize,
                                  String time, String appId,String type){
        ModelAndView mav = new ModelAndView();
        Map map = init(request,time,appId);
        map.put("type",type);
        map.put("types",VoiceFileRecord.types);
        mav.addAllObjects(map);
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_record/sum?appId={1}&type={2}&startTime={3}&endTime={4}";
        mav.addObject("sum",RestRequest.buildSecurityRequest(token).get(uri, Map.class,appId,type,map.get("time"),map.get("time")).getData());
        String uri1 = PortalConstants.REST_PREFIX_URL+"/rest/voice_file_record/plist/time?pageNo={1}&pageSize={2}&appId={3}&type={4}&startTime={5}&endTime={6}";
        mav.addObject("pageObj",RestRequest.buildSecurityRequest(token).getPage(uri1,Map.class,pageNo,pageSize,appId,type,map.get("time"),map.get("time")).getData());
        mav.setViewName("/console/statistics/billdetail/recording");
        return mav;
    }
    /**
     * IVR定制服务
     * @param request
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @param time 时间
     * @param appId 应用id
     * @return
     */
    @RequestMapping("/ivr")
    public ModelAndView ivr(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize, String time, String appId){
        ModelAndView mav = new ModelAndView();
        Map<String,String> map = init(request,time,appId,App.PRODUCT_VOICE);
        mav.addAllObjects(map);
        mav.addObject("sum",sum(request,CallSession.TYPE_VOICE_IVR,map.get("time"),map.get("appId")).getData());
        mav.addObject("pageObj",getPageList(request,pageNo,pageSize, CallSession.TYPE_VOICE_IVR,map.get("time"),map.get("appId")).getData());
        mav.setViewName("/console/statistics/billdetail/ivr");
        return mav;
    }
    /**
     * 会议
     * @param request
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @param time 时间
     * @param appId 应用id
     * @return
     */
    @RequestMapping("/metting")
    public ModelAndView metting(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize, String time, String appId){
        ModelAndView mav = new ModelAndView();
        Map<String,String> map = init(request,time,appId,App.PRODUCT_VOICE);
        mav.addAllObjects(map);
        mav.addObject("sum",sum(request,CallSession.TYPE_VOICE_MEETING,map.get("time"),map.get("appId")).getData());
        mav.addObject("pageObj",getPageList(request,pageNo,pageSize, CallSession.TYPE_VOICE_MEETING,map.get("time"),map.get("appId")).getData());
        mav.setViewName("/console/statistics/billdetail/metting");
        return mav;
    }
    /**
     * 语音回拨
     * @param request
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @param time 时间
     * @param appId 应用id
     * @return
     */
    @RequestMapping("/callback")
    public ModelAndView callback(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize, String time, String appId){
        ModelAndView mav = new ModelAndView();
        Map<String,String> map = init(request,time,appId,App.PRODUCT_VOICE);
        mav.addAllObjects(map);
        mav.addObject("sum",sum(request,CallSession.TYPE_VOICE_CALLBACK,map.get("time"),map.get("appId")).getData());
        mav.addObject("pageObj",getPageList(request,pageNo,pageSize, CallSession.TYPE_VOICE_CALLBACK,map.get("time"),map.get("appId")).getData());
        mav.setViewName("/console/statistics/billdetail/callback");
        return mav;
    }
    /**
     * 呼叫中心
     * @param request
     * @return
     */
    @RequestMapping("/callcenter")
    public ModelAndView callcenter(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize, String time, String appId){
        ModelAndView mav = new ModelAndView();
        Map<String,String> map = init(request,time,appId,App.PRODUCT_CALL_CENTER);
        mav.addAllObjects(map);
        mav.addObject("sum",sum(request,App.PRODUCT_CALL_CENTER,map.get("time"),map.get("appId")).getData());
        mav.addObject("pageObj",getPageList(request,pageNo,pageSize, App.PRODUCT_CALL_CENTER,map.get("time"),map.get("appId")).getData());
        mav.setViewName("/console/statistics/billdetail/callcenter");
        return mav;
    }
    /**
     * 下载
     * @param request
     * @param time 时间
     * @param appId 应用id
     * @return
     */
    @RequestMapping("{path}/download")
    public void download(HttpServletRequest request, HttpServletResponse response, @PathVariable String path, String time, String appId){
        String oType = "";
        String title = "";
        String one = "";
        String[] headers = null;
        String[] values = null;
        String serviceType = "";
        if("notify".equals(path)){//语音通知
            oType = CallSession.TYPE_VOICE_NOTIFY;
            title = "语音通知";
            headers = new String[]{"呼叫时间","主叫","被叫","消费金额","时长（秒）"};
            values = new String[]{"callStartDt","fromNum","toNum","cost","costTimeLong"};
            serviceType = App.PRODUCT_VOICE;
        }else if("code".equals(path)){//语音验证码
            oType = CallSession.TYPE_VOICE_VOICECODE;
            title = "语音验证码";
            headers = new String[]{"发送时间","主叫","被叫","挂机时间","消费金额","时长（秒）"};
            values = new String[]{"callStartDt","fromNum","toNum","callEndDt","cost","costTimeLong"};
            serviceType = App.PRODUCT_VOICE;
        }
        /*else if("recording".equals(type)){//录音
            oType = CallSession.TYPE_VOICE_RECORDING;
            title = "录音";
            headers = new String[]{};
            values = new String[]{};
        }*/
        else if("ivr".equals(path)) {// 自定义IVR
            oType = CallSession.TYPE_VOICE_IVR;
            title = " 自定义IVR";
            headers = new String[]{"呼叫时间","呼叫类型","主叫","被叫","消费金额","时长（秒）"};
            values = new String[]{"callStartDt","ivrType:1=呼入;2=呼出","fromNum","toNum","cost","costTimeLong"};
            serviceType = App.PRODUCT_VOICE;
        }else if("metting".equals(path)){// 语音会议
            oType = CallSession.TYPE_VOICE_MEETING;
            title = " 语音会议";
            headers = new String[]{"会议标识ID","呼叫时间","参与者","参与类型","消费金额","时长（秒）"};
            values = new String[]{"sessionId","callStartDt","joinType:0-fromNum;1-toNum;2-fromNum","joinType:0=创建;1=邀请加入;2=呼入加入","cost","costTimeLong"};
            serviceType = App.PRODUCT_VOICE;
        }else if("callback".equals(path)){//语音回拨
            oType = CallSession.TYPE_VOICE_CALLBACK;
            title = "语音回拨";
            headers = new String[]{"呼叫时间","主叫","被叫","消费金额","时长（秒）"};
            values = new String[]{"callStartDt","fromNum","toNum","cost","costTimeLong"};
            serviceType = App.PRODUCT_VOICE;
        }else if("callcenter".equals(path)){
            oType = CallSession.TYPE_VOICE_CALLBACK;
            title = "呼叫中心";
            headers = new String[]{"呼叫时间","呼叫类型","主叫","被叫","消费金额","时长（秒）"};
            values = new String[]{"callStartDt","ivrType:1=呼入;2=呼出","fromNum","toNum","cost","costTimeLong"};
            serviceType = App.PRODUCT_CALL_CENTER;
        }
        List list = null;
        if(StringUtils.isNotEmpty(oType)){
            Map<String,String> map = init(request,time,appId,serviceType);
            list = (List)getList(request,oType,map.get("time"),map.get("appId")).getData();
        }
        String appName = "";
        if(StringUtils.isNotEmpty(appId)){
            appName = ((App)getAppById(request,appId).getData()).getName();
        }else{
            appName = "全部";
        }
        one = title+" 时间："+time+" 应用："+appName;
        downloadExcel(title,one,headers,values,list,null,"cost",response);
    }
    /**
     * 统计
     * @param request
     * @param type 类型
     * @param time 时间
     * @param appId 应用id
     * @return
     */
    private RestResponse sum(HttpServletRequest request,String type,String time,String appId){
        String token = getSecurityToken(request);
        String uri =  PortalConstants.REST_PREFIX_URL  + "/rest/voice_cdr/sum?type={1}&time={2}&appId={3}";
        return RestRequest.buildSecurityRequest(token).get(uri, Map.class,type,time,appId);
    }
    /**
     * 获取页面数据
     * @param request
     * @param type 类型
     * @param time 时间
     * @param appId 应用
     * @return
     */
    private RestResponse getList(HttpServletRequest request,String type,String time,String appId){
        String token = getSecurityToken(request);
        String uri =  PortalConstants.REST_PREFIX_URL  + "/rest/voice_cdr/list?type={1}&time={2}&appId={3}";
        RestResponse<List<VoiceCdr>> result = RestRequest.buildSecurityRequest(token).getList(uri, VoiceCdr.class, type, time, appId);
        if(result.isSuccess() && result.getData() != null){
            List<VoiceCdr> voiceCdrs = result.getData();
            if(voiceCdrs != null && voiceCdrs.size() > 0){
                for(VoiceCdr cdr:voiceCdrs){
                    String toNum = cdr.getToNum();
                    if(StringUtils.isNotBlank(toNum)){
                        String[] split = toNum.split("@");
                        cdr.setToNum(split[0]);
                    }
                }
            }
        }
        return result;
    }
    /**
     * 获取页面分页数据
     * @param request
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @param type 类型
     * @param time 时间
     * @param appId 应用
     * @return
     */
    private RestResponse getPageList(HttpServletRequest request,Integer pageNo,Integer pageSize,String type,String time,String appId){
        String token = getSecurityToken(request);
        String uri =  PortalConstants.REST_PREFIX_URL  + "/rest/voice_cdr/plist?pageNo={1}&pageSize={2}&type={3}&time={4}&appId={5}";
        RestResponse<Page<VoiceCdr>> result = RestRequest.buildSecurityRequest(token).getPage(uri, VoiceCdr.class, pageNo, pageSize, type, time, appId);
        if(result.isSuccess() && result.getData() != null){
            List<VoiceCdr> voiceCdrs = result.getData().getResult();
            if(voiceCdrs != null && voiceCdrs.size() > 0){
                for(VoiceCdr cdr:voiceCdrs){
                    String toNum = cdr.getToNum();
                    if(StringUtils.isNotBlank(toNum)){
                        String[] split = toNum.split("@");
                        cdr.setToNum(split[0]);
                    }
                }
            }
        }
        return result;
    }
    /**
     * 处理初始条件
     * @param request
     * @param time
     * @param appId
     * @return
     */
    public Map init(HttpServletRequest request, String time, String appId,String serviceType){
        Map map = new HashMap();
        List<App> appList = (List<App>)getBillAppList(request,serviceType).getData();
        map.put("appList",appList);
        if(StringUtil.isEmpty(appId)){
            if(StringUtils.isEmpty(appId)){
                if(appList.size()>0) {
                    appId = appList.get(0).getId();
                }
            }
        }
        map.put("appId",appId);
        if(StringUtils.isEmpty(time)){
            time = DateUtils.formatDate(new Date(),"yyyy-MM-dd");
        }
        map.put("time",time);
        return map;
    }
    public Map init(HttpServletRequest request, String time, String appId){
        Map map = new HashMap();
        List<App> appList = (List<App>)getAppList(request).getData();
        map.put("appList",appList);
        if(StringUtil.isEmpty(appId)){
            if(StringUtils.isEmpty(appId)){
                if(appList.size()>0) {
                    appId = appList.get(0).getId();
                }
            }
        }
        map.put("appId",appId);
        if(StringUtils.isEmpty(time)){
            time = DateUtils.formatDate(new Date(),"yyyy-MM-dd");
        }
        map.put("time",time);
        return map;
    }


}
