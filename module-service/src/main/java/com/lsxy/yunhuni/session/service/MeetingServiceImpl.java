package com.lsxy.yunhuni.session.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.session.model.Meeting;
import com.lsxy.yunhuni.api.session.service.MeetingService;
import com.lsxy.yunhuni.session.dao.MeetingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by zhangxb on 2016/7/19.
 */
@Service
public class MeetingServiceImpl extends AbstractService<Meeting> implements MeetingService {
    @Autowired
    private MeetingDao meetingDao;
    @Override
    public BaseDaoInterface<Meeting, Serializable> getDao() {
        return meetingDao;
    }

}
