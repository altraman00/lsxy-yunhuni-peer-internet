package com.lsxy.yunhuni.config.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.config.model.ApiGwRedBlankNum;
import com.lsxy.yunhuni.api.config.service.ApiGwRedBlankNumService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.config.dao.ApiGwRedBlankNumDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2016/8/23.
 */
@Service
public class ApiGwRedBlankNumServiceImpl extends AbstractService<ApiGwRedBlankNum> implements ApiGwRedBlankNumService {
    @Autowired
    ApiGwRedBlankNumDao apiGwRedBlankNumDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public BaseDaoInterface<ApiGwRedBlankNum, Serializable> getDao() {
        return this.apiGwRedBlankNumDao;
    }

    @Override
    public boolean isRedNum(String number) {
        ApiGwRedBlankNum result = apiGwRedBlankNumDao.findFirstByNumberAndTypeAndStatus(number,ApiGwRedBlankNum.TYPE_RED, ApiGwRedBlankNum.STATUS_ENABLED);
        return result != null;
    }

    @Override
    public boolean isBlackNum(String number) {
        ApiGwRedBlankNum result = apiGwRedBlankNumDao.findFirstByNumberAndTypeAndStatus(number,ApiGwRedBlankNum.TYPE_BLACK, ApiGwRedBlankNum.STATUS_ENABLED);
        return result != null;
    }

    @Override
    public Page<ApiGwRedBlankNum> getPage(Integer pageNo, Integer pageSize, Integer type) {
        String hql = " FROM ApiGwRedBlankNum obj WHERE obj.deleted=0  ";
        if(type!=null){
            hql +=" AND obj.type='"+type+"' ";
        }
        return this.pageList(hql,pageNo,pageSize);
    }

    @Override
    public long findByNumber(String number) {
        String sql = " SELECT COUNT(id) FROM db_lsxy_bi_yunhuni.tb_oc_config_num_redblacklist WHERE deleted=0 AND number='"+number+"' ";
        long r = jdbcTemplate.queryForObject(sql,Long.class);
        return r;
    }
}
