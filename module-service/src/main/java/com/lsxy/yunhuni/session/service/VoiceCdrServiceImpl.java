package com.lsxy.yunhuni.session.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.yunhuni.api.session.model.CallSession;
import com.lsxy.yunhuni.api.session.model.VoiceCdr;
import com.lsxy.yunhuni.api.session.service.VoiceCdrService;
import com.lsxy.yunhuni.session.dao.VoiceCdrDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Page<VoiceCdr> pageList(Integer pageNo,Integer pageSize, Integer type,String tenantId, String time, String appId) {
        Date date1 = DateUtils.parseDate(time,"yyyy-MM-dd");
        Date date2 = DateUtils.parseDate(time+" 23:59:59","yyyy-MM-dd HH:mm:ss");
        String sql = "from db_lsxy_bi_yunhuni.tb_bi_voice_cdr where deleted=0 and type=? and tenant_id=? and app_id=? and create_time>=? and create_time<=?";
        String sqlCount = "select count(1) "+sql;
        Integer totalCount = jdbcTemplate.queryForObject(sqlCount,Integer.class,new Object[]{type,tenantId,appId,date1,date2});
        sql = "select "+StringUtil.sqlName(VoiceCdr.class)+sql+" limit ?,?";
        pageNo--;
        List rows = jdbcTemplate.queryForList(sql,new Object[]{type,tenantId,appId,pageNo*pageSize,pageSize,date1,date2});
        List list = new ArrayList();
        for(int i=0;i<rows.size();i++){
            VoiceCdr voiceCdr = new VoiceCdr();
            try {
                BeanUtils.copyProperties(voiceCdr,rows.get(i));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            list.add(voiceCdr);
        }
        Page<VoiceCdr> page = new Page((pageNo)*pageSize+1,totalCount,pageSize,list);
        return page;
    }

    @Override
    public Map sumCost( Integer type ,String tenantId, String time, String appId) {
        Date date1 = DateUtils.parseDate(time,"yyyy-MM-dd");
        Date date2 = DateUtils.parseDate(time+" 23:59:59","yyyy-MM-dd HH:mm:ss");
        String costType = " SUM(cost) as cost";
        if(CallSession.TYPE_VOICE_RECORDING==type){
            costType = " sum(record_size) as size,sum(cost) as money ";
        }
        String sql = "select "+costType+" from db_lsxy_bi_yunhuni.tb_bi_voice_cdr  where deleted=0 and type=? and tenant_id=? and app_id=?  and create_time>=? and create_time<=? ";
        Map result = this.jdbcTemplate.queryForMap(sql,new Object[]{type,tenantId,appId,date1,date2});
        return result;
    }
}
