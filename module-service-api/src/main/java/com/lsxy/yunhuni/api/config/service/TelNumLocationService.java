package com.lsxy.yunhuni.api.config.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.config.model.TelnumLocation;

/**
 * Created by liups on 2016/10/13.
 */
public interface TelNumLocationService extends BaseService<TelnumLocation> {
    int TYPE_TELEPHONE = 1; //固话
    int TYPE_MOBILE = 2;    //手机

    /**
     * 获取号码类型
     * @param num
     * @return
     */
    int getTypeOfNum(String num);

    /**
     * 获取一个固话的区号
     * @param telephone
     * @return
     */
    String getAreaCodeOfTelephone(String telephone);

    /**
     * 获取一个手机号的区号
     * @param mobile
     * @return
     */
    String getCityOfMobile(String mobile);

}
