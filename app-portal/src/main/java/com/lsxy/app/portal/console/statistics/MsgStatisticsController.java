package com.lsxy.app.portal.console.statistics;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.app.model.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * Created by zhangxb on 2017/3/10.
 */
@Controller
@RequestMapping("/console/statistics/msg/")
public class MsgStatisticsController extends AbstractPortalController {
    private static final Logger logger = LoggerFactory.getLogger(MsgStatisticsController.class);

    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request, ConsumeStatisticsVo consumeStatisticsVo){
        ModelAndView mav = new ModelAndView();
        mav.addObject("appList",getBillAppList(request, App.PRODUCT_MSG).getData());
        mav.addObject("consumeStatisticsVo",consumeStatisticsVo);//时间
        mav.setViewName("/console/statistics/msg/index");
        return mav;
    }
    /**
     * 异步分页信息
     * @param request
     * @param consumeStatisticsVo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/plist")
    @ResponseBody
    public RestResponse pageList(HttpServletRequest request,ConsumeStatisticsVo consumeStatisticsVo,
                                 @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20")Integer pageSize){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL  + "/rest/msg_statistics/plist?tenantId={1}&appId={2}&type={3}&startTime={4}&pageNo={5}&pageSize={6}";
        Class clazz = MsgStatisticsVo.class;
        String appId = "-1".equals(consumeStatisticsVo.getAppId())?null:consumeStatisticsVo.getAppId();
        String tenantId = getCurrentAccount(request).getTenant().getId();
        RestResponse restResponse = RestRequest.buildSecurityRequest(token).getPage(uri,clazz ,tenantId,appId,consumeStatisticsVo.getType(),consumeStatisticsVo.getStartTime(),pageNo,pageSize);
        return restResponse;
    }

    /**
     * 异步获取图表显示信息
     * @param request
     * @param consumeStatisticsVo
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public RestResponse list(HttpServletRequest request,ConsumeStatisticsVo consumeStatisticsVo){
        String token = getSecurityToken(request);
        String appId = "-1".equals(consumeStatisticsVo.getAppId())?null:consumeStatisticsVo.getAppId();
        String tenantId = getCurrentAccount(request).getTenant().getId();
        String uri = PortalConstants.REST_PREFIX_URL  +   "/rest/msg_statistics/list?tenantId={1}&appId={2}&startTime={3}&type={4}";
        RestResponse<List<MsgStatisticsVo>> restResponse =  RestRequest.buildSecurityRequest(token).getList(uri, MsgStatisticsVo.class,tenantId,appId,consumeStatisticsVo.getStartTime(),consumeStatisticsVo.getType());
        List<MsgStatisticsVo> list =  restResponse.getData();
        Map map = new HashMap();
        if(ConsumeStatisticsVo.TYPE_MONTH.equals(consumeStatisticsVo.getType())){
            Map map1 = getArrays(list, 12);
            map.putAll(map1);
        }else {
            Map map1 = getArrays(list, DateUtils.parseDate(consumeStatisticsVo.getStartTime(), "yyyy-MM"));
            map.putAll(map1);
        }
        map.put("count",list.size());
        return RestResponse.success(map);
    }

    /**
     * 获取列表数据
     * @param list 待处理的list
     * @return
     */
    private Map getArrays(List list,Object date) {
        int leng = getLong(date);
        Long[] succ = new Long[leng];
        Long[] fail = new Long[leng];
        for(int j=0;j<leng;j++){
            succ[j] = 0L;
            fail[j] = 0L;
        }
        for(int i=0;i<list.size();i++){
            Object obj = list.get(i);
            if(obj instanceof MsgStatisticsVo){
                MsgStatisticsVo temp = (MsgStatisticsVo)obj;
                int index = temp.getNum()-1;
                succ[index] = temp.getTotalSucc();
                fail[index] = temp.getTotalFail();
            }
        }
        return new HashMap<String,Long[]>(){{
            put("succ",succ);
            put("fail",fail);
        }};
    }
    private int getLong(Object obj){
        int r = 0;
        if (obj instanceof Date) {
            r = Integer.valueOf(DateUtils.getLastDate((Date)obj).split("-")[2]);
        } else if (obj instanceof Integer) {
            r =Integer.valueOf((Integer)obj);
        }
        return r;
    }
}
