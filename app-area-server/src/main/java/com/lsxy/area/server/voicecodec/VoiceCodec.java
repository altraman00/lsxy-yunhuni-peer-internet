package com.lsxy.area.server.voicecodec;

import com.lsxy.framework.core.utils.StringUtil;

/**
 * Created by liuws on 2017/3/10.
 *
 */
public class VoiceCodec {

    //我们支持的线路供应商编码 选择顺序列表
    private static final String LINE_CODECS="G729_20MS,G723_5_3_30MS,G723_6_3_30MS,G711_ALAW_20MS,G711_ULAW_20MS";
    private static final String[] LINE_CODEC_LIST = LINE_CODECS.split(",");

    //我们支持的内部分机编码 选择列表
    private static final String EXTENSION_CODECS = "iLBC_30MS,G729_20MS,G711_ALAW_20MS,G711_ULAW_20MS";
    private static final String[] EXTENSION_CODEC_LIST = EXTENSION_CODECS.split(",");

    private static final VoiceCodecStrategy strategy = new DefaultVoiceCodecStrategy();

    public static String getLineCodecs(){
        return LINE_CODECS;
    }
    public static String getExtensionCodecs(){
        return EXTENSION_CODECS;
    }

    public static String selectLineCodec(String[] codes){
        return strategy.select(LINE_CODEC_LIST,codes);
    }

    public static String selectLineCodec(String codes){
        if(StringUtil.isBlank(codes)){
            return null;
        }
        String[] codec_arr = codes.split(",");
        return selectLineCodec(codec_arr);
    }

    public static String selectExtensionCodec(String[] codes){
        return strategy.select(EXTENSION_CODEC_LIST,codes);
    }

    public static String selectExtensionCodec(String codes){
        if(StringUtil.isBlank(codes)){
            return null;
        }
        String[] codec_arr = codes.split(",");
        return selectExtensionCodec(codec_arr);
    }
}
