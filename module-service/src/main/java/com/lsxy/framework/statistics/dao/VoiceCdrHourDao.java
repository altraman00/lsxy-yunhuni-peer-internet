package com.lsxy.framework.statistics.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.statistics.model.VoiceCdrHour;

import java.io.Serializable;
import java.util.Date;

/**
 * 通话记录统计（session统计）小时统计DAO
 * Created by zhangxb on 2016/8/1.
 */
public interface VoiceCdrHourDao extends BaseDaoInterface<VoiceCdrHour,Serializable> {
    VoiceCdrHour findFirstByAppIdAndDtAndTenantIdIsNullAndTypeIsNull(String appId, Date currentHour);
}
