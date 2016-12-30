package com.lsxy.yunhuni.session.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.session.model.CallSession;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;

/**
 * 会话查询类
 * Created by liups on 2016/6/29.
 */
public interface CallSessionDao extends BaseDaoInterface<CallSession, Serializable> {
    /**
     * 根据状态获取应用的会话条数
     * @param status 会话状态
     * @param appId 应用ID
     * @return 条数
     */
    Long countByStatusAndAppId(int status,String appId);

    @Modifying
    @Query("update CallSession call set call.status=:status where relevanceId=:relevanceId")
    public void updateStatusByRelevanceId(@Param("relevanceId") String relevanceId, @Param("status") Integer status);
}
