package com.lsxy.app.portal.rest.stastistic;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.api.statistics.service.VoiceCdrHourService;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by liups on 2016/9/14.
 */
@RequestMapping("/rest/voice_cdr_hour")
@RestController
public class VoiceCdrHourController extends AbstractRestController {
    @Autowired
    VoiceCdrHourService voiceCdrHourService;

    @RequestMapping("/call_status")
    public RestResponse callStatus(){
        Account account = getCurrentAccount();
        Map map = voiceCdrHourService.calAverageCall(account.getTenant().getId());
        return RestResponse.success(map);
    }
}
