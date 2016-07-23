package com.lsxy.app.portal.rest.stastistic;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.api.consume.model.ConsumeDay;
import com.lsxy.framework.api.consume.service.ConsumeDayService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * 根据时间和应用获取列表数据
     * @param appId 应用id
     * @param startTime 时间
     * @return
     */
    @RequestMapping("/list")
    public RestResponse list(String appId,String startTime){
        String userName = getCurrentAccountUserName();
        List<ConsumeDay> list =  consumeDayService.list(userName,appId,startTime);
        return RestResponse.success(list);
    }

    /**
     * 获取分页数据
     * @param appId 应用id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @return
     */
    @RequestMapping("/page")
    public RestResponse pageList(String appId,String startTime,String endTime ,Integer pageNo ,Integer pageSize){
        String userName = getCurrentAccountUserName();
        if(endTime.length()==0){endTime=startTime;}
        Page<ConsumeDay> page =  consumeDayService.pageList(userName,appId,startTime,endTime,pageNo,pageSize);
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
