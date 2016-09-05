package com.lsxy.framework.mq.topic;

import com.lsxy.framework.config.SystemConfig;

/**
 * Created by tandy on 16/9/5.
 * 由于在ons中,测试环境和生产环境的topic不能设置为一样的,所以将topic纳入配置文件进行配置
 */
public class MQTopicConstants {

    //账号相关MQ的topic
    public static final String TOPIC_FRAMEWORK_ACCOUNT= SystemConfig.getProperty("global.mq.topic.framework.account","topic_framework_account");

    //测试相关topic
    public static final String TOPIC_TEST=SystemConfig.getProperty("global.mq.topic.test","yunhuni_topic_test");


}
