package com.lsxy.framework.sms.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.sms.model.SMSSendLog;
import com.lsxy.framework.api.sms.service.SMSSendLogService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.sms.dao.SMSSendLogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by Tandy on 2016/6/24.
 */
@Service
public class SMSSendLogServiceImpl extends AbstractService<SMSSendLog> implements SMSSendLogService {

    @Autowired
    private SMSSendLogDao smsSendLogDao;

    @Override
    public BaseDaoInterface<SMSSendLog, Serializable> getDao() {
        return smsSendLogDao;
    }

    @Override
    public Page<SMSSendLog> findByMobile(String mobile, int pageNo, int pageSize) {
        String hql = "from SMSSendLog obj where obj.sendTo=?1 order by obj.createTime desc";
        return this.findByCustom(hql,true,pageNo,pageSize,mobile);
    }
}
