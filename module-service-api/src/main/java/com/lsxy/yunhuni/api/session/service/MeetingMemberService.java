package com.lsxy.yunhuni.api.session.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.session.model.MeetingMember;

/**
 * 会议成员
 * Created by zhangxb on 2016/7/19.
 */
public interface MeetingMemberService extends BaseService<MeetingMember> {

    MeetingMember findBySessionId(String sessionId);
}
