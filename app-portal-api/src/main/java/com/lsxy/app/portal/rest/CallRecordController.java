package com.lsxy.app.portal.rest;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.callrecord.service.CallRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by liups on 2016/6/29.
 */
@RequestMapping("/rest/callrecord")
@RestController
public class CallRecordController extends AbstractRestController {
    @Autowired
    private CallRecordService callRecordService;

    /**
     * 查找应用当前呼叫数据
     * @throws Exception
     */
    @RequestMapping("/current_record_statistics")
    public RestResponse currentRecordStatistics(String appId) throws Exception{
        Map statistics= callRecordService.currentRecordStatistics(appId);
        return RestResponse.success(statistics);
    }

}
