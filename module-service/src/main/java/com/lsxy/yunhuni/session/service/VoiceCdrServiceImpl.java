package com.lsxy.yunhuni.session.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.session.model.VoiceCdr;
import com.lsxy.yunhuni.api.session.service.VoiceCdrService;
import com.lsxy.yunhuni.session.dao.VoiceCdrDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by zhangxb on 2016/7/19.
 */
public class VoiceCdrServiceImpl extends AbstractService<VoiceCdr> implements  VoiceCdrService{
    @Autowired
    VoiceCdrDao voiceCdrDao;
    @Override
    public BaseDaoInterface<VoiceCdr, Serializable> getDao() {
        return voiceCdrDao;
    }


    @Override
    public Page<VoiceCdr> pList(Integer pageNo,Integer pageSize,String tenantId, Integer type, String time, String appId) {
        String hql = "from VoiceCdr obj where obj.type=?1 and obj.tenantId=?2 and obj.appId=?3 ";
        Page<VoiceCdr> page =  this.pageList(hql,pageNo,pageSize,type,tenantId,appId);
        return page;
    }

    @Override
    public BigDecimal sumCost(String tenantId, Integer type, String time, String appId) {
        BigDecimal result = new BigDecimal("0");
        String hql = "select sum(obj.cost) from VoiceCdr obj where obj.deleted=0 and obj.type=?1 and obj.tenantId=?2 and obj.appId=?3 ";
        Query query = this.getEm().createQuery(hql);
        query.setParameter(1,type);
        query.setParameter(2,tenantId);
        query.setParameter(3,appId);
        Object obj = query.getSingleResult();
        if(!StringUtils.isEmpty(String.valueOf(obj))){
            result = new BigDecimal(String.valueOf(obj));
        }
        return result;
    }
}
