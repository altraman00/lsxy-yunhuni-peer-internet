package com.lsxy.framework.cache.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Created by liuws on 2016/11/15.
 */
public class Lua {

    private static final Logger logger = LoggerFactory.getLogger(Lua.class);

    public static final String LOCK = loadLua("/lua/lock.lua");

    public static final String LOOKUPAGENT = loadLua("/lua/lookupAgent.lua");

    public static final String LOOKUPAGENTFORIDLE = loadLua("/lua/lookupAgentForIdle.lua");

    public static final String LOKUPQUEUE = loadLua("/lua/lookupQueue.lua");

    public static final String AGENTLOGIN = loadLua("/lua/agentLogin.lua");
    /**
     * 加载lua脚本
     * @param path
     * @return
     */
    private static String loadLua(String path){
        StringBuffer lua = new StringBuffer();
        BufferedReader reader = null;
        try{
            InputStream input = Lua.class.getResourceAsStream(path);
            if(input == null){
                input = Lua.class.getClassLoader().getResourceAsStream(path);
            }
            String read;
            reader = new BufferedReader(new InputStreamReader(input, Charset.forName("UTF-8")));
            while ((read = reader.readLine()) != null) {
                if(read.trim().startsWith("--") && !read.trim().startsWith("--[[") && !read.trim().startsWith("--]]")){
                    //忽略单行注释 节省网络io
                    continue;
                }
                if(read == null || read.length() == 0){
                    //忽略空行
                    continue;
                }
                //非调试模式下不需要redis.log
                if(read.trim().startsWith("redis.log(") /*&& !logger.isDebugEnabled()*/){
                    continue;
                }
                lua.append(read).append("\n");
            }
        }catch (Throwable t){
            logger.error("lua脚本读取失败",t);
            throw new RuntimeException("lua脚本读取失败");
        }finally {
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return lua.toString();
    }
}
