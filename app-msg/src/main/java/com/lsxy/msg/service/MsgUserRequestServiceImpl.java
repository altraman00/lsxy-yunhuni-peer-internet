package com.lsxy.msg.service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.msg.api.model.MsgConstant;
import com.lsxy.msg.api.model.MsgSendRecord;
import com.lsxy.msg.api.model.MsgUserRequest;
import com.lsxy.msg.api.service.MsgUserRequestService;
import com.lsxy.msg.dao.MsgUserRequestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by liups on 2017/3/1.
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class MsgUserRequestServiceImpl extends AbstractService<MsgUserRequest> implements MsgUserRequestService {
    @Autowired
    MsgUserRequestDao msgUserRequestDao;

    @Override
    public BaseDaoInterface<MsgUserRequest, Serializable> getDao() {
        return this.msgUserRequestDao;
    }

    @Override
    public Page<MsgUserRequest> getPageByCondition(Integer pageNo, Integer pageSize, String sendType,String appId,Date start, Date end, int isMass, String taskName, String mobile) {
        boolean flag = isMass==1?true:false;
        String hql = " from MsgUserRequest obj where obj.sendType=?1 and obj.isMass=?2 ";
        if(StringUtils.isNotEmpty(appId)){
            hql += " and obj.appId = '"+appId+"' ";
        }
        if(StringUtils.isNotEmpty(taskName)){
            hql += " and obj.taskName like '%"+taskName+"%' ";
        }
        if(StringUtils.isNotEmpty(mobile)){
            hql += " and obj.mobile like '%"+mobile+"%' ";
        }
        if(start !=null && end != null) {
            hql += "and obj.sendTime between ?3 and ?4 ";
            return pageList( hql, pageNo, pageSize,sendType,flag,start,end);
        }else{
            return pageList( hql,pageNo,pageSize,sendType,flag);
        }

    }

    @Override
    public void updateStateByMsgKey(String msgKey, int state) {
        msgUserRequestDao.updateStateByMsgKey(msgKey,state);
    }

    @Override
    public List<MsgUserRequest> findAwaitedRequets() {
        return null;
    }

}
