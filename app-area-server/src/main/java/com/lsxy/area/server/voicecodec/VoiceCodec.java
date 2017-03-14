package com.lsxy.area.server.voicecodec;

import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.JSONUtil2;
import com.lsxy.framework.core.utils.StringUtil;

import java.util.List;

/**
 * Created by liuws on 2017/3/10.
 *
 */
public class VoiceCodec {

    //我们支持的内部分机编码 选择列表
    private static final String EXTENSION_CODECS = SystemConfig.getProperty("area.server.extension.codec");
    private static final String[] EXTENSION_CODEC_LIST = EXTENSION_CODECS.split(",");

    private static final VoiceCodecStrategy strategy = new DefaultVoiceCodecStrategy();

    public static String filteLineCodecs(String lineCodes){
        if(StringUtil.isBlank(lineCodes)){
            return null;
        }
        StringBuilder result = new StringBuilder();
        String[] codecs = lineCodes.split(",");
        for (String codec: codecs) {
            if(StringUtil.isNotBlank(codec)){
                result.append(codec.trim());
                result.append(",");
            }
        }
        return result.substring(0,result.length());
    }

    public static String getExtensionCodecs(){
        return EXTENSION_CODECS;
    }

    public static String selectLineCodec(String[] lineCodecs,String[] codecs){
        return strategy.select(lineCodecs,codecs);
    }

    public static String selectLineCodec(String lineCodecs,String codecs){
        if(StringUtil.isBlank(codecs)){
            return null;
        }
        if(StringUtil.isBlank(lineCodecs)){
            return null;
        }
        String[] line_codec_arr = lineCodecs.split(",");
        String[] codec_arr = codecs.replaceAll("\\[","").replaceAll("\\]","").split(",");
        return selectLineCodec(line_codec_arr,codec_arr);
    }

    public static String selectExtensionCodec(String[] codes){
        return strategy.select(EXTENSION_CODEC_LIST,codes);
    }

    public static String selectExtensionCodec(String codes){
        if(StringUtil.isBlank(codes)){
            return null;
        }
        String[] codec_arr = codes.replaceAll("\\[","").replaceAll("\\]","").split(",");
        return selectExtensionCodec(codec_arr);
    }
}
