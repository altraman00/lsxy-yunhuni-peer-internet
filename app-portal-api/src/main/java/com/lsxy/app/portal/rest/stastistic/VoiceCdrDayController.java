package com.lsxy.app.portal.rest.stastistic;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.api.statistics.model.VoiceCdrDay;
import com.lsxy.framework.api.statistics.service.VoiceCdrDayService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.web.rest.RestResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 通话记录(session)日统计
 * Created by zhangxb on 2016/7/6.
 */
@RequestMapping("/rest/voice_cdr_day")
@RestController
public class VoiceCdrDayController extends AbstractRestController {
    @Autowired
    VoiceCdrDayService voiceCdrDayService;

    /**
     * 获取用户某时间的列表数据
     * @param tenantId 对于租户
     * @param appId 应用id
     * @param type 消费类型
     * @param startTime 时间
     * @param endTime 结束时间
     * @return
     */
    @RequestMapping("/list")
    public RestResponse list(String tenantId, String appId, String type, String startTime, String endTime ){
        Date date1 = DateUtils.parseDate(startTime,"yyyy-MM");
        if(StringUtils.isEmpty(endTime)){
            endTime = startTime;
        }
        Date date2 =  DateUtils.parseDate(DateUtils.getMonthLastTime(DateUtils.parseDate(endTime,"yyyy-MM")),"yyyy-MM-dd HH:mm:ss");
        List<VoiceCdrDay> list =  voiceCdrDayService.list(tenantId,  appId,  type,  date1,  date2 );
        return RestResponse.success(list);
    }

}
