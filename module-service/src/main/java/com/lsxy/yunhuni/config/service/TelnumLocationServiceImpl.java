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
    public String getAreaNameByAreaCode(String areaCode) {
        String sql = "SELECT DISTINCT IFNULL(city,'') FROM db_lsxy_bi_yunhuni.tb_oc_config_telnum_location WHERE area_code='"+areaCode+"'";
        String result = null;
        try {
            result = jdbcTemplate.queryForObject(sql, String.class);
        }catch (Exception e){}//捕获空指针异常
        return result;
    }
}
