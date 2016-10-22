package com.lsxy.yunhuni.session.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.yunhuni.api.session.model.CallCenter;
import com.lsxy.yunhuni.api.session.service.CallCenterService;
import com.lsxy.yunhuni.session.dao.CallCenterDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * Created by zhangxb on 2016/10/22.
 */
@Service
public class CallCenterServiceImpl extends AbstractService<CallCenter> implements CallCenterService {
    @Autowired
    private CallCenterDao callCenterDao;
    @Override
    public BaseDaoInterface<CallCenter, Serializable> getDao() {
        return this.callCenterDao;
    }

    @Override
    public Page<CallCenter> pList(Integer pageNo,Integer pageSize,String tenantId, String appId, String startTime, String endTime, String type, String agent) {
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
            hql += " AND  obj.endTime='"+endTime+"' ";
        }
        if(StringUtil.isNotEmpty(type)){
            hql += " AND  obj.type='"+type+"' ";
        }
        if(StringUtil.isNotEmpty(agent)){
            hql += " AND  obj.agent like '%"+agent+"'%";
        }
        Page<CallCenter> page = this.pageList(pageNo,pageSize);
        return null;
    }
}
