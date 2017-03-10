package com.lsxy.area.server.voicecodec;

import com.lsxy.framework.core.utils.StringUtil;

/**
 * Created by liuws on 2017/3/10.
 */
public class DefaultVoiceCodecStrategy implements VoiceCodecStrategy{

    @Override
    public String select(String[] base, String[] codecs) {
        String result = null;
        if(base !=null && base.length>0 && codecs!=null && codecs.length>0){
            i:for(int i=0,len=base.length;i<len;i++){
                String base_codec = base[i];
                if(base_codec == null){
                    continue i;
                }
                base_codec = base_codec.trim();
                if(StringUtil.isEmpty(base_codec)){
                    continue i;
                }
                j:for(int j=0,jlen=codecs.length;j<jlen;j++){
                    String codec = codecs[j];
                    if(codec == null){
                        continue j;
                    }
                    codec = codec.trim();
                    if(StringUtil.isEmpty(codec)){
                        continue j;
                    }
                    if(base_codec.equals(codec)){
                        result = base_codec;
                        return result;
                    }
                }
            }
        }
        return result;
    }

}
