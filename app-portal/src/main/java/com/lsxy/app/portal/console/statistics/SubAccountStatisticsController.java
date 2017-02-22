package com.lsxy.app.portal.console.statistics;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.session.model.CallSession;
import com.lsxy.yunhuni.api.session.model.VoiceCdr;
import com.lsxy.yunhuni.api.statistics.model.SubaccountStatisticalVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.WebAsyncTask;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by zhangxb on 2017/2/21.
 */
@Controller
@RequestMapping("/console/statistics/subaccount")
public class SubAccountStatisticsController extends AbstractPortalController {
    private static final Logger logger = LoggerFactory.getLogger(SubAccountStatisticsController.class);
    /**
     * 消费统计首页
     * @param request
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize, String monthTime, String dayTime, String appId,String stime,String subId){
        ModelAndView mav = new ModelAndView();
        mav.addObject("appList",getAppList(request).getData());
        Date date = new Date();
        if(StringUtil.isEmpty(appId)){
            appId = "all";
        }
        mav.addObject("appId",appId);
        if(StringUtils.isEmpty(monthTime)){
            monthTime = DateUtils.formatDate(new Date(),"yyyy-MM");
        }
        mav.addObject("monthTime",monthTime);
        if(StringUtils.isEmpty(dayTime)){
            dayTime = DateUtils.formatDate(new Date(),"yyyy-MM-dd");
        }
        if(StringUtils.isEmpty(stime)){
            stime = "day";
        }
        String startTime ;
        if("day".equals(stime)){
            startTime = dayTime;
        }else{
            startTime = monthTime;
        }
        mav.addObject("stime",stime);
        mav.addObject("dayTime",dayTime);
        //参数初始化完成
        //获取参数记录
        String token = getSecurityToken(request);
        Account account = getCurrentAccount(request);
        //tenantId, String appId, String subId, String startTime, String endTime
        String uri =  PortalConstants.REST_PREFIX_URL  + "/rest/api_sub_account_"+stime+"/plist?pageNo={1}&pageSize={2}&tenantId={3}&appId={4}&subId={5}&startTime={6}&endTime={7}";
        RestResponse result = RestRequest.buildSecurityRequest(token).getPage(uri, SubaccountStatisticalVO.class, pageNo, pageSize, account.getTenant().getId(), appId,subId,startTime,null);
        String uri2 =  PortalConstants.REST_PREFIX_URL  + "/rest/api_sub_account_"+stime+"/sum?tenantId={3}&appId={4}&subId={5}&startTime={6}&endTime={7}";
        RestResponse result2 = RestRequest.buildSecurityRequest(token).getPage(uri2, SubaccountStatisticalVO.class, account.getTenant().getId(), appId,subId,startTime,null);
        mav.addObject("sum",result2.getData());
        mav.addObject("pageObj",result.getData());
        mav.setViewName("/console/statistics/subaccount/index");
        return mav;
    }
    @RequestMapping("{path}/download")
    @ResponseBody
    public String download(HttpServletRequest request, HttpServletResponse response, @PathVariable String path, String start, String end, String appId) {
//        String oType = "";
//        String title = "";
//        String one = "";
//        String[] headers = null;
//        String[] values = null;
//        String serviceType = "";
//        if ("notify".equals(path)) {//语音通知
//            oType = CallSession.TYPE_VOICE_NOTIFY;
//            title = "语音通知";
//            headers = new String[]{"子账号", "子账号密钥", "所属应用", "话务量（分钟）", "消费金额（元）","语音总用量 /配额（分钟）","坐席数/配额（个）"};
//            values = new String[]{"callStartDt", "fromNum", "toNum", "cost", "costTimeLong"};
//            serviceType = App.PRODUCT_VOICE;
//        } else if ("code".equals(path)) {//语音验证码
//            oType = CallSession.TYPE_VOICE_VOICECODE;
//            title = "语音验证码";
//            headers = new String[]{"发送时间", "主叫", "被叫", "挂机时间", "消费金额", "时长（秒）"};
//            values = new String[]{"callStartDt", "fromNum", "toNum", "callEndDt", "cost", "costTimeLong"};
//            serviceType = App.PRODUCT_VOICE;
//        } else if ("ivr".equals(path)) {// 自定义IVR
//            oType = CallSession.TYPE_VOICE_IVR;
//            title = " 自定义IVR";
//            headers = new String[]{"呼叫时间", "呼叫类型", "主叫", "被叫", "消费金额", "时长（秒）"};
//            values = new String[]{"callStartDt", "ivrType:1=呼入;2=呼出", "fromNum", "toNum", "cost", "costTimeLong"};
//            serviceType = App.PRODUCT_VOICE;
//        } else if ("metting".equals(path)) {// 语音会议
//            oType = CallSession.TYPE_VOICE_MEETING;
//            title = " 语音会议";
//            headers = new String[]{"会议标识ID", "呼叫时间", "参与者", "参与类型", "消费金额", "时长（秒）"};
//            values = new String[]{"sessionId", "callStartDt", "joinType:0-fromNum;1-toNum;2-fromNum", "joinType:0=创建;1=邀请加入;2=呼入加入", "cost", "costTimeLong"};
//            serviceType = App.PRODUCT_VOICE;
//        } else if ("callback".equals(path)) {//语音回拨
//            oType = CallSession.TYPE_VOICE_CALLBACK;
//            title = "语音回拨";
//            headers = new String[]{"呼叫时间", "主叫", "被叫", "消费金额", "时长（秒）"};
//            values = new String[]{"callStartDt", "fromNum", "toNum", "cost", "costTimeLong"};
//            serviceType = App.PRODUCT_VOICE;
//        } else if ("callcenter".equals(path)) {
//            oType = CallSession.TYPE_CALL_CENTER;
//            title = "呼叫中心";
//            headers = new String[]{"呼叫时间", "呼叫类型", "主叫", "被叫", "消费金额", "时长（秒）"};
//            values = new String[]{"callStartDt", "ivrType:1=呼入;2=呼出", "fromNum", "toNum", "cost", "costTimeLong"};
//            serviceType = App.PRODUCT_CALL_CENTER;
//        }
//        List list = null;
//        if (StringUtils.isNotEmpty(oType)) {
//            Map<String, String> map = init(request, start, end, appId, serviceType);
//            list = (List) getList(request, oType, map.get("start"), map.get("end"), map.get("appId")).getData();
//        }
//        String appName = "";
//        if (StringUtils.isNotEmpty(appId)) {
//            appName = ((App) getAppById(request, appId).getData()).getName();
//        } else {
//            appName = "全部";
//        }
//        one = title + " 时间：" + start + "到" + end + ",应用：" + appName;
//        downloadExcel(title, one, headers, values, list, null, "cost", response);
        return "";
    }
}
