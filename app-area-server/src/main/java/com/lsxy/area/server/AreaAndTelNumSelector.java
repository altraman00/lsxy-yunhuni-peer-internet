package com.lsxy.area.server;

import com.lsxy.area.api.exceptions.AppOffLineException;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourceTelenumService;
import com.lsxy.yunhuni.api.resourceTelenum.service.TelnumToLineGatewayService;
import com.lsxy.yunhuni.api.resourceTelenum.service.TestNumBindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by liups on 2016/10/10.
 */
@Component
public class AreaAndTelNumSelector {

    @Autowired
    private AppService appService;

    @Autowired
    TestNumBindService testNumBindService;

    @Autowired
    ResourceTelenumService resourceTelenumService;

    @Autowired
    TelnumToLineGatewayService telnumToLineGatewayService;

    public Map<String, String> getTelnumberAndAreaId(App app, String... to) throws AppOffLineException {
        List<String> tos = Arrays.asList(to);
        Map<String,String> result = new HashMap<>();
        //TODO 获取号码和区域ID
        if(app.getStatus() == app.STATUS_ONLINE){
            result.put("oneTelnumber",appService.findOneAvailableTelnumber(app));
            result.put("areaId",app.getArea().getId());
        }else{
            List<String> testNums = testNumBindService.findNumByAppId(app.getId());
            if(testNums != null && testNums.size() > 0 && testNums.containsAll(tos)){
                //获取测试用的区域
                String areaId = SystemConfig.getProperty("area.server.test.area.id", "area001");
                String oneTelnumber = resourceTelenumService.findOneFreeNumber(areaId);
                result.put("oneTelnumber",oneTelnumber);
                result.put("areaId", areaId);
            }else{
                throw new AppOffLineException();
            }
        }
        return result;
    }

    public String getAreaId(App app){
        if(app.getStatus() == app.STATUS_ONLINE){
            return app.getArea().getId();
        }else{
            return SystemConfig.getProperty("area.server.test.area.id", "area001");
        }
    }

}
