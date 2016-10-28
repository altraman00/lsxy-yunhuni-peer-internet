package com.lsxy.yunhuni.config.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.config.model.TelnumLocation;
import com.lsxy.yunhuni.api.config.service.TelnumLocationService;
import com.lsxy.yunhuni.config.dao.TelnumLocationDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxb on 2016/10/25.
 */
@Service
public class TelnumLocationServiceImpl extends AbstractService<TelnumLocation> implements TelnumLocationService {
    @Autowired
    TelnumLocationDao telnumLocationDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public BaseDaoInterface<TelnumLocation, Serializable> getDao() {
        return this.telnumLocationDao;
    }

    @Override
    public List<String> getProvinceList() {
        String sql = "SELECT DISTINCT province  FROM db_lsxy_bi_yunhuni.tb_oc_config_telnum_location";
        List<String> list = jdbcTemplate.queryForList(sql,String.class);
        return list;
    }

    @Override
    public List<Map<String,Object>> getCityAndAreaCode(String province) {
        String sql = "SELECT DISTINCT city as city,area_code as areaCode FROM db_lsxy_bi_yunhuni.tb_oc_config_telnum_location WHERE province='"+province+"'";
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
        return list;
    }
    @Override
    public int getTypeOfNum(String num) {
        if(num.startsWith("00") || num.length() < 11 || num.length() > 12){
            throw new RuntimeException("无效的号码");
        }
        if(num.startsWith("1") && num.length()==11){
            return TelnumLocationService.TYPE_MOBILE;
        }else{
            return TelnumLocationService.TYPE_TELEPHONE;
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
    public String getAreaCodeOfMobile(String mobile) {
        String checkNum = mobile.substring(0,7);
        TelnumLocation location = telnumLocationDao.findFirstByMobile(checkNum);
        if(location == null){
            throw new RuntimeException("无效的号码");
        }else{
            return location.getAreaCode();
        }
    }

    @Override
    public String solveNum(String num, String telPhoneRule,String mobileRule, String areaCode) {
        int typeOfNum = getTypeOfNum(num);
        switch (typeOfNum){
            //固话
            case TYPE_TELEPHONE:{
                switch (telPhoneRule){
                    //全部加区号
                    case "0":{
                        return num;
                    }
                    //被叫归属地与线路归属地一致,去掉区号
                    default:{
                        String numAreaCode = getAreaCodeOfTelephone(num);
                        if(areaCode.equals(numAreaCode)){
                            num = num.replaceFirst(numAreaCode, "");
                        }
                        return num;
                    }
                }
            }
            //手机
            case TYPE_MOBILE:{
                switch (mobileRule){
                    //全部加0
                    case "0":{
                        return "0" + num;
                    }
                    //全部不加0
                    case "1":{
                        return num;
                    }
                    //被叫归属地与线路归属地不一致，加0
                    default:{
                        String numAreaCode = getAreaCodeOfMobile(num);
                        if(!areaCode.equals(numAreaCode)){
                            num = "0" + num;
                        }
                        return num;
                    }
                }
            }
            default:{
                return num;
            }
        }
    }
}
