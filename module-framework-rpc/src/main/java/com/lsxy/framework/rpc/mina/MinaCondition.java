package com.lsxy.framework.rpc.mina;

import com.lsxy.framework.config.SystemConfig;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Created by tandy on 16/7/29.
 */
public class MinaCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return SystemConfig.getProperty("global.rpc.provider","mina").equals("mina");
    }
}
