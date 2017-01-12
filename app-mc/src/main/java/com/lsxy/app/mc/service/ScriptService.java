package com.lsxy.app.mc.service;

import com.lsxy.app.mc.exceptions.ScriptFileNotExistException;
import com.lsxy.framework.core.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;

/**
 * Created by tandy on 17/1/12.
 */
@Component
public class ScriptService {
    public static final String SCRIPT_START="start.sh";
    public static final String SCRIPT_STOP="stop.sh";
    public static final String SCRIPT_UPDATE = "update.sh";

    private static final Logger logger = LoggerFactory.getLogger(ScriptService.class);
    static final String TEMP_DIR = System.getProperty("java.io.tmpdir");


    /**
     * 服务启动时清理临时目录中的脚本，以更新脚本文件到最新
     */
    @PostConstruct
    public void init(){
        File file = new File(TEMP_DIR + File.separator + SCRIPT_START);
        if(file.exists()) {
            if(logger.isDebugEnabled()){
                logger.debug("remove script file : {} ",file.getAbsolutePath());
            }
            FileUtil.delFile(file.getAbsolutePath());
        }

        file = new File(TEMP_DIR + File.separator + SCRIPT_STOP);
        if(file.exists()) {
            if(logger.isDebugEnabled()){
                logger.debug("remote script file : {}",file.getAbsolutePath() );
            }
            FileUtil.delFile(file.getAbsolutePath());
        }
        file = new File(TEMP_DIR + File.separator + SCRIPT_UPDATE);
        if(file.exists()) {
            if(logger.isDebugEnabled()){
                logger.debug("remote script file : {}",file.getAbsolutePath() );
            }
            FileUtil.delFile(file.getAbsolutePath());
        }
    }
    /**
     * 指定脚本文件执行前，丢到临时文件目录
     * @param scriptName
     * @return
     */
    public String prepareScript(String scriptName) throws ScriptFileNotExistException {
        String result = null;
        File file = new File(TEMP_DIR + File.separator + scriptName);
        if(!file.exists()){
            if(logger.isDebugEnabled()){
                logger.debug("copy script {} to {}",scriptName,file.getAbsolutePath());
            }

            try {
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("scripts/"+scriptName);
                if(logger.isDebugEnabled()){
                    logger.debug("config stream is : " + is);
                }
                InputStream in = this.getClass().getClassLoader().getResourceAsStream("/scripts/"+scriptName);
                if(in == null){
                    throw new ScriptFileNotExistException(scriptName);
                }
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                FileUtil.copyStream(in,fos);
                result = file.getAbsolutePath();
                if(logger.isDebugEnabled()){
                    logger.debug("script file {} parepared ok,path is {}",scriptName,file.getAbsolutePath());
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            if(logger.isDebugEnabled()){
                logger.debug("script {} is exist in {}",scriptName,file.getAbsolutePath());
            }
            result = file.getAbsolutePath();
        }
        return result;
    }
}
