package com.lsxy.yunhuni.config.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.config.model.TelnumLocation;
import com.lsxy.yunhuni.api.config.service.TelNumLocationService;
import com.lsxy.yunhuni.config.dao.TelnumLocationDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2016/10/13.
 */
@Service
public class TelNumLocationServiceImpl extends AbstractService<TelnumLocation> implements TelNumLocationService {
    @Autowired
    TelnumLocationDao telnumLocationDao;
    @Override
    public BaseDaoInterface<TelnumLocation, Serializable> getDao() {
        return this.telnumLocationDao;
    }

    @Override
    public int getTypeOfNum(String num) {
        if(num.startsWith("00") || num.length() < 11 || num.length() > 12){
            throw new RuntimeException("无效的号码");
        }
        if(num.startsWith("1") && num.length()==11){
            return TelNumLocationService.TYPE_MOBILE;
        }else{
            return TelNumLocationService.TYPE_TELEPHONE;
        }
    }

    @Override
    public String getAreaCodeOfTelephone(String telephone) {
        String areaCode = telnumLocationDao.findAreaCodeByTelephone(telephone);
        if(StringUtils.isBlank(areaCode)){
            throw new RuntimeException("无效的号码");
        }else{
            String shortTel = telephone.replace(areaCode, "");
            if(shortTel.length() < 7 || shortTel.length() > 8){
                throw new RuntimeException("无效的号码");
            }
            return areaCode;
        }
    }

    @Override
    public String getCityOfMobile(String mobile) {
        String checkNum = mobile.substring(0,7);
        TelnumLocation location = telnumLocationDao.findFirstByMobile(checkNum);
        if(location == null){
            throw new RuntimeException("无效的号码");
        }else{
            return location.getCity();
        }
    }


}
