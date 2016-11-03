package com.lsxy.area.server;

import com.lsxy.area.api.exceptions.AppOffLineException;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.config.model.Area;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        List<TelnumSortEntity> toNum = new ArrayList<>();
        List<TelnumSortEntity> to1Num = new ArrayList<>();
        List<TelnumSortEntity> to2Num = new ArrayList<>();
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
                    addToTelnumSortEntity(to1, to2, lineGateways, to1Num, to2Num, callTelnumber);
                }else{
                    //当两个呼出号码不一样时，查两次线路
                    addToTelnumSortEntity(to1, lineGateways, to1Num, telnumber.get(0));
                    //组装数据
                    addToTelnumSortEntity(to2, lineGateways, to2Num, telnumber.get(1));
                }
            }else{
                telnumber = resourceTelenumService.findDialingTelnumber(lineIds,app,from1);
                addToTelnumSortEntity(to1, lineGateways, toNum, telnumber.get(0));
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
                String areaId = null;
                Area area = app.getArea();
                if(area != null){
                    areaId = area.getId();
                }
                if(StringUtils.isBlank(areaId)){
                    areaId = SystemConfig.getProperty("area.server.test.area.id", "area001");
                }
                String testNum = SystemConfig.getProperty("portal.test.call.number");
                if(StringUtils.isNotBlank(testNum)){
                    callNum = resourceTelenumService.findByTelNumber(testNum);
                }
                if(callNum == null){
                    throw new RuntimeException("没有可用测试号码");
                }
                List<LineGateway> lineGateways = lineGatewayToPublicService.findAllLineGatewayByAreaId(areaId);
                if(isDuoCall){
                    addToTelnumSortEntity(to1, to2, lineGateways, to1Num, to2Num, callNum);
                }else{
                    addToTelnumSortEntity(to1, lineGateways, toNum, callNum);
                }
            }else{
                throw new AppOffLineException();
            }
        }
        selector = new Selector(app.getArea().getId(),toNum,to1Num,to2Num);
        return selector;
    }

    private void addToTelnumSortEntity(String to1, String to2, List<LineGateway> lineGateways, List<TelnumSortEntity> to1Num, List<TelnumSortEntity> to2Num, ResourceTelenum callTelnumber) {
        List<TelnumToLineGateway> ttgs = telnumToLineGatewayService.getDialingLinesByNumber(callTelnumber.getTelNumber());
        //租户拥有的线路和号码能呼出的线路进行一次交集计算，并组装数据
        lineGateways.parallelStream().forEach(lg -> {
            Optional<TelnumToLineGateway> first = ttgs.parallelStream().filter(ttg -> ttg.getLineId().equals(lg.getId())).findFirst();
            if(first.isPresent()){
                TelnumToLineGateway telnumToLineGateway = first.get();
                TelnumSortEntity entity1 = getTelnumSortEntity(to1, callTelnumber, lg, telnumToLineGateway);
                to1Num.add(entity1);
                TelnumSortEntity entity2 = getTelnumSortEntity(to2, callTelnumber, lg, telnumToLineGateway);
                to2Num.add(entity2);
            }
        });
    }

    private void addToTelnumSortEntity(String to, List<LineGateway> lineGateways, List<TelnumSortEntity> to1Num, ResourceTelenum telenum) {
        List<TelnumToLineGateway> ttgs = telnumToLineGatewayService.getDialingLinesByNumber(telenum.getTelNumber());
        //租户拥有的线路和号码能呼出的线路进行一次交集计算，并组装数据
        lineGateways.parallelStream().forEach(lg -> {
            Optional<TelnumToLineGateway> first = ttgs.parallelStream().filter(ttg -> ttg.getLineId().equals(lg.getId())).findFirst();
            if(first.isPresent()){
                TelnumToLineGateway telnumToLineGateway = first.get();
                TelnumSortEntity entity = getTelnumSortEntity(to, telenum, lg, telnumToLineGateway);
                to1Num.add(entity);
            }
        });
    }

    private TelnumSortEntity getTelnumSortEntity(String to, ResourceTelenum svTelnumber, LineGateway lg, TelnumToLineGateway telnumToLineGateway) {
        TelnumSortEntity entity = null;
        if("1".equals(telnumToLineGateway.getIsDialing())){
            //主叫
            entity = new TelnumSortEntity(svTelnumber.getCallUri(),
                    telnumLocationService.solveNum(to,lg.getTelAreaRule(),lg.getMobileAreaRule(),lg.getAreaCode()),
                    lg.getSipProviderDomain(),lg.getSipProviderIp(),lg.getPriority());
        }else if("1".equals(telnumToLineGateway.getIsThrough())){
            //透传
            entity = new TelnumSortEntity(svTelnumber.getTelNumber(),
                    telnumLocationService.solveNum(to,lg.getTelAreaRule(),lg.getMobileAreaRule(),lg.getAreaCode()),
                    lg.getSipProviderDomain(),lg.getSipProviderIp(),lg.getPriority());
        }
        return entity;
    }


    public String getAreaId(App app){
        String areaId = null;
        Area area = app.getArea();
        if(area != null){
            areaId = area.getId();
        }
        if(StringUtils.isBlank(areaId)){
            areaId = SystemConfig.getProperty("area.server.test.area.id", "area001");
        }
        return areaId;
    }

    //排序接口
    public static List<TelnumFormat> sortTelnumSortEntity(List<TelnumSortEntity> list){
        List<TelnumFormat> result = new ArrayList();
        List<TelnumSortEntity> entities = list.parallelStream().sorted((s1, s2) -> s1.getPriority().compareTo(s2.getPriority())).collect(Collectors.toList());
        for(TelnumSortEntity entity:entities){
            result.add(entity.getTelnumFormat());
        }
        return result;
    }


    public static class TelnumSortEntity{
        private Integer priority;
        private TelnumFormat telnumFormat;

        public TelnumSortEntity(String from, String to, String domain, String proxy, Integer priority) {
            this.telnumFormat = new TelnumFormat(from, to, domain, proxy);
            this.priority = priority;
        }

        public Integer getPriority() {
            return priority;
        }

        public TelnumFormat getTelnumFormat() {
            return telnumFormat;
        }
    }

    public static class TelnumFormat{
        protected String from;
        protected String to;
        protected String domain;
        protected String proxy;

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

    public static class Selector{
        private String areaId;
        private List<TelnumFormat> toNum = new ArrayList<>();
        private List<TelnumFormat> to1Num = new ArrayList<>();
        private List<TelnumFormat> to2Num = new ArrayList<>();

        public Selector() {
        }

        public Selector(String areaId, List<TelnumSortEntity> toNum, List<TelnumSortEntity> to1Num, List<TelnumSortEntity> to2Num) {
            this.areaId = areaId;
            this.toNum = sortTelnumSortEntity(toNum);
            this.to1Num = sortTelnumSortEntity(to1Num);
            this.to2Num = sortTelnumSortEntity(to2Num);
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

        public String getOneTelnumber() {
            String telNum = null;
            if(this.toNum != null && toNum.size()> 0){
                telNum = toNum.get(0).getFrom();
            }
            if(StringUtils.isBlank(telNum)){
                if(this.to1Num != null && to1Num.size()> 0){
                    telNum = to1Num.get(0).getFrom();
                }
            }
            return telNum;
        }
        public String getToUri(){
            String toUri = null;
            if(this.toNum != null && toNum.size()> 0){
                toUri = toNum.get(0).getTo() + "@" + toNum.get(0).getDomain();
            }
            return toUri;
        }
        public String getTo1Uri(){
            String toUri = null;
            if(this.to1Num != null && to1Num.size()> 0){
                toUri = to1Num.get(0).getTo() + "@" + to1Num.get(0).getDomain();
            }
            return toUri;
        }
        public String getTo2Uri(){
            String toUri = null;
            if(this.to2Num != null && to2Num.size()> 0){
                toUri = to2Num.get(0).getTo() + "@" + to2Num.get(0).getDomain();
            }
            return toUri;
        }
    }


}
