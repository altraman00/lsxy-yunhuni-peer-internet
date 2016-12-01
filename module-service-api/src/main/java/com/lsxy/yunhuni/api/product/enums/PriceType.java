package com.lsxy.yunhuni.api.product.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxb on 2016/11/22.
 */
public enum PriceType {
    type1(2,6,"秒","6秒"),
    type2(2,60,"秒","60秒"),
    type3(4,1,"G/月","G/月"),
    type4(1,1,"个","个"),
    type5(3,1,"月","月"),
    type6(5,1,"个/月","个/月");

    private Integer calType;    //1、按数量，2、按时长3、按月
    private Integer timeUnit;   //单位时长(单位秒)
    private String unit;   //单位
    private String key;//
    PriceType(Integer calType, Integer timeUnit, String unit,String key) {
        this.calType = calType;
        this.timeUnit = timeUnit;
        this.unit = unit;
        this.key = key;
    }
    public static List getPriceTypeAllByCalType(Integer calType){
        List list = new ArrayList<>();
        PriceType[] values = PriceType.values();
        for(PriceType value:values){
            if(calType==value.getCalType()) {
                Map map = new HashMap<>();
                map.put("id", value.getKey());
                map.put("name", value.getKey());
                list.add(map);
            }
        }
        return list;
    }
    public static List getPriceTypeAll(){
        List list = new ArrayList<>();
        PriceType[] values = PriceType.values();
        for(PriceType value:values){
            Map map = new HashMap<>();
            map.put("id",value.getKey());
            map.put("name",value.getKey());
            list.add(map);
        }
        return list;
    }
    public static PriceType getPriceTypeById(String id){
        PriceType[] values = PriceType.values();
        for(PriceType value:values){
            if(value.getKey().equals(id)){
                return value;
            }
        }
        return null;
    }

    public String getKey() {
        return key;
    }

    public Integer getCalType() {
        return calType;
    }

    public Integer getTimeUnit() {
        return timeUnit;
    }

    public String getUnit() {
        return unit;
    }
}
