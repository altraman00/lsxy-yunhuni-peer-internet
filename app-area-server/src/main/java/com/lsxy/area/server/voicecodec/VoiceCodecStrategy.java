package com.lsxy.area.server.voicecodec;

/**
 * Created by liuws on 2017/3/10.
 */
public interface VoiceCodecStrategy {

    /**从codes中选一个**/
    public String select(String[] base,String[] codecs);
}
