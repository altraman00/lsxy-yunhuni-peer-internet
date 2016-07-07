package com.lsxy.app.portal.rest;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.api.consume.model.ConsumeMonth;
import com.lsxy.framework.api.consume.service.ConsumeMonthService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 消费月统计
 * Created by zhangxb on 2016/7/6.
 */
@RequestMapping("/rest/consume_month")
@RestController
public class ConsumeMonthController extends AbstractRestController {
    @Autowired
    ConsumeMonthService consumeMonthService;
    /**
     * 根据时间和应用获取列表数据
     * @param appId 应用id
     * @param startTime 时间
     * @return
     */
    @RequestMapping("/list")
    public RestResponse list(String appId,String startTime){
        String userName = getCurrentAccountUserName();
        List<ConsumeMonth> list =  consumeMonthService.list(userName,appId,startTime);
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
        Page<ConsumeMonth> page =  consumeMonthService.pageList(userName,appId,startTime,endTime,pageNo,pageSize);
        return RestResponse.success(page);
    }
}
