package com.lsxy.app.portal.rest.cost;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.yunhuni.api.consume.model.Consume;
import com.lsxy.yunhuni.api.consume.service.ConsumeService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消费记录
 * Created by zhangxb on 2016/7/8.
 */
@RequestMapping("/rest/consume")
@RestController
public class ConsumeController extends AbstractRestController {
    @Autowired
    ConsumeService consumeService;
    /**
     * 获取分页数据
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @RequestMapping("/page")
    public RestResponse pageList(Integer pageNo , Integer pageSize,String startTime,String endTime){
        String userName = getCurrentAccountUserName();
        Page<Consume> page =  consumeService.pageList(userName,pageNo,pageSize,startTime,endTime);
        return RestResponse.success(page);
    }
}
