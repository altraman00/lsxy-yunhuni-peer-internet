package com.lsxy.yunhuni.session.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.session.model.MeetingMember;

import java.io.Serializable;

/**
 * Created by liuws on 2016/9/6.
 */
public interface MeetingMemberDao extends BaseDaoInterface<MeetingMember, Serializable> {
    MeetingMember findFirstBySessionId(String sessionId);
}
