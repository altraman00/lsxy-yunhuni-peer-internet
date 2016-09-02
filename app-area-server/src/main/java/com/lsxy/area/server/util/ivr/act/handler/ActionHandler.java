package com.lsxy.area.server.util.ivr.act.handler;

import com.lsxy.area.server.util.ivr.act.domain.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liuws on 2016/9/2.
 */
public abstract class ActionHandler {
    public static final Logger logger = LoggerFactory.getLogger(ActionHandler.class);

    /**
     * 处理哪个动作就返回哪个，全小写
     * @return
     */
    public abstract String getAction();

    public abstract boolean handle(String callId,Action act);

}
