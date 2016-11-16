package com.lsxy.yunhuni.api.config.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.config.model.TelnumLocation;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangxb on 2016/10/25.
 */
public interface TelnumLocationService extends BaseService<TelnumLocation> {
    int TYPE_TELEPHONE = 1; //固话
    int TYPE_MOBILE = 2;    //手机
    /**
     * 获取全部省份
     * @return
     */
    List<String> getProvinceList();

    /**
     * 根据省份获取城市和区号
     * @param province
     * @return
     */
    List<Map<String,Object>> getCityAndAreaCode(String province );

    int getTypeOfNum(String num);

    String getAreaCodeOfTelephone(String telnum);

    String getAreaCodeOfMobile(String s);

    String getAreaNameByAreaCode(String areaCode);

    /**
     * 根据区号规则处理号码
     * @param num
     * @return
     */
    String solveNum(String num, String telPhoneRule,String mobileRule,String areaCode);

    /**
     * 获取号码中的城市
     * @return
     */
    List<Map<String,Object>> getCityAndAreaCodeByTelenum( );

}
