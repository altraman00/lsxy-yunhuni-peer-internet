package com.lsxy.app.mc.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 * Created by tandy on 17/1/6.
 */
public class RunShellUtil {
    private static final Logger logger = LoggerFactory.getLogger(RunShellUtil.class);

    /**
     * 执行shell脚本
     * @param scriptFilePath
     * @return
     * @throws ShellExecuteException
     */
    public static String run(String scriptFilePath,int timeOutSeconds) throws ShellExecuteException {
        String result = null;
        try {
            if(logger.isDebugEnabled()){
                logger.debug("execute shell : {}",scriptFilePath);
            }
            long d1 = System.currentTimeMillis();
//            String shpath="/home/felven/word2vec/demo-classes.sh";
            Process ps = Runtime.getRuntime().exec(scriptFilePath);
            if(logger.isDebugEnabled()){
                logger.debug("executed command and waiting...");
            }
            ps.waitFor(timeOutSeconds, TimeUnit.SECONDS);

            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            result = sb.toString();
            if(logger.isDebugEnabled()){
                logger.debug("execute shell : {}  down, spend {} ms",scriptFilePath,System.currentTimeMillis()-d1);
            }
        }
        catch (Exception e) {
            logger.error("执行脚本出现异常："+scriptFilePath,e);
            throw new ShellExecuteException(scriptFilePath,e);
        }
        return result;
    }
}
