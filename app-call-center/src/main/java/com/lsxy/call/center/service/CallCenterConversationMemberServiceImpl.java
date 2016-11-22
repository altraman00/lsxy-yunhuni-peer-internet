package com.lsxy.call.center.service;

import com.lsxy.call.center.api.model.CallCenterConversationMember;
import com.lsxy.call.center.api.service.CallCenterConversationMemberService;
import com.lsxy.call.center.dao.CallCenterConversationMemberDao;
import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;
import java.io.Serializable;
import java.util.List;
/**
 * Created by liuws on 2016/11/18.
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class CallCenterConversationMemberServiceImpl extends AbstractService<CallCenterConversationMember> implements CallCenterConversationMemberService {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    private CallCenterConversationMemberDao callCenterConversationMemberDao;
    @Override
    public BaseDaoInterface<CallCenterConversationMember, Serializable> getDao() {
        return callCenterConversationMemberDao;
    }
    @Override
    public List<String> getListBySessionId(String sessionId) {
        String sql = "SELECT DISTINCT relevance_id FROM db_lsxy_bi_yunhuni.tb_bi_call_center_conversation_member  WHERE deleted=0 AND session_id=? ";
        return jdbcTemplate.queryForList(sql,String.class,sessionId);
    }
}
