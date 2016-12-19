package com.lsxy.app.portal.rest.stastistic;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.yunhuni.api.statistics.model.VoiceCdrMonth;
import com.lsxy.yunhuni.api.statistics.service.VoiceCdrMonthService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 通话记录(session)月统计
 * Created by zhangxb on 2016/7/6.
 */
@RequestMapping("/rest/voice_cdr_month")
@RestController
public class VoiceCdrMonthController extends AbstractRestController {
    @Autowired
    VoiceCdrMonthService voiceCdrMonthService;
    /**
     * 根据时间和应用获取列表数据
     * @param appId 应用id
     * @param startTime 时间
     * @return
     */
    @RequestMapping("/list")
    public RestResponse list(String tenantId, String appId, String type, String startTime, String endTime ){
        Date date1 = DateUtils.parseDate(startTime,"yyyy");
        if(StringUtils.isEmpty(endTime)){
            endTime = startTime;
        }
        Date date2  = DateUtils.parseDate(DateUtils.getLastYearByDate(endTime)+" 23:59:59","yyyy-MM-dd HH:mm:ss");
        List<VoiceCdrMonth> list =  voiceCdrMonthService.list(tenantId,appId,type,date1,date2);
        return RestResponse.success(list);
    }


}
