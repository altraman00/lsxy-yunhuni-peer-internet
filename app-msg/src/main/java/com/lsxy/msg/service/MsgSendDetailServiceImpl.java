package com.lsxy.msg.service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.msg.api.model.MsgSendDetail;
import com.lsxy.msg.api.service.MsgSendDetailService;
import com.lsxy.msg.dao.MsgSendDetailDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liups on 2017/3/1.
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class MsgSendDetailServiceImpl extends AbstractService<MsgSendDetail> implements MsgSendDetailService {
    @Autowired
    MsgSendDetailDao msgSendDetailDao;

    @Override
    public BaseDaoInterface<MsgSendDetail, Serializable> getDao() {
        return this.msgSendDetailDao;
    }

    @Override
    public Page<MsgSendDetail> getPageByContiton(Integer pageNo, Integer pageSize, String msgKey, String mobile, String state) {
        String hql = " from MsgSendDetail obj where obj.msgKey=?1 ";
        if(StringUtils.isNotEmpty(mobile)){
            hql += " and obj.mobile like '%"+mobile+"%' ";
        }
        if(StringUtils.isNotEmpty(state)){
            hql += " and obj.state = '"+state+"' ";
        }
        return pageList(hql,pageNo,pageSize,msgKey);
    }

    @Override
    public List<MsgSendDetail> findByMsgKey(String msgKey) {
        return msgSendDetailDao.findByMsgKey(msgKey);
    }
}
