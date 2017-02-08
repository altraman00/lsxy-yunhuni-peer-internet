package com.lsxy.call.center.utils;

import com.lsxy.framework.core.utils.LuaUtils;

/**
 * Created by liuws on 2016/11/15.
 */
public class Lua {

    public static final String LOOKUPAGENTLRU = LuaUtils.load("/lua/lookupAgentLRU.lua");

    public static final String LOOKUPAGENTRANDOM = LuaUtils.load("/lua/lookupAgentRANDOM.lua");

    public static final String LOOKUPAGENTFORIDLE = LuaUtils.load("/lua/lookupAgentForIdle.lua");

    public static final String LOKUPQUEUE = LuaUtils.load("/lua/lookupQueue.lua");

    public static final String AGENTLOGIN = LuaUtils.load("/lua/agentLogin.lua");

}
