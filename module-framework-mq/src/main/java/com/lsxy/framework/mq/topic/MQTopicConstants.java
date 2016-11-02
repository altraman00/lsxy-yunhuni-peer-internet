package com.lsxy.framework.mq.topic;

import com.lsxy.framework.config.SystemConfig;

/**
 * Created by tandy on 16/9/5.
 * 由于在ons中,测试环境和生产环境的topic不能设置为一样的,所以将topic纳入配置文件进行配置
 */
public class MQTopicConstants {

    //账号相关MQ的topic
    public static final String TOPIC_APP_PORTAL = SystemConfig.getProperty("global.mq.topic.app.portal","topic_app_portal");
    public static final String TOPIC_APP_OC = SystemConfig.getProperty("global.mq.topic.app.oc","topic_app_oc");
    public static final String TOPIC_AREA_SERVER = SystemConfig.getProperty("global.mq.topic.area.server","topic.area.server");
    public static final String TOPIC_CALL_CENTER = SystemConfig.getProperty("global.mq.topic.call.center","topic.call.center");

    //测试相关topic
    public static final String TOPIC_TEST=SystemConfig.getProperty("global.mq.topic.test","yunhuni_topic_test");


}
