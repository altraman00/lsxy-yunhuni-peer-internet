package com.lsxy.call.center.api.model;

import java.util.Date;

/**
 * 分机状态(redis)
 * Created by liuws on 2016/11/7.
 */
public class ExtensionStatus {

    private Integer lastAction;//    last_action          int comment '最近的动作',
    private Date lastActionTime;//    last_action_time     datetime comment '最近的动作发生的时间',
    private Date lastRegisterTime;//    last_register_time   datetime comment '最近注册成功的时间。注册成功后超过register_expires无新的成功注册，视为注册超时，当离线处理。',
    private Integer lastRegisterStatus;//    last_register_status int comment '最近注册状态 2xx: 成功。0表示没有任何注册',
    private Integer registerExpires;//    register_expires     int comment '注册超过该时间后，需要重新中注册。该值应出现在SIP服务器返回的Register回复消息的Expires头域中。',

}
