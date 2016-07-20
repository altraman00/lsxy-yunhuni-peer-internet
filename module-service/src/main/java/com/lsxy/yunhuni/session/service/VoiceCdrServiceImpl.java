package com.lsxy.yunhuni.session.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.framework.core.utils.EntityUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.session.model.VoiceCdr;
import com.lsxy.yunhuni.api.session.service.VoiceCdrService;
import com.lsxy.yunhuni.session.dao.VoiceCdrDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxb on 2016/7/19.
 */
@Service
public class VoiceCdrServiceImpl extends AbstractService<VoiceCdr> implements  VoiceCdrService{
    @Autowired
    private VoiceCdrDao voiceCdrDao;
    @Override
    public BaseDaoInterface<VoiceCdr, Serializable> getDao() {
        return voiceCdrDao;
    }


    @Override
    public Page<VoiceCdr> pageList(Integer pageNo,Integer pageSize, Integer type,String tenantId, String time, String appId) {
        Page<VoiceCdr> page = null;
        String hql = "select sum(cost) from db_lsxy_bi_yunhuni.tb_bi_voice_cdr ";
        String sql = "select count(1) from db_lsxy_bi_yunhuni.tb_bi_voice_cdr ";
        Integer totalCount = this.queryForObject(sql,Integer.class,new Object[]{});
        sql = "select * from db_lsxy_bi_yunhuni.tb_bi_voice_cdr ";
        List rows = this.getJdbcTemplate().queryForList(sql);
        List list = new ArrayList();
        for(int i=0;i<rows.size();i++){
            VoiceCdr voiceCdr = new VoiceCdr();
            try {
                EntityUtils.copyProperties(voiceCdr,rows.get(i));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            list.add(voiceCdr);
        }
       // Page page = new Page((pageNo)*pageSize+1,totalCount,pageSize,list);

        return page;
    }

    @Override
    public BigDecimal sumCost( Integer type ,String tenantId, String time, String appId) {
        String sql = "select sum(cost) from db_lsxy_bi_yunhuni.tb_bi_voice_cdr ";
        BigDecimal result = this.queryForObject(sql,BigDecimal.class,new Object[]{});
        return result;
    }
}
