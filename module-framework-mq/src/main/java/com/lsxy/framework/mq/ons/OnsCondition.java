package com.lsxy.framework.mq.ons;

import com.lsxy.framework.config.SystemConfig;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Created by Tandy on 2016/7/21.
 * 配置文件中通过配置来决定如何配置mq 的配置
 */
public class OnsCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String provider = SystemConfig.getProperty("global.mq.provider","ons");
        return provider.equals("ons");
    }
}
