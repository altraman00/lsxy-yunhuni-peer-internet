package com.lsxy.yunhuni.session.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.session.model.MeetingMember;
import com.lsxy.yunhuni.api.session.service.MeetingMemberService;
import com.lsxy.yunhuni.session.dao.MeetingMemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by zhangxb on 2016/7/19.
 */
@Service
public class MeetingMemberServiceImpl extends AbstractService<MeetingMember> implements MeetingMemberService {
    @Autowired
    private MeetingMemberDao meetingMemberDao;
    @Override
    public BaseDaoInterface<MeetingMember, Serializable> getDao() {
        return meetingMemberDao;
    }

    @Override
    public MeetingMember findBySessionId(String sessionId) {
        return meetingMemberDao.findFirstBySessionId(sessionId);
    }
}
