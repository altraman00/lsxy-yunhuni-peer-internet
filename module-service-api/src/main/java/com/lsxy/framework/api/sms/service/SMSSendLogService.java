package com.lsxy.framework.api.sms.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.sms.model.SMSSendLog;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.utils.Page;

public interface SMSSendLogService extends BaseService<SMSSendLog> {


    /**
     * 根据手机号查询短信发送记录
     * @param mobile  手机号
     * @param pageNo    页号
     * @param pageSize  页大小
     */
    Page<SMSSendLog> findByMobile(String mobile, int pageNo, int pageSize);
}
