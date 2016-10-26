package com.lsxy.yunhuni.api.config.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.config.model.TelnumLocation;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangxb on 2016/10/25.
 */
public interface TelnumLocationService extends BaseService<TelnumLocation> {
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
}
