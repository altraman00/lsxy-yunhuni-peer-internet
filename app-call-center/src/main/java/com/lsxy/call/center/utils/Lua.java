package com.lsxy.call.center.utils;

import com.lsxy.call.center.service.AgentSkillServiceImpl;
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
    private static final Logger logger = LoggerFactory.getLogger(AgentSkillServiceImpl.class);

    public static final String LOOKUPAGENT = loadLua("/lua/lookupAgent.lua");

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
