package com.lsxy.area.server;

import com.lsxy.area.api.exceptions.AppOffLineException;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.config.service.LineGatewayService;
import com.lsxy.yunhuni.api.config.service.LineGatewayToTenantService;
import com.lsxy.yunhuni.api.config.service.TelnumLocationService;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;
import com.lsxy.yunhuni.api.resourceTelenum.model.TelnumToLineGateway;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourceTelenumService;
import com.lsxy.yunhuni.api.resourceTelenum.service.TelnumToLineGatewayService;
import com.lsxy.yunhuni.api.resourceTelenum.service.TestNumBindService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by liups on 2016/10/10.
 */
@Component
public class AreaAndTelNumSelector {

    @Autowired
    TestNumBindService testNumBindService;
    @Autowired
    ResourceTelenumService resourceTelenumService;
    @Autowired
    TelnumToLineGatewayService telnumToLineGatewayService;
    @Autowired
    LineGatewayService lineGatewayService;
    @Autowired
    TelnumLocationService telnumLocationService;
    @Autowired
    LineGatewayToTenantService lineGatewayToTenantService;

    public Selector getTelnumberAndAreaId(App app, String from,String to)throws AppOffLineException{
        return getTelnumberAndAreaId(app,false,from,to,null,null);
    }

    public Selector getTelnumberAndAreaId(App app,boolean isDuoCall ,String from1,String to1,String from2,String to2) throws AppOffLineException {
        Selector selector;
        //TODO 获取号码和区域ID
        if(app.getStatus() == app.STATUS_ONLINE){
            List<LineGateway> lineGateways = lineGatewayToTenantService.findByTenantId(app.getTenant().getId());
            if(lineGateways == null || lineGateways.size() == 0){

            }

            List<ResourceTelenum> telnumber;
            if(isDuoCall){
                telnumber = resourceTelenumService.findDialingTelnumber(app,from1,from2);
                if(telnumber.get(0).getTelNumber().equals(telnumber.get(1).getTelNumber())){
                    List<TelnumToLineGateway> ttgs = telnumToLineGatewayService.getDialingLinesByNumber(telnumber.get(0).getTelNumber());
                    //查出所有的线路
                    for(TelnumToLineGateway ttg:ttgs){
                        LineGateway line = lineGatewayService.findById(ttg.getLineId());

                    }
                }else{
                    List<TelnumToLineGateway> ttgs0 = telnumToLineGatewayService.getDialingLinesByNumber(telnumber.get(0).getTelNumber());
                    List<TelnumToLineGateway> ttgs1 = telnumToLineGatewayService.getDialingLinesByNumber(telnumber.get(1).getTelNumber());
                }
            }else{
                telnumber = resourceTelenumService.findDialingTelnumber(app);
                List<TelnumToLineGateway> ttgs = telnumToLineGatewayService.getDialingLinesByNumber(telnumber.get(0).getTelNumber());
            }

            selector = new Selector(telnumber,app.getArea().getId());
        }else{
            List<String> tos = new ArrayList<>();
            if(StringUtils.isNotBlank(to1)){
                tos.add(to1);
            }
            if(StringUtils.isNotBlank(to2)){
                tos.add(to2);
            }
            List<String> testNums = testNumBindService.findNumByAppId(app.getId());
            if(testNums != null && testNums.size() > 0 && testNums.containsAll(tos)){
                //获取测试用的区域
                String areaId = SystemConfig.getProperty("area.server.test.area.id", "area001");
                ResourceTelenum telnumber = resourceTelenumService.findOneFreeDialingNumber(areaId);
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
