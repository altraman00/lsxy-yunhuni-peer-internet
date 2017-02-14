package com.lsxy.app.portal.rest.stastistic;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.session.model.VoiceCdr;
import com.lsxy.yunhuni.api.session.service.VoiceCdrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 通话记录(session)
 * Created by zhangxb on 2016/7/19.
 */
@RequestMapping("/rest/voice_cdr")
@RestController
public class VoiceCdrController extends AbstractRestController {
    @Autowired
    VoiceCdrService voiceCdrService;
    /**
     * 获取数据
     * @param type 类型
     * @param start 开始时间
     * @param end 结束时间
     * @param appId 应用id
     * @return
     */
    @RequestMapping("/list")
    public RestResponse list(String type,String start,String end,String appId){
        Date startTime = DateUtils.parseDate(start,"yyyy-MM-dd");
        Date endTime = DateUtils.parseDate(end + " 23:59:59","yyyy-MM-dd HH:mm:ss");
        List<VoiceCdr> list = voiceCdrService.listCdr(type,getCurrentAccount().getTenant().getId(),startTime,endTime,appId);
        return RestResponse.success(list);
    }
    /**
     * 获取分页数据
     * @param pageNo 当前页
     * @param pageSize 每页记录数
     * @param type 类型
     * @param start 开始时间
     * @param end 结束时间
     * @param appId 应用id
     * @return
     */
    @RequestMapping("/plist")
    public RestResponse pageList(Integer pageNo,Integer pageSize,String type,String start,String end,String appId){
        Date startTime = DateUtils.parseDate(start,"yyyy-MM-dd");
        Date endTime = DateUtils.parseDate(end+" 23:59:59","yyyy-MM-dd HH:mm:ss");
        Page<VoiceCdr> page = voiceCdrService.pageList(pageNo,pageSize,type,getCurrentAccount().getTenant().getId(),startTime,endTime,appId);
        return RestResponse.success(page);
    }

    /**
     * 根据查询类型，应用id，统计当前日期的消费额度或者验证码条数
     * @param type 类型 查看产品表code字段或枚举类ProductCode
     * @param start 开始时间
     * @param end 结束时间
     * @param appId 应用id
     * @return
     */
    @RequestMapping("/sum")
    public RestResponse sumCdr(String type,String start,String end,String appId){
        Date startTime = DateUtils.parseDate(start,"yyyy-MM-dd");
        Date endTime = DateUtils.parseDate(end+" 23:59:59","yyyy-MM-dd HH:mm:ss");
        Map map = voiceCdrService.sumCost(type,getCurrentAccount().getTenant().getId(),startTime,endTime,appId);
        return RestResponse.success(map);
    }

    /**
     * 查找应用当前呼叫数据
     * @throws Exception
     */
    @RequestMapping("/current_record_statistics")
    public RestResponse currentRecordStatistics(String appId) throws Exception{
        Map statistics= voiceCdrService.currentRecordStatistics(appId);
        return RestResponse.success(statistics);
    }
//    @RequestMapping("/get_avg_ddr")
//    public RestResponse getAvgCdr( String appId, String startTime, String endTime) throws Exception{
//        Map map= voiceCdrService.getAvgCdr(getCurrentAccount().getTenant().getId(),appId,startTime,endTime);
//        return RestResponse.success(map);
//    }


}
