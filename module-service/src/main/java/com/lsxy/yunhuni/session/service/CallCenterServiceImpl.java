package com.lsxy.yunhuni.session.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.yunhuni.api.session.model.CallCenter;
import com.lsxy.yunhuni.api.session.service.CallCenterService;
import com.lsxy.yunhuni.session.dao.CallCenterDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxb on 2016/10/22.
 */
@Service
public class CallCenterServiceImpl extends AbstractService<CallCenter> implements CallCenterService {
    @Autowired
    private CallCenterDao callCenterDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public BaseDaoInterface<CallCenter, Serializable> getDao() {
        return this.callCenterDao;
    }

    @Override
    public Page<CallCenter> pList(Integer pageNo,Integer pageSize,String tenantId, String appId, String startTime, String endTime, String type,String callnum, String agent) {
        String hql = " FROM CallCenter obj where 1=1";
        if(StringUtil.isNotEmpty(tenantId)){
            hql += " AND  obj.tenantId='"+tenantId+"' ";
        }
        if(StringUtil.isNotEmpty(appId)){
            hql += " AND  obj.appId='"+appId+"' ";
        }
        if(StringUtil.isNotEmpty(startTime)){
            hql += " AND  obj.startTime='"+startTime+"' ";
        }
        if(StringUtil.isNotEmpty(endTime)){
            hql += " AND  obj.startTime='"+endTime+"' ";
        }
        if(StringUtil.isNotEmpty(type)){
            hql += " AND  obj.type='"+type+"' ";
        }
        if(StringUtil.isNotEmpty(callnum)){
            hql += " AND ( obj.fromNum='"+callnum+"' OR obj.toNum='"+callnum+"'  ) ";
        }
        if(StringUtil.isNotEmpty(agent)){
            hql += " AND  obj.agent like '%"+agent+"'%";
        }
        Page<CallCenter> page = this.pageList(pageNo,pageSize);
        return page;
    }

    @Override
    public List<CallCenter> getAllList(String tenantId, String appId, String startTime, String endTime, String type, String callnum, String agent) {
        String hql = " FROM CallCenter obj where 1=1";
        if(StringUtil.isNotEmpty(tenantId)){
            hql += " AND  obj.tenantId='"+tenantId+"' ";
        }
        if(StringUtil.isNotEmpty(appId)){
            hql += " AND  obj.appId='"+appId+"' ";
        }
        if(StringUtil.isNotEmpty(startTime)){
            hql += " AND  obj.startTime='"+startTime+"' ";
        }
        if(StringUtil.isNotEmpty(endTime)){
            hql += " AND  obj.startTime='"+endTime+"' ";
        }
        if(StringUtil.isNotEmpty(type)){
            hql += " AND  obj.type='"+type+"' ";
        }
        if(StringUtil.isNotEmpty(callnum)){
            hql += " AND ( obj.fromNum='"+callnum+"' OR obj.toNum='"+callnum+"'  ) ";
        }
        if(StringUtil.isNotEmpty(agent)){
            hql += " AND  obj.agent like '%"+agent+"'%";
        }
        List<CallCenter> list = this.list(hql);
        return list;
    }

    @Override
    public Map sum(String tenantId, String appId, String startTime, String endTime, String type,String callnum, String agent) {
        String sql = "SELECT COUNT(id) AS num,IFNULL(sum(cost),0) AS cost FROM db_lsxy_bi_yunhuni.tb_bi_call_center  WHERE  deleted=0 ";
        if(StringUtil.isNotEmpty(tenantId)){
            sql += " AND  tenant_id='"+tenantId+"' ";
        }
        if(StringUtil.isNotEmpty(appId)){
            sql += " AND  app_id='"+appId+"' ";
        }
        if(StringUtil.isNotEmpty(startTime)){
            sql += " AND  start_time='"+startTime+"' ";
        }
        if(StringUtil.isNotEmpty(endTime)){
            sql += " AND  start_time='"+endTime+"' ";
        }
        if(StringUtil.isNotEmpty(type)){
            sql += " AND  type='"+type+"' ";
        }
        if(StringUtil.isNotEmpty(callnum)){
            sql += " AND ( obj.from_num='"+callnum+"' OR obj.to_num='"+callnum+"'  ) ";
        }
        if(StringUtil.isNotEmpty(agent)){
            sql += " AND  agent like '%"+agent+"'%";
        }
        Map result = this.jdbcTemplate.queryForMap(sql);
        return result;
    }
}
