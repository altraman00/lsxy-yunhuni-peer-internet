package com.lsxy.msg.service;

import com.lsxy.msg.api.service.MsgSendService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by liups on 2017/3/7.
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class MsgSendServiceImpl implements MsgSendService {

    @Override
    public String sendUssd(String mobile, String tempId, String tempArgw) {
        return null;
    }

    @Override
    public String sendMassUssd(String taskName, String tempId, String tempIdArgs, String mobiles, Date sendTime) {
        return null;
    }

    @Override
    public String sendSms(String mobile, String tempId, String tempArgw) {
        return null;
    }

    @Override
    public String sendMassSms(String taskName, String tempId, String tempIdArgs, String mobiles, Date sendTime) {
        return null;
    }
}
