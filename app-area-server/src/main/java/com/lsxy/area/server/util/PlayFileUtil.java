package com.lsxy.area.server.util;

import com.lsxy.framework.core.exceptions.api.PlayFileNotExistsException;
import com.lsxy.yunhuni.api.file.service.VoiceFilePlayService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 放音文件转换成cti 路径
 * Created by liuws on 2016/9/10.
 */
@Component
public class PlayFileUtil {

    @Autowired
    private VoiceFilePlayService voiceFilePlayService;

    private static final String FORMAT = "/data/prd/p/0/%s/%s/%s.%s";

    /**
     * 将aaaa.wav|bbb.wav|ccc.wav转换成uuid1.wav|uuid2.wav|uuid3.wav
     * @param playFile
     * @return
     */
    public String convertArray(String tenantId,String appId,String playFile) throws PlayFileNotExistsException{
        if(StringUtils.isBlank(playFile)){
            return null;
        }
        String[] files = playFile.split("\\|");
        if(files == null || files.length == 0){
            throw new PlayFileNotExistsException();
        }
        return StringUtils.join(convertArray(tenantId,appId, files),"|");
    }

    /**
     * 将[aaaa.wav,bbb.wav]转换成[uuid1.wav,uuid2.wav]
     * @param files
     * @return
     */
    public List<String> convertArray(String tenantId,String appId,Collection<String> files) throws PlayFileNotExistsException{
        if(files == null || files.size() == 0){
            return null;
        }
        List<String> fileresults = new ArrayList<>();
        for(String file : files){
            String f = convert(tenantId,appId,file);
            if(f != null){
                fileresults.add(f);
            }
        }
        return fileresults;
    }

    /**
     * 将[aaaa.wav,bbb.wav]转换成[uuid1.wav,uuid2.wav]
     * @param files
     * @return
     */
    public List<String> convertArray(String tenantId,String appId,String[] files) throws PlayFileNotExistsException{
        if(files == null || files.length == 0){
            return null;
        }
        return convertArray(tenantId,appId, Arrays.asList(files));
    }

    /**
     * 将当个文件转换成cti需要的格式
     * @param tenantId
     * @param appId
     * @param file
     * @return
     * @throws PlayFileNotExistsException
     */
    public String convert(String tenantId,String appId,String file) throws PlayFileNotExistsException{
        if(StringUtils.isBlank(file)){
            return null;
        }
        String f = voiceFilePlayService.getVerifiedFile(appId,file);
        if(f == null){
            throw new PlayFileNotExistsException();
        }
        return convert(tenantId,appId,file,f);
    }

    private String convert(String tenantId,String appId,String olefile,String newfile){
        String sufix = olefile.substring(olefile.lastIndexOf(".") + 1);
        return String.format(FORMAT,tenantId,appId,newfile,sufix);
    }
}
