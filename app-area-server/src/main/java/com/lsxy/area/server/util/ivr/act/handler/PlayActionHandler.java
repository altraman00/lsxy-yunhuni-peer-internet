package com.lsxy.area.server.util.ivr.act.handler;

import com.lsxy.area.server.util.ivr.act.domain.Action;
import org.springframework.stereotype.Component;

/**
 * Created by liuws on 2016/9/2.
 */
@Component
public class PlayActionHandler extends ActionHandler{

    @Override
    public String getAction() {
        return "play";
    }

    @Override
    public boolean handle(String callId, Action act) {
        logger.info("callId={},act={}",callId,act);
        return false;
    }
}
