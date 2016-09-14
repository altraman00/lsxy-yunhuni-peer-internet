package com.lsxy.area.server.util.ivr.act.handler;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liuws on 2016/9/2.
 */
public abstract class ActionHandler {
    public static final Logger logger = LoggerFactory.getLogger(ActionHandler.class);

    /**
     * 忽略大小写
     * @return
     */
    public abstract String getAction();

    public abstract boolean handle(String callId,Element root,String next);

}
