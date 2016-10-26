package com.lsxy.app.portal.rest.stastistic;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.session.model.CallCenter;
import com.lsxy.yunhuni.api.session.service.CallCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangxb on 2016/10/22.
 */
@RequestMapping("/rest/call_center")
@RestController
public class CallCenterController extends AbstractRestController {
    @Autowired
    CallCenterService callCenterService;
    @RequestMapping("/plist")
    public RestResponse plist(Integer pageNo,Integer pageSize,String appId,String startTime,String endTime,String type,String callnum,String agent){
        try{
            DateUtils.parseDate(startTime,"yyyy-MM-dd");
            DateUtils.parseDate(endTime,"yyyy-MM-dd");
            startTime += " 00:00:00";
            endTime += " 23:59:59";
        }catch (Exception e){
            return RestResponse.failed("0000","返回日期类型错误");
        }
        Page<CallCenter> page =  callCenterService.pList( pageNo, pageSize,getCurrentAccount().getTenant().getId(), appId, startTime, endTime, type,callnum, agent);
        return RestResponse.success(page);
    }
    @RequestMapping("/list")
    public RestResponse list(String appId,String startTime,String endTime,String type,String callnum,String agent){
        try{
            DateUtils.parseDate(startTime,"yyyy-MM-dd");
            DateUtils.parseDate(endTime,"yyyy-MM-dd");
            startTime += " 00:00:00";
            endTime += " 23:59:59";
        }catch (Exception e){
            return RestResponse.failed("0000","返回日期类型错误");
        }
        List<CallCenter> list =  callCenterService.getAllList(getCurrentAccount().getTenant().getId(), appId, startTime, endTime, type,callnum, agent);
        return RestResponse.success(list);
    }
    @RequestMapping("/sum")
    public RestResponse sum(String appId,String startTime,String endTime,String type,String callnum,String agent){
        try{
            DateUtils.parseDate(startTime,"yyyy-MM-dd");
            DateUtils.parseDate(endTime,"yyyy-MM-dd");
            startTime += " 00:00:00";
            endTime += " 23:59:59";
        }catch (Exception e){
            return RestResponse.failed("0000","返回日期类型错误");
        }
        Map result =  callCenterService.sum(getCurrentAccount().getTenant().getId(), appId, startTime, endTime, type,callnum, agent);
        return RestResponse.success(result);
    }
}
