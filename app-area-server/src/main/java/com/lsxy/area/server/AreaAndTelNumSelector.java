package com.lsxy.area.server;

import com.lsxy.area.api.exceptions.AppOffLineException;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.model.AppOnlineAction;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.config.service.LineGatewayService;
import com.lsxy.yunhuni.api.config.service.LineGatewayToPublicService;
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
import java.util.stream.Collectors;

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
    @Autowired
    LineGatewayToPublicService lineGatewayToPublicService;

    public Selector getTelnumberAndAreaId(App app, String from,String to)throws AppOffLineException{
        return getTelnumberAndAreaId(app,false,from,to,null,null);
    }

    public Selector getTelnumberAndAreaId(App app,boolean isDuoCall ,String from1,String to1,String from2,String to2) throws AppOffLineException {
        Selector selector;
        //TODO 获取号码和区域ID
        List<TelnumFormat> toNum = new ArrayList<>();
        List<TelnumFormat> to1Num = new ArrayList<>();
        List<TelnumFormat> to2Num = new ArrayList<>();
        if(app.getStatus() == app.STATUS_ONLINE){
            //查找租户私有线路
            List<LineGateway> lineGateways = lineGatewayToTenantService.findByTenantIdAndAreaId(app.getTenant().getId(),app.getArea().getId());
            if(lineGateways == null || lineGateways.size() == 0){
                //如果没有私有线路，找公共线路
                lineGateways = lineGatewayToPublicService.findAllLineGatewayByAreaId(app.getArea().getId());
            }
            if(lineGateways == null || lineGateways.size() == 0){
                //TODO 没有线路，则抛出异常
                throw new RuntimeException("没有可用线路");
            }
            //所拥有的线路ID列表
            List<String> lineIds = lineGateways.parallelStream().map(LineGateway::getId).collect(Collectors.toList());

            List<ResourceTelenum> telnumber;
            if(isDuoCall){
                //获取一个可呼出的号码
                telnumber = resourceTelenumService.findDialingTelnumber(lineIds,app,from1,from2);
                if(telnumber.get(0).getTelNumber().equals(telnumber.get(1).getTelNumber())){
                    ResourceTelenum callTelnumber = telnumber.get(0);
                    //当两个呼出号码一样时，查一次线路就够了
                    //查出所有的线路
                    //组装数据
                    addToTelnumFormat(to1, to2, lineGateways, to1Num, to2Num, callTelnumber);
                }else{
                    //当两个呼出号码不一样时，查两次线路
                    addToTelnumFormat(to1, lineGateways, to1Num, telnumber.get(0));
                    //组装数据
                    addToTelnumFormat(to2, lineGateways, to2Num, telnumber.get(1));
                }
            }else{
                telnumber = resourceTelenumService.findDialingTelnumber(lineIds,app,from1);
                addToTelnumFormat(to1, lineGateways, toNum, telnumber.get(0));
            }

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
                ResourceTelenum callNum = null;
                //获取测试用的区域
                String areaId = SystemConfig.getProperty("area.server.test.area.id", "area001");
                String testNum = SystemConfig.getProperty("portal.test.call.number");
                if(StringUtils.isNotBlank(testNum)){
                    callNum = resourceTelenumService.findByTelNumber(testNum);
                }
                if(callNum == null){
                    throw new RuntimeException("没有可用测试号码");
                }
                List<LineGateway> lineGateways = lineGatewayToPublicService.findAllLineGatewayByAreaId(areaId);
                if(isDuoCall){
                    addToTelnumFormat(to1, to2, lineGateways, to1Num, to2Num, callNum);
                }else{
                    addToTelnumFormat(to1, lineGateways, toNum, callNum);
                }
            }else{
                throw new AppOffLineException();
            }
        }
        selector = new Selector(app.getArea().getId(),toNum,to1Num,to2Num);
        return selector;
    }

    private void addToTelnumFormat(String to1, String to2, List<LineGateway> lineGateways, List<TelnumFormat> to1Num, List<TelnumFormat> to2Num, ResourceTelenum callTelnumber) {
        List<TelnumToLineGateway> ttgs = telnumToLineGatewayService.getDialingLinesByNumber(callTelnumber.getTelNumber());
        //租户拥有的线路和号码能呼出的线路进行一次交集计算，并组装数据
        for(LineGateway lg:lineGateways){
            Optional<TelnumToLineGateway> first = ttgs.parallelStream().filter(ttg -> ttg.getLineId().equals(lg.getId())).findFirst();
            if(first.isPresent()){
                TelnumToLineGateway telnumToLineGateway = first.get();
                TelnumFormat telnumFormat1 = getTelnumFormat(to1, callTelnumber, lg, telnumToLineGateway);
                to1Num.add(telnumFormat1);
                TelnumFormat telnumFormat2 = getTelnumFormat(to2, callTelnumber, lg, telnumToLineGateway);
                to2Num.add(telnumFormat2);
            }
        }
    }

    private void addToTelnumFormat(String to, List<LineGateway> lineGateways, List<TelnumFormat> to1Num, ResourceTelenum telenum) {
        List<TelnumToLineGateway> ttgs = telnumToLineGatewayService.getDialingLinesByNumber(telenum.getTelNumber());
        //租户拥有的线路和号码能呼出的线路进行一次交集计算，并组装数据
        for(LineGateway lg:lineGateways){
            Optional<TelnumToLineGateway> first = ttgs.parallelStream().filter(ttg -> ttg.getLineId().equals(lg.getId())).findFirst();
            if(first.isPresent()){
                TelnumToLineGateway telnumToLineGateway = first.get();
                TelnumFormat telnumFormat = getTelnumFormat(to, telenum, lg, telnumToLineGateway);
                to1Num.add(telnumFormat);
            }
        }
    }

    private TelnumFormat getTelnumFormat(String to, ResourceTelenum svTelnumber, LineGateway lg, TelnumToLineGateway telnumToLineGateway) {
        TelnumFormat telnumFormat1 = null;
        if("1".equals(telnumToLineGateway.getIsDialing())){
            //主叫
            telnumFormat1 = new TelnumFormat(svTelnumber.getCallUri(),
                    telnumLocationService.solveNum(to,lg.getTelAreaRule(),lg.getMobileAreaRule(),lg.getAreaCode()),
                    lg.getSipProviderDomain(),lg.getSipProviderIp());
        }else if("1".equals(telnumToLineGateway.getIsThrough())){
            //透传
            telnumFormat1 = new TelnumFormat(svTelnumber.getTelNumber(),
                    telnumLocationService.solveNum(to,lg.getTelAreaRule(),lg.getMobileAreaRule(),lg.getAreaCode()),
                    lg.getSipProviderDomain(),lg.getSipProviderIp());
        }
        return telnumFormat1;
    }


    public String getAreaId(App app){
        if(app.getStatus() == app.STATUS_ONLINE){
            return app.getArea().getId();
        }else{
            return SystemConfig.getProperty("area.server.test.area.id", "area001");
        }
    }

    public class TelnumFormat{
        private String from;
        private String to;
        private String domain;
        private String proxy;

        public TelnumFormat() {
        }

        public TelnumFormat(String from, String to, String domain, String proxy) {
            this.from = from;
            this.to = to;
            this.domain = domain;
            this.proxy = proxy;
        }

        public String getFrom() {
            return from;
        }

        public String getTo() {
            return to;
        }

        public String getDomain() {
            return domain;
        }

        public String getProxy() {
            return proxy;
        }
    }

    public class Selector{
        private String areaId;
        private List<TelnumFormat> toNum = new ArrayList<>();
        private List<TelnumFormat> to1Num = new ArrayList<>();
        private List<TelnumFormat> to2Num = new ArrayList<>();

        public Selector() {
        }

        public Selector(String areaId, List<TelnumFormat> toNum, List<TelnumFormat> to1Num, List<TelnumFormat> to2Num) {
            this.areaId = areaId;
            this.toNum = toNum;
            this.to1Num = to1Num;
            this.to2Num = to2Num;
        }

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        public List<TelnumFormat> getToNum() {
            return toNum;
        }

        public void setToNum(List<TelnumFormat> toNum) {
            this.toNum = toNum;
        }

        public List<TelnumFormat> getTo1Num() {
            return to1Num;
        }

        public void setTo1Num(List<TelnumFormat> to1Num) {
            this.to1Num = to1Num;
        }

        public List<TelnumFormat> getTo2Num() {
            return to2Num;
        }

        public void setTo2Num(List<TelnumFormat> to2Num) {
            this.to2Num = to2Num;
        }

        public ResourceTelenum getOneTelnumber() {
            return new ResourceTelenum();
        }
    }

}
