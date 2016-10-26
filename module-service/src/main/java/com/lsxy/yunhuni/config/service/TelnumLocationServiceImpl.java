package com.lsxy.yunhuni.config.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.config.model.TelnumLocation;
import com.lsxy.yunhuni.api.config.service.TelnumLocationService;
import com.lsxy.yunhuni.config.dao.TelnumLocationDao;
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
    public String getAreaCodeOfTelephone(String telphone) {
        return telnumLocationDao.findAreaCodeByTelephone(telphone);
    }
}
