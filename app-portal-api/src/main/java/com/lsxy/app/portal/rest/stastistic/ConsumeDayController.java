package com.lsxy.app.portal.rest.stastistic;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.api.statistics.model.ConsumeDay;
import com.lsxy.framework.api.statistics.service.ConsumeDayService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 消费日统计
 * Created by zhangxb on 2016/7/6.
 */
@RequestMapping("/rest/consume_day")
@RestController
public class ConsumeDayController extends AbstractRestController {
    @Autowired
    ConsumeDayService consumeDayService;

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
    public RestResponse list(String tenantId, String appId, String type, Date startTime, Date endTime ){
        List<ConsumeDay> list =  consumeDayService.list(tenantId,  appId,  type,  startTime,  endTime );
        return RestResponse.success(list);
    }

    /**
     * 获取分页数据
     * @param tenantId 对于租户
     * @param appId 应用id
     * @param type 消费类型
     * @param startTime 时间 yyyy-MM
     * @param endTime 结束时间 yyyy-MM
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @return
     */
    @RequestMapping("/page")
    public RestResponse pageList(String tenantId, String appId,String type,String startTime, String endTime,Integer pageNo,Integer pageSize){
        Date date1 = DateUtils.parseDate(startTime,"yyyy-MM");
        if(StringUtils.isEmpty(endTime)){
            endTime = startTime;
        }
        Date date2 =  DateUtils.parseDate(DateUtils.getMonthLastTime(DateUtils.parseDate(endTime,"yyyy-MM")),"yyyy-MM-dd HH:mm:ss");
        Page<ConsumeDay> page =  consumeDayService.pageList( tenantId,  appId, type, date1,  date2, pageNo, pageSize);
        return RestResponse.success(page);
    }

    /**
     * 获取一定时段内的统计数据的条数，参数是传月份 yyyy-MM
     * @param appId 应用ID，可为空
     * @param startTime 开始月份,不可为空
     * @param endTime 结束月份,不可为空
     * @return
     */
    @RequestMapping("/count_by_time")
    public RestResponse countByTime(String appId,String startTime,String endTime){
        String userName = getCurrentAccountUserName();
        if(StringUtils.isBlank(endTime)){
            endTime=startTime;
        }
        Long count =  consumeDayService.countByTime(userName,appId,startTime,endTime);
        return RestResponse.success(count);
    }

    /**
     * 获取一定时段内的统计数据，参数是传月份 yyyy-MM
     * @param appId 应用ID，可为空
     * @param startTime 开始月份,不可为空
     * @param endTime 结束月份,不可为空
     * @return
     */
    @RequestMapping("/list_by_time")
    public RestResponse listByTime(String appId,String startTime,String endTime,Integer pageNo ,Integer pageSize){
        String userName = getCurrentAccountUserName();
        if(StringUtils.isBlank(endTime)){
            endTime=startTime;
        }
        List<ConsumeDay> pageList =  consumeDayService.pageListByTime(userName,appId,startTime,endTime,pageNo ,pageSize);
        return RestResponse.success(pageList);
    }
}
