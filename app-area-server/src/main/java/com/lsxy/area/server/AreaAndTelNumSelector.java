package com.lsxy.area.server;

import com.lsxy.area.api.exceptions.AppOffLineException;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;
import com.lsxy.yunhuni.api.resourceTelenum.model.TelnumToLineGateway;
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



    public Selector getTelnumberAndAreaId(App app, String... to) throws AppOffLineException {
        List<String> tos = Arrays.asList(to);
        Selector selector;
        //TODO 获取号码和区域ID
        if(app.getStatus() == app.STATUS_ONLINE){
            ResourceTelenum telnumber = appService.findOneAvailableTelnumber(app);
            List<TelnumToLineGateway> dialingLineIdsByNumber = telnumToLineGatewayService.getDialingLinesByNumber(telnumber.getTelNumber());
            selector = new Selector(telnumber,app.getArea().getId());
        }else{
            List<String> testNums = testNumBindService.findNumByAppId(app.getId());
            if(testNums != null && testNums.size() > 0 && testNums.containsAll(tos)){
                //获取测试用的区域
                String areaId = SystemConfig.getProperty("area.server.test.area.id", "area001");
                ResourceTelenum telnumber = resourceTelenumService.findOneFreeNumber(areaId);
                selector = new Selector(telnumber,areaId);
            }else{
                throw new AppOffLineException();
            }
        }
        return selector;
    }


    public String getAreaId(App app){
        if(app.getStatus() == app.STATUS_ONLINE){
            return app.getArea().getId();
        }else{
            return SystemConfig.getProperty("area.server.test.area.id", "area001");
        }
    }

    public class Selector{
        private ResourceTelenum oneTelnumber;
        private String areaId;

        public Selector(ResourceTelenum oneTelnumber, String areaId) {
            this.oneTelnumber = oneTelnumber;
            this.areaId = areaId;
        }

        public ResourceTelenum getOneTelnumber() {
            return oneTelnumber;
        }

        public void setOneTelnumber(ResourceTelenum oneTelnumber) {
            this.oneTelnumber = oneTelnumber;
        }

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }
    }

}
